package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;
import lombok.Getter;

@Getter
public class ForwardParamSource implements ParamSource {
    private final int index;

    public ForwardParamSource(int index) {
        this.index = index;
    }

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes) {
        return nodeRuntimes[index].getReturnValue();
    }
}
