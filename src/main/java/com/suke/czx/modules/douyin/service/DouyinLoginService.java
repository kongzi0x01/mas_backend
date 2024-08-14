package com.suke.czx.modules.douyin.service;

import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import com.suke.zhjg.common.autofull.util.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DouyinLoginService {

    @Autowired
    MasUserService masUserService;

    @Autowired
    MasItemService masItemService;

    @Autowired
    MasOrderService masOrderService;

    @Autowired
    WxEndpointService wxEndpointService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate rest;

    @Value("${douyin.appid}")
    private String appid;

    @Value("${douyin.secret}")
    private String secret;

    public Map login(Map<String, String> params, String phone) {
        log.info("login params:{}", params);
        String url = "https://developer.toutiao.com/api/apps/v2/jscode2session";
        // String url = "https://open-sandbox.douyin.com/api/apps/v2/jscode2session";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        Object response = rest.postForObject(url, httpEntity, Object.class);
        log.info("login response:{}", response);
        // 解析JSON格式的返回
        Map<String, Object> map = (Map<String, Object>) response;
        map.forEach((k, v) -> log.info("{}:{}", k, v));
        if (map.get("err_no").equals(0)) {
            Map<String, String> data = (Map<String, String>) map.get("data");
            String openid = data.get("openid");
            MasUser user = masUserService.findUserByOpenId(openid);
            if (user == null) {
                user = new MasUser();
                user.setOpenid(openid);
                user.setCreateTime(new Date());
            }
            user.setPhone(phone);
            user.setUpdateTime(new Date());
            masUserService.saveOrUpdate(user);
        }
        return map;
    }

    public String getPhoneByUuid(String smsUuid) {
        String phone = (String) redisTemplate.opsForValue().get("phone:" + smsUuid);
        return phone;
    }

    public String checkSmsCode(String phone, String smsCode) {
        String result = wxEndpointService.verifySmsAndLogin(phone, smsCode);
        return result;
    }

    public R fetchSmsCode(String phone) {
        String result = wxEndpointService.fetchSmsCode(phone);
        if ("success".equals(result)) {
            String uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("phone:" + uuid, phone, 10 * 60, TimeUnit.SECONDS);
            return R.ok().setData(uuid);
        } else {
            return R.error(result).setData(result);
        }
    }

    public R pre_purchase(MasUser user, MasItem item) {
        // FIXME String result = wxEndpointService.checkCoupon(user.getPhone(),
        // item.getUuid());
        String result = "success";
        if ("success".equals(result)) {
            MasOrder order = new MasOrder();
            order.setItemId(item.getUuid());
            order.setUserId(user.getId());
            order.setStatus(2);
            String orderNo = "DK-" + UUID.randomUUID();
            order.setOrderNo(orderNo);
            masOrderService.save(order);
            return R.ok().setData(order);
        } else {
            return R.error(result).setData(result);
        }
    }

    public R purchase(MasOrder order) {
        if (order == null) {
            return R.error("订单不存在").setData("订单不存在");
        }
        MasUser user = masUserService.getById(order.getUserId());
        if (Strings.isBlank(user.getPhone())) {
            return R.error(1005, "请先绑定手机号").setData("请先绑定手机号");
        }

        // FIXME String result = wxEndpointService.checkCoupon(user.getPhone(),
        // item.getUuid());
        String result = "success";
        if (!"success".equals(result)) {
            return R.error(result).setData(result);
        }
        // 订单创建时间距今是否超过15分钟
        if (new Date().getTime() - order.getCreateTime().getTime() > 15 * 60 * 1000) {
            return R.error(1006, "订单已过期").setData("订单已过期");
        }

        String url = "https://developer.toutiao.com/api/apps/ecpay/v1/create_order";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        Map<String, Object> params = new HashMap<>();
        MasItem item = masItemService.getById(order.getItemId());
        params.put("app_id", appid);
        params.put("out_order_no", order.getOrderNo());
        params.put("total_amount", item.getPrice().multiply(new BigDecimal(100)).intValue());
        params.put("subject", item.getName());
        params.put("body", item.getName());
        params.put("valid_time", 15 * 60);
        params.put("sign", Sign.requestSign(params));
        log.info("create_order params:{}", params);
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        Object response = rest.postForObject(url, httpEntity, Object.class);
        log.info("create_order response:{}", response);
        // 解析JSON格式的返回
        Map<String, Object> map = (Map<String, Object>) response;
        if (map.get("err_no").equals(0)) {
            Map<String, String> data = (Map<String, String>) map.get("data");
            order.setDouyinOrderId(data.get("order_id"));
            order.setDouyinOrderToken(data.get("order_token"));
            order.setUpdateTime(new Date());
            masOrderService.updateById(order);
            return R.ok().setData(map);
        } else {
            return R.error().setData(map);
        }
    }
}
