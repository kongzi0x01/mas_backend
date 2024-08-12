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
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

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
    public R login(@RequestParam(defaultValue = "") String anonymous_code, @RequestParam String code,  @RequestParam(required = false) String smsUuid,  @RequestParam(required = false) String smsCode) {

        String phone = douyinLoginService.getPhoneByUuid(smsUuid);
        if(phone == null){
            return R.error(1008,"验证码已过期").setData("验证码已过期");
        }
        String result = douyinLoginService.checkSmsCode(phone, smsCode);
        if(!"success".equals(result)){
            return R.error(1003,result).setData(result);
        }

        Map<String, String> map= new HashMap<>();
        map.put("appid", appid);
        map.put("secret", secret);
        map.put("code", code);
        map.put("anonymous_code", anonymous_code);
        Map loginResult = douyinLoginService.login(map, phone);
        Integer errno = (Integer) loginResult.get("err_no");
        if(errno == 0){
            return R.ok();
        }else {
            return R.error(errno,"login failed").setData(loginResult);
        }
    }

    @ApiOperation(value = "登录")
    @PostMapping("/save_user")
    @AuthIgnore
    public R save_user(@RequestParam String openId, @RequestParam(defaultValue = "") String phone, @RequestParam(defaultValue = "") String username) {

        MasUser dbuser = masUserService.findUserByOpenId(openId);
        if(ObjectUtil.isNull(dbuser)) {
            dbuser = new MasUser();
            dbuser.setOpenid(openId);
            dbuser.setCreateTime(new Date());
        }
        dbuser.setUsername(username);
        dbuser.setPhone(phone);
        dbuser.setUpdateTime(new Date());
        masUserService.saveOrUpdate(dbuser);
        return R.ok();
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
        MasItem item = masItemService.getById(uuid);
        if (ObjectUtil.isNull(item)) {
            return R.error(1001,"代金券不存在").setData("代金券不存在");
        }
        if(item.getRemain() == 0){
            return R.error(1002,"代金券已抢光").setData("代金券已抢光");
        }
        if(!item.getOnSale()){
            return R.error(1003,"代金券已下架").setData("代金券已下架");
        }

        MasUser user = masUserService.findUserByOpenId(openId);
        if(user == null){
            return R.error(1004,"用户不存在").setData("用户不存在");
        }
        if(Strings.isBlank(user.getPhone())){
            return R.error(1005, "请先绑定手机号").setData("请先绑定手机号");
        }

        return douyinLoginService.pre_purchase(user, item);
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
        MasUser user = masUserService.findUserByOpenId(openId);
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
        MasUser user = masUserService.getById(order.getUserId());
        if(!user.getOpenid().equals(openId)){
            return R.error(1007,"代金券不属于该用户").setData("代金券不属于该用户");
        }
        order.setStatus(4);
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
    }

    @ApiOperation(value = "支付回调")
    @PostMapping("/callback")
    @AuthIgnore
    public Map<String, Object> callback(@RequestBody(required = false) Map<String, Object> params) {
        log.info("params:{}", params);
        Map<String, Object> result = new HashMap<>();
        result.put("err_no", 0);
        result.put("err_tips", "success");
        return result;
    }

    @ApiOperation(value = "解密手机号数据")
    @PostMapping("/decryptPhoneData")
    @AuthIgnore
    public R decryptPhoneData(@RequestParam String encryptedData, @RequestParam String iv, @RequestParam String sessionKey) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] sessionKeyBytes = decoder.decode(sessionKey);
        byte[] ivBytes = decoder.decode(iv);
        byte[] encryptedBytes = decoder.decode(encryptedData);

        String phone;
        try{
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(sessionKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
            byte[] ret = cipher.doFinal(encryptedBytes);
            phone = new String(ret);
        }catch (Exception e){
            return R.error().setData("解密失败");
        }
        return R.ok().setData(phone);
    }

    @ApiOperation(value = "获取手机验证码")
    @PostMapping("/fetchSmsCode")
    @AuthIgnore
    public R fetchSmsCode(@RequestParam String phone) {
        return douyinLoginService.fetchSmsCode(phone);
    }
}
