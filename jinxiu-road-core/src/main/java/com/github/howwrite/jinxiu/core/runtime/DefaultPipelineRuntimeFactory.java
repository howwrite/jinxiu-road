package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class DefaultPipelineRuntimeFactory implements PipelineRuntimeFactory {

    private final NodeRuntimeFactory nodeRuntimeFactory;

    @Override
    public PipelineRuntime buildPipelineRuntime(@Nonnull PipelineMeta pipelineMeta, @Nonnull Object initValue) {
        return new PipelineRuntime(pipelineMeta, initValue, buildNodeRuntimeInstances(pipelineMeta));
    }


    private NodeRuntime[] buildNodeRuntimeInstances(PipelineMeta pipelineMeta) {
        NodeMeta[] nodeMetas = pipelineMeta.getNodes();
        NodeRuntime[] nodeRuntimeInstances = new NodeRuntime[nodeMetas.length];
        for (int i = 0; i < nodeMetas.length; i++) {
            NodeMeta nodeMeta = nodeMetas[i];
            nodeRuntimeInstances[i] = nodeRuntimeFactory.buildNodeRuntime(nodeMeta);
        }
        return nodeRuntimeInstances;
    }
}
