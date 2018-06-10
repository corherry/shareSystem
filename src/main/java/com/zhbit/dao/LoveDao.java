package com.zhbit.dao;

public interface LoveDao {
    int addLove(int userId, int topicId);
    int deleteLove(int userId, int topicId);
}
