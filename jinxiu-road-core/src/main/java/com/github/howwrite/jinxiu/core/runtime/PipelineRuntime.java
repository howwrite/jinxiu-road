package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import lombok.Data;

@Data
public class PipelineRuntime {
    private final PipelineMeta pipelineMeta;
    private final NodeRuntime[] nodeRuntimes;
    private final Object initValue;

    public PipelineRuntime(PipelineMeta pipelineMeta, Object initValue, NodeRuntime[] nodeRuntimes) {
        this.pipelineMeta = pipelineMeta;
        this.nodeRuntimes = nodeRuntimes;
        this.initValue = initValue;
    }

    public void recordForwardReturnValues(int index, Object returnValue) {
        nodeRuntimes[index].setReturnValue(returnValue);
    }

    public Object getResult() {
        return nodeRuntimes[pipelineMeta.getReturnValueNodeIndex()].getReturnValue();
    }
}
