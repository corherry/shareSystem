package com.zhbit.controller;

import com.aliyuncs.exceptions.ClientException;
import com.zhbit.service.SendMessageService;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Scope("prototype")
@RequestMapping("/sendMessageController")
public class SendMessageController {

    @Resource(name = "sendMessageService")
    private SendMessageService sendMessageService;


    @ResponseBody
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    public String sendMessage(String account, int type, int op) throws ClientException {
        String result = sendMessageService.sendMessage(account, type, op);
        return JsonUtil.toJson("result", result);
    }

}
