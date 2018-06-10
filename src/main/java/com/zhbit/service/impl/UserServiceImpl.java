package com.zhbit.service.impl;

import com.zhbit.dao.ExperienceDao;
import com.zhbit.dao.UserDao;
import com.zhbit.entity.User.SearchUserInfo;
import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.User.User;
import com.zhbit.entity.User.UserMainInfo;
import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.SearchTopicInfo;
import com.zhbit.service.UserService;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.MD5Util;
import com.zhbit.util.RedisUtil;
import com.zhbit.util.SolrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "userDao")
    private UserDao userDao;

    @Resource(name = "experienceDao")
    private ExperienceDao experienceDao;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登陆
     *@param loginUser
     *@return
     */
    public int login(User loginUser) {
        User user = userDao.findUserByAccount(loginUser.getAccount());
        if(user == null){ //账号未注册
            return 0;
        }else if(MD5Util.validate(loginUser.getPassword(), user.getPassword()) == false){ //密码错误
            return -2;
        }else{ //登陆成功
            String token = String.valueOf(user.getId());
            //修改最后登陆时间和经验值
            int point = user.getExperiencePoint();
            String curDateTime = CalendarUtil.getCurrentDateTime();
            if(StringUtils.isBlank(user.getLastLoginTime()) || CalendarUtil.isSameDay(user.getLastLoginTime(), curDateTime) == false){ //每天第一次登陆+经验值
                point += experienceDao.queryPointByType("firstLogin");
                user.setExperiencePoint(point);
            }
            user.setLastLoginTime(curDateTime);
            user.setUptime(curDateTime);
            int count = userDao.update(user);
            if(count > 0) {
                redisUtil.set("login:user:id:" + token, String.valueOf(user.getId()), 1800);
            }
            return user.getId();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int register(User user, String code) {
        String correctCode = String.valueOf(redisUtil.get("register:user:account:"+user.getAccount()+":code"));
        if(code.equals(correctCode) == false){
            return -2;
        }
        User exisetedUser = userDao.findUserByAccount(user.getAccount());
        if(exisetedUser != null){
            return 0;
        }else{
            exisetedUser = userDao.findUserByUserName(user.getUserName());
            if(exisetedUser != null){
                return -1;
            }else{
                String md5Password = MD5Util.encode2hex(user.getPassword());
                user.setPassword(md5Password);
                String curDateTime = CalendarUtil.getCurrentDateTime();
                user.setAddtime(curDateTime);
                user.setUptime(curDateTime);
                user.setLastLoginTime(curDateTime);
                int point = experienceDao.queryPointByType("firstLogin");
                user.setExperiencePoint(point);
                int id = userDao.add(user);
                if(id > 0){
                    redisUtil.set("login:user:id:" + String.valueOf(id), String.valueOf(id), 1800);
                }
                return id;
            }
        }
    }

    /**
     * 登出
     *@param id
     *@return
     */
    public void logout(int id) {
        redisUtil.deleteKey("login:user:id:" + String.valueOf(id));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int resetPassword(int id, String oldPassword, String newPassword) {
        User user = userDao.findUserById(id);
        if(MD5Util.encode2hex(oldPassword).equals(user.getPassword()) == false){
            return -2;
        }
        if(MD5Util.encode2hex(newPassword).equals(user.getPassword())){
            return -1;
        }
        user.setPassword(MD5Util.encode2hex(newPassword));
        user.setUptime(CalendarUtil.getCurrentDateTime());
        int result = userDao.update(user);
        if(result > 0){
            redisUtil.deleteKey("login:user:id:" + String.valueOf(id));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateUserMainInfo(UserMainInfo userInfo) {
        if(userInfo.getBirthday().length() == 0){
            userInfo.setBirthday(null);
        }
        User user = userDao.findUserByUserName(userInfo.getUserName());
        if(user != null && user.getId() != userInfo.getId()){
            return -1;
        }

        user = userDao.findUserById(userInfo.getId());

        //更新solr索引
        SearchUserInfo searchUserInfo = new SearchUserInfo();
        searchUserInfo.setId(userInfo.getId());
        searchUserInfo.setUserName(userInfo.getUserName());
        searchUserInfo.setAddtime(CalendarUtil.localToCST(user.getAddtime()));
        String solrUrl = "http://127.0.0.1:8983/solr/user";
        HttpSolrClient client = new HttpSolrClient(solrUrl);
        SolrUtil.addBean(searchUserInfo, client);
        return userDao.updateUserMainInfo(userInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int forgetPassword(String account, String newPassword) {
        User user = userDao.findUserByAccount(account);
        if(MD5Util.encode2hex(newPassword).equals(user.getPassword())){
            return -1;
        }
        user.setPassword(MD5Util.encode2hex(newPassword));
        user.setUptime(CalendarUtil.getCurrentDateTime());
        int result = userDao.update(user);
        return result;
    }

    public boolean checkPassword(int id, String password) {
        User user = userDao.findUserById(id);
        if(user.getPassword().equals(MD5Util.encode2hex(password))){
            return true;
        }else{
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateUserHeadPic(String headPic, String userId) {
        return userDao.updateUserHeadPic(headPic, userId);
    }

    public User findUserByAccount(String account) {
        return userDao.findUserByAccount(account);
    }

    public User findUserById(int id) {
        User user =  userDao.findUserById(id);
        user.setAddtime(CalendarUtil.getDate(user.getAddtime()));
        return user;
    }

    public UserInfo queryIndexUserInfoById(int userId) {
        return userDao.queryIndexUserInfoById(userId);
    }

    public User findUserByUserName(String userName) {
        return userDao.findUserByUserName(userName);
    }

    public boolean checkResetPasswordCode(String account, String code) {
        String correctCode = String.valueOf(redisUtil.get("forgetPassword:user:account:"+account+":code"));
        return correctCode.equals(code);
    }

    public UserInfo queryHomePageUserInfo(int userId, int seeUserId) {
        UserInfo userInfo = userDao.queryHomePageUserInfo(userId, seeUserId);
        userInfo.setCreateTime(CalendarUtil.getDate(userInfo.getCreateTime()));
        return userInfo;
    }

    public Page<UserInfo> queryUserInfoByUserName(int userId, String userName, int page, int size) {
        Page<SearchUserInfo> pageList = userDao.queryUserInfoByUserName(userName, page, size);
        List<SearchUserInfo> list = pageList.getResultList();
        String idStr = "";
        for(int i = 0; i < list.size(); i++){
            SearchUserInfo searchUserInfo = list.get(i);
            idStr += searchUserInfo.getId() + ",";
        }
        List<UserInfo> resultList = new ArrayList<UserInfo>();
        if(StringUtils.isBlank(idStr) == false) {
            idStr = idStr.substring(0, idStr.length() - 1);
            resultList = userDao.queryUserInfoByIdstr(idStr, userId);
        }
        for(int i = 0; i < resultList.size(); i++){
            UserInfo userInfo = resultList.get(i);
            userInfo.setCreateTime(CalendarUtil.formatDateTime(userInfo.getCreateTime()));
        }
        Page<UserInfo> resultPageList = new Page<UserInfo>(page, size, pageList.getTotalRow(), resultList);
        return resultPageList;
    }
}
