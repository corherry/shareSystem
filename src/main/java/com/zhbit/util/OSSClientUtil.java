package com.zhbit.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;

public class OSSClientUtil {

    private String endpoint = "oss-cn-beijing.aliyuncs.com";
    // accessKey
    private String accessKeyId = "LTAIKQTMPstvrg4B";
    private String accessKeySecret = "mKHcTRpgCPGfDWOD4P5KVOvH37WKe4";
    //空间
    private String bucketName = "share-system";

    private OSSClient ossClient;

    public OSSClientUtil() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    public void init(){
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }
    /**
     * 上传图片至OSS
     * @param file 上传文件
     * @param folderdir 模拟文件夹名
     * @return String 返回的唯一图片名称
     * */
    public String uploadObject2OSS(MultipartFile file, String folderdir) {
        String url = null;
        try {
            //以输入流的形式上传文件
            InputStream is = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String fileType=fileName.substring(fileName.lastIndexOf(".")+1);

            //文件名
            String fileNameNew = UUID.randomUUID().toString().toUpperCase().replace("-", "")+"."+fileType;

            //创建上传Object的Metadata
            ObjectMetadata metadata = new ObjectMetadata();
            //上传的文件的长度
            metadata.setContentLength(is.available());
            //指定该Object被下载时的网页的缓存行为
            metadata.setCacheControl("no-cache");
            //指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            //指定该Object被下载时的内容编码格式
            metadata.setContentEncoding("utf-8");
            //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            //如果没有扩展名则填默认值application/octet-stream
            metadata.setContentType(getContentType(fileNameNew));

            //上传文件   (上传文件流的形式)
            PutObjectResult putResult = ossClient.putObject(bucketName, folderdir + fileNameNew, is, metadata);
            url = "https://share-system.oss-cn-beijing.aliyuncs.com/" + folderdir + fileNameNew;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 文件的contentType
     */
    public String getContentType(String fileName){
        //文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if(".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if(".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if(".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)  || ".png".equalsIgnoreCase(fileExtension) ) {
            return "image/jpeg";
        }
        if(".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if(".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if(".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if(".ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if(".doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if(".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        //默认返回类型
        return "image/jpeg";
    }

}

