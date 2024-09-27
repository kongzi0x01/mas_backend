package com.suke.czx.modules.douyin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import com.suke.czx.modules.wx.service.WxEndpointService;
import com.suke.zhjg.common.autofull.util.R;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
public class DouyinEndpointService {

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

    public Map<String, Object> login(Map<String, String> params, String phone) {
        log.info("login params:{}", params);
        Map<String, Object> map = douyinLogin(params);
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

    // 抖音登录
    private Map<String, Object> douyinLogin(Map<String, String> params) {
        String url = "https://developer.toutiao.com/api/apps/v2/jscode2session";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        Object response = rest.postForObject(url, httpEntity, Object.class);
        log.info("login response:{}", response);
        // 解析JSON格式的返回
        Map<String, Object> map = (Map<String, Object>) response;
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
        String result = wxEndpointService.checkCoupon(user.getPhone(), item.getUuid());
        // String result = "success";
        if ("success".equals(result)) {
            MasOrder order = new MasOrder();
            order.setItemId(item.getUuid());
            order.setUserId(user.getId());
            order.setStatus(2);
            order.setNotifyStatus(0);
            String orderNo = "DK-" + UUID.randomUUID();
            order.setOrderNo(orderNo);
            order.setCreateTime(new Date());
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


        String result = wxEndpointService.checkCoupon(user.getPhone(), order.getItemId());

        // String result = "success";
        if (!"success".equals(result)) {
            return R.error(result).setData(result);
        }
        // 订单创建时间距今是否超过15分钟
        if (new Date().getTime() - order.getCreateTime().getTime() > 15 * 60 * 1000) {
            order.setStatus(3);
            masOrderService.updateById(order);
            return R.error(1006, "订单已超时").setData("订单已超时");
        }

        Object response = douyinCreateOrder(order);
        log.info("create_order response:{}", response);
        // 解析JSON格式的返回
        Map<String, Object> map = (Map<String, Object>) response;
        if (map.get("err_no").equals(0)) {
            Map<String, String> data = (Map<String, String>) map.get("data");
            order.setDouyinOrderId(data.get("order_id"));
            order.setDouyinOrderToken(data.get("order_token"));
            order.setUpdateTime(new Date());
            String wxresult = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 0);
            if(!"success".equals(wxresult)){
                log.error("wx服创建订单信息失败", result);
            }else{
                order.setNotifyStatus(1);
            }
            masOrderService.updateById(order);
            douyinPushOrder(order);
            return R.ok().setData(map);
        } else {
            return R.error().setData(map);
        }
    }

    // 抖音预下单
    private Object douyinCreateOrder(MasOrder order) {
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
        return response;
    }

    public Object douyinPushOrder(MasOrder order){
        String url = "https://developer.toutiao.com/api/apps/order/v2/push";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        Map<String, Object> params = new HashMap<>();
        String accessToken = getAccessToken();
        MasUser user = masUserService.getById(order.getUserId());
        Long updateTime = System.currentTimeMillis()/1000;
        String orderDetail = getOrderDetail(order);
        params.put("access_token", accessToken);
        params.put("app_name", "douyin");
        params.put("open_id", user.getOpenid());
        params.put("order_detail", orderDetail);
        params.put("order_type", 0);
        params.put("update_time", updateTime);

        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        Object response = rest.postForObject(url, httpEntity, Object.class);
        log.info("douyinPushOrder result:{}", response);
        return response;
    }

    private String getAccessToken(){
        String accessToken = (String) redisTemplate.opsForValue().get("access_token");
        if(Strings.isBlank(accessToken)){
            accessToken = getDouyinAccessToken();
            redisTemplate.opsForValue().set("access_token", accessToken, 100, TimeUnit.MINUTES);
        }
        log.info("accessToken: {}", accessToken);
        return accessToken;
    }

    private String getOrderDetail(MasOrder order){
        MasItem item = masItemService.getById(order.getItemId());
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> subItem = new HashMap<>();
        Integer price = item.getPrice().multiply(new BigDecimal(100)).intValue();
        subItem.put("item_code", item.getUuid());
        subItem.put("img", item.getIndexUrl());
        subItem.put("title", item.getName());
        subItem.put("amount", 1);
        subItem.put("price", price);
        map.put("order_id", order.getOrderNo());
        map.put("create_time", order.getCreateTime().getTime());
        map.put("status", getOrderDetailStatusDesc(order)); // 待支付,已支付,已取消,已超时,已核销,退款中,已退款,退款失败
        map.put("amount", 1);
        map.put("total_price", price);
        map.put("detail_url", item.getIndexUrl());
        map.put("item_list", Collections.singleton(subItem));

        ObjectMapper objectMapper = new ObjectMapper();
        String str = null;
        try {
            str = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        log.info(str);
        return str;
    }

    private String getOrderDetailStatusDesc(MasOrder order){
        // 1已支付,2待支付,3超时, 4已核销, 5已过期
        if(order.getStatus() == 1){
            return "已支付";
        }else if(order.getStatus() == 2){
            return "待支付";
        }else if(order.getStatus() == 3){
            return "已超时";
        }else if(order.getStatus() == 4){
            return "已核销";
        }else if(order.getStatus() == 5){
            return "已取消";
        }else{
            return "已取消";
        }
    }

    private String getDouyinAccessToken(){
        String url = "https://developer.toutiao.com/api/apps/v2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("grant_type", "client_credential");
        HttpEntity httpEntity = new HttpEntity<>(params, headers);
        Object response = rest.postForObject(url, httpEntity, Object.class);
        log.info("getDouyinAccessToken result:{}", response);
        Map<String, Object> map = (Map<String, Object>) response;
        Map<String, String> data = (Map<String, String>) map.get("data");
        return data.get("access_token");
    }

    public void purchase_callback(Map<String, Object> params){
        String msgStr = (String) params.get("msg");
        JSONObject msg = JSONUtil.parseObj(msgStr);
        String sign = (String) params.get("msg_signature");
        String type = (String) params.get("type");
        String timestamp = (String) params.get("timestamp");
        String nonce = (String) params.get("nonce");
        List<String> sortedString = Arrays.asList(secret, timestamp, nonce, msgStr);
        String expectSign = Sign.callbackSign(sortedString);
        log.info(expectSign);
        if(!Objects.equals(expectSign, sign)){
            log.info("签名不匹配,{} <==> {}", sign, expectSign);
            return;
        }

        String orderNo = msg.getStr("cp_orderno");
        String payment_order_no = msg.getStr("payment_order_no");
        Long paid_at = msg.getLong("paid_at");
        Long total_amount = msg.getLong("total_amount");
        MasOrder order = masOrderService.getByOrderNo(orderNo);
        MasUser user = masUserService.getById(order.getUserId());
        order.setDouyinCallback(String.valueOf(params));
        if(!"payment".equals(type)){
            String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 2);
            if(!"success".equals(result)){
                log.error("wx服更新订单信息失败", result);
            }else{
                order.setNotifyStatus(2);
            }

            order.setStatus(3);
            masOrderService.updateById(order);
            douyinPushOrder(order);
            log.info("订单超时");
            return;
        }
        MasItem item = masItemService.getById(order.getItemId());
        order.setPuchasedNo(payment_order_no);
        order.setStatus(1);
        order.setPuchasedTime(new Date(paid_at*1000));
        order.setUpdateTime(new Date());
        order.setExpireTime(new Date((paid_at + item.getValidDays()*24*60*60)*1000));
        order.setPuchasedAmount(total_amount);
        String result = wxEndpointService.buyCoupon(user.getPhone(), order.getItemId(), order.getOrderNo(), 1);
        if(!"success".equals(result)){
            log.error("wx服更新订单信息失败", result);
        }else{
            order.setNotifyStatus(2);
        }
        masOrderService.updateById(order);
        douyinPushOrder(order);
    }

    public MasOrder orderDetail(String orderNo){
        MasOrder order = masOrderService.getByOrderNo(orderNo);
        order.setItem(masItemService.getById(order.getItemId()));
        return order;
    }

    public List<String> getBannerUrls(){
        return wxEndpointService.getBannerUrls();
    }
}
