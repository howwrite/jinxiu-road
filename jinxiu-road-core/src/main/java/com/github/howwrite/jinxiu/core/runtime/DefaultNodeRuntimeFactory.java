package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.node.NodeProvider;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class DefaultNodeRuntimeFactory implements NodeRuntimeFactory {
    private final NodeProvider nodeProvider;

    @Override
    public NodeRuntime buildNodeRuntime(NodeMeta nodeMeta) {
        return new NodeRuntime(nodeMeta, nodeProvider.findNodeByType(nodeMeta.getNodeClass()),
                new AtomicInteger(nodeMeta.getParentNodeIndexes().length));
    }
}
