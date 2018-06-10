package com.zhbit.service.impl;

import com.zhbit.service.TokenService;
import com.zhbit.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisUtil redisUtil;

    public int getUserByToken(String token) {
        int id = Integer.parseInt((String)redisUtil.get("login:user:id:" + token));
        return id;
    }
}
