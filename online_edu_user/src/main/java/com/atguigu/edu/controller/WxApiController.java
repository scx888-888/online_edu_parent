package com.atguigu.edu.controller;

import com.atguigu.edu.entity.MemberCenter;
import com.atguigu.edu.service.MemberCenterService;
import com.atguigu.edu.utils.HttpClientUtils;
import com.atguigu.edu.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx/")
@CrossOrigin
public class WxApiController {
    @Autowired
    private MemberCenterService memberCenterService;
    @Value("${wx.open.app_id}")
    private String WX_OPEN_APP_ID;
    @Value("${wx.open.app_secret}")
    private String WX_OPEN_APP_SECRET;
    @Value("${wx.open.redirect_url}")
    private String WX_OPEN_REDIRECT_URL;


    //1.生成二维码
    @GetMapping("login")
    public String qrCode() throws Exception{
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //需要对浏览器地址进行编码
        String encodeUrl=URLEncoder.encode(WX_OPEN_REDIRECT_URL,"UTF-8");
        String state="atguigu";
        String qrCodeUrl = String.format(baseUrl, WX_OPEN_APP_ID, encodeUrl, state);
        return "redirect:"+qrCodeUrl;
    }

    //2.扫描成功之后的回调接口
    @GetMapping("callback")
    public String callback(String code) throws Exception{
        //a.通过code+appid+appsecret获取access_token+openid
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        baseAccessTokenUrl=String.format(baseAccessTokenUrl,WX_OPEN_APP_ID,WX_OPEN_APP_SECRET,code);
        //模拟发起http请求
        String retVal = HttpClientUtils.get(baseAccessTokenUrl);
        Gson gson = new Gson();
        HashMap retMap = gson.fromJson(retVal, HashMap.class);
        String accessToken =(String) retMap.get("access_token");
        String openid =(String) retMap.get("openid");

        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";
        userInfoUrl=String.format(userInfoUrl,accessToken,openid);
        //模拟发起http请求
        String infoVal = HttpClientUtils.get(userInfoUrl);
        HashMap infoMap = gson.fromJson(infoVal, HashMap.class);
        String nickname =(String) infoMap.get("nickname");
        String headimgurl =(String) infoMap.get("headimgurl");

        //判断数据库中是否已经有该记录
        QueryWrapper<MemberCenter> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openid);
        MemberCenter existMember = memberCenterService.getOne(wrapper);
        if(existMember==null){
            //把用户个人信息存储起来
            existMember = new MemberCenter();
            existMember.setNickname(nickname);
            existMember.setAvatar(headimgurl);
            existMember.setOpenid(openid);
            memberCenterService.save(existMember);
        }
        String token = JwtUtils.geneJsonWebToken(existMember);
        return "redirect:http://127.0.0.1:3000?token="+token;
    }

}
