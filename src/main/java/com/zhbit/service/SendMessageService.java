package com.zhbit.service;

import com.aliyuncs.exceptions.ClientException;

public interface SendMessageService {

    String sendMessage(String account, int type, int op) throws ClientException;
}
