package com.suke.czx.modules.sys.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description //TODO $
 * @Date 12:47
 * @Author ywj
 **/
@Data
public class RouterInfo {

    public UserInfoVO userInfo;

    public List<SysMenuNewVO> menus;

}
