package com.suke.czx.modules.masParam.controller;

import java.util.Map;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.suke.czx.modules.masParam.entity.MasParam;
import com.suke.czx.modules.masParam.service.MasParamService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;


/**
 * 
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-08-07 23:39:32
 */
@RestController
@AllArgsConstructor
@RequestMapping("/masParam/index")
@Api(value = "MasParamController", tags = "")
public class MasParamController extends AbstractController {
    private final MasParamService masParamService;

    /**
     * 列表
     */
    @ApiOperation(value = "列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<MasParam> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {

        }
        IPage<MasParam> listPage = masParamService.page(mpPageConvert.<MasParam>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增
     */
    @ApiOperation(value = "新增数据")
    @SysLog("新增数据")
                @PostMapping("/save")
            public R save(@RequestBody MasParam masParam) {
            masParamService.save(masParam);
        return R.ok();
    }


    /**
     * 修改
     */
    @ApiOperation(value = "修改数据")
    @SysLog("修改数据")
                @PostMapping("/update")
            public R update(@RequestBody MasParam masParam) {
            masParamService.updateById(masParam);
        return R.ok();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除数据")
    @SysLog("删除数据")
                @PostMapping("/delete")
            public R delete(@RequestBody MasParam masParam) {
            masParamService.removeById(masParam.getId());
        return R.ok();
    }

}
