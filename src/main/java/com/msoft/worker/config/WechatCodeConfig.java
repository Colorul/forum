package com.msoft.worker.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Dblink
 * @date 2020/4/8 14:45
 **/
@Setter
@Getter
@Component
@ConfigurationProperties("wechat.code")
public class WechatCodeConfig {
    private String appId;
    private String appSecret;
    private String grantType;
    private String requestUrl;
}
