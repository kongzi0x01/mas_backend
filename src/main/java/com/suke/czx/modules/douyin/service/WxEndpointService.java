package com.suke.czx.modules.douyin.service;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masItem.service.MasItemService;
import com.suke.czx.modules.masOrder.entity.MasOrder;
import com.suke.czx.modules.masOrder.service.MasOrderService;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class WxEndpointService {

    @Autowired
    private MasItemService masItemService;

    @Autowired
    private MasUserService masUserService;

    @Autowired
    private MasOrderService masOrderService;

    @Autowired
    RestTemplate rest;

    public Integer item_checkout(MasOrder order) {
        order.setStatus(4);
        order.setUpdateTime(new Date());
        masOrderService.updateById(order);
        return 0;
    }

    public String fetchSmsCode(String phone){
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", "2");
        params.put("verifyCode", "111111");
        String body = getVerifyOrLoginBody(params);
        Map result = httpPost("https://openapi.dekuncn.com/gateway/dekun-plus-third/uat/dekun-boot/toktik/verifyOrLogin",body);
        if(result == null){
            return "获取验证码失败";
        }
        Integer code = (Integer) result.get("code");
        Boolean success = (Boolean) result.get("success");
        String message = (String)result.get("message");
        if(code == 200 && success) return "success";
        else return message;
    }

    public String verifySmsAndLogin(String phone, String smsCode){
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", "1");
        params.put("verifyCode", smsCode);
        String body = getVerifyOrLoginBody(params);
        Map result = httpPost("https://openapi.dekuncn.com/gateway/dekun-plus-third/uat/dekun-boot/toktik/verifyOrLogin",body);
        if(result == null){
            return "登录失败";
        }
        Integer code = (Integer) result.get("code");
        Boolean success = (Boolean) result.get("success");
        String message = (String)result.get("message");
        if(code == 200 && success) return "success";
        else return message;
    }

    public String checkCoupon(String phone, String couponId){
        Map<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("couponId", couponId);
        String body = getCheckCouponBody(params);
        Map result = httpPost("https://openapi.dekuncn.com/gateway/dekun-plus-third/uat/dekun-boot/toktik/checkCoupon", body);
        if(result == null){
            return "校验失败";
        }
        Integer code = (Integer) result.get("code");
        Boolean success = (Boolean) result.get("success");
        String message = (String)result.get("message");
        if(code == 200 && success) return "success";
        else return message;

    }

    private Map httpPost(String url, String body) {
        Map<String, String> headers = getHeaders(body);
        String result = HttpRequest.post(url).addHeaders(headers).body(body).execute().body();
        log.info("login response:{}", result);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(result, Map.class);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getHeaders(String body) {
        String appkey = "d92fb2cc9436f22b";
        String timestamp = Long.toString(System.currentTimeMillis());
        String apipassword = "78b13253001ca367";
        String sign = getSign(appkey, timestamp,body, apipassword);
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Gateway-Apikey", appkey);
        headers.put("X-Gateway-Timestamp", timestamp);
        headers.put("X-Gateway-Sign", sign);
        return headers;
    }

    private String getSign(String appkey, String timestamp,String body, String apipassword) {
        String str = appkey + timestamp + body+ apipassword;
        log.info(str);
        String sign = MD5.create().digestHex(str);
        log.info(sign);
        return sign;
    }
    private String getVerifyOrLoginBody(Map<String, Object> params){
        return "{\"phone\":\""+ params.get("phone")+"\",\"type\":\""+ params.get("type")+"\",\"verifyCode\":\""+ params.get("verifyCode")+"\"}";
    }

    private String getCheckCouponBody(Map<String, Object> params){
        return "{\"couponId\":\""+ params.get("couponId")+"\",\"phone\":\""+ params.get("phone")+"\"}";
    }

    public static void main(String[] args) throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("phone", "13580478805");
        params.put("type", "2");
        params.put("verifyCode", "111111");
        String appkey = "d92fb2cc9436f22b";
        String timestamp = 1723465637226L+"";
        String apipassword = "78b13253001ca367";
        //String str = getSign(params, appkey, timestamp, apipassword);
        //System.out.println(str);
        String result = "{\"success\":true,\"message\":\"成功\",\"code\":200,\"result\":null,\"timestamp\":1723469758228}";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(result, Map.class);
        System.out.println(map);
    }

}