package com.msoft.worker.exception;

import com.msoft.worker.constans.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;

public class NotLoginException  extends AbstractThrowableProblem {
    public NotLoginException() {
        super(ErrorConstants.NOT_LOGIN,"未登录或登录信息已过期！");
    }

}
