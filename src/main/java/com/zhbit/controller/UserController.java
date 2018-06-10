package com.zhbit.controller;

import com.zhbit.entity.User.UserInfo;
import com.zhbit.entity.User.User;
import com.zhbit.entity.User.UserMainInfo;
import com.zhbit.entity.page.Page;
import com.zhbit.service.UserService;
import com.zhbit.util.CookieUtil;
import com.zhbit.util.JsonUtil;
import com.zhbit.util.OSSClientUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller
@Scope("prototype")
@RequestMapping("/userController")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/queryIndexUserInfoById", method = RequestMethod.GET)
    public String queryIndexUserInfoById(int userId){
        UserInfo userInfo = userService.queryIndexUserInfoById(userId);
        return JsonUtil.toJson("userInfo", userInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/queryHomePageUserInfo", method = RequestMethod.GET)
    public String queryHomePageUserInfo(int userId, int seeUserId){
        UserInfo userInfo = userService.queryHomePageUserInfo(userId, seeUserId);
        return JsonUtil.toJson("userInfo", userInfo);
    }


    @ResponseBody
    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    public String findUserById(int userId){
        User user = userService.findUserById(userId);
        return JsonUtil.toJson("user", user);
    }


    @ResponseBody
    @RequestMapping(value = "/isExistedAccount", method = RequestMethod.GET)
    public String isExistedAccount(String account){
        User user = userService.findUserByAccount(account);
        if(user == null){
            return JsonUtil.toJson("result", "0");
        }else{
            return JsonUtil.toJson("result", "1");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserHeadPic", method = RequestMethod.GET)
    public String updateUserHeadPic(String headPic, String userId){
        int count = userService.updateUserHeadPic(headPic, userId);
        return JsonUtil.toJson("result", count);
    }

    @ResponseBody
    @RequestMapping(value = "/isUnameExisted", method = RequestMethod.GET)
    public String isUnameExisted(String userName){
        User user = userService.findUserByUserName(userName);
        if(user == null){
            return JsonUtil.toJson("result", 0);
        }else{
            return JsonUtil.toJson("result", user.getId());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String resetPassword(int id, String oldPassword, String newPassword, HttpServletResponse response){
        int result = userService.resetPassword(id, oldPassword, newPassword);
        if(result > 0){
            CookieUtil.removeCookie(response, "token");
        }
        return JsonUtil.toJson("result", result);
    }

    @ResponseBody
    @RequestMapping(value = "/updateUserMainInfo", method = RequestMethod.GET)
    public String updateUserMainInfo(UserMainInfo userInfo){

        int count = userService.updateUserMainInfo(userInfo);
        return JsonUtil.toJson("result", count);
    }

    @ResponseBody
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
    public String forgetPassword(String account, String newPassword){
        int result = userService.forgetPassword(account, newPassword);
        return JsonUtil.toJson("result", result);
    }

    @ResponseBody
    @RequestMapping(value = "/checkPassword", method = RequestMethod.GET)
    public String checkPassword(int id, String password){
        boolean isCorrect = userService.checkPassword(id, password);
        return JsonUtil.toJson("isCorrect", isCorrect);
    }

    @ResponseBody
    @RequestMapping(value = "/checkResetPasswordCode", method = RequestMethod.GET)
    public String checkResetPasswordCode(String account, String code){
        boolean isCorrect = userService.checkResetPasswordCode(account, code);
        return JsonUtil.toJson("isCorrect", isCorrect);
    }

    @ResponseBody
    @RequestMapping(value = "/uploadHeadPic", method = RequestMethod.POST)
    public String uploadHeadPic(MultipartFile file){
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        String url = ossClientUtil.uploadObject2OSS(file, "image/headpic/");
        return JsonUtil.toJson("url", url);
    }

    @ResponseBody
    @RequestMapping(value = "/queryUserInfoByUserName", method = RequestMethod.GET)
    public String queryUserInfoByUserName(int userId, String userName, int page, int size){
        Page<UserInfo> list = userService.queryUserInfoByUserName(userId, userName, page, size);
        return JsonUtil.toJson("list", list);
    }
}
