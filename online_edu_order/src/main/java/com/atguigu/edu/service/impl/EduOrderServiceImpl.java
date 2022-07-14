package com.atguigu.edu.service.impl;

import com.atguigu.edu.entity.EduOrder;
import com.atguigu.edu.entity.EduPayLog;
import com.atguigu.edu.mapper.EduOrderMapper;
import com.atguigu.edu.service.EduOrderService;
import com.atguigu.edu.service.EduPayLogService;
import com.atguigu.edu.utils.HttpClient;
import com.atguigu.edu.utils.OrderNoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-06-02
 */
@Service
public class EduOrderServiceImpl extends ServiceImpl<EduOrderMapper, EduOrder> implements EduOrderService {
    @Value("${wx.pay.app_id}")
    private String WX_PAY_APP_ID;
    @Value("${wx.pay.mch_id}")
    private String WX_PAY_MCH_ID;
    @Value("${wx.pay.spbill_create_ip}")
    private String WX_PAY_SPBILL_IP;
    @Value("${wx.pay.xml_key}")
    private String WX_PAY_XML_KEY;
    @Value("${wx.pay.notify_url}")
    private String WX_PAY_NOTIFY_URL;

    @Autowired
    private EduPayLogService payLogService;

    @Override
    public String createOrder(String courseId, String memberId) {
        EduOrder order = new EduOrder();
        //b.生成订单号 需要唯一
        String orderNo = OrderNoUtil.getOrderNo();
        order.setOrderNo(orderNo);
        //c.课程信息需要远程RPC调用其他微服务
        order.setCourseId(courseId);
        order.setCourseTitle("2022夏女装新款白色印花T恤碎花");
        order.setCourseCover("http://img30.360buyimg.com/popWareDetail/jfs/t1/120142/4/25376/161408/62395627Eedeb2ce1/1dcac57d9a8d3a71.jpg");
        //d.用户信息需要远程RPC调用其他微服务
        order.setTeacherName("李老师");
        order.setMemberId(memberId);
        order.setNickName("enjoy6288");
        order.setMobile("13286634434");
        order.setTotalFee(new BigDecimal(0.01));
        order.setPayType(1);
        order.setStatus(0);
        //e.保存到数据库里面
        baseMapper.insert(order);
        //f.返回订单编号
        return orderNo;
    }

    @Override
    public Map<String, Object> createPayQrCode(String orderNo) throws Exception{
        //根据订单id查询订单信息
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        EduOrder order = baseMapper.selectOne(wrapper);

        //1.需要组织参数 先组织成一个map 封装成为一个xml
        Map<String, String> requestParams = new HashMap<>();
        //公众账号ID
        requestParams.put("appid",WX_PAY_APP_ID);
        //商户号
        requestParams.put("mch_id",WX_PAY_MCH_ID);
        //随机字符串
        requestParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //商品描述
        requestParams.put("body",order.getCourseTitle());
        //商户订单号
        requestParams.put("out_trade_no",orderNo);
        //标价金额	传递过来的数据单位为元 元转换为分 需要乘100
        String totalFee=order.getTotalFee().multiply(new BigDecimal(100)).intValue()+"";
        requestParams.put("total_fee",totalFee);
        //终端IP
        requestParams.put("spbill_create_ip",WX_PAY_SPBILL_IP);
        //通知地址
        requestParams.put("notify_url",WX_PAY_NOTIFY_URL);
        //交易类型
        requestParams.put("trade_type","NATIVE");
        //2.调用微信官方接口
        String paramsXml = WXPayUtil.generateSignedXml(requestParams, WX_PAY_XML_KEY);
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
        httpClient.setXmlParam(paramsXml);
        httpClient.setHttps(true);
        httpClient.post();
        //3.得到结果 解析出支付二维码
        String content = httpClient.getContent();
        Map<String, String> txRetMap = WXPayUtil.xmlToMap(content);

        Map<String, Object> retMap = new HashMap<>();
        String codeUrl = txRetMap.get("code_url");
        retMap.put("qrCodeUrl",codeUrl);
        retMap.put("orderNo",orderNo);
        retMap.put("totalFee",order.getTotalFee());
        retMap.put("courseId",order.getCourseId());
        return retMap;
    }

    @Override
    public Map<String, String> queryPayState(String orderNo) throws Exception {
        //a.封装查询需要的参数
        Map<String, String> requestParams = new HashMap<>();
        //公众账号ID
        requestParams.put("appid",WX_PAY_APP_ID);
        //商户号
        requestParams.put("mch_id",WX_PAY_MCH_ID);
        //随机字符串
        requestParams.put("nonce_str", WXPayUtil.generateNonceStr());
        //商户订单号
        requestParams.put("out_trade_no",orderNo);
        //b.请求接口
        String paramsXml = WXPayUtil.generateSignedXml(requestParams, WX_PAY_XML_KEY);
        HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        httpClient.setXmlParam(paramsXml);
        httpClient.setHttps(true);
        httpClient.post();
        //c.解析结果
        String content = httpClient.getContent();
        System.out.println(content);
        Map<String, String> txRetMap = WXPayUtil.xmlToMap(content);
        return txRetMap;
    }

    @Transactional
    @Override
    public void updateOrderStatus(Map<String, String> txRetMap) throws Exception {
        //a.修改商户订单状态
        String orderNo = txRetMap.get("out_trade_no");
        QueryWrapper<EduOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        EduOrder order = baseMapper.selectOne(wrapper);
        order.setStatus(1);
        baseMapper.updateById(order);


        QueryWrapper<EduPayLog> logWrapper = new QueryWrapper<>();
        logWrapper.eq("order_no",orderNo);
        EduPayLog existLog = payLogService.getOne(logWrapper);
        if(existLog==null){
            //b.往支付日志写数据--为了不扯皮
            EduPayLog payLog = new EduPayLog();
            payLog.setOrderNo(orderNo);
            //20220602114447
            String payTime = txRetMap.get("time_end");
            Date payTimeDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(payTime);
            payLog.setPayTime(payTimeDate);
            payLog.setTotalFee(order.getTotalFee());
            payLog.setTransactionId(txRetMap.get("transaction_id"));
            payLog.setTradeState(txRetMap.get("trade_state"));
            payLog.setPayType(1);
            payLogService.save(payLog);
        }

    }
}
