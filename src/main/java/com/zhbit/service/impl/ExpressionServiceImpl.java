package com.zhbit.service.impl;

import com.zhbit.dao.ExpressionDao;
import com.zhbit.entity.expression.Expression;
import com.zhbit.service.ExpressionService;
import com.zhbit.util.JsonUtil;
import com.zhbit.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("expressionService")
public class ExpressionServiceImpl implements ExpressionService{

    @Resource(name = "expressionDao")
    private ExpressionDao expressionDao;

    @Autowired
    private RedisUtil redisUtil;

    public List<Expression> queryExpression() {
        List<Expression> expressionList = new ArrayList<Expression>();
        if(redisUtil.hasKey("expression")){
            expressionList = (List<Expression>) JsonUtil.jsonToList((String) redisUtil.get("expression"), Expression.class);
        }else{
            expressionList = expressionDao.queryExpression();
            redisUtil.set("expression", JsonUtil.ObjectToJson(expressionList), 180);
        }
        return expressionList;
    }
}
