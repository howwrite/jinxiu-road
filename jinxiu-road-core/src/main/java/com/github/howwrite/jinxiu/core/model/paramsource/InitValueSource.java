package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;

import java.lang.reflect.Type;

public class InitValueSource extends BaseParamSource {
    public InitValueSource(Type paramType) {
        super(paramType);
    }

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes, Object[] globalValues) {
        return initValue;
    }
}
