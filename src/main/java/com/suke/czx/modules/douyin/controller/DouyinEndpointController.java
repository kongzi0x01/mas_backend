package com.suke.czx.modules.douyin.controller;

import cn.hutool.core.util.ObjectUtil;
import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.modules.douyin.service.DouyinLoginService;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.czx.modules.masParam.entity.MasParam;
import com.suke.czx.modules.masParam.service.MasParamService;
import com.suke.czx.modules.masPicture.entity.MasPicture;
import com.suke.czx.modules.masPicture.service.MasPictureService;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-05 22:12:50
 */
@RestController
@RequestMapping("/douyin")
@Api(value = "DouyinEndpointController", tags = "")
@Slf4j
public class DouyinEndpointController {

    @Autowired
    private DouyinLoginService douyinLoginService;

    @Autowired
    private MasItemService masItemService;

    @Autowired
    private MasOrderService masOrderService;

    @Autowired
    private MasUserService masUserService;

    @Autowired
    private MasPictureService masPictureService;

    @Autowired
    private MasParamService masParamService;

    @Value("${douyin.appid}")
    private String appid;

    @Value("${douyin.secret}")
    private String secret;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @AuthIgnore
    public R login(@RequestParam(defaultValue = "") String anonymous_code, @RequestParam String code) {

        Map<String, String> map= new HashMap<>();
        map.put("appid", appid);
        map.put("secret", secret);
        map.put("code", code);
        map.put("anonymous_code", anonymous_code);
        Map result = douyinLoginService.login(map);
        Integer errno = (Integer) result.get("err_no");
        if(errno == 0){
            return R.ok().setData(result);
        }else {
            return R.error(errno,"login failed").setData(result);
        }
    }

    @ApiOperation(value = "代金券列表")
    @PostMapping("/items")
    @AuthIgnore
    public R items(@RequestParam(defaultValue = "")  String openId) {
        Map<String, Object> params = new HashMap<>();
        params.put("on_sale", true);
        List<MasItem> result = masItemService.listByMap(params);
        return R.ok().setData(result);
    }

    @ApiOperation(value = "代金券详情")
    @PostMapping("/item")
    @AuthIgnore
    public R item(@RequestParam(defaultValue = "")  String openId, @RequestParam String uuid) {
        MasItem result = masItemService.getById(uuid);
        if (ObjectUtil.isNull(result)) {
            return R.error(1001,"代金券不存在").setData("代金券不存在");
        }
        if(result.getRemain() == 0){
            return R.error(1002,"代金券已抢光").setData("代金券已抢光");
        }
        if(!result.getOnSale()){
            return R.error(1003,"代金券已下架").setData("代金券已下架");
        }
        return R.ok().setData(result);
    }

    @ApiOperation(value = "下单前校验")
    @PostMapping("/pre_purchase")
    @AuthIgnore
    public R pre_purchase(@RequestParam String openId, @RequestParam String uuid) {
        MasItem result = masItemService.getById(uuid);
        if (ObjectUtil.isNull(result)) {
            return R.error(1001,"代金券不存在").setData("代金券不存在");
        }
        if(result.getRemain() == 0){
            return R.error(1002,"代金券已抢光").setData("代金券已抢光");
        }
        if(!result.getOnSale()){
            return R.error(1003,"代金券已下架").setData("代金券已下架");
        }

        MasUser user = douyinLoginService.findUserByOpenId(openId);
        if(user == null){
            return R.error(1004,"用户不存在").setData("用户不存在");
        }

        return R.ok();
    }

    @ApiOperation(value = "下单")
    @PostMapping("/purchase")
    @AuthIgnore
    public R purchase(@RequestParam String openId, @RequestParam String uuid) {

        return R.ok();
    }

    @ApiOperation(value = "我的代金券")
    @PostMapping("/my_items")
    @AuthIgnore
    public R my_items(@RequestParam String openId) {
        MasUser user = douyinLoginService.findUserByOpenId(openId);
        if(user == null){
            return R.error(1004,"用户不存在").setData("用户不存在");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        List<MasOrder> orders = masOrderService.listByMap(params);
        orders.forEach(masOrder -> masOrder.setItem(masItemService.getById(masOrder.getItemId())));
        return R.ok().setData(orders);
    }

    @ApiOperation(value = "核销代金券")
    @PostMapping("/check_item")
    @AuthIgnore
    public R check_item(@RequestParam String openId, @RequestParam String orderId) {
        MasOrder order = masOrderService.getById(orderId);
        if (ObjectUtil.isNull(order)) {
            return R.error(1005,"代金券不存在").setData("代金券不存在");
        }
        if(order.getStauts() !=  0){
            return R.error(1006,"代金券无效").setData("代金券无效");
        }
        MasUser user = masUserService.getById(order.getUserId());
        if(!user.getOpenid().equals(openId)){
            return R.error(1007,"代金券不属于该用户").setData("代金券不属于该用户");
        }
        order.setStauts(1);
        masOrderService.updateById(order);
        return R.ok();
    }

    @ApiOperation(value = "根据pkey获取图片url")
    @PostMapping("/getImgUrlByPkey")
    @AuthIgnore
    public R getImgUrlByPkey(@RequestParam String pkey) {
        Map<String, Object> params = new HashMap<>();
        params.put("pkey", pkey);
        List<MasPicture> picture = masPictureService.listByMap(params);
        if(!picture.isEmpty()){
            return R.ok().setData(picture.get(0).getUrl());
        }else{
            return R.error(1008,"图片不存在").setData("图片不存在");
        }
    }


    @ApiOperation(value = "代金券使用说明")
    @PostMapping("/getGuideline")
    @AuthIgnore
    public R getGuideline() {
        MasParam param = masParamService.getById("1");
        return R.ok().setData(param.getValue());
    }}
