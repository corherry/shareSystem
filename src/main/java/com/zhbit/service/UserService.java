package com.zhbit.service;

import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.User.User;
import com.zhbit.entity.User.UserMainInfo;
import com.zhbit.entity.page.Page;

public interface UserService {

    int login(User loginUser);

    int register(User user, String code);

    User findUserByAccount(String account);

    int updateUserMainInfo(UserMainInfo userInfo);

    int updateUserHeadPic(String headPic, String userId);

    User findUserById(int id);

    void logout(int id);

    int resetPassword(int id, String oldPassword, String newPassword);

    boolean checkPassword(int id, String password);

    UserInfo queryIndexUserInfoById(int userId);

    User findUserByUserName(String userName);

    boolean checkResetPasswordCode(String account, String code);

    int forgetPassword(String account, String newPassword);

    UserInfo queryHomePageUserInfo(int userId, int seeUserId);

    Page<UserInfo> queryUserInfoByUserName(int userId, String userName, int page, int size);
}
