package com.msoft.worker.config.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "msoft.jwt")
public class JwtProperties {
    private String secretKey = "zv08ri9h+XXo3KkeaLp4DdFGrRG1nqbKo+htx7/8LyFCAUHdIFrGMi1TJ+h4NPW0Y7+PzC2LgHr/LYsolonaQA==";
    private long tokenValidityInSeconds = 10*60*100000;
}
