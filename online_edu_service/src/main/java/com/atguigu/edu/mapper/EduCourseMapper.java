package com.atguigu.edu.mapper;

import com.atguigu.edu.entity.EduCourse;
import com.atguigu.response.CourseConfirmVO;
import com.atguigu.response.CourseDetailInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-24
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CourseConfirmVO queryCourseConfirmInfo(@Param("courseId") String courseId);

    CourseDetailInfoVO queryCourseDetailById(String courseId);
}
