package com.suke.czx.modules.douyin.service;

import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import com.suke.zhjg.common.autofull.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DouyinLoginService {

    @Autowired
    MasUserService masUserService;

    @Autowired
    WxEndpointService wxEndpointService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate rest;
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
        //解析JSON格式的返回
        Map<String, Object> map = (Map<String, Object>) response;
        map.forEach((k, v) -> log.info("{}:{}", k, v));
        if(map.get("err_no").equals(0)){
            Map<String, String> data = (Map<String, String>) map.get("data");
            String openid = data.get("openid");
            MasUser user = masUserService.findUserByOpenId(openid);
            if(user == null){
                user = new MasUser();
                user.setOpenid(openid);
                user.setCreateTime(new Date());
            }
            user.setPhone(phone);
            user.setUpdateTime(new Date());
            masUserService.save(user);
        }
        return map;
    }

    public String getPhoneByUuid(String smsUuid){
        String phone = (String) redisTemplate.opsForValue().get("phone:"+smsUuid);
        return phone;
    }

    public String checkSmsCode(String phone, String smsCode){
        String result  =  wxEndpointService.verifySmsAndLogin(phone, smsCode);
        return result;
    }

    public R fetchSmsCode(String phone){
        String result  = wxEndpointService.fetchSmsCode(phone);
        if("success".equals(result)){
            String uuid = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("phone:"+uuid, phone, 10*60, TimeUnit.SECONDS);
            return R.ok().setData(uuid);
        }else{
            return R.error(result).setData(result);
        }
    }

    public R pre_purchase(MasUser user, MasItem item){
        String result = wxEndpointService.checkCoupon(user.getPhone(), item.getUuid());
        if("success".equals(result)){
            return R.ok().setData(item);
        }else{
            return R.error(result).setData(result);
        }
    }
}
