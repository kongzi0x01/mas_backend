package com.suke.czx.modules.sys.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(value = "mas")
public class MasProperties {
    String uploadDir;
}
