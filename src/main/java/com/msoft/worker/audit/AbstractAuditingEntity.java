package com.msoft.worker.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@FieldNameConstants
public class AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 8902857866714686095L;
    @Id
    private String id;
    private String remark;

    private Integer isValid = 1;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    @Version
    private Integer version;

}
