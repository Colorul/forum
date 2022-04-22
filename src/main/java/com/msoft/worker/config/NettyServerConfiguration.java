package com.msoft.worker.config;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerConfiguration implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(httpServer ->
            httpServer.httpRequestDecoder(spec -> spec.maxInitialLineLength(102400))
        );
    }
}
