package com.msoft.worker.config.security;

import com.msoft.worker.constans.RoleEnum;
import com.msoft.worker.repository.domain.user.UserRepository;
import com.msoft.worker.repository.domain.user.WechatUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final static String DEFAULT_ROLE = RoleEnum.Normal.name();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String openId) {
        WechatUser wechatUser = this.userRepository.findFirstByOpenIdOrderByCreatedDateDesc(openId).orElse(null);
        if (Objects.isNull(wechatUser)) {
            wechatUser = new WechatUser();
            wechatUser.setOpenId(openId);
            wechatUser.setPassword(passwordEncoder.encode(openId));
            wechatUser.setRole(RoleEnum.Normal);
            this.userRepository.save(wechatUser);
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(wechatUser.getOpenId(), passwordEncoder.encode(openId), Stream.of(wechatUser.getRole().name(), DEFAULT_ROLE)
                .map(this::convert)
                .collect(Collectors.toList()));
        return Mono.just(userDetails);
    }

    private SimpleGrantedAuthority convert(String role) {
        return new SimpleGrantedAuthority(role);
    }
}