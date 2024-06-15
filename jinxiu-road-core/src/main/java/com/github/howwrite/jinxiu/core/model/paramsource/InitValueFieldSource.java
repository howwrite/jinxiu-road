package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

@Getter
public class InitValueFieldSource extends BaseParamSource {
    private final Field field;

    public InitValueFieldSource(Type paramType, Field field) {
        super(paramType);
        this.field = field;
    }

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes, Object[] globalValues) {
        try {
            return field.get(initValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
