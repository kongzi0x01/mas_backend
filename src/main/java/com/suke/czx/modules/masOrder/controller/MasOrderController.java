package com.suke.czx.modules.masOrder.controller;

import java.util.Map;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;


/**
 * 订单
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-08 20:14:41
 */
@RestController
@AllArgsConstructor
@RequestMapping("/masOrder/mas")
@Api(value = "MasOrderController", tags = "订单")
public class MasOrderController extends AbstractController {
    private final MasOrderService masOrderService;

    /**
     * 列表
     */
    @ApiOperation(value = "订单列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<MasOrder> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {

        }
        IPage<MasOrder> listPage = masOrderService.page(mpPageConvert.<MasOrder>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增订单
     */
    @ApiOperation(value = "新增订单数据")
    @SysLog("新增订单数据")
                @PostMapping("/save")
            public R save(@RequestBody MasOrder masOrder) {
            masOrderService.save(masOrder);
        return R.ok();
    }


    /**
     * 修改
     */
    @ApiOperation(value = "修改订单数据")
    @SysLog("修改订单数据")
                @PostMapping("/update")
            public R update(@RequestBody MasOrder masOrder) {
            masOrderService.updateById(masOrder);
        return R.ok();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除订单数据")
    @SysLog("删除订单数据")
                @PostMapping("/delete")
            public R delete(@RequestBody MasOrder masOrder) {
            masOrderService.removeById(masOrder.getId());
        return R.ok();
    }

}
