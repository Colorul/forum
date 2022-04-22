package com.msoft.worker.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice(basePackages = "com.msoft")
public class GlobalExceptionHandler {
    /**
     * 处理 ServiceException 异常
     */
    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public R serviceExceptionHandler(RuntimeException ex) {
        // 包装 CommonResult 结果
        return new R("500", ex.getMessage());
    }

    /**
     * 处理其它 Exception 异常
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R exceptionHandler(Exception e) {
        return new R("500", e.getMessage());
    }
}
