package com.zhbit.controller;

import com.zhbit.service.LoveService;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Scope("prototype")
@RequestMapping("/loveController")
public class LoveController {

    @Resource(name="loveService")
    private LoveService loveService;

    @ResponseBody
    @RequestMapping(value = "/updateLove", method = RequestMethod.GET)
    public String updateLove(int userId, int topicId, int op){
        int point = loveService.updateLove(userId, topicId, op);
        return JsonUtil.toJson("point", point);
    }
}
