package com.zhbit.util;

public class RandomCodeUtil {

    public static String getCode(){
        return String.valueOf((int)((Math.random()*9+1)*100000));
    }

}
