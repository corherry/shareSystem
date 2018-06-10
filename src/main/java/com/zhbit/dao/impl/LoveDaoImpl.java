package com.zhbit.dao.impl;

import com.zhbit.dao.LoveDao;
import com.zhbit.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository("loveDao")
public class LoveDaoImpl implements LoveDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addLove(int userId, int topicId) {
        String sql = "insert into love(user_id, topic_id, created_at, updated_at) values(?, ?, ?, ?)";
        return jdbcTemplate.update(sql, new Object[]{userId, topicId, CalendarUtil.getCurrentDateTime(), CalendarUtil.getCurrentDateTime()});
    }

    public int deleteLove(int userId, int topicId) {
        String sql = "delete from love where user_id=? and topic_id=?";
        return jdbcTemplate.update(sql, new Object[]{userId, topicId});
    }
}
