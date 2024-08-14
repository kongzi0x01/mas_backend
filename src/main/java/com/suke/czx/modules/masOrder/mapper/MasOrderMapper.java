package com.suke.czx.modules.masOrder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.suke.czx.modules.masOrder.entity.MasOrder;

import java.util.List;

/**
 * 订单
 * 
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-08 20:14:41
 */
public interface MasOrderMapper extends BaseMapper<MasOrder> {
	MasOrder getByOrderNo(String orderNo);
	List<MasOrder> queryByStatusAndNotifyStatus(Integer status, Integer notifyStatus);
}
