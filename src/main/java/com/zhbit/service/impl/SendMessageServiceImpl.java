package com.zhbit.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.zhbit.dao.UserDao;
import com.zhbit.entity.User.User;
import com.zhbit.service.SendMessageService;
import com.zhbit.util.MailUtil;
import com.zhbit.util.RandomCodeUtil;
import com.zhbit.util.RedisUtil;
import com.zhbit.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sendMessageService")
public class SendMessageServiceImpl implements SendMessageService {

    @Autowired
    private RedisUtil redisUtil;

    @Resource(name="userDao")
    private UserDao userDao;

    public String sendMessage(String account, int type, int op) throws ClientException{
        String code = RandomCodeUtil.getCode();
        if(type == 1){ //手机
            if(op == 1){ //注册
                redisUtil.set("register:user:account:" + account + ":code", code, 300);
            }else if(op == 2){ //找回密码
                User user = userDao.findUserByAccount(account);
                if(user == null) {
                    return "error";
                }
                redisUtil.set("forgetPassword:user:account:"+account+":code", code, 300);
            }
            return SmsUtil.sendSms(account, code).getCode();
        }else{ //邮箱
            String content = "";
            if(op == 1){ //注册
                content = MailUtil.getRegisterEmailContent(account, code);
                MailUtil.sendMail(account, "注册", content);
                redisUtil.set("register:user:account:"+account+":code", code, 300);
            }else if(op == 2){ //找回密码
                User user = userDao.findUserByAccount(account);
                if(user == null){
                    return "error";
                }
                content = MailUtil.getForgetPasswordEmailContent(account, code);
                MailUtil.sendMail(account, "找回密码", content);
                redisUtil.set("forgetPassword:user:account:"+account+":code", code, 300);
            }
            return "ok";
        }
    }
}
