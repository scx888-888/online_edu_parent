package com.atguigu.edu.controller;

import com.atguigu.response.RetVal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/edu")
@CrossOrigin
public class EntranceController {
    @PostMapping("/user/login")
    public RetVal login(){
            return RetVal.success().data("token","admin");
    }
    @GetMapping("/user/info")
    public RetVal userInfo(){
        return RetVal.success().data("roles","[scx]")
                .data("name","scx")
                .data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @PostMapping("/user/logout")
    public RetVal logout(){
        return RetVal.success().data("token",null);
    }




}
