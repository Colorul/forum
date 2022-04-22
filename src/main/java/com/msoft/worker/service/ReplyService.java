package com.msoft.worker.service;

import com.msoft.worker.config.exception.R;
import com.msoft.worker.exception.EntityNotFoundException;
import com.msoft.worker.exception.ExceptionWrapper;
import com.msoft.worker.repository.domain.worker.Reply;
import com.msoft.worker.repository.domain.worker.ReplyRepository;
import com.msoft.worker.repository.domain.worker.Topic;
import com.msoft.worker.repository.domain.worker.TopicRepository;
import com.msoft.worker.repository.mapper.TopicMapper;
import com.msoft.worker.repository.model.*;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Validated
@AllArgsConstructor
public class ReplyService {
    private final TopicRepository topicRepository;
    private final ReplyRepository replyRepository;
    private final WechatService wechatService;
    private final MongoTemplate mongoTemplate;
    private final MsgService msgService;
    private final ContentCheckService contentCheckService;

    public Mono<Boolean> save(@Valid @NotNull ReplyVm vm) {
        return SecurityUtil.getUserVo().map(user -> {
            Topic topic = this.topicRepository.findById(vm.getTopicId()).orElseThrow(() -> new ExceptionWrapper("非法参数找不到相关话题信息,或已被移除"));
            Reply reply = new Reply();
            BeanUtils.copyProperties(vm, reply);
            if (!"0".equals(vm.getParentId())) {
                this.replyRepository.findById(vm.getParentId()).orElseThrow(() -> new ExceptionWrapper("非法参数找不到相关话题信息,或已被移除"));
            }
            reply.setUserId(user.getId());
            topic.reply();
            msgService.sendMsg(user, topic, reply);
            contentCheckService.check(vm.getAccessCode(), user.getOpenId(), vm.getContent());
            this.topicRepository.save(topic);
            this.replyRepository.save(reply);
            return Boolean.TRUE;
        });
    }


    public Mono<Page<ReplyVo>> page(Pageable pageable, String topicId) {
        ReplyQuery query = new ReplyQuery();
        query.setTopicId(topicId);
        List<ReplyVo> collect = this.replyRepository.findByTopicId(topicId).stream().map(this::mapping).collect(Collectors.toList());
        Map<String, ReplyVo> tree = buildTree(collect);
        query.setIdList(tree.values().stream().map(ReplyVo::getId).toList());
        return Mono.just(query.buildPage(mongoTemplate, pageable).map(item -> this.mapping(item, tree)));
    }


    private Map<String, ReplyVo> buildTree(List<ReplyVo> list) {
        List<ReplyVo> collect = list.stream().filter(item -> "0".equals(item.getParentId())).collect(Collectors.toList());
        collect.forEach(item -> this.buildTree(item, list));
        return collect.stream().collect(Collectors.toMap(ReplyVo::getId, v -> v, (v1, v2) -> v2));
    }

    private void buildTree(ReplyVo parent, List<ReplyVo> all) {
        List<ReplyVo> collect = all.stream().filter(item -> parent.getId().equals(item.getParentId())).collect(Collectors.toList());
        parent.setChildren(collect);
        if (!collect.isEmpty()) {
            collect.forEach(item -> {
                item.setParentUser(parent.getUser());
                this.buildTree(item, all);
            });
        }
    }

    private ReplyVo mapping(Reply entity, Map<String, ReplyVo> replyMap) {
        ReplyVo vo = new ReplyVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setUser(wechatService.getById(entity.getUserId()));
        vo.setChildren(Objects.isNull(replyMap.get(entity.getId()))?null : replyMap.get(entity.getId()).getChildren());
        if (Objects.nonNull(vo.getChildren()) && !vo.getChildren().isEmpty()) {
            List<ReplyVo> result = new ArrayList<>(vo.getChildren());
            treeToList(vo.getChildren(),result);
            vo.setChildren(result);
            vo.getChildren().forEach(item -> {
                item.setChildren(null);
            });
        }
        return vo;
    }

    private ReplyVo mapping(Reply entity) {
        ReplyVo vo = new ReplyVo();
        BeanUtils.copyProperties(entity, vo);
        vo.setUser(wechatService.getById(entity.getUserId()));
        if (!"0".equals(entity.getParentId())) {
            this.replyRepository.findById(entity.getParentId()).ifPresent(item -> {
                vo.setParentUser(wechatService.getById(item.getUserId()));
            });
        }
        return vo;
    }

    private void treeToList(List<ReplyVo> list, List<ReplyVo> result) {
        list.forEach(item -> {
            if (Objects.nonNull(item) && !list.isEmpty()) {
                result.addAll(item.getChildren());
                this.treeToList(item.getChildren(), result);
            }
        });
    }

    public Mono<Boolean> deleteById(@NotBlank String id) {
        return SecurityUtil.getUserVo().map(user -> {
            Reply topic = this.replyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if (!user.getId().equals(topic.getUserId())) {
                throw new ExceptionWrapper("无权修改他人回复！");
            }
            this.replyRepository.delete(topic);
            return Boolean.TRUE;
        });

    }


}
