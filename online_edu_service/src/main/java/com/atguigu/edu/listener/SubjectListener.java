package com.atguigu.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.entity.ExcelSubject;
import com.atguigu.edu.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SubjectListener extends AnalysisEventListener<ExcelSubject> {
    @Autowired
    private EduSubjectService subjectService;

    @Transactional
    @Override
    public void invoke(ExcelSubject excelSubject, AnalysisContext analysisContext) {
        //读取到的数据分为两列 第一列是一级分类 第二列是二级分类
        String parentSubjectName = excelSubject.getParentSubjectName();
        //保存一级分类的时候 需要判断该分类是否在数据库中存在 如果不存在 parent_id=0
        EduSubject parentSubject = subjectService.existSubject(parentSubjectName, "0");
        if(parentSubject==null){
            parentSubject=new EduSubject();
            parentSubject.setTitle(parentSubjectName);
            parentSubject.setParentId("0");
            //保存成功之后是否有id--答案是有
            subjectService.save(parentSubject);
        }
        String childSubjectName = excelSubject.getChildSubjectName();
        String parentSubjectId = parentSubject.getId();
        //保存二级分类的时候 需要判断该分类是否在数据库中存在 如果不存在 parent_id=一级分类的id
        EduSubject childSubject = subjectService.existSubject(childSubjectName, parentSubjectId);
        if(childSubject==null){
            childSubject=new EduSubject();
            childSubject.setTitle(childSubjectName);
            childSubject.setParentId(parentSubjectId);
            //保存成功之后是否有id
            subjectService.save(childSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
