package com.msoft.worker.config;

public final class JwtSecurityDefaults {

    interface Authentication {

        interface Jwt {

            String secret = null;
            String base64Secret = null;
            long tokenValidityInSeconds = 180000000; // 半小时
            long tokenValidityInSecondsForRememberMe = 2592000; // 30 小时;
        }
    }
}
