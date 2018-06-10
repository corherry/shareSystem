package com.zhbit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.*;
import com.zhbit.service.TopicService;
import com.zhbit.util.JsonUtil;
import com.zhbit.util.OSSClientUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("/topicController")
public class TopicController {

    @Resource(name = "topicService")
    private TopicService topicService;

    @ResponseBody
    @RequestMapping(value = "/queryIndexTopic", method = RequestMethod.GET)
    public String queryIndexTopic(int userId, int category, int range, int page, int size, String order){
        List<TopicInfo> topicList = topicService.queryIndexTopic(userId, category, range, page, size, order);
        return JsonUtil.toJson("topicList", topicList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryTopicByUserId", method = RequestMethod.GET)
    public String queryTopicByUserId(int userId, int seeUserId, int category, int page, int size, String order){
        List<TopicInfo> topicList = topicService.queryTopicByUserId(userId, seeUserId, category,  page, size, order);
        return JsonUtil.toJson("topicList", topicList);
    }

    @ResponseBody
    @RequestMapping(value = "/uploadTopicPic", method = RequestMethod.POST)
    public String uploadTopicPic(MultipartFile file){
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        String url = ossClientUtil.uploadObject2OSS(file, "image/upload/");
        return JsonUtil.toJson("url", url);
    }

    @ResponseBody
    @RequestMapping(value = "/publishTopic", method = RequestMethod.GET)
    public String publishTopic(Topic topic){
        int point = topicService.publishTopic(topic);
        return JsonUtil.toJson("point", point);
    }

    @ResponseBody
    @RequestMapping(value = "/queryTopicClass", method = RequestMethod.GET)
    public String queryTopicClass(){
        List<TopicClass> topicClassList = topicService.queryTopicClass();
        return JsonUtil.toJson("topicClassList", topicClassList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryMyTopic", method = RequestMethod.GET)
    public String queryMyTopic(int userId, int page, int size){
        List<TopicTransmission> topicList = topicService.queryMyTopic(userId, page, size);
        return JsonUtil.toJson("topicList", topicList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryLoveTopic", method = RequestMethod.GET)
    public String queryLoveTopic(int userId, int page, int size){
        List<TopicTransmission> topicList = topicService.queryLoveTopic(userId, page, size);
        return JsonUtil.toJson("topicList", topicList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryCollectTopic", method = RequestMethod.GET)
    public String queryCollectTopic(int userId, int page, int size){
        List<TopicTransmission> topicList = topicService.queryCollectTopic(userId, page, size);
        return JsonUtil.toJson("topicList", topicList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryRelationTopicCount", method = RequestMethod.GET)
    public String queryRelationTopicCount(int userId){
        RelationTopicCount topicCount = topicService.queryRelationTopicCount(userId);
        return JsonUtil.toJson("topicCount", topicCount);
    }


    @ResponseBody
    @RequestMapping(value = "/queryTopicSeeRank", method = RequestMethod.GET)
    public String queryTopicSeeRank(){
        List<TopicRank> topicRankList = topicService.queryTopicSeeRank();
        return JsonUtil.toJson("topicRankList", topicRankList);

    }

    @ResponseBody
    @RequestMapping(value = "/queryTopicCommentRank", method = RequestMethod.GET)
    public String queryTopicCommentRank(){
        List<TopicRank> topicRankList = topicService.queryTopicCommentRank();
        return JsonUtil.toJson("topicRankList", topicRankList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryTopicDetail", method = RequestMethod.GET)
    public String queryTopicDetail(int userId, int topicId){
        TopicDetail topicDetail = topicService.queryTopicDetail(userId, topicId);
        return JsonUtil.toJson("topicDetail", topicDetail);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(int id){
        int count = topicService.delete(id);
        return JsonUtil.toJson("result", count);
    }

    @ResponseBody
    @RequestMapping(value = "/updateAuthority", method = RequestMethod.GET)
    public String updateAuthority(int topicId, int authority){
        int count = topicService.updateAuthority(topicId, authority);
        return JsonUtil.toJson("result", count);
    }

    @ResponseBody
    @RequestMapping(value = "/queryTopicByTitle", method = RequestMethod.GET)
    public String queryTopicByTitle(String title, int page, int size){
        Page<TopicInfo> list = topicService.queryTopicByTitle(title, page, size);
        return JsonUtil.toJson("list", list);
    }

}
