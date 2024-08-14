package com.suke.czx.modules.masOrder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.suke.czx.modules.masOrder.mapper.MasOrderMapper;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;

import java.util.List;


/**
 * 订单
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-08 20:14:41
 */
@Service
public class MasOrderServiceImpl extends ServiceImpl<MasOrderMapper, MasOrder> implements MasOrderService {
    @Override
    public MasOrder getByOrderNo(String orderNo) {
        return baseMapper.getByOrderNo(orderNo);
    }

    @Override
    public List<MasOrder> queryByStatusAndNotifyStatus(Integer status, Integer notifyStatus){
        return baseMapper.queryByStatusAndNotifyStatus(status, notifyStatus);
    }
}
