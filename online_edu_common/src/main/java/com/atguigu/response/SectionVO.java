package com.atguigu.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SectionVO {
    @ApiModelProperty(value = "小节ID")
    private String id;
    @ApiModelProperty(value = "小节名称")
    private String title;
    @ApiModelProperty(value = "视频的id")
    private String videoSourceId;
}
