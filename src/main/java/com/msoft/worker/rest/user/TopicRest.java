package com.msoft.worker.rest.user;

import com.msoft.worker.repository.model.TopicQuery;
import com.msoft.worker.repository.model.TopicVm;
import com.msoft.worker.repository.model.TopicVo;
import com.msoft.worker.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("topics")
@RestController
@Api(value = "话题API", tags = "话题API")
public record TopicRest(TopicService topicService) {

    @PostMapping(produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "新增话题")
    public Mono<Boolean> save(@RequestBody TopicVm vm) {
        return this.topicService.save(vm);
    }

    @PostMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "更新话题")
    public Mono<Boolean> update(@RequestBody TopicVm vm, @PathVariable String id) {
        return this.topicService.update(id,vm);
    }

    @GetMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "id查询")
    public Mono<TopicVo> findOne(@PathVariable String id) {
        return this.topicService.findOne(id);
    }

    @GetMapping(produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "分页查询")
    public Mono<Page<TopicVo>> page(Pageable pageable, TopicQuery query) {
        return this.topicService.page(pageable, query, Boolean.FALSE);
    }

    @GetMapping(value = "self", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "分页查询自己发布的")
    public Mono<Page<TopicVo>> pageSelf(Pageable pageable, TopicQuery query) {
        return this.topicService.page(pageable, query, Boolean.TRUE);
    }

    @DeleteMapping(value = "{id}", produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "删除")
    public Mono<Boolean> delete(@PathVariable String id) {
        return this.topicService.deleteById(id);
    }

}
