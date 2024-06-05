package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class AsyncNodeRuntime extends NodeRuntime {
    private final AtomicInteger waitRunParentNodeNum;
    private final int[] childNodeIndexes;

    public AsyncNodeRuntime(NodeMeta nodeMeta, Node nodeInstance, int[] childNodeIndexes, AtomicInteger waitRunParentNodeNum) {
        super(nodeMeta, nodeInstance);
        this.childNodeIndexes = childNodeIndexes;
        this.waitRunParentNodeNum = waitRunParentNodeNum;
    }
}
