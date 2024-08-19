package com.suke.czx.modules.douyin.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import com.suke.czx.modules.wx.service.WxEndpointService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@EnableScheduling
public class OrderCheckerService {

    @Autowired
    MasOrderService masOrderService;
    @Autowired
    WxEndpointService wxEndpointService;
    @Autowired
    MasUserService masUserService;
    
    @Scheduled(fixedRate = 5*60*1000)
    public void check() {
        log.info("===== 检查订单状态 =====");
        List<MasOrder> toUpdateOrders = new LinkedList<>();
        List<MasOrder> orders = masOrderService.queryByStatusAndNotifyStatus(1, 0);
        for (MasOrder order : orders) {
            MasUser user = masUserService.getById(order.getUserId());
            String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 0);
            if(!"success".equals(result)){
                log.error("wx服创建订单信息失败", order.getOrderNo());
            }else{
                log.info("wx服创建订单信息成功", order.getOrderNo());
                order.setNotifyStatus(1);
                toUpdateOrders.add(order);
            }
        }
        orders = masOrderService.queryByStatusAndNotifyStatus(2, 0);
        for (MasOrder order : orders) {
            MasUser user = masUserService.getById(order.getUserId());
            String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 0);
            if(!"success".equals(result)){
                log.error("wx服创建订单信息失败", order.getOrderNo());
            }else{
                log.info("wx服创建订单信息成功", order.getOrderNo());
                order.setNotifyStatus(1);
                toUpdateOrders.add(order);
            }
        }
        orders = masOrderService.queryByStatusAndNotifyStatus(3, 0);
        for (MasOrder order : orders) {
            MasUser user = masUserService.getById(order.getUserId());
            String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 0);
            if(!"success".equals(result)){
                log.error("wx服创建订单信息失败", order.getOrderNo());
            }else{
                log.info("wx服创建订单信息成功", order.getOrderNo());
                order.setNotifyStatus(1);
                toUpdateOrders.add(order);
            }
        }
        masOrderService.updateBatchById(toUpdateOrders);
        toUpdateOrders.clear();
        orders = masOrderService.queryByStatusAndNotifyStatus(1, 1);
        for (MasOrder order : orders) {
            MasUser user = masUserService.getById(order.getUserId());
            String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 1);
            if(!"success".equals(result)){
                log.error("wx服更新订单信息失败", order.getOrderNo());
            }else{
                log.info("wx服更新订单信息成功", order.getOrderNo());
                order.setNotifyStatus(2);
                toUpdateOrders.add(order);
            }
        }
        orders = masOrderService.queryByStatusAndNotifyStatus(3, 1);
        for (MasOrder order : orders) {
            MasUser user = masUserService.getById(order.getUserId());
            String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 2);
            if(!"success".equals(result)){
                log.error("wx服更新订单信息失败", order.getOrderNo());
            }else{
                log.info("wx服更新订单信息成功", order.getOrderNo());
                order.setNotifyStatus(2);
                toUpdateOrders.add(order);
            }
        }
        masOrderService.updateBatchById(toUpdateOrders);
        log.info("===== 检查订单状态 ===== END ");
    }
}
