package com.zhbit.controller;

import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.relation.RelationUser;
import com.zhbit.service.RelationService;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Component
@Controller
@Scope("prototype")
@RequestMapping("/relationController")
public class RelationController {

    @Resource(name = "relationService")
    private RelationService relationService;

    @ResponseBody
    @RequestMapping(value = "/updateRelation", method = RequestMethod.GET)
    public String updateRelation(int userId, int attendId, int type){
        int point = relationService.updateRelation(userId, attendId, type);
        return JsonUtil.toJson("point", point);
    }

    @ResponseBody
    @RequestMapping(value = "/findFansByUserId", method = RequestMethod.GET)
    public String findFansByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc){
        List<UserInfo> relationUserList = relationService.findFansByUserId(userId, seeUserId, page, size, order, ascOrDesc);
        return JsonUtil.toJson("relationUserList", relationUserList);
    }

    @ResponseBody
    @RequestMapping(value = "/findFriendByUserId", method = RequestMethod.GET)
    public String findFriendByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc){
        List<UserInfo> relationUserList = relationService.findFriendByUserId(userId, seeUserId, page, size, order, ascOrDesc);
        return JsonUtil.toJson("relationUserList", relationUserList);
    }

    @ResponseBody
    @RequestMapping(value = "/findFriend", method = RequestMethod.GET)
    public String findFriend(int userId){
        List<String> names = relationService.findFriend(userId);
        return JsonUtil.toJson("names", names);
    }

    @ResponseBody
    @RequestMapping(value = "/findEachAttendByUserId", method = RequestMethod.GET)
    public String findEachAttendByUserId(int userId, int page, int size, String order, String ascOrDesc){
        List<RelationUser> relationUserList = relationService.findEachAttendByUserId(userId, page, size, order, ascOrDesc);
        return JsonUtil.toJson("relationUserList", relationUserList);
    }

    @ResponseBody
    @RequestMapping(value = "/isAttend", method = RequestMethod.GET)
    public String isAttend(int userId, int attendId){
        int count = relationService.isAttend(userId, attendId);
        return JsonUtil.toJson("count", count);
    }

}
