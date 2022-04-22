package com.msoft.worker.audit;

import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author Dblink
 * @date 2020/6/5 9:47
 **/
@Configuration
public class SpringSecurityAuditorAware implements ReactiveBeforeConvertCallback<AbstractAuditingEntity> {

    @Override
    public Publisher<AbstractAuditingEntity> onBeforeConvert(AbstractAuditingEntity entity, String collection) {
        var user = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(it -> it != null && it.isAuthenticated())
                .map(Authentication::getPrincipal)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.empty());

        if (entity.getId() == null) {
            entity.setCreatedDate(LocalDateTime.now());
        }
        entity.setLastModifiedDate(LocalDateTime.now());

        return user
                .map(u -> {
                            if (entity.getId() == null) {
                                entity.setCreatedBy(u.getUsername());
                            }
                            entity.setLastModifiedBy(u.getUsername());

                            return entity;
                        }
                )
                .defaultIfEmpty(entity);
    }
}
