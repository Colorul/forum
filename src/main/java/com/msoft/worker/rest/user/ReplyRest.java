package com.msoft.worker.rest.user;

import com.msoft.worker.repository.model.*;
import com.msoft.worker.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("reply")
@RestController
@Api(value = "话题回复API", tags = "话题回复API")
public record ReplyRest(ReplyService replyService) {

    @PostMapping(produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "新增回复")
    public Mono<Boolean> save(@RequestBody ReplyVm vm) {
        return this.replyService.save(vm);
    }

    @GetMapping(value = "{topicId}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "分页查询某个话题下的回复")
    public Mono<Page<ReplyVo>> page(Pageable pageable, @PathVariable String topicId) {
        return this.replyService.page(pageable, topicId);
    }

    @DeleteMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "删除")
    public Mono<Boolean> delete(@PathVariable String id) {
        return this.replyService.deleteById(id);
    }

}
