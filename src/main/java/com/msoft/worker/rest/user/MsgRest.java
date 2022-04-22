package com.msoft.worker.rest.user;

import com.msoft.worker.repository.model.MsgVo;
import com.msoft.worker.service.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequestMapping("msg")
@RestController
@Api(value = "消息API", tags = "消息API")
public record MsgRest(MsgService msgService) {

    @GetMapping(produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "分页查询消息")
    public Mono<Page<MsgVo>> page(Pageable pageable) {
        return msgService.page(pageable);
    }

    @PutMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "阅读消息")
    public Mono<Boolean> page(@PathVariable String id) {
        return msgService.read(id);
    }

}
