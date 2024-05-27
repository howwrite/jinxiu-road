package com.github.howwrite.jinxiu.core.model;

import lombok.Getter;

/**
 * 表示node执行错误的结果
 */
@Getter
public class ExecutorErrorResult {
    /**
     * 错误的具体内容
     */
    private final Throwable throwable;

    public ExecutorErrorResult(Throwable throwable) {
        this.throwable = throwable;
    }
}
