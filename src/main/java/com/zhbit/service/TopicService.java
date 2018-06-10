package com.zhbit.service;

import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.*;

import java.text.ParseException;
import java.util.List;

public interface TopicService {

    List<TopicInfo> queryIndexTopic(int userId, int category, int range, int page, int size, String order);

    List<TopicInfo> queryTopicByUserId(int userId, int seeUserId, int category, int page, int size, String order);

    List<TopicTransmission> queryMyTopic(int userId, int page, int size);

    List<TopicTransmission> queryLoveTopic(int userId, int page, int size);

    List<TopicTransmission> queryCollectTopic(int userId, int page, int size);

    int publishTopic(Topic topic);

    List<TopicClass> queryTopicClass();

    RelationTopicCount queryRelationTopicCount(int userId);

    List<TopicRank> queryTopicSeeRank();

    List<TopicRank> queryTopicCommentRank();

    TopicDetail queryTopicDetail(int userId, int topicId);

    int delete(int id);

    int updateAuthority(int topicId, int authority);

    Page<TopicInfo> queryTopicByTitle(String title, int page, int size);
}
