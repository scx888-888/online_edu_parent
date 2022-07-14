package com.atguigu.edu.controller;


import com.atguigu.edu.entity.EduOrder;
import com.atguigu.edu.service.EduOrderService;
import com.atguigu.edu.utils.JwtUtils;
import com.atguigu.response.RetVal;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author zhangqiang
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {
    @Autowired
    private EduOrderService orderService;
    //1.根据课程id下订单
    @GetMapping("createOrder/{courseId}")
    public RetVal createOrder(@PathVariable String courseId, HttpServletRequest request){
        //拿不到值 没有完善
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        String orderNo=orderService.createOrder(courseId,memberId);
        return RetVal.success().data("orderNo",orderNo);
    }

    //2.根据订单号查询订单信息
    @GetMapping("getOrderByOrderNo/{orderNo}")
    public RetVal getOrderByOrderNo(@PathVariable String orderNo){
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        EduOrder order = orderService.getOne(wrapper);
        return RetVal.success().data("orderInfo",order);
    }
    //3.微信服务器下单
    @GetMapping("createPayQrCode/{orderNo}")
    public RetVal createPayQrCode(@PathVariable String orderNo) throws Exception {
        Map<String, Object> map=orderService.createPayQrCode(orderNo);
        return RetVal.success().data(map);
    }
    //4.查询(微信那边)订单支付状态
    @GetMapping("queryPayState/{orderNo}")
    public RetVal queryPayState(@PathVariable String orderNo) throws Exception {
        Map<String, String> txRetMap=orderService.queryPayState(orderNo);
        //微信支付成功之后应该做的事情
        if(txRetMap.get("trade_state").equals("SUCCESS")){
            orderService.updateOrderStatus(txRetMap);
            return RetVal.success().message("支付成功");
        }else{
            return RetVal.error().message("支付失败");
        }

    }
}

