package com.msoft.worker.repository.domain.user;

import com.msoft.worker.audit.AbstractAuditingEntity;
import com.msoft.worker.constans.RoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
public class WechatUser extends AbstractAuditingEntity {
    private String unionId;
    private String openId;
    private String name;
    private String userName;
    private String password;
    private RoleEnum role = RoleEnum.Normal;
    private Date lastLoginTime;
    private String phone;
    private String avatar;
    private Sex sex;
    private String school;
}
