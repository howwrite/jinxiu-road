package com.github.howwrite.jinxiu.core.pipeline;

import com.github.howwrite.jinxiu.core.node.NodeMeta;
import lombok.Data;

@Data
public class PipelineMeta {
    /**
     * 流水线名称，用于运行时识别执行的是哪条流水线
     */
    private final String name;

    /**
     * 流水线中节点元信息列表，节点顺序有依赖关系
     */
    private final NodeMeta[] nodes;

    /**
     * 初始值类型
     */
    private final Class<?> initValueType;

    /**
     * 当前产生返回值的节点下标。
     * 执行结束后会将该节点的返回值作为流水线的返回值返回。
     */
    private final int returnValueNodeIndex;

    /**
     * 没有前置依赖节点的节点下标列表
     */
    private final int[] noParentNodeIndexList;
}