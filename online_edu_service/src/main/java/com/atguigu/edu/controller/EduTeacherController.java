package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduTeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.atguigu.request.TeacherConditionVO;
import com.atguigu.response.RetVal;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("/edu/teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;
//    //1.查询所有讲师
//    @GetMapping
//    public List<EduTeacher> getAllTeacher(){
//        List<EduTeacher> teacherList = teacherService.list(null);
//        EduTeacher teacher = new EduTeacher();
//        teacher.setSort(1).setName("xxx");
//        return teacherList;
//    }
//
//    //2.删除讲师
//    @DeleteMapping("{id}")
//    public boolean deleteTeacherById(@ApiParam(name="id",value = "讲师id",required = true) @PathVariable String id){
//        boolean flag = teacherService.removeById(id);
//        return flag;
//    }

    //1.查询所有讲师
    @GetMapping
    public RetVal getAllTeacher()  throws Exception{
        List<EduTeacher> teacherList = teacherService.list(null);
//        try {
//            int a=10/0;
//        } catch (Exception e) {
//            System.out.println("这是一个算术异常--写很多代码处理异常");
//            throw new EduException();
//        }
        return RetVal.success().data("teacherList",teacherList);
    }

    //2.删除讲师
    @DeleteMapping("{id}")
    public RetVal deleteTeacherById(@ApiParam(name="id",value = "讲师id",required = true) @PathVariable String id){
        boolean flag = teacherService.removeById(id);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //3.分页查询讲师信息
    @GetMapping("queryTeacherPage/{pageNum}/{pageSize}")
    public RetVal queryTeacherPage(@PathVariable long pageNum,
                                   @PathVariable long pageSize){
        Page<EduTeacher> teacherPage = new Page<>(pageNum,pageSize);
        teacherService.page(teacherPage,null);
        //总记录数
        long total = teacherPage.getTotal();
        List<EduTeacher> teacherList = teacherPage.getRecords();
        return RetVal.success().data("teacherList",teacherList).data("total",total);
    }
    //4.分页查询讲师信息带条件
    @GetMapping("queryTeacherPageByCondition/{pageNum}/{pageSize}")
    public RetVal queryTeacherPageByCondition(@PathVariable long pageNum,
                                              @PathVariable long pageSize,
                                              TeacherConditionVO teacherConditionVO){
        Page<EduTeacher> teacherPage = new Page<>(pageNum,pageSize);
        teacherService.queryTeacherPageByCondition(teacherPage, teacherConditionVO);
        //总记录数
        long total = teacherPage.getTotal();
        List<EduTeacher> teacherList = teacherPage.getRecords();
        return RetVal.success().data("teacherList",teacherList).data("total",total);
    }
    //5.添加讲师
    @PostMapping
    public RetVal saveTeacher(EduTeacher teacher){
        boolean flag = teacherService.save(teacher);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //6.修改讲师
    @PutMapping
    public RetVal updateTeacher(EduTeacher teacher){
        boolean flag = teacherService.updateById(teacher);
        if(flag){
            return RetVal.success();
        }else{
            return RetVal.error();
        }
    }
    //7.根据id查询讲师
    @GetMapping("{id}")
    public RetVal queryTeacherById(@PathVariable String id){
        EduTeacher teacher = teacherService.getById(id);
        return RetVal.success().data("teacher",teacher);
    }

    public static void main(String[] args) {
//        ArrayList<String> list=new ArrayList<String>();
//        list.add("111");
//        list.add("222");
//        list.add("333");
//        for (String s : list) {
//            list.add("www");
//            System.out.println(list);
//        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        System.out.println(format);
    }

}

