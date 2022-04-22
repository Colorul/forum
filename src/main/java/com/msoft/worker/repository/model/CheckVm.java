package com.msoft.worker.repository.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckVm {
    private Integer version = 2;
    private String openid;
    private Integer scene = 3;
    private String content;
}
