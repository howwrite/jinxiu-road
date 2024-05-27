package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.node.NodeMeta;

public interface NodeRuntimeFactory {
    NodeRuntime buildNodeRuntime(NodeMeta nodeMeta);
}
