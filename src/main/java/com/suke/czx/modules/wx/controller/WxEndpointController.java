package com.suke.czx.modules.wx.controller;

import cn.hutool.core.util.ObjectUtil;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.czx.modules.wx.service.WxEndpointService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-05 22:12:50
 */
@RestController
@RequestMapping("/wx")
@Api(value = "WxEndpointController", tags = "")
@Slf4j
public class WxEndpointController {

    @Autowired
    private MasItemService masItemService;

    @Autowired
    private MasOrderService masOrderService;

    @Autowired
    private WxEndpointService wxEndpointService;

    /**
     * 上架/更新
     */
    @ApiOperation(value = "上架/更新")
    @PostMapping("/item_save")
    @AuthIgnore
    public R item_save(@RequestBody MasItem masItem) {
        masItem.setOnSale(true);
        if (!Strings.isBlank(masItem.getUuid())) {
            MasItem item = masItemService.getById(masItem.getUuid());
            if (item != null) {
                masItemService.updateById(masItem);
                return R.ok("优惠券更新成功");
            } else {
                masItemService.save(masItem);
                return R.ok("优惠券上架成功");
            }
        } else {
            return R.error("优惠券UUID不能为空").setData("优惠券UUID不能为空");
        }
    }

    /**
     * 下架
     */
    @ApiOperation(value = "下架/更新")
    @PostMapping("/item_remove")
    @AuthIgnore
    public R item_remove(@RequestBody MasItem vo) {
        MasItem item = masItemService.getById(vo.getUuid());
        if (ObjectUtil.isNull(item)) {
            return R.error(1001, "代金券不存在").setData("代金券不存在");
        } else {
            item.setOnSale(false);
            masItemService.updateById(item);
        }
        return R.ok();
    }

    /**
     * 核销代金券
     */
    @ApiOperation(value = "核销代金券")
    @PostMapping("/item_checkout")
    @AuthIgnore
    public R item_checkout(@RequestBody MasOrder vo) {
        MasOrder order = masOrderService.getByOrderNo(vo.getOrderNo());
        if(ObjectUtil.isNull(order)){
            return R.error(1003, "订单不存在").setData("订单不存在");
        }

        Integer result = wxEndpointService.item_checkout(order);
        if (result == 0) {
            return R.ok().setData("代金券核销成功");
        } else {
            return R.error(result, "无可核销代金券").setData("无可核销代金券");
        }
    }
}
