package com.suke.czx.modules.masUser.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.suke.czx.modules.masUser.mapper.MasUserMapper;
import com.suke.czx.modules.masUser.entity.MasUser;
import com.suke.czx.modules.masUser.service.MasUserService;


/**
 * 用户
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-07 16:13:06
 */
@Service
public class MasUserServiceImpl extends ServiceImpl<MasUserMapper, MasUser> implements MasUserService {

    @Override
    public MasUser findUserByOpenId(String openid) {
        //根据openid查询用户
        return baseMapper.findByOpenId(openid);
    }

    @Override
    public MasUser findByPhone(String phone) {
        //根据手机号查询用户
        return baseMapper.findByPhone(phone);
    }
}
