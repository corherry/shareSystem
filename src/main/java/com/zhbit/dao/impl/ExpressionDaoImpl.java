package com.zhbit.dao.impl;

import com.zhbit.dao.ExpressionDao;
import com.zhbit.entity.expression.Expression;
import com.zhbit.util.TargetRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("expressionDao")
public class ExpressionDaoImpl implements ExpressionDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Expression> queryExpression() {
        String sql = "select (select type_name from expressionclass where id=expression.expressionclass_id) category, phrase, pic from expression order by id asc";
        return jdbcTemplate.query(sql, new TargetRowMapper<Expression>(Expression.class));
    }
}
