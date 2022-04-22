package com.msoft.worker.exception;

import com.msoft.worker.constans.ErrorConstants;
import org.zalando.problem.AbstractThrowableProblem;

/**
 * @author Dblink
 * @date 2020/6/1 11:39
 **/
public class EntityNotFoundException extends AbstractThrowableProblem{

        public EntityNotFoundException(String message) {
            super(ErrorConstants.ENTITY_NOT_FOUND, message);
        }

    public EntityNotFoundException() {
        super(ErrorConstants.ENTITY_NOT_FOUND, "未找到相关数据");
    }
}
