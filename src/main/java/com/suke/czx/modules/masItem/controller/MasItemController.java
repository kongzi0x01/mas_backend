package com.suke.czx.modules.masItem.controller;

import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;


/**
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-05 22:12:50
 */
@RestController
@AllArgsConstructor
@RequestMapping("/masItem/index")
@Api(value = "MasItemController", tags = "")
public class MasItemController extends AbstractController {
    private final MasItemService masItemService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<MasItem> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {

        }
        IPage<MasItem> listPage = masItemService.page(mpPageConvert.<MasItem>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增
     */
    @ApiOperation(value = "新增数据")
    @SysLog("新增数据")
    @PostMapping("/save")
    public R save(@RequestBody MasItem masItem) {
        masItemService.save(masItem);
        return R.ok();
    }


    /**
     * 修改
     */
    @ApiOperation(value = "修改数据")
    @SysLog("修改数据")
    @PostMapping("/update")
    public R update(@RequestBody @Validated MasItem masItem) {
        masItemService.updateById(masItem);
        return R.ok();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除数据")
    @SysLog("删除数据")
    @PostMapping("/delete")
    public R delete(@RequestBody MasItem masItem) {
        masItemService.removeById(masItem.getId());
        return R.ok();
    }

}
