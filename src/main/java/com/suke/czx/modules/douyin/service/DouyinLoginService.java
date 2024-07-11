package com.suke.czx.modules.douyin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class DouyinLoginService {

    @Autowired
    RestTemplate rest;
    public Map login(MultiValueMap<String, String> params) {
        String url = "https://open-sandbox.douyin.com/api/apps/v2/jscode2session";

        ResponseEntity<Object> response = rest.postForEntity(url, params, Object.class);
        //解析JSON格式的返回
        Map<String, Object> map = (Map<String, Object>) response.getBody();
        map.forEach((k, v) -> log.info("{}:{}", k, v));
        return map;
    }
}
