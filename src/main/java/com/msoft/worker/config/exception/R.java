package com.msoft.worker.config.exception;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class R {
    private String code = "500";
    private String message;
}
