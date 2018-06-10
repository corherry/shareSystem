package com.zhbit.controller;

import com.zhbit.entity.expression.Expression;
import com.zhbit.service.ExpressionService;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("/expressionController")
public class ExpressionController {

    @Resource(name = "expressionService")
    private ExpressionService expressionService;

    @ResponseBody
    @RequestMapping(value = "/queryExpression", method = RequestMethod.GET)
    public String queryExpression(){
        List<Expression> expressionList = expressionService.queryExpression();
        return JsonUtil.toJson("expressionList", expressionList);
    }

}
