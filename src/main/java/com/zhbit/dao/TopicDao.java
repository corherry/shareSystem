package com.zhbit.dao;

import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.*;

import java.util.List;

public interface TopicDao {


    List<TopicInfo> queryAllTopic(int userId, int category, int page, int size, String order);

    List<TopicInfo> queryFriendTopic(int userId, int category, int page, int size, String order);

    List<TopicInfo> queryTopicByUserId(int userId, int seeUserId, int category, int page, int size, String order);

    List<TopicTransmission> queryMyTopic(int userId, int page, int size);

    List<TopicTransmission> queryLoveTopic(int userId, int page, int size);

    List<TopicTransmission> queryCollectTopic(int userId, int page, int size);

    List<TopicInfo> queryTopicByIdStr(String idStr);

    int queryTopicCountByDate(int userId, String curDateTime);

    int add(Topic topic);

    List<TopicClass> queryTopicClass();

    RelationTopicCount queryRelationTopicCount(int userId);

    List<TopicRank> queryTopicSeeRank();

    List<TopicRank> queryTopicCommentRank();

    TopicDetail queryTopicDetail(int userId, int topicId);

    int delete(int id);

    int updateAuthority(int topicId, int authority);

    Page<SearchTopicInfo> queryTopicByTitle(String title, int page, int size);

}
