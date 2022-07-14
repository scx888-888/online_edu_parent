package com.atguigu.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
@Component
public class OssTemplate {
    @Value("${oss.endpoint}")
    public String endPoint;
    @Value("${oss.accessKeyId}")
    public String accessKeyId;
    @Value("${oss.accessKeySecret}")
    public String accessKeySecret;
    @Value("${oss.bucketName}")
    public String bucketName;

    //1.文件的上传
    public String uploadFile(String fileName,InputStream inputStream) throws Exception {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        //返回一个文件地址 https://java0106.oss-cn-hangzhou.aliyuncs.com/2.jpg
        String retUrl="https://"+bucketName+"."+endPoint+"/"+fileName;
        return retUrl;
    }

    //2.文件的删除
    public void deleteSingleFile(String fileName) throws Exception {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, fileName);
        ossClient.shutdown();
    }

    //3.文件的多个删除
    public void deleteMultiFile(List<String> fileNameList) throws Exception {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNameList));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        ossClient.shutdown();
    }
}
