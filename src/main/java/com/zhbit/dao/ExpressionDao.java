package com.zhbit.dao;

import com.zhbit.entity.expression.Expression;

import java.util.List;

public interface ExpressionDao {

    List<Expression> queryExpression();
}
