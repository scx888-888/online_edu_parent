package com.atguigu.edu.service;

import com.atguigu.edu.handler.VideoServiceHandler;
import com.atguigu.response.RetVal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@FeignClient(value = "EDU-VIDEO",fallback = VideoServiceHandler.class)
public interface VideoServiceFeign {
    //1.视频上传
    @PostMapping("/aliyun/uploadAliyunVideo")
    public RetVal uploadAliyunVideo(MultipartFile file);
    //2.删除单个视频
    @DeleteMapping("/aliyun/{videoId}")
    public RetVal deleteSingleVideo(@PathVariable("videoId") String videoId);
    //3.删除多个视频
    @DeleteMapping("/aliyun/deleteMultiVideo")
    public RetVal deleteMultiVideo(@RequestParam("videoIdList") List<String> videoIdList);
}
