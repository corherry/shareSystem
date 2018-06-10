package com.zhbit.controller;

import com.zhbit.service.SeeService;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Scope("prototype")
@RequestMapping("/seeController")
public class SeeController {

    @Resource(name = "seeService")
    private SeeService seeService;

    @ResponseBody
    @RequestMapping(value = "/updateSeeCount", method = RequestMethod.GET)
    public String updateSeeCount(int userId, int topicId){
        int result = seeService.updateSeeCount(userId, topicId);
        return JsonUtil.toJson("result", result);
    }

}
