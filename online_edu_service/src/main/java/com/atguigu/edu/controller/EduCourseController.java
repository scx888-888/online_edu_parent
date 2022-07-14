package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.request.CourseCondition;
import com.atguigu.request.CourseInfoVo;
import com.atguigu.response.CourseConfirmVO;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-24
 */
@RestController
@RequestMapping("/edu/course")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    //1.保存课程信息
    @PostMapping("saveCourseInfo")
    public RetVal saveCourseInfo(CourseInfoVo courseInfoVo){
        courseService.saveCourseInfo(courseInfoVo);
        return RetVal.success();
    }
    //2.分页查询课程信息带条件
    @GetMapping("queryCoursePageByCondition/{pageNum}/{pageSize}")
    public RetVal queryCoursePageByCondition(
            @PathVariable("pageNum") long pageNum,
            @PathVariable("pageSize") long pageSize,
            CourseCondition courseCondition) {
        Page<EduCourse> coursePage = new Page<>(pageNum, pageSize);
        courseService.queryCoursePageByCondition(coursePage, courseCondition);
        long total = coursePage.getTotal();
        List<EduCourse> courseList = coursePage.getRecords();
        return RetVal.success().data("total", total).data("courseList", courseList);
    }

    //3.根据课程id查询课程信息
    @GetMapping("{id}")
    public RetVal getCourseById(@PathVariable String id){
        CourseInfoVo courseInfoVo=courseService.getCourseById(id);
        return RetVal.success().data("courseInfoVo",courseInfoVo);
    }

    //4.更新课程信息
    @PutMapping("updateCourseInfo")
    public RetVal updateCourseInfo(CourseInfoVo courseInfoVo){
        courseService.updateCourseInfo(courseInfoVo);
        return RetVal.success();
    }

    //5.课程发布确认
    @GetMapping("queryCourseConfirmInfo/{courseId}")
    public RetVal queryCourseConfirmInfo(@PathVariable String courseId){
        CourseConfirmVO courseConfirmVO=courseService.queryCourseConfirmInfo(courseId);
        return RetVal.success().data("courseConfirmVO",courseConfirmVO);
    }
    //6.课程的发布
    @GetMapping("publishCourse/{courseId}")
    public RetVal publishCourse(@PathVariable String courseId){
        EduCourse course = new EduCourse();
        course.setId(courseId);
        course.setStatus("Normal");
        courseService.updateById(course);
        return RetVal.success();
    }
    //7.删除课程
    @DeleteMapping("{courseId}")
    public RetVal deleteCourseById(@PathVariable String courseId){
        courseService.deleteCourseById(courseId);
        return RetVal.success();
    }


}

