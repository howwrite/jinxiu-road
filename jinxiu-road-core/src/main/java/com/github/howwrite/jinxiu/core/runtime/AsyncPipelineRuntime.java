package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import lombok.Getter;

@Getter
public class AsyncPipelineRuntime extends PipelineRuntime {
    private final int[] noParentIndexList;

    public AsyncPipelineRuntime(PipelineMeta pipelineMeta, Object initValue, AsyncNodeRuntime[] nodeRuntimes, int[] noParentIndexList,
                                Object[] globalValues) {
        super(pipelineMeta, initValue, nodeRuntimes, globalValues);
        this.noParentIndexList = noParentIndexList;
    }

    public AsyncNodeRuntime getAsyncNodeRuntime(int index) {
        return (AsyncNodeRuntime) getNodeRuntimes()[index];
    }
}
