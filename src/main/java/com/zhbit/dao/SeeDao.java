package com.zhbit.dao;

public interface SeeDao {

    int updateSeeCount(int userId, int topicId);

    int add(int userId, int topicId);

    int querySeeCount(int userId, int topicId);
}
