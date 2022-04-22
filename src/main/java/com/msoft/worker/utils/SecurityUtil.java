package com.msoft.worker.utils;

import com.msoft.worker.WorkerApplication;
import com.msoft.worker.repository.model.UserVo;
import com.msoft.worker.service.WechatService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

public class SecurityUtil {

    public static Mono<UserVo> getUserVo() {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.error(new IllegalStateException("ReactiveSecurityContext is empty")))
                .map(SecurityContext::getAuthentication)
                .map(authentication -> WorkerApplication.applicationContext.getBean(WechatService.class).getCurrentUserVo(authentication))
                .cast(UserVo.class);
    }

}
