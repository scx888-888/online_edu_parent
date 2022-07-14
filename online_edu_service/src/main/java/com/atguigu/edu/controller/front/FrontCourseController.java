package com.atguigu.edu.controller.front;


import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.response.ChapterVO;
import com.atguigu.response.CourseDetailInfoVO;
import com.atguigu.response.RetVal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-18
 */
@RestController
@RequestMapping("/edu/front/course")
@CrossOrigin
public class FrontCourseController {
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduCourseService courseService;

    //1.分页查询课程列表
    //2.课程详情信息的查询
    @GetMapping("queryCourseDetailById/{courseId}")
    public RetVal queryCourseDetailById(@PathVariable String courseId) {
        //a.章节小节对应关系
        List<ChapterVO> chapterAndSection = chapterService.getChapterAndSection(courseId);
        //b.课程详情其余部分
        CourseDetailInfoVO courseDetailInfoVO=courseService.queryCourseDetailById(courseId);
        return RetVal.success()
                .data("courseDetailInfoVO",courseDetailInfoVO)
                .data("chapterAndSection",chapterAndSection);
    }


}

