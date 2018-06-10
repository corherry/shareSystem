package com.zhbit.dao.impl;

import com.zhbit.dao.UserDao;
import com.zhbit.entity.User.SearchUserInfo;
import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.User.User;
import com.zhbit.entity.User.UserMainInfo;
import com.zhbit.entity.page.Page;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.SolrUtil;
import com.zhbit.util.TargetRowMapper;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository("userDao")
public class UserDaoImpl implements UserDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int add(final User user) {
        final String sql = "insert into user(account, password, type, user_name, sex, birthday, address, experience_point, last_login_time, created_at, updated_at, headPic, blurb) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, user.getAccount());
                ps.setObject(2, user.getPassword());
                ps.setObject(3, user.getType());
                ps.setObject(4, user.getUserName());
                ps.setObject(5, user.getSex());
                ps.setObject(6, user.getBirthday());
                ps.setObject(7, user.getAddress());
                ps.setObject(8, user.getExperiencePoint());
                ps.setObject(9, user.getLastLoginTime());
                ps.setObject(10, user.getAddtime());
                ps.setObject(11, user.getUptime());
                ps.setObject(12, user.getHeadPic());
                ps.setObject(13, user.getBlurb());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int update(User user) {
        String sql = "update user set account=?, password=?, type=?, user_name=?, sex=?, headPic=?, birthday=?, address=?, experience_point=?, last_login_time=?, blurb=?, created_at=?, updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{user.getAccount(), user.getPassword(), user.getType(), user.getUserName(), user.getSex(), user.getHeadPic(), user.getBirthday(), user.getAddress(), user.getExperiencePoint(), user.getLastLoginTime(), user.getBlurb(), user.getAddtime(), user.getUptime(), user.getId()});
    }

    public int updateUserExperiencePoint(int userId, int point) {
        String sql = "update user set experience_point=experience_point+"+ point +", updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{CalendarUtil.getCurrentDateTime(), userId});
    }

    public int updateUserMainInfo(UserMainInfo userInfo) {
        String sql = "update user set user_name=?, sex=?, address=?, birthday=?, blurb=?, updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{userInfo.getUserName(), userInfo.getSex(), userInfo.getAddress(), userInfo.getBirthday(), userInfo.getBlurb(), CalendarUtil.getCurrentDateTime(), userInfo.getId()});
    }

    /**
     * 根据id查找用户
     *@param id
     *@return
     */
    public User findUserById(int id) {
        String sql = "select id, account, password, type, user_name userName, sex, headPic, birthday, address, experience_point experiencePoint, last_login_time lastLoginTime, blurb, created_at addtime, updated_at uptime from user where id=?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, new TargetRowMapper<User>(User.class));
        return user;
    }

    /**
     * 根据账号查找用户
     *@param account 账号
     *@return
     */
    public User findUserByAccount(String account) {
        String sql = "select id, account, password, type, user_name userName, sex, headPic, birthday, address, experience_point experiencePoint, last_login_time lastLoginTime, blurb, created_at addtime, updated_at uptime from user where account=?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new Object[]{account}, new TargetRowMapper<User>(User.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    public UserInfo queryIndexUserInfoById(int userId) {
        String sql = "select id userId, user_name userName, sex, headPic, " +
                "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level,"+
                "(select count(*) from relation where user_id=u.id) friendCount," +
                "(select count(*) from relation where attend_id=u.id) fansCount, " +
                "(select count(*) from topic where user_id=u.id) publishCount "+
                "from user u " +
                "where u.id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new TargetRowMapper<UserInfo>(UserInfo.class));
    }

    public User findUserByUserName(String userName) {
        String sql = "select id, account, password, type, user_name userName, sex, headPic, birthday, address, experience_point experiencePoint, last_login_time lastLoginTime, blurb, created_at addtime, updated_at uptime from user where user_name=?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new Object[]{userName}, new TargetRowMapper<User>(User.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    public UserInfo queryHomePageUserInfo(int userId, int seeUserId) {
        String sql = "";
        if(userId == seeUserId){
            sql = "select id userId, user_name userName, sex, headPic, address, birthday, u.created_at createTime, blurb," +
                    "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level,"+
                    "(select count(*) from relation where user_id=u.id) friendCount," +
                    "(select count(*) from relation where attend_id=u.id) fansCount, " +
                    "(select count(*) from topic where user_id=u.id) publishCount "+
                    "from user u " +
                    "where u.id=?";
        }else{
            sql = "select id userId, user_name userName, sex, headPic, address, birthday, u.created_at createTime, blurb," +
                    "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level,"+
                    "(select count(*) from relation where user_id=u.id) friendCount," +
                    "(select count(*) from relation where attend_id=u.id) fansCount, " +
                    "(select count(*) from topic where user_id=u.id and (authority=3 or (authority=2 and "+userId+" in (select attend_id from relation where user_id="+seeUserId+")))) publishCount "+
                    "from user u " +
                    "where u.id=?";
        }
        return jdbcTemplate.queryForObject(sql, new Object[]{seeUserId}, new TargetRowMapper<UserInfo>(UserInfo.class));
    }

    public int updateUserHeadPic(String headPic, String userId) {
        String sql = "update user set headPic=?, updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{headPic, CalendarUtil.getCurrentDateTime(), userId});
    }

    public Page<SearchUserInfo> queryUserInfoByUserName(String userName, int page, int size) {
        String solrUrl = "http://127.0.0.1:8983/solr/user";
        HttpSolrClient client = new HttpSolrClient(solrUrl);
        return SolrUtil.query(client, "userName:*"+userName+"*", page, size, SearchUserInfo.class);
    }

    public List<UserInfo> queryUserInfoByIdstr(String idStr, int userId) {
        String sql = "select id userId, user_name userName, address, sex, headPic, blurb, created_at createTime," +
                "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level,"+
                "(select count(*) from relation where user_id=u.id) friendCount," +
                "(select count(*) from relation where attend_id=u.id) fansCount, " +
                "(select count(*) from topic where user_id=u.id) publishCount, "+
                "(select count(*) from relation where user_id="+userId+" and attend_id=u.id) isAttend "+
                "from user u " +
                "where u.id in (" +idStr+") order by createTime desc";
        return jdbcTemplate.query(sql, new TargetRowMapper<UserInfo>(UserInfo.class));
    }
}

