package com.msoft.worker.repository.domain.worker;

import com.msoft.worker.audit.AbstractAuditingEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
@ToString(callSuper = true)
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public class Topic extends AbstractAuditingEntity {
    private String content;
    private String userId;
    private String title;
    private String school;
    private Long click = 0L;
    private Long reply = 0L;

    public void click() {
        this.click++;
    }

    public void reply() {
        this.reply++;
    }
}
