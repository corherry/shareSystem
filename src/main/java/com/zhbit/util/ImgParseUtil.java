package com.zhbit.util;

import java.util.Arrays;
import java.util.List;

public class ImgParseUtil {

    public static List<String> parseImg(String pic){
        String pics[] = pic.split(",");
        return Arrays.asList(pics);
    }

}
