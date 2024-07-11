package com.suke.czx.modules.masUser.controller;

import java.util.Map;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;


/**
 * 用户
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-07 16:13:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/masUser/mas")
@Api(value = "MasUserController", tags = "用户")
public class MasUserController extends AbstractController {
    private final MasUserService masUserService;

    /**
     * 列表
     */
    @ApiOperation(value = "用户列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<MasUser> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {

        }
        IPage<MasUser> listPage = masUserService.page(mpPageConvert.<MasUser>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增用户
     */
    @ApiOperation(value = "新增用户数据")
    @SysLog("新增用户数据")
                @PostMapping("/save")
            public R save(@RequestBody MasUser masUser) {
            masUserService.save(masUser);
        return R.ok();
    }


    /**
     * 修改
     */
    @ApiOperation(value = "修改用户数据")
    @SysLog("修改用户数据")
                @PostMapping("/update")
            public R update(@RequestBody MasUser masUser) {
            masUserService.updateById(masUser);
        return R.ok();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除用户数据")
    @SysLog("删除用户数据")
                @PostMapping("/delete")
            public R delete(@RequestBody MasUser masUser) {
            masUserService.removeById(masUser.getId());
        return R.ok();
    }

}
