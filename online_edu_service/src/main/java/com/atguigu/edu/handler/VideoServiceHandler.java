package com.atguigu.edu.handler;

import com.atguigu.edu.service.VideoServiceFeign;
import com.atguigu.response.RetVal;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Component
public class VideoServiceHandler implements VideoServiceFeign {
    @Override
    public RetVal uploadAliyunVideo(MultipartFile file) {
        //还有一大堆要处理业务逻辑代码
        return RetVal.error().message("兜底数据");
    }

    @Override
    public RetVal deleteSingleVideo(String videoId) {
        //还有一大堆要处理业务逻辑代码
        return RetVal.error().message("兜底数据");
    }

    @Override
    public RetVal deleteMultiVideo(List<String> videoIdList) {
        //还有一大堆要处理业务逻辑代码
        return RetVal.error().message("兜底数据");
    }
}
