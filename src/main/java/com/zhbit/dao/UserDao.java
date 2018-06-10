package com.zhbit.dao;

import com.zhbit.entity.User.SearchUserInfo;
import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.User.User;
import com.zhbit.entity.User.UserMainInfo;
import com.zhbit.entity.page.Page;

import java.util.List;

public interface UserDao {

    int add(User user);

    int update(User user);

    int updateUserExperiencePoint(int userId, int point);

    int updateUserMainInfo(UserMainInfo userInfo);

    int updateUserHeadPic(String headPic, String userId);

    User findUserById(int id);

    User findUserByAccount(String account);

    UserInfo queryIndexUserInfoById(int userId);

    User findUserByUserName(String userName);

    UserInfo queryHomePageUserInfo(int userId, int seeUserId);

    Page<SearchUserInfo> queryUserInfoByUserName(String userName, int page, int size);

    List<UserInfo> queryUserInfoByIdstr(String idStr, int userId);
}
