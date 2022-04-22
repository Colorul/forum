package com.msoft.worker.config.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultWrapper {

    private String message;
}
