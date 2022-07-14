package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduCourseDescription;
import com.atguigu.edu.mapper.EduCourseMapper;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduCourseDescriptionService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.request.CourseCondition;
import com.atguigu.request.CourseInfoVo;
import com.atguigu.response.CourseConfirmVO;
import com.atguigu.response.CourseDetailInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-24
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    private EduCourseDescriptionService descriptionService;
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduSectionService sectionService;
    @Override
    public void saveCourseInfo(CourseInfoVo courseInfoVo) {
        EduCourse course = new EduCourse();
//        course.setTitle(courseInfoVo.getTitle());
//        course.setTeacherId(courseInfoVo.getTeacherId());
        BeanUtils.copyProperties(courseInfoVo,course);
        //c.保存课程基本信息
        baseMapper.insert(course);
        //d.保存课程描述信息
        EduCourseDescription description = new EduCourseDescription();
        //e.两张表共用一个主键
        description.setId(course.getId());
        description.setDescription(courseInfoVo.getDescription());
        descriptionService.save(description);

    }

    @Override
    public void queryCoursePageByCondition(Page<EduCourse> coursePage, CourseCondition courseCondition) {
        //获取每个查询参数
        String title = courseCondition.getTitle();
        String status = courseCondition.getStatus();
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        //判断以上传递过来的参数是否为空
        if(StringUtils.isNotEmpty(title)){
            wrapper.like("title",title);
        }

        if(StringUtils.isNotEmpty(status)){
            wrapper.ge("status",status);
        }
        baseMapper.selectPage(coursePage,wrapper);
    }

    @Override
    public CourseInfoVo getCourseById(String id) {
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        //查询课程基本信息
        EduCourse course = baseMapper.selectById(id);
        BeanUtils.copyProperties(course,courseInfoVo);
        //查询课程详细信息
        EduCourseDescription courseDescription = descriptionService.getById(id);
        if(courseDescription!=null){
            courseInfoVo.setDescription(courseDescription.getDescription());
        }
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //修改基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,course);
        baseMapper.updateById(course);
        //修改描述信息
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        descriptionService.updateById(description);
    }

    @Override
    public CourseConfirmVO queryCourseConfirmInfo(String courseId) {
        return baseMapper.queryCourseConfirmInfo(courseId);
    }

    @Override
    public void deleteCourseById(String courseId) {
        //a.该课程所对应章节
        chapterService.deleteChapterByCourseId(courseId);
        //b.该课程所对应小节
        sectionService.deleteSectionByCourseId(courseId);
        //c.该课程所对应描述信息
        descriptionService.removeById(courseId);
        //d.该课程所对应的基本信息
        baseMapper.deleteById(courseId);
        //e.整个过程考虑事务
    }

    @Override
    public CourseDetailInfoVO queryCourseDetailById(String courseId) {
        return baseMapper.queryCourseDetailById(courseId);
    }
}
