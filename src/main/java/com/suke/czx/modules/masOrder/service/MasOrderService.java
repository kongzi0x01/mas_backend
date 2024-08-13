package com.suke.czx.modules.masOrder.service;

import com.suke.czx.modules.masOrder.entity.MasOrder;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 订单
 * 
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-08 20:14:41
 */
public interface MasOrderService extends IService<MasOrder> {
    MasOrder getByOrderNo(String orderNo);
}
