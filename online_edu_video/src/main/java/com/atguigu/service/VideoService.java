package com.atguigu.service;

import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    String uploadAliyunVideo(MultipartFile file) throws Exception;

    boolean deleteSingleVideo(String videoId) throws Exception;

    String getVideoPlayAuth(String videoId);
}
