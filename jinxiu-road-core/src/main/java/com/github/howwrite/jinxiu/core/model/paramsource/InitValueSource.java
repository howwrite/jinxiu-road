package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;

import java.lang.reflect.Field;
import java.util.function.Function;

public class InitValueSource implements ParamSource {
    private final Function<Object, Object> function;

    public InitValueSource() {
        this.function = Function.identity();
    }

    public InitValueSource(Field field) {
        this.function = (obj) -> {
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes) {
        return function.apply(initValue);
    }
}
