package com.msoft.worker.exception;

import com.msoft.worker.constans.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;

/**
 * @author Dblink
 * @date 2020/5/25 14:58
 **/
public class ExceptionWrapper extends AbstractThrowableProblem {
    private static final long serialVersionUID = 4118176989375842070L;

    public ExceptionWrapper(String message) {
        super(ErrorConstants.ACCOUNT_NOT_ACTIVATED,message);
    }
}
