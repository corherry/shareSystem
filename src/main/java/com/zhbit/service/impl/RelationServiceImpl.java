package com.zhbit.service.impl;

import com.zhbit.dao.ExperienceDao;
import com.zhbit.dao.RelationDao;
import com.zhbit.dao.UserDao;
import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.relation.RelationUser;
import com.zhbit.service.RelationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("relationService")
public class RelationServiceImpl implements RelationService {

    @Resource(name = "relationDao")
    private RelationDao relationDao;

    @Resource(name = "experienceDao")
    private ExperienceDao experienceDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateRelation(int userId, int attendId, int type) {
        int result = 0;
        int point = 0;
        if(type == 0){ //加关注
            result = relationDao.addRelation(userId, attendId);
            if(result > 0) {
                point = experienceDao.queryPointByType("attend");
                userDao.updateUserExperiencePoint(userId, point);
            }
        }else if(type > 0){ //取消关注
            result = relationDao.deleteRelation(userId, attendId);
            if(result > 0){
                point = experienceDao.queryPointByType("removeAttend");
                userDao.updateUserExperiencePoint(userId, point);
            }
        }
        return point;
    }

    public List<UserInfo> findFansByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc) {
        return relationDao.findFansByUserId(userId, seeUserId, page, size, order, ascOrDesc);
    }

    public List<UserInfo> findFriendByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc) {
        return relationDao.findFriendByUserId(userId, seeUserId, page, size, order, ascOrDesc);
    }

    public List<RelationUser> findEachAttendByUserId(int userId, int page, int size, String order, String ascOrDesc) {
        return relationDao.findEachAttendByUserId(userId, page, size, order, ascOrDesc);
    }

    public List<String> findFriend(int userId) {
        return relationDao.findFriend(userId);
    }

    public int isAttend(int userId, int attendId) {
        return relationDao.isAttend(userId, attendId);
    }
}
