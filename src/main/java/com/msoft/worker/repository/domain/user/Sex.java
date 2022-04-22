package com.msoft.worker.repository.domain.user;

import lombok.Getter;

@Getter
public enum Sex {
    Man("男"), Woman("女");
    private String desc;
    Sex(String desc) {
        this.desc = desc;
    }
}
