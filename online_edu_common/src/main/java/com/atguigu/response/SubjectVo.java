package com.atguigu.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubjectVo {
    @ApiModelProperty(value = "课程分类id")
    private String id;
    @ApiModelProperty(value = "课程分类名称")
    private String title;
    @ApiModelProperty(value = "课程分类子集合")
    private List<SubjectVo> children=new ArrayList<>();
}
