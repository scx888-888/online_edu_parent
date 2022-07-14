package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduChapter;
import com.atguigu.edu.entity.EduSection;
import com.atguigu.edu.mapper.EduChapterMapper;
import com.atguigu.edu.service.EduChapterService;
import com.atguigu.edu.service.EduSectionService;
import com.atguigu.exception.EduException;
import com.atguigu.response.ChapterVO;
import com.atguigu.response.SectionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-25
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduSectionService sectionService;

    @Override
    public boolean saveChapter(EduChapter chapter) {
        //判断是否存在
        EduChapter existChapter = existChapter(chapter.getCourseId(), chapter.getTitle());
        if (existChapter == null) {
            int insert = baseMapper.insert(chapter);
            return insert > 0;
        } else {
            throw new EduException(20001, "章节已经重复");
        }
    }

    public EduChapter existChapter(String courseId, String chapterName) {
        QueryWrapper<EduChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.eq("title", chapterName);
        EduChapter chapter = baseMapper.selectOne(queryWrapper);
        return chapter;

    }

    @Override
    public boolean deleteChapter(String chapterId) {
        //判断是否有小节
        QueryWrapper<EduSection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapterId);
        int count = sectionService.count(queryWrapper);
        //没有小节
        if (count == 0) {
            int i = baseMapper.deleteById(chapterId);
            return i > 0;
        } else {
            throw new EduException(20001, "该章节存在小节");
        }
    }

    @Override
    public List<ChapterVO> getChapterAndSection(String courseId) {
        ArrayList<ChapterVO> chapterVoList = new ArrayList<>();
        //a.设计一个返回给章节小节的数据结构
        //b.查询所有的章节
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<EduChapter> chapterList = baseMapper.selectList(wrapper);
        //c.查询所有的小节
        QueryWrapper<EduSection> wrapperSection = new QueryWrapper<>();
        wrapperSection.eq("course_id", courseId);
        List<EduSection> sectionList = sectionService.list(wrapperSection);
        //d.把小节塞到章节里面去 进行迭代
        for (EduChapter chapter : chapterList) {
            ChapterVO chapterVO = new ChapterVO();
            BeanUtils.copyProperties(chapter,chapterVO);
            //把小节封装到章节里面
            for (EduSection section : sectionList) {
                //判断条件 section里面的chapterId=章节的id
                if(section.getChapterId().equals(chapter.getId())){
                    SectionVO sectionVO = new SectionVO();
                    BeanUtils.copyProperties(section,sectionVO);
                    chapterVO.getChildren().add(sectionVO);
                }
            }
            chapterVoList.add(chapterVO);
        }
        return chapterVoList;
    }

    @Override
    public void deleteChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
