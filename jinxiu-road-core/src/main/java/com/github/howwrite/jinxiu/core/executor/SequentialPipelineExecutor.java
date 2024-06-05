package com.github.howwrite.jinxiu.core.executor;

import com.github.howwrite.jinxiu.core.node.NodeInstanceProvider;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import com.github.howwrite.jinxiu.core.runtime.PipelineRuntime;

import javax.annotation.Nonnull;

public class SequentialPipelineExecutor extends BasePipelineExecutor {
    public SequentialPipelineExecutor(NodeInstanceProvider nodeInstanceProvider) {
        super(nodeInstanceProvider);
    }

    @Override
    public Object go(@Nonnull PipelineMeta pipelineMeta, @Nonnull Object initValue) {
        NodeMeta[] nodes = pipelineMeta.getNodes();
        PipelineRuntime pipelineRuntime = buildPipelineRuntime(pipelineMeta, initValue);
        for (int i = 0; i < nodes.length; i++) {
            invokeNode(pipelineRuntime, i, () -> {
            }, () -> {
            }, e -> {
                throw e;
            });
        }
        return pipelineRuntime.getResult();
    }
}
