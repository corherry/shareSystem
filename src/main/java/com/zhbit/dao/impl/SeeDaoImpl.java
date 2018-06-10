package com.zhbit.dao.impl;

import com.zhbit.dao.SeeDao;
import com.zhbit.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("seeDao")
public class SeeDaoImpl implements SeeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int updateSeeCount(int userId, int topicId) {
        String sql = "update see set num=num+1,updated_at=? where user_id=? and topic_id=?";
        return jdbcTemplate.update(sql, new Object[]{CalendarUtil.getCurrentDateTime(), userId, topicId});
    }

    public int add(int userId, int topicId) {
        String sql = "insert into see(user_id, topic_id, num, created_at, updated_at) values(?,?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{userId, topicId, 1, CalendarUtil.getCurrentDateTime(), CalendarUtil.getCurrentDateTime()});
    }

    public int querySeeCount(int userId, int topicId) {
        String sql = "select num from see where user_id=? and topic_id=?";
        try{
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, topicId}, Integer.class);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }
}
