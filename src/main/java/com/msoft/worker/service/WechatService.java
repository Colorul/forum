package com.msoft.worker.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.msoft.worker.config.WechatCodeConfig;
import com.msoft.worker.config.security.jwt.JwtTokenProvider;
import com.msoft.worker.exception.EntityNotFoundException;
import com.msoft.worker.repository.domain.user.UserRepository;
import com.msoft.worker.repository.domain.user.WechatUser;
import com.msoft.worker.repository.mapper.UserMapper;
import com.msoft.worker.repository.model.UpdateUserVm;
import com.msoft.worker.repository.model.UserVo;
import com.msoft.worker.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public record WechatService(WechatCodeConfig wechatCodeConfig,
                            JwtTokenProvider tokenProvider,
                            ReactiveAuthenticationManager authenticationManager,
                            UserRepository userRepository) {
    private static final String WECHAT_OPENID_CODE = "openid";

    public Mono<String> login(@NotBlank String code) {
        JSONObject jsonObject = this.getCode(code);
        if (jsonObject == null || jsonObject.size() == 0 || !jsonObject.containsKey(WECHAT_OPENID_CODE)) {
            log.error("请求微信登录接口发生异常: result:" + (jsonObject != null ? jsonObject.toJSONString() : ""));
            throw new RuntimeException("登录失败，请稍后再试！");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(jsonObject.getString(WECHAT_OPENID_CODE), jsonObject.getString(WECHAT_OPENID_CODE));
        Mono<Authentication> block = this.authenticationManager.authenticate(authenticationToken);
        saveOrUpdate(jsonObject);
        return block.map(tokenProvider::createToken);
    }

    private void saveOrUpdate(JSONObject jsonObject) {
        WechatUser wechatUser = this.userRepository.findFirstByOpenIdOrderByCreatedDateDesc(jsonObject.get(WECHAT_OPENID_CODE).toString()).orElse(null);
        if (Objects.isNull(wechatUser)) {
            wechatUser = new WechatUser();
            wechatUser.setOpenId(jsonObject.get(WECHAT_OPENID_CODE).toString());
        }
        wechatUser.setLastLoginTime(new Date());
        this.userRepository.save(wechatUser);
    }

    public JSONObject getCode(String code) {
        try {
            String url = wechatCodeConfig.getRequestUrl() + "?appid=" + wechatCodeConfig.getAppId() + "&secret=" + wechatCodeConfig.getAppSecret() + "&js_code=" + code + "&grant_type=" + wechatCodeConfig.getGrantType();
            return this.getJsonFromWeb(url);
        } catch (Exception e) {
            throw new RuntimeException("登陆失败");
        }
    }

    /**
     * 调用第三方get接口 并返回结果
     *
     * @param apiUrl 调用地址 含参数
     * @return 结果
     */
    private JSONObject getJsonFromWeb(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            return JSON.parseObject(sb.toString());
        } catch (Exception e) {
            log.error(String.format("获取%s内容失败,message:%s", apiUrl, e.getMessage()), e);
        }
        return new JSONObject();
    }

    public Mono<UserVo> current() {
        return SecurityUtil.getUserVo();
    }

    @Cacheable(cacheNames = "USER:INFO", key = "#id")
    public UserVo getById(String id) {
        return UserMapper.INSTANCE.fromEntity(this.userRepository.findById(id).orElse(new WechatUser()));
    }

    public UserVo getCurrentUserVo(Authentication authentication) {
        return UserMapper.INSTANCE.fromEntity(this.userRepository.findFirstByOpenIdOrderByCreatedDateDesc(((User) authentication.getPrincipal()).getUsername()).orElseThrow(() -> new AuthenticationCredentialsNotFoundException("未登录，或用户已被禁用！")));
    }

    public Mono<UserVo> update(UpdateUserVm vm) {
        return SecurityUtil.getUserVo().map(userVo -> {
            WechatUser wechatUser = this.userRepository.findById(userVo.getId()).orElseThrow(EntityNotFoundException::new);
            if (StringUtils.hasText(vm.getName())) {
                wechatUser.setName(vm.getName());
            }
            if (StringUtils.hasText(vm.getUserName())) {
                wechatUser.setUserName(vm.getUserName());
            }
            if (StringUtils.hasText(vm.getPhone())) {
                wechatUser.setPhone(vm.getPhone());
            }
            if (StringUtils.hasText(vm.getAvatar())) {
                wechatUser.setAvatar(vm.getAvatar());
            }
            if (Objects.nonNull(vm.getSex())) {
                wechatUser.setSex(vm.getSex());
            }
            if (StringUtils.hasText(vm.getSchool())) {
                wechatUser.setSchool(vm.getSchool());
            }
            this.userRepository.save(wechatUser);
            return UserMapper.INSTANCE.fromEntity(wechatUser);
        });
    }
}
