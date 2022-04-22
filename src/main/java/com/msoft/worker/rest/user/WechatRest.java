package com.msoft.worker.rest.user;

import com.msoft.worker.repository.model.UpdateUserVm;
import com.msoft.worker.repository.model.UserVo;
import com.msoft.worker.service.WechatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("wechat")
@RestController
@Api(value = "用户API", tags = "用户API")
public record WechatRest(WechatService wechatService) {
    @PostMapping(value = "login/{accessCode}", produces = "application/json;charset=UTF-8")
    public Mono<String> login(@PathVariable String accessCode) {
        return wechatService.login(accessCode);
    }

    @PostMapping(value = "/current", produces = "application/json;charset=UTF-8")
    public Mono<UserVo> current() {
        return wechatService.current();
    }

    @PutMapping(value = "/user", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "更新用户信息")
    public Mono<UserVo> update(@RequestBody UpdateUserVm vm) {
        return wechatService.update(vm);
    }
}
