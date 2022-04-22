package com.msoft.worker.exception;

import com.msoft.worker.constans.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;

public class UserNotFoundException extends AbstractThrowableProblem {

    public UserNotFoundException() {
        super(ErrorConstants.ACCOUNT_NOT_FOUND, "User not found");
    }
}
