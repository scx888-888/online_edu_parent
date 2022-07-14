package com.atguigu.easyexcel;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EasyExcelTest {
    //1.往excel中写数据
    @Test
    public void testWriteExcel(){
        //a.往那个文件中写数据
        String filePath="C:\\220106\\one.xls";
        //b.写什么样的数据
        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Student student = new Student();
            student.setStuNo(i);
            student.setStuName("子康哥哥"+i);
            studentList.add(student);
        }
        //c.往那个sheet写数据
        EasyExcel.write(filePath,Student.class).sheet("学生列表").doWrite(studentList);
    }

    //2.从excel中读数据
    @Test
    public void testReadExcel(){
        //a.往那个文件中写数据
        String filePath="C:\\220106\\one.xls";
        EasyExcel.read(filePath,Student.class,new ReadExcelListener()).doReadAll();
    }
}
