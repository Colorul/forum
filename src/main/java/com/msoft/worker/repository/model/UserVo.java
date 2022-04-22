package com.msoft.worker.repository.model;

import com.msoft.worker.constans.RoleEnum;
import com.msoft.worker.repository.domain.user.Sex;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel(value = "用户信息")
public class UserVo {
    private String id;
    @ApiModelProperty(value = "微信 openId")
    private String openId;
    @ApiModelProperty(value = "微信 unionId")
    private String unionId;
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "微信用户名")
    private String userName;
    @ApiModelProperty(value = "角色")
    private RoleEnum role;
    private Date lastLoginTime;
    private String phone;
    private String avatar;
    private Sex sex;
    private String school;
}
