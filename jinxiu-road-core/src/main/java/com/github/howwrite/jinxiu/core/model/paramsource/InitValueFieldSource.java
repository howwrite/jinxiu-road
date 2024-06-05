package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
public class InitValueFieldSource implements ParamSource {
    private final Field field;

    public InitValueFieldSource(Field field) {
        this.field = field;
    }

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes) {
        try {
            return field.get(initValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
