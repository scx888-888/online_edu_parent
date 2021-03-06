package com.atguigu.edu.service;

import com.atguigu.edu.entity.MemberCenter;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author zhangqiang
 * @since 2022-05-28
 */
public interface MemberCenterService extends IService<MemberCenter> {

    Integer queryRegisterNum(String day);
}
