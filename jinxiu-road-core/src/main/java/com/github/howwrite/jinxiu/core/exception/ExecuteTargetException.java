package com.github.howwrite.jinxiu.core.exception;

import lombok.Getter;

@Getter
public class ExecuteTargetException extends RuntimeException {
    private static final long serialVersionUID = -270244046878296039L;

    private final Throwable target;

    public ExecuteTargetException(Throwable target) {
        this.target = target;
    }

    public Throwable getCause() {
        return target;
    }
}
