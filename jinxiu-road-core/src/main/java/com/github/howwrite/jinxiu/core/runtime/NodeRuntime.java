package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

public class NodeRuntime {
    @Getter
    private final NodeMeta nodeMeta;

    @Getter
    private final AtomicInteger waitRunParentNodeNum;

    @Getter
    private final Node nodeInstance;

    @Getter
    @Setter
    private volatile Object returnValue;


    public NodeRuntime(NodeMeta nodeMeta, Node nodeInstance, AtomicInteger waitRunParentNodeNum) {
        this.nodeMeta = nodeMeta;
        this.nodeInstance = nodeInstance;
        this.waitRunParentNodeNum = waitRunParentNodeNum;
    }
}
