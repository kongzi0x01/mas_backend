package com.suke.czx.common.event;

import com.suke.czx.modules.sys.entity.SysLoginLog;
import org.springframework.context.ApplicationEvent;

/**
 * @Description //TODO $
 * @Date 11:29
 * @Author ywj
 **/
public class LoginLogEvent extends ApplicationEvent {

    public LoginLogEvent(SysLoginLog source) {
        super(source);
    }
}
