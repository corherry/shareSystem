package com.zhbit.dao.impl;

import com.zhbit.dao.ExperienceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("experienceDao")
public class ExperienceDaoImpl implements ExperienceDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int queryPointByType(String type) {
        String sql = "select point from experience where type_name=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{type}, Integer.class);
    }
}
