package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.service.EduSubjectService;
import com.atguigu.response.RetVal;
import com.atguigu.response.SubjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-23
 */
@RestController
@RequestMapping("/edu/subject")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;
    //1.导入excel数据到数据库中
    @PostMapping("uploadSubject")
    public RetVal uploadSubject(MultipartFile file) throws IOException {
        subjectService.uploadSubject(file);
        return RetVal.success();
    }
    //2.课程分类列表展示
    @GetMapping("getAllSubject")
    public RetVal getAllSubject() {
        List<SubjectVo> subjectVoList=subjectService.getAllSubject();
        return RetVal.success().data("allSubjectVo",subjectVoList);
    }
    //3.根据节点id删除
    @DeleteMapping("{id}")
    public RetVal deleteSubjectById(@PathVariable String id) {
        boolean flag=subjectService.deleteSubjectById(id);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //4.添加一级分类
    @PostMapping("saveParentSubject")
    public RetVal saveParentSubject(EduSubject subject) {
        boolean flag=subjectService.saveParentSubject(subject);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //5.添加二级分类
    @PostMapping("saveChildSubject")
    public RetVal saveChildSubject(EduSubject subject) {
        boolean flag=subjectService.saveChildSubject(subject);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }

}

