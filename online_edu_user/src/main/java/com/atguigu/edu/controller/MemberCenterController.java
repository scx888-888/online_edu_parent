package com.atguigu.edu.controller;


import com.atguigu.edu.service.MemberCenterService;
import com.atguigu.edu.utils.JwtUtils;
import com.atguigu.response.MemberCenterVo;
import com.atguigu.response.RetVal;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-28
 */
@RestController
@RequestMapping("/member/center/")
@CrossOrigin
public class MemberCenterController {
    @Autowired
    private MemberCenterService centerService;

    //1.获取每天的注册人数
    @GetMapping("queryRegisterNum/{day}")
    public RetVal queryRegisterNum(@PathVariable("day") String day){
        Integer resterNum=centerService.queryRegisterNum(day);
        return RetVal.success().data("registerNum",resterNum);
    }

    //2.根据token获取用户的信息
    @GetMapping("getUserInfoByToken/{token}")
    public RetVal getUserInfoByToken(@PathVariable String token){
        Claims claims = JwtUtils.checkJWT(token);
        String id =(String) claims.get("id");
        String nickname =(String) claims.get("nickname");
        String avatar =(String) claims.get("avatar");

        MemberCenterVo memberCenterVo = new MemberCenterVo();
        memberCenterVo.setId(id);
        memberCenterVo.setNickname(nickname);
        memberCenterVo.setAvatar(avatar);
        return RetVal.success().data("memberCenterVo",memberCenterVo);
    }

}

