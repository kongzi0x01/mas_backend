package com.suke.czx.modules.douyin.controller;

import com.suke.czx.common.annotation.AuthIgnore;
import com.suke.czx.modules.douyin.service.DouyinLoginService;
import com.suke.zhjg.common.autofull.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-05 22:12:50
 */
@RestController
@RequestMapping("/douyin")
@Api(value = "DouyinEndpointController", tags = "")
public class DouyinEndpointController {

    @Autowired
    private DouyinLoginService douyinLoginService;

    @Value("${douyin.appid}")
    private String appid;

    @Value("${douyin.secret}")
    private String secret;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @AuthIgnore
    public R list(@RequestParam(defaultValue = "") String anonymous_code, @RequestParam String code) {

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("appid", appid);
        map.add("secret", secret);
        map.add("code", code);
        map.add("anonymous_code", anonymous_code);

        Map result = douyinLoginService.login(map);
        return R.ok().setData(result);
    }
}
