package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import lombok.Getter;
import lombok.Setter;

@Getter
public class NodeRuntime {
    private final NodeMeta nodeMeta;

    private final Node nodeInstance;

    @Setter
    private volatile Object returnValue;

    public NodeRuntime(NodeMeta nodeMeta, Node nodeInstance) {
        this.nodeMeta = nodeMeta;
        this.nodeInstance = nodeInstance;
    }
}
