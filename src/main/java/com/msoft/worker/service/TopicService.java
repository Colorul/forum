package com.msoft.worker.service;

import com.msoft.worker.exception.EntityNotFoundException;
import com.msoft.worker.exception.ExceptionWrapper;
import com.msoft.worker.repository.domain.worker.Topic;
import com.msoft.worker.repository.domain.worker.TopicRepository;
import com.msoft.worker.repository.mapper.TopicMapper;
import com.msoft.worker.repository.model.TopicQuery;
import com.msoft.worker.repository.model.TopicVm;
import com.msoft.worker.repository.model.TopicVo;
import com.msoft.worker.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Validated
@AllArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final WechatService wechatService;
    private final MongoTemplate mongoTemplate;
    private final ContentCheckService contentCheckService;

    public Mono<Boolean> save(@Valid @NotNull TopicVm vm) {
        return SecurityUtil.getUserVo().map(user -> {
            Topic topic = new Topic();
            topic.setUserId(user.getId());
            topic.setContent(vm.getContent());
            topic.setTitle(vm.getTitle());
            topic.setSchool(user.getSchool());
            contentCheckService.check(vm.getAccessCode(), user.getOpenId(), vm.getContent() + vm.getTitle());
            this.topicRepository.save(topic);
            return Boolean.TRUE;
        });
    }

    public Mono<Boolean> update(String id, TopicVm vm) {
        return SecurityUtil.getUserVo().map(user -> {
            Topic topic = this.topicRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if (!user.getId().equals(topic.getUserId())) {
                throw new ExceptionWrapper("无权修改他人话题！");
            }
            topic.setContent(vm.getContent());
            topic.setTitle(vm.getTitle());
            contentCheckService.check(vm.getAccessCode(), user.getOpenId(), vm.getContent() +vm.getTitle());
            this.topicRepository.save(topic);
            return Boolean.TRUE;
        });
    }

    public Mono<TopicVo> findOne(@NotBlank String id) {
        Topic topic = this.topicRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        topic.click();
        this.topicRepository.save(topic);
        return Mono.just(TopicMapper.INSTANCE.fromEntity(topic));
    }

    public Mono<Page<TopicVo>> page(Pageable pageable, TopicQuery query, boolean querySelf) {
        return SecurityUtil.getUserVo().map(user -> {
            if (querySelf) {
                query.setUserId(user.getId());
            }
            query.setSchool(user.getSchool());
            return query.buildPage(this.mongoTemplate, pageable).map(this::mapping);
        });
    }

    private TopicVo mapping(Topic entity) {
        TopicVo vo = new TopicVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setUser(wechatService.getById(entity.getUserId()));
        return vo;
    }


    public Mono<Boolean> deleteById(@NotBlank String id) {
        return SecurityUtil.getUserVo().map(user -> {
            Topic topic = this.topicRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if (!user.getId().equals(topic.getUserId())) {
                throw new ExceptionWrapper("无权修改他人话题！");
            }
            this.topicRepository.delete(topic);
            return Boolean.TRUE;
        });

    }




}
