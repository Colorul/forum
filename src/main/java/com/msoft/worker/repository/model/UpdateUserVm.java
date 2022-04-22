package com.msoft.worker.repository.model;

import com.msoft.worker.repository.domain.user.Sex;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserVm {
    @ApiModelProperty(value = "用户名")
    private String name;
    @ApiModelProperty(value = "微信用户名")
    private String userName;
    private String phone;
    private String avatar;
    private Sex sex;
    private String school;
}
