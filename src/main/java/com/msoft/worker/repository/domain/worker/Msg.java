package com.msoft.worker.repository.domain.worker;

import com.msoft.worker.audit.AbstractAuditingEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Msg extends AbstractAuditingEntity {
    private MsgType type;
    private String topicId;
    private String replyId;
    private String msg;
    private MsgStatus status;
    private String userId;

    public void read() {
        this.status = MsgStatus.hadRead;
    }
}
