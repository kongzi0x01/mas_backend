package com.suke.czx.modules.douyin.service;

import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class DouyinLoginService {

    @Autowired
    MasUserService masUserService;

    @Autowired
    RestTemplate rest;
    public Map login(Map<String, String> params) {
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
            MasUser user = new MasUser();
            user.setOpenid(data.get("openid"));
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            masUserService.save(user);
        }
        return map;
    }

    public MasUser findUserByOpenId(String openid){
        Map<String, Object> params = new HashMap<>();
        params.put("openid", openid);
        List<MasUser> result = masUserService.listByMap(params);
        if(!result.isEmpty()){
            return result.get(0);
        }else return null;
    }
}
