package com.github.howwrite.jinxiu.core.pipeline;

import com.github.howwrite.jinxiu.core.model.PipelineMetaBuildParam;
import com.github.howwrite.jinxiu.core.model.ValueMeta;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.node.NodeMetaFactory;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class DefaultPipelineMetaFactory implements PipelineMetaFactory {
    private final NodeMetaFactory nodeMetaFactory;

    @Override
    public PipelineMeta buildPipelineMeta(@Nonnull PipelineMetaBuildParam pipelineMetaBuildParam) {
        Class<? extends Node>[] nodeTypes = pipelineMetaBuildParam.getNodeTypes();
        if (nodeTypes == null || nodeTypes.length == 0) {
            throw new IllegalArgumentException("nodeTypes is null or empty");
        }
        String pipelineName = pipelineMetaBuildParam.getName();
        if (StringUtils.isBlank(pipelineName)) {
            throw new IllegalArgumentException("nodeName is blank");
        }
        Class<? extends Node> returnValueNodeType = Objects.requireNonNull(pipelineMetaBuildParam.getReturnValueNodeType());
        ValueMeta[] forwardReturnValueMetas = new ValueMeta[nodeTypes.length];
        NodeMeta[] nodeList = new NodeMeta[nodeTypes.length];
        Class<?> initValueType = pipelineMetaBuildParam.getInitValueType();
        int returnValueNodeIndex = Integer.MAX_VALUE;
        for (int i = 0; i < nodeTypes.length; i++) {
            Class<? extends Node> nodeType = nodeTypes[i];
            if (nodeType.equals(returnValueNodeType)) {
                returnValueNodeIndex = Math.min(returnValueNodeIndex, i);
            }
            NodeMeta nodeMeta = nodeMetaFactory.buildNodeMeta(i, nodeType, initValueType, forwardReturnValueMetas, nodeList);
            nodeList[i] = nodeMeta;
            String valueKey = StringUtils.isBlank(nodeMeta.getExecuteAnnotation().valueKey()) ? null : nodeMeta.getExecuteAnnotation().valueKey();
            forwardReturnValueMetas[i] = new ValueMeta(valueKey, nodeMeta.getExecuteMethod().getReturnType());
        }
        if (returnValueNodeIndex == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("not found provider return value node");
        }
        int[] noParentNodeIndexList = buildNoParentNodeIndexList(nodeList);
        return new PipelineMeta(pipelineName, nodeList, initValueType, returnValueNodeIndex, noParentNodeIndexList);
    }

    public int[] buildNoParentNodeIndexList(NodeMeta[] nodeList) {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < nodeList.length; i++) {
            result.add(i);
        }
        for (NodeMeta nodeMeta : nodeList) {
            nodeMeta.getChildNodeIndexList().forEach(result::remove);
        }
        return result.stream().mapToInt(i -> i).toArray();
    }

}
