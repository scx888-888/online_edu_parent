package com.atguigu.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.edu.entity.EduSubject;
import com.atguigu.edu.entity.ExcelSubject;
import com.atguigu.edu.listener.SubjectListener;
import com.atguigu.edu.mapper.EduSubjectMapper;
import com.atguigu.edu.service.EduSubjectService;
import com.atguigu.exception.EduException;
import com.atguigu.response.SubjectVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Autowired
    private SubjectListener subjectListener;

    @Override
    public void uploadSubject(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream, ExcelSubject.class,subjectListener).doReadAll();
    }

    @Override
    public EduSubject existSubject(String parentSubjectName, String parentId) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();

        wrapper.eq("parent_id",parentId);
        wrapper.eq("title",parentSubjectName);
        EduSubject existSubject = baseMapper.selectOne(wrapper);
        return existSubject;
    }

    @Override
    public List<SubjectVo> getAllSubject() {
        //a.设计一个返回给前端页面的vo对象
        //b.查询所有课程分类
        List<EduSubject> allSubjectList = baseMapper.selectList(null);
        //存放一级课程分类集合(前端)
        ArrayList<SubjectVo> parentSubjectVoList = new ArrayList<>();
        //c.先找到所有的一级分类(组长)
        for (EduSubject eduSubject : allSubjectList) {
            //判断标准 parent_id=0
            if(eduSubject.getParentId().equals("0")){
                SubjectVo subjectVo = new SubjectVo();
                subjectVo.setId(eduSubject.getId());
                subjectVo.setTitle(eduSubject.getTitle());
                parentSubjectVoList.add(subjectVo);
            }
        }
        //d.把一级分类放到一个角落(map) 为了二级分类更好的找到一级分类
        Map<String, SubjectVo> parentSubjectMap = new HashMap<>();
        for (SubjectVo parentSubjectVo : parentSubjectVoList) {
            //key一级分类的id  value一级分类对象本身
            parentSubjectMap.put(parentSubjectVo.getId(),parentSubjectVo);
        }
        //e.找到所有的二级分类(找组员)
        for (EduSubject chidlSubject : allSubjectList) {
            //判断标准 parent_id!=0
            if(!chidlSubject.getParentId().equals("0")){
                //拿到二级分类里面的parentId(组号) 从map中找到该二级分类的一级分类
                SubjectVo parentSubjectVo = parentSubjectMap.get(chidlSubject.getParentId());
                SubjectVo childSubjectVo=new SubjectVo();
                childSubjectVo.setId(chidlSubject.getId());
                childSubjectVo.setTitle(chidlSubject.getTitle());
                //得到一级分类的chidren 把二级分类赛进去 成为它的子节点
                parentSubjectVo.getChildren().add(childSubjectVo);
            }
        }
        //f.返回所有的一级分类
        return parentSubjectVoList;
    }

    @Override
    public boolean deleteSubjectById(String id) {
        //拿到这个id去查它是否有子节点
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id",id);
        Integer count = baseMapper.selectCount(wrapper);
        //如果count==0 代表没有子节点 可以删除
        if(count==0){
            int rows = baseMapper.deleteById(id);
            return rows>0;
        }else{
           throw new EduException(20001,"该节点存在子节点");
        }

    }

    @Override
    public boolean saveParentSubject(EduSubject subject) {
        //先判断一级分类是否存在
        EduSubject parentSubject = existSubject(subject.getTitle(), "0");
        if(parentSubject==null){
            parentSubject=new EduSubject();
            parentSubject.setTitle(subject.getTitle());
            parentSubject.setParentId("0");
            //保存成功之后是否有id--答案是有
            int rows = baseMapper.insert(parentSubject);
            return rows>0;
        }
        return false;
    }

    @Override
    public boolean saveChildSubject(EduSubject subject) {
        //先判断二级分类是否存在
        EduSubject childSubject = existSubject(subject.getTitle(), subject.getParentId());
        if(childSubject==null){
            int rows = baseMapper.insert(subject);
            return rows>0;
        }
        return false;
    }
}
