package com.atguigu.controller;

import com.atguigu.response.RetVal;
import com.atguigu.service.VideoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/aliyun")
@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;
    //1.视频上传
    @PostMapping("uploadAliyunVideo")
    public RetVal uploadAliyunVideo(MultipartFile file) throws Exception{
        String videoId=videoService.uploadAliyunVideo(file);
        return RetVal.success().data("videoId",videoId);
    }
    //2.删除单个视频
    @DeleteMapping("{videoId}")
    public RetVal deleteSingleVideo(@PathVariable("videoId") String videoId) throws Exception{
        boolean flag =videoService.deleteSingleVideo(videoId);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }

    //3.删除多个视频
    @DeleteMapping("deleteMultiVideo")
    public RetVal deleteMultiVideo(@RequestParam("videoIdList") List<String> videoIdList) throws Exception{
        //把一个list转换为一个String 以逗号分隔开
        String videoIdListString = StringUtils.join(videoIdList, ",");
        boolean flag =videoService.deleteSingleVideo(videoIdListString);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }

    //4.获取播放凭证函数
    @GetMapping("getVideoPlayAuth/{videoId}")
    public RetVal getVideoPlayAuth(@PathVariable String videoId) throws Exception{
        String playAuth=videoService.getVideoPlayAuth(videoId);
        return RetVal.success().data("playAuth",playAuth);
    }

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
//        StringBuilder sb = new StringBuilder();
//        for (String s : list) {
//            sb.append(s+",");
//        }
//        System.out.println(sb.toString());
        String join = StringUtils.join(list, ",");
        System.out.println(join);
    }





}
