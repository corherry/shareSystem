package com.zhbit.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhbit.dao.ExperienceDao;
import com.zhbit.dao.TopicDao;
import com.zhbit.dao.UserDao;
import com.zhbit.entity.expression.Expression;
import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.*;
import com.zhbit.service.TopicService;
import com.zhbit.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("topicService")
public class TopicServiceImpl implements TopicService{

    @Resource(name = "topicDao")
    private TopicDao topicDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Resource(name = "experienceDao")
    private ExperienceDao experienceDao;

    @Autowired
    private RedisUtil redisUtil;

    public List<TopicInfo> queryIndexTopic(int userId, int category, int range, int page, int size, String order){
        List<TopicInfo> topicList = new ArrayList<TopicInfo>();
        if(range == 0){ //查全部
            topicList = topicDao.queryAllTopic(userId, category, page, size, order);
        }else if(range == 1){ //查看好友
            topicList = topicDao.queryFriendTopic(userId, category, page, size, order);
        }
        for(int i = 0; i < topicList.size(); i++){
            TopicInfo topicInfo = topicList.get(i);
            topicInfo.setAddtime(CalendarUtil.formatShowDateTime(topicInfo.getAddtime()));
            topicList.set(i, topicInfo);
        }
        return topicList;
    }

    public List<TopicInfo> queryTopicByUserId(int userId, int seeUserId, int category, int page, int size, String order) {
        List<TopicInfo> topicList = topicDao.queryTopicByUserId(userId, seeUserId, category, page, size, order);
        for(int i = 0; i < topicList.size(); i++){
            TopicInfo topicInfo = topicList.get(i);
            topicInfo.setAddtime(CalendarUtil.formatShowDateTime(topicInfo.getAddtime()));
            topicList.set(i, topicInfo);
        }
        return topicList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int publishTopic(Topic topic) {
        String curDateTime = CalendarUtil.getCurrentDateTime();
        int point = 0;
        int count = topicDao.queryTopicCountByDate(topic.getUserId(), curDateTime.substring(0, 10));
        if(count < 5){
            point = experienceDao.queryPointByType("publish");
            userDao.updateUserExperiencePoint(topic.getUserId(), point);
        }
        topic.setAddtime(curDateTime);
        topic.setUptime(curDateTime);
        int id = topicDao.add(topic);
        return point;

    }

    public List<TopicClass> queryTopicClass() {
        List<TopicClass> topicClassList = new ArrayList<TopicClass>();
        if(redisUtil.hasKey("topicclass")){
            topicClassList = (List<TopicClass>) JsonUtil.jsonToList((String) redisUtil.get("topicclass"), TopicClass.class);
        }else{
            topicClassList = topicDao.queryTopicClass();
            redisUtil.set("topicclass", JsonUtil.ObjectToJson(topicClassList), 180);
        }
        return topicClassList;
    }

    public List<TopicTransmission> queryMyTopic(int userId, int page, int size) {
        List<TopicTransmission> topicList = topicDao.queryMyTopic(userId, page, size);
        for(int i = 0; i < topicList.size(); i++){
            TopicTransmission topic = topicList.get(i);
            topic.setAddtime(CalendarUtil.formatDateTime(topic.getAddtime()));
            topicList.set(i, topic);
        }
        return topicList;
    }

    public List<TopicTransmission> queryLoveTopic(int userId, int page, int size) {
        List<TopicTransmission> topicList = topicDao.queryLoveTopic(userId, page, size);
        for(int i = 0; i < topicList.size(); i++){
            TopicTransmission topic = topicList.get(i);
            topic.setAddtime(CalendarUtil.formatDateTime(topic.getAddtime()));
            topicList.set(i, topic);
        }
        return topicList;
    }

    public List<TopicTransmission> queryCollectTopic(int userId, int page, int size) {
        List<TopicTransmission> topicList = topicDao.queryCollectTopic(userId, page, size);
        for(int i = 0; i < topicList.size(); i++){
            TopicTransmission topic = topicList.get(i);
            topic.setAddtime(CalendarUtil.formatDateTime(topic.getAddtime()));
            topicList.set(i, topic);
        }
        return topicList;
    }

    public RelationTopicCount queryRelationTopicCount(int userId) {
        return topicDao.queryRelationTopicCount(userId);
    }

    public List<TopicRank> queryTopicSeeRank() {
        List<TopicRank> topicRankList = new ArrayList<TopicRank>();
        if(redisUtil.hasKey("topicSeeRank")){
            topicRankList = (List<TopicRank>) JsonUtil.jsonToList((String) redisUtil.get("topicSeeRank"), TopicRank.class);
        }else{
            topicRankList = topicDao.queryTopicSeeRank();
            redisUtil.set("topicSeeRank", JsonUtil.ObjectToJson(topicRankList), 180);
        }
        return topicRankList;
    }

    public List<TopicRank> queryTopicCommentRank() {
        List<TopicRank> topicRankList = new ArrayList<TopicRank>();
        if(redisUtil.hasKey("topicCommentRank")){
            topicRankList = (List<TopicRank>) JsonUtil.jsonToList((String) redisUtil.get("topicCommentRank"), TopicRank.class);
        }else{
            topicRankList = topicDao.queryTopicCommentRank();
            redisUtil.set("topicCommentRank", JsonUtil.ObjectToJson(topicRankList), 180);
        }
        return topicRankList;
    }

    public TopicDetail queryTopicDetail(int userId, int topicId){
        TopicDetail topicDetail = topicDao.queryTopicDetail(userId, topicId);
        topicDetail.setAddtime(CalendarUtil.formatDateTime(topicDetail.getAddtime()));
        topicDetail.setContent(SensitiveWordFilter.replaceSenstiveWords(topicDetail.getContent()));
        if(!StringUtils.isBlank(topicDetail.getPic())) {
            topicDetail.setPicGroup(ImgParseUtil.parseImg(topicDetail.getPic()));
        }
        return topicDetail;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int delete(int id) {
        String solrUrl = "http://127.0.0.1:8983/solr/topic";
        HttpSolrClient client = new HttpSolrClient(solrUrl);
        SolrUtil.deleteById(String.valueOf(id), client);
        return topicDao.delete(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateAuthority(int topicId, int authority) {
        return topicDao.updateAuthority(topicId, authority);
    }

    public Page<TopicInfo> queryTopicByTitle(String title, int page, int size) {
        Page<SearchTopicInfo> pageList = topicDao.queryTopicByTitle(title, page, size);
        List<SearchTopicInfo> list = pageList.getResultList();
        String idStr = "";
        for(int i = 0; i < list.size(); i++){
            idStr += list.get(i).getId() + ",";
        }
        List<TopicInfo> topicInfoList = new ArrayList<TopicInfo>();
        if(StringUtils.isBlank(idStr) == false) {
            idStr = idStr.substring(0, idStr.length() - 1);
            topicInfoList = topicDao.queryTopicByIdStr(idStr);
        }
        for(int i = 0; i < topicInfoList.size(); i++){
            TopicInfo topicInfo = topicInfoList.get(i);
            topicInfo.setAddtime(CalendarUtil.formatShowDateTime(topicInfo.getAddtime()));
            topicInfoList.set(i, topicInfo);
        }
        return new Page<TopicInfo>(page, size, pageList.getTotalRow(), topicInfoList);
    }
}
