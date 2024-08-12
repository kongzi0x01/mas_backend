package com.suke.czx.modules.masUser.service;

import com.suke.czx.modules.masUser.entity.MasUser;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户
 * 
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-07 16:13:06
 */
public interface MasUserService extends IService<MasUser> {

    //根据openid查询用户
    MasUser findUserByOpenId(String openid);

    //根据phone查询
    MasUser findByPhone(String phone);
}
