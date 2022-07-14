package com.atguigu.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.service.VideoService;
import com.atguigu.utils.VideoUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class VideoServiceImpl implements VideoService {
    @Value("${oss.accessKeyId}")
    public String accessKeyId;
    @Value("${oss.accessKeySecret}")
    public String accessKeySecret;

    @Override
    public String uploadAliyunVideo(MultipartFile file) throws Exception {
        String videoId=null;
        //online.mp4
        String fileName = file.getOriginalFilename();
        String title=fileName.substring(0,fileName.lastIndexOf("."));
        //上传的文件是一个流
        InputStream inputStream = file.getInputStream();
        UploadStreamRequest streamRequest = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(streamRequest);
        //上传成功
        if (response.isSuccess()) {
            videoId=response.getVideoId();
        }
        return videoId;
    }

    @Override
    public boolean deleteSingleVideo(String videoId) throws Exception {
        DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
        DeleteVideoResponse response = new DeleteVideoResponse();
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoId);
        response = client.getAcsResponse(request);
        return true;
    }

    @Override
    public String getVideoPlayAuth(String videoId) {
        DefaultAcsClient client = VideoUtils.initVodClient(accessKeyId, accessKeySecret);
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);
            response =client.getAcsResponse(request);
            return response.getPlayAuth();
        } catch (Exception e) {
        }
        return null;
    }
}
