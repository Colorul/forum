package com.msoft.worker.constans;

import lombok.Getter;

/**
 * 用户角色
 */
@Getter
public enum RoleEnum {
    Normal("普通用户"), Vip("Vip");
    private String desc;
    RoleEnum(String desc) {
        this.desc = desc;
    }
}
