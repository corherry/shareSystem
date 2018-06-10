package com.zhbit.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class JsonUtil {

    public static String toJson(String key, Object value){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject.toString();
    }

    public static String ObjectToJson(Object value){
        return JSONObject.toJSONString(value);

    }

    public static Object jsonToBean(String json, Class<?> clazz){
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        return JSONObject.parseObject(json, clazz);
    }

    public static List<?> jsonToList(String json, Class<?> clazz){
        if (StringUtils.isBlank(json) || clazz == null) {
            return null;
        }
        return JSONObject.parseArray(json, clazz);
    }

}
