package com.zhbit.dao.impl;

import com.zhbit.dao.RelationDao;
import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.relation.RelationUser;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.PageUtil;
import com.zhbit.util.TargetRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("relationDao")
public class RelationDaoImpl implements RelationDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addRelation(int userId, int attendId) {
        String sql = "insert into relation(user_id, attend_id, created_at, updated_at) values(?, ?, ?, ?)";
        return jdbcTemplate.update(sql, new Object[]{userId, attendId, CalendarUtil.getCurrentDateTime(), CalendarUtil.getCurrentDateTime()});
    }

    public int deleteRelation(int userId, int attendId) {
        String sql = "delete from relation where user_id=? and attend_id=?";
        return jdbcTemplate.update(sql, new Object[]{userId, attendId});
    }

    public List<UserInfo> findFansByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc) {
        try {
            return jdbcTemplate.query(PageUtil.getPageSql("u.id userId, user_name userName, address, sex, headPic, blurb, r.created_at createTime," +
                            "(select count(*) from relation where user_id=" + userId + " and attend_id=r.user_id) isAttend, " +
                            "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level,"+
                            "(select count(*) from relation where user_id=r.user_id) friendCount," +
                            "(select count(*) from relation where attend_id=r.user_id) fansCount, " +
                            "(select count(*) from topic where user_id=r.user_id) publishCount",
                    "user u, relation r",
                    "and attend_id=? and u.id=r.user_id",
                    order, ascOrDesc), new Object[]{seeUserId, (page - 1) * size, size}, new TargetRowMapper<UserInfo>(UserInfo.class));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<UserInfo> findFriendByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc) {

        try {
            return jdbcTemplate.query(PageUtil.getPageSql("u.id userId, user_name userName, address, sex, headPic, blurb, r.created_at createTime," +
                            "(select count(*) from relation where user_id=" + userId + " and attend_id=r.attend_id) isAttend, " +
                            "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level,"+
                            "(select count(*) from relation where user_id=r.user_id) friendCount," +
                            "(select count(*) from relation where attend_id=r.user_id) fansCount, " +
                            "(select count(*) from topic where user_id=r.user_id) publishCount",
                    "user u, relation r",
                    "and user_id=? and u.id=r.attend_id",
                    order, ascOrDesc), new Object[]{seeUserId, (page - 1) * size, size}, new TargetRowMapper<UserInfo>(UserInfo.class));

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<RelationUser> findEachAttendByUserId(int userId, int page, int size, String order, String ascOrDesc) {

        try {
            return jdbcTemplate.query(PageUtil.getPageSql("u.id,user_name userName,sex,headPic, address," +
                            "(select count(*) from relation where attend_id=" + userId + " and user_id=r.user_id) isEachAttend, " +
                            "(select count(*) from relation where user_id=r.user_id) friendCount," +
                            "(select count(*) from relation where attend_id=r.user_id) fansCount, " +
                            "(select count(*) from topic where user_id=r.user_id) publishCount",
                    "user u, (select *from relation where attend_id=" + userId + " and user_id in (select attend_id from relation where user_id=" + userId + ")) r",
                    "and u.id=r.user_id",
                    "r." + order, ascOrDesc), new Object[]{(page - 1) * size, size}, new TargetRowMapper<RelationUser>(RelationUser.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<String> findFriend(int userId) {
        String sql = "select user_name userName from user u, relation r where r.user_id=? and u.id=r.attend_id";
        return jdbcTemplate.queryForList(sql, new Object[]{userId}, String.class);
    }

    public int isAttend(int userId, int attendId) {
        String sql = "select count(*) from relation where user_id=? and attend_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, attendId}, Integer.class);
    }
}
