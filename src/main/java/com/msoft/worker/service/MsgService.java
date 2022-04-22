package com.msoft.worker.service;

import com.msoft.worker.exception.EntityNotFoundException;
import com.msoft.worker.exception.ExceptionWrapper;
import com.msoft.worker.repository.domain.worker.*;
import com.msoft.worker.repository.model.MsgVo;
import com.msoft.worker.repository.model.UserVo;
import com.msoft.worker.utils.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Slf4j
@Service
@Validated
@AllArgsConstructor
public class MsgService {
    private final MsgRepository msgRepository;

    public void sendMsg(UserVo userVo, Topic topic, Reply reply) {
        Msg msg = new Msg();
        msg.setMsg("话题[" + topic.getTitle() + "]收到新的回复, 请前往查看！");
        msg.setStatus(MsgStatus.New);
        msg.setTopicId(topic.getId());
        msg.setType(MsgType.Topic);
        msg.setUserId(userVo.getId());
        if (Objects.nonNull(reply)) {
            msg.setReplyId(reply.getId());
        }
        this.msgRepository.save(msg);
    }

    public Mono<Page<MsgVo>> page(Pageable pageable) {
        return SecurityUtil.getUserVo().map(user -> this.msgRepository.findByUserIdOrderByStatusAscCreatedDateDesc(user.getId(), pageable).map(item -> {
            MsgVo vo = new MsgVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }));
    }

    public Mono<Boolean> read(@NotBlank String id) {
        return SecurityUtil.getUserVo().map(user -> {
            Msg msg = this.msgRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if (!user.getId().equals(msg.getUserId())) {
                throw new ExceptionWrapper("非法请求，非法操作！");
            }
            return Boolean.TRUE;
        });
    }
}
