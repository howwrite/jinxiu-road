package com.github.howwrite.jinxiu.core.model.paramsource;

import java.lang.reflect.Type;

public abstract class BaseParamSource implements ParamSource {
    private final Type paramType;

    protected BaseParamSource(Type paramType) {
        this.paramType = paramType;
    }

    @Override
    public Type getParamType() {
        return paramType;
    }
}
