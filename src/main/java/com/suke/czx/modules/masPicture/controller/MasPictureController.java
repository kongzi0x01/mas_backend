package com.suke.czx.modules.masPicture.controller;

import java.util.Map;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.suke.czx.modules.masPicture.entity.MasPicture;
import com.suke.czx.modules.masPicture.service.MasPictureService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.common.base.AbstractController;


/**
 * 图片管理
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-08-05 21:32:04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/masPicture/index")
@Api(value = "MasPictureController", tags = "图片管理")
public class MasPictureController extends AbstractController {
    private final MasPictureService masPictureService;

    /**
     * 列表
     */
    @ApiOperation(value = "图片管理列表")
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        QueryWrapper<MasPicture> queryWrapper = new QueryWrapper<>();
        final String keyword = mpPageConvert.getKeyword(params);
        if (StrUtil.isNotEmpty(keyword)) {

        }
        IPage<MasPicture> listPage = masPictureService.page(mpPageConvert.<MasPicture>pageParamConvert(params), queryWrapper);
        return R.ok().setData(listPage);
    }


    /**
     * 新增图片管理
     */
    @ApiOperation(value = "新增图片管理数据")
    @SysLog("新增图片管理数据")
                @PutMapping("/save")
            public R save(@RequestBody MasPicture masPicture) {
            masPictureService.save(masPicture);
        return R.ok();
    }


    /**
     * 修改
     */
    @ApiOperation(value = "修改图片管理数据")
    @SysLog("修改图片管理数据")
                @PutMapping("/update")
            public R update(@RequestBody MasPicture masPicture) {
            masPictureService.updateById(masPicture);
        return R.ok();
    }


    /**
     * 删除
     */
    @ApiOperation(value = "删除图片管理数据")
    @SysLog("删除图片管理数据")
                @DeleteMapping("/delete")
            public R delete(@RequestBody MasPicture masPicture) {
            masPictureService.removeById(masPicture.getId());
        return R.ok();
    }

}
