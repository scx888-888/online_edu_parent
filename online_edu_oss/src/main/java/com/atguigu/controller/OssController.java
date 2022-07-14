package com.atguigu.controller;

import com.atguigu.service.OssService;
import com.atguigu.response.RetVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/oss")
@CrossOrigin
public class OssController {
    @Autowired
    private OssService ossService;
    //1.文件上传
    @PostMapping("uploadFile")
    public RetVal uploadFile(MultipartFile file) throws Exception {
        String retUrl=ossService.uploadFile(file);
        return RetVal.success().data("retUrl",retUrl);
    }
    //1.文件上传
    @PostMapping("uploadFile1")
    public RetVal uploadFile1(@RequestPart("avatar") MultipartFile avatar,
                             @RequestPart("life") MultipartFile life,
                             @RequestPart("secret") MultipartFile secret) {

        return RetVal.success();
    }

    //1.文件上传
    @PostMapping("uploadFile2")
    public RetVal uploadFile2(@RequestPart("avatar") MultipartFile avatar,
                             @RequestPart("secret") MultipartFile[] secret) {

        return RetVal.success();
    }

    //1.文件上传
    @PostMapping("uploadFile3")
    public RetVal uploadFile3(@RequestPart("avatar") MultipartFile avatar,
                             @RequestPart("secret") MultipartFile secret,
                             @RequestParam("secretDesc") String secretDesc) {

        return RetVal.success();
    }
    //1.文件上传
    @PostMapping("uploadFile4")
    public RetVal uploadFile4(@RequestPart("images") MultipartFile[] images) {

        return RetVal.success();
    }


    //2.文件删除
    @PostMapping("deleteFile")
    public RetVal deleteFile(String fileName) throws Exception {
        boolean flag=ossService.deleteFile(fileName);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }


}
