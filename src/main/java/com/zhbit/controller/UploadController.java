package com.zhbit.controller;

import com.zhbit.util.JsonUtil;
import com.zhbit.util.OSSClientUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Scope("prototype")
@RequestMapping("/uploadController")
public class UploadController {

    @ResponseBody
    @RequestMapping(value = "/uploadTopicImage", method = RequestMethod.POST)
    public String uploadTopicImage(MultipartFile file){
        OSSClientUtil ossClientUtil = new OSSClientUtil();
        String url = ossClientUtil.uploadObject2OSS(file, "image/upload/");
        return JsonUtil.toJson("url", url);
    }
}
