package com.atguigu.edu.controller.front;


import com.atguigu.edu.entity.EduBanner;
import com.atguigu.edu.entity.EduCourse;
import com.atguigu.edu.entity.EduTeacher;
import com.atguigu.edu.service.EduBannerService;
import com.atguigu.edu.service.EduCourseService;
import com.atguigu.edu.service.EduTeacherService;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/edu/front/")
@CrossOrigin
public class FrontIndexController {
    @Autowired
    private EduBannerService bannerService;
    @Autowired
    private EduTeacherService teacherService;
    @Autowired
    private EduCourseService courseService;
    @Autowired
    private RedisTemplate redisTemplate;



    //1.首页banner
//    @GetMapping("getAllBanner")
//    public RetVal getAllBanner() {
//        StringRedisSerializer keySerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(keySerializer);
//        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jacksonSerializer.setObjectMapper(om);
//        redisTemplate.setValueSerializer(jacksonSerializer);
//
//        List<EduBanner> bannerList = (List<EduBanner>)redisTemplate.opsForValue().get("index::banner");
//        if(bannerList==null){
//            bannerList = bannerService.list(null);
//            redisTemplate.opsForValue().set("index::banner",bannerList);
//        }
//        return RetVal.success().data("bannerList",bannerList);
//    }
    //1.首页banner
    @Cacheable(value = "index",key = "'banner'")
    @GetMapping("getAllBanner")
    public RetVal getAllBanner() {
        List<EduBanner> bannerList = bannerService.list(null);
        return RetVal.success().data("bannerList",bannerList);
    }
    //2.讲师和课程信息
    @Cacheable(value="index",key = "'teacherCourse'")
    @GetMapping("queryTeacherAndCourse")
    public RetVal queryTeacherAndCourse() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> courseList = courseService.list(wrapper);

        QueryWrapper<EduTeacher> teacherWrapper = new QueryWrapper<>();
        teacherWrapper.orderByAsc("sort");
        teacherWrapper.last("limit 4");
        List<EduTeacher> teacherList = teacherService.list(teacherWrapper);

        return RetVal.success()
                .data("courseList",courseList)
                .data("teacherList",teacherList);
    }



}

