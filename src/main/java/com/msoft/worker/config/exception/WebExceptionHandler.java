package com.msoft.worker.config.exception;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class WebExceptionHandler implements org.springframework.web.server.WebExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        R r = new R();
        if (ex instanceof RuntimeException) {
            r.setMessage(ex.getMessage());
        }
        if (ex instanceof AuthenticationException) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        byte[] bytes = JSON.toJSONString(r).getBytes(StandardCharsets.UTF_8);
        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer wrap = dataBufferFactory.wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(wrap.write(JSON.toJSONString(r), StandardCharsets.UTF_8)));
    }
}
