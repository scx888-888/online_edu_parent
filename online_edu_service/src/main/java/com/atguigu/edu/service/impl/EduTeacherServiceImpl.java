package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.mapper.EduTeacherMapper;
import com.atguigu.edu.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.request.TeacherConditionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-18
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public void queryTeacherPageByCondition(Page<EduTeacher> teacherPage, TeacherConditionVO teacherConditionVO) {
        //1.构造一个查询条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        String name = teacherConditionVO.getName();
        //判断是否为空
        if(StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        Integer level = teacherConditionVO.getLevel();
        if(level!=null){
            wrapper.eq("level",level);
        }
        String beginTime = teacherConditionVO.getBeginTime();
        String endTime = teacherConditionVO.getEndTime();
        if(StringUtils.isNotEmpty(beginTime)){
            wrapper.ge("gmt_create",beginTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            wrapper.le("gmt_create",endTime);
        }
        //增加倒序排
        wrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(teacherPage,wrapper);
    }

    @Override
    public Map<String, Object> queryTeacherPage(Page<EduTeacher> teacherPage) {
        baseMapper.selectPage(teacherPage,null);
        boolean hasPrevious = teacherPage.hasPrevious();
        boolean hasNext = teacherPage.hasNext();
        long currentPage = teacherPage.getCurrent();
        long pages = teacherPage.getPages();
        long total = teacherPage.getTotal();
        long size = teacherPage.getSize();
        List<EduTeacher> teacherList = teacherPage.getRecords();

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("teacherList", teacherList);
        retMap.put("pages", pages);
        retMap.put("total", total);
        retMap.put("currentPage", currentPage);
        retMap.put("size", size);
        retMap.put("hasNext", hasNext);
        retMap.put("hasPrevious", hasPrevious);
        return retMap;
    }
}
