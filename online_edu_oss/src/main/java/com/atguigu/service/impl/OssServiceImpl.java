package com.atguigu.service.impl;

import com.atguigu.oss.OssTemplate;
import com.atguigu.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private OssTemplate ossTemplate;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        //1.文件的原始名称 45.20.jpg
        String fileName = file.getOriginalFilename();
        String filePrefix = UUID.randomUUID().toString().replaceAll("-", "");
        String fileSuffix=fileName.substring(fileName.lastIndexOf("."));
        fileName=filePrefix+fileSuffix;
        InputStream inputStream = file.getInputStream();
        return ossTemplate.uploadFile(fileName,inputStream);
    }

    @Override
    public boolean deleteFile(String fileName) throws Exception {
        ossTemplate.deleteSingleFile(fileName);
        return true;
    }
}
