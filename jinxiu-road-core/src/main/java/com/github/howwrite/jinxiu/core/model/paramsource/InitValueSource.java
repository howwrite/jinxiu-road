package com.github.howwrite.jinxiu.core.model.paramsource;

import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;

public class InitValueSource implements ParamSource {

    @Override
    public Object getParam(Object initValue, NodeRuntime[] nodeRuntimes) {
        return initValue;
    }
}
