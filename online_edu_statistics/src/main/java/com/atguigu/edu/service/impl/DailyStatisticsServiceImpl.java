package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.DailyStatistics;
import com.atguigu.edu.mapper.DailyStatisticsMapper;
import com.atguigu.edu.service.DailyStatisticsService;
import com.atguigu.edu.service.UserServiceFeign;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-28
 */
@Service
public class DailyStatisticsServiceImpl extends ServiceImpl<DailyStatisticsMapper, DailyStatistics> implements DailyStatisticsService {
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Override
    public void generateData(String day) {
        //1.通过远程RPC调用各个微服务获取数据
        RetVal retVal = userServiceFeign.queryRegisterNum(day);
        Integer registerNum=(Integer) retVal.getData().get("registerNum");
        //2.拿到数据之后保存到数据库里面
        DailyStatistics dailyStatistics = new DailyStatistics();
        //设置日期
        dailyStatistics.setDateCalculated(day);
        dailyStatistics.setRegisterNum(registerNum);
        dailyStatistics.setLoginNum(RandomUtils.nextInt(400,500));
        dailyStatistics.setVideoViewNum(RandomUtils.nextInt(300,400));
        dailyStatistics.setCourseNum(RandomUtils.nextInt(100,200));
        baseMapper.insert(dailyStatistics);
    }

    @Override
    public Map<String, Object> showStatistics(String dataType, String beginTime, String endTime) {
        //返回X轴数据(时间)与Y轴数据(对应数量信息)
        QueryWrapper<DailyStatistics> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated",beginTime,endTime);
        wrapper.orderByAsc("date_calculated");
        List<DailyStatistics> statisticsList = baseMapper.selectList(wrapper);
        List<String> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();
        for (DailyStatistics statistics : statisticsList) {
            //单个日期
            String dateCalculated = statistics.getDateCalculated();
            xData.add(dateCalculated);
            switch (dataType){
                case "login_num":
                    //日期对于的登录数据
                    Integer loginNum = statistics.getLoginNum();
                    yData.add(loginNum);
                    break;
                case "register_num":
                    //日期对于的登录数据
                    Integer registerNum = statistics.getRegisterNum();
                    yData.add(registerNum);
                    break;
                case "video_view_num":
                    //日期对于的登录数据
                    Integer videoViewNum = statistics.getVideoViewNum();
                    yData.add(videoViewNum);
                    break;
                case "course_num":
                    //日期对于的登录数据
                    Integer courseNum = statistics.getCourseNum();
                    yData.add(courseNum);
                    break;
                default:
                    break;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("xData",xData);
        map.put("yData",yData);
        return map;
    }
}
