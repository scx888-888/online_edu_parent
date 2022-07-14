package com.atguigu;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OssTest {
    //1.文件的上传
    @Test
    public void testUpload() throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tNH2GF4SMBs8koHrVTU";
        String accessKeySecret = "ZNy4DYynQFRVrkGzZ4fUvkzfrnFPTO";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "java0106";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream = new FileInputStream("C:\\220106\\1.jpg");
        //第二个参数代表上传到阿里云OSS之后名字叫什么
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "new.jpg", inputStream);
        // 上传文件。
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
    }

    //2.文件的删除
    @Test
    public void testDeleteSingleFile() throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tNH2GF4SMBs8koHrVTU";
        String accessKeySecret = "ZNy4DYynQFRVrkGzZ4fUvkzfrnFPTO";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "java0106";
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        String objectName = "白色4.jpg";
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件或目录。如果要删除目录，目录必须为空。
        ossClient.deleteObject(bucketName, objectName);
        ossClient.shutdown();
    }

    //3.文件的多个删除
    @Test
    public void testDeleteMultiFile() throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tNH2GF4SMBs8koHrVTU";
        String accessKeySecret = "ZNy4DYynQFRVrkGzZ4fUvkzfrnFPTO";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "java0106";
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件或目录。如果要删除目录，目录必须为空。
        List<String> fileNameList = new ArrayList<String>();
        fileNameList.add("new.jpg");
        fileNameList.add("2.jpg");
        DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(fileNameList));
        List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
        ossClient.shutdown();
    }


}
