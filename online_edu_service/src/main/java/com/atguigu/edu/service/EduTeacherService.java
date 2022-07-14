package com.atguigu.edu.service;

import com.atguigu.edu.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.request.TeacherConditionVO;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-18
 */
public interface EduTeacherService extends IService<EduTeacher> {

    void queryTeacherPageByCondition(Page<EduTeacher> teacherPage, TeacherConditionVO teacherCondition);

    Map<String, Object> queryTeacherPage(Page<EduTeacher> teacherPage);
}
