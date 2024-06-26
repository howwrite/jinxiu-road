package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public class ForwardParamSource extends BaseParamSource {
    private final int index;

    public ForwardParamSource(Type paramType, int index) {
        super(paramType);
        this.index = index;
    }

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes, Object[] globalValues) {
        return nodeRuntimes[index].getReturnValue();
    }
}
