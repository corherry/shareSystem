package com.zhbit.controller;

import com.zhbit.service.CollectService;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Scope("prototype")
@RequestMapping("/collectController")
public class CollectController {

    @Resource(name = "collectService")
    private CollectService collectService;

    @ResponseBody
    @RequestMapping(value = "/changeCollect", method = RequestMethod.GET)
    public String changeCollect(int userId, int topicId, int type){
        int point = collectService.changeCollect(userId, topicId, type);
        return JsonUtil.toJson("point", point);
    }
}
