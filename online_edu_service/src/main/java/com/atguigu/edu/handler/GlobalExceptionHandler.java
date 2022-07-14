package com.atguigu.edu.handler;

import com.atguigu.exception.EduException;
import com.atguigu.response.RetVal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    //只要你有异常就会被它捕获
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RetVal error(Exception e){
        System.out.println("全局异常出现了");
        e.printStackTrace();
        return RetVal.error().message(e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public RetVal error(ArithmeticException e){
        System.out.println("特殊异常出现了");
        e.printStackTrace();
        return RetVal.error().message(e.getMessage());
    }

    @ExceptionHandler(EduException.class)
    @ResponseBody
    public RetVal error(EduException e){
        System.out.println("自定义异常出现了");
        e.printStackTrace();
        return RetVal.error().message(e.getMessage());
    }
}
