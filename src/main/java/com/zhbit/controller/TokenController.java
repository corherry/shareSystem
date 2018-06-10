package com.zhbit.controller;

import com.zhbit.entity.User.User;
import com.zhbit.entity.topic.TopicRank;
import com.zhbit.service.TopicService;
import com.zhbit.service.UserService;
import com.zhbit.util.CookieUtil;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("/tokenController")
public class TokenController {

    @Resource(name = "userService")
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(User loginUser, HttpServletResponse response){
        int result = userService.login(loginUser);
        if(result >0) {
            CookieUtil.addCookie(response, "token", String.valueOf(result), -1);
        }
        return JsonUtil.toJson("loginId", result);
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(User user, String code, HttpServletResponse response){
        int id =  userService.register(user, code);
        if(id > 0){
            CookieUtil.addCookie(response, "token", String.valueOf(id), -1);
        }
        return JsonUtil.toJson("registerId", id);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletResponse response, int id){
        CookieUtil.removeCookie(response, "token");
        userService.logout(id);
    }

}
