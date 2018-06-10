package com.zhbit.dao.impl;

import com.zhbit.dao.CollectDao;
import com.zhbit.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("collectDao")
public class CollectDaoImpl implements CollectDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addCollect(int userId, int topicId) {
        String sql = "insert into collection(user_id, topic_id, created_at, updated_at) values(?, ?, ?, ?)";
        return jdbcTemplate.update(sql, new Object[]{userId, topicId, CalendarUtil.getCurrentDateTime(), CalendarUtil.getCurrentDateTime()});
    }

    public int removeCollect(int userId, int topicId) {
        String sql = "delete from collection where user_id=? and topic_id=?";
        return jdbcTemplate.update(sql, new Object[]{userId, topicId});
    }
}
