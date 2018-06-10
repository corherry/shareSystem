package com.zhbit.dao;

import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.relation.RelationUser;

import java.util.List;

public interface RelationDao {

    int addRelation(int userId, int attendId);

    int deleteRelation(int userId, int attendId);

    List<UserInfo> findFansByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc);

    List<UserInfo> findFriendByUserId(int userId, int seeUserId, int page, int size, String order, String ascOrDesc);

    List<RelationUser> findEachAttendByUserId(int userId, int page, int size, String order, String ascOrDesc);

    List<String> findFriend(int userId);

    int isAttend(int userId, int attendId);
}
