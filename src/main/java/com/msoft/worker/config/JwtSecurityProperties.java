package com.msoft.worker.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.security")
public class JwtSecurityProperties {
    private final Authentication authentication = new Authentication();

    public Authentication getAuthentication() {
        return authentication;
    }

    public static class Authentication {

        private final Jwt jwt = new Jwt();

        public Jwt getJwt() {
            return jwt;
        }

        public static class Jwt {

            private String secret = JwtSecurityDefaults.Authentication.Jwt.secret;

            private String base64Secret = JwtSecurityDefaults.Authentication.Jwt.base64Secret;

            private long tokenValidityInSeconds = JwtSecurityDefaults.Authentication.Jwt
                    .tokenValidityInSeconds;

            private long tokenValidityInSecondsForRememberMe = JwtSecurityDefaults.Authentication.Jwt
                    .tokenValidityInSecondsForRememberMe;

            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public String getBase64Secret() {
                return base64Secret;
            }

            public void setBase64Secret(String base64Secret) {
                this.base64Secret = base64Secret;
            }

            public long getTokenValidityInSeconds() {
                return tokenValidityInSeconds;
            }

            public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
                this.tokenValidityInSeconds = tokenValidityInSeconds;
            }

            public long getTokenValidityInSecondsForRememberMe() {
                return tokenValidityInSecondsForRememberMe;
            }

            public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
                this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
            }
        }
    }
}
