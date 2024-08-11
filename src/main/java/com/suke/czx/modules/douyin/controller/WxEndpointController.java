package com.suke.czx.modules.douyin.controller;

import cn.hutool.core.util.ObjectUtil;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.common.annotation.SysLog;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
    /**
     * 上架/更新
     */
    @ApiOperation(value = "上架/更新")
    @SysLog("上架/更新")
    @PostMapping("/item_save")
    @AuthIgnore
    public R item_save(@RequestBody MasItem masItem) {
        masItem.setOnSale(true);
        masItemService.save(masItem);
        return R.ok();
    }

    /**
     * 下架
     */
    @ApiOperation(value = "上架/更新")
    @SysLog("上架/更新")
    @PostMapping("/item_remove")
    @AuthIgnore
    public R item_remove(@RequestParam String uuid) {
        MasItem item = masItemService.getById(uuid);
        if (ObjectUtil.isNull(item)) {
            return R.error(1001,"代金券不存在").setData("代金券不存在");
        }else{
            item.setOnSale(false);
        }
        return R.ok();
    }
}
