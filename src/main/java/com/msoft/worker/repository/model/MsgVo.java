package com.msoft.worker.repository.model;

import com.msoft.worker.repository.domain.worker.MsgStatus;
import com.msoft.worker.repository.domain.worker.MsgType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MsgVo {
    private String id;
    private MsgType type;
    private String topicId;
    private String replyId;
    private String msg;
    private MsgStatus status;
    private String userId;
}
