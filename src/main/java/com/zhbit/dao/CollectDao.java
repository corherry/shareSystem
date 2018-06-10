package com.zhbit.dao;

public interface CollectDao {

    int addCollect(int userId, int topicId);

    int removeCollect(int userId, int topicId);
}
