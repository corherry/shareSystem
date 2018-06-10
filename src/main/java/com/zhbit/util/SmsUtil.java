package com.zhbit.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Autowired;


public class SmsUtil {

    //产品名称
    private static final String PRODUCT = "Dysmsapi";
    //产品域名
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private static final String ACCESS_KEY_ID = "LTAIKQTMPstvrg4B";

    private static final String ACCESS_KEY_SECRET = "mKHcTRpgCPGfDWOD4P5KVOvH37WKe4";

    private static final String TIMEOUT = "300000";

    private static final String SIGNNAME = "乐享";

    private static final String TEMPLATECODE = "SMS_130245124";


    public static SendSmsResponse sendSms(String phone, String code) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTIMEOUT", TIMEOUT);
        System.setProperty("sun.net.client.defaultReadTIMEOUT", TIMEOUT);

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", PRODUCT, DOMAIN);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();

        request.setMethod(MethodType.POST);
        //待发送手机号
        request.setPhoneNumbers(phone);
        //短信签名
        request.setSignName(SIGNNAME);
        //短信模板
        request.setTemplateCode(TEMPLATECODE);
        //模板中的变量替换JSON串
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        SendSmsResponse sendSmsResponse = new SendSmsResponse();
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        }catch (ClientException e){
            e.printStackTrace();
        }

        return sendSmsResponse;
    }


}
