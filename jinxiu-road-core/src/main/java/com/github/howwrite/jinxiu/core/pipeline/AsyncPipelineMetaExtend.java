package com.github.howwrite.jinxiu.core.pipeline;

import com.github.howwrite.jinxiu.core.model.paramsource.ForwardParamSource;
import com.github.howwrite.jinxiu.core.model.paramsource.ParamSource;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class AsyncPipelineMetaExtend {
    private final PipelineMeta pipelineMeta;
    private final int[][] parentNodeIndexesArray;
    private final int[][] childNodeIndexesArray;
    private final int[] noParentNodeIndexesArray;

    public AsyncPipelineMetaExtend(PipelineMeta pipelineMeta) {
        this.pipelineMeta = pipelineMeta;
        List<Set<Integer>> parentNodeIndexes = new ArrayList<>();
        List<Set<Integer>> childNodeIndexes = new ArrayList<>();
        Set<Integer> noParentNodeIndexes = new HashSet<>();
        int nodeNum = pipelineMeta.getNodes().length;
        for (int i = 0; i < nodeNum; i++) {
            Set<Integer> parentIndexList = new HashSet<>();
            parentNodeIndexes.add(parentIndexList);
            childNodeIndexes.add(new HashSet<>());
            NodeMeta nodeMeta = pipelineMeta.getNodes()[i];
            boolean hasParent = false;
            for (ParamSource paramSource : nodeMeta.getParamSources()) {
                if (!(paramSource instanceof ForwardParamSource)) {
                    continue;
                }
                hasParent = true;
                ForwardParamSource forwardParamSource = (ForwardParamSource) paramSource;
                int parentIndex = forwardParamSource.getIndex();
                parentIndexList.add(parentIndex);
                // 增加当前节点父类
                parentNodeIndexes.get(i).add(parentIndex);
                // 前置使用该父类的节点都是本节点的父类
                parentNodeIndexes.get(i).addAll(childNodeIndexes.get(parentIndex));
                // 父类的子节点增加当前节点
                childNodeIndexes.get(parentIndex).add(i);
            }
            if (!hasParent) {
                noParentNodeIndexes.add(i);
            }
        }

        parentNodeIndexesArray = new int[nodeNum][];
        childNodeIndexesArray = new int[nodeNum][];
        for (int i = 0; i < nodeNum; i++) {
            parentNodeIndexesArray[i] = new int[parentNodeIndexes.get(i).size()];
            int index = 0;
            for (Integer integer : parentNodeIndexes.get(i)) {
                parentNodeIndexesArray[i][index++] = integer;
            }

            childNodeIndexesArray[i] = new int[childNodeIndexes.get(i).size()];
            index = 0;
            for (Integer integer : childNodeIndexes.get(i)) {
                childNodeIndexesArray[i][index++] = integer;
            }
        }
        noParentNodeIndexesArray = new int[noParentNodeIndexes.size()];
        int index = 0;
        for (Integer noParentNodeIndex : noParentNodeIndexes) {
            noParentNodeIndexesArray[index++] = noParentNodeIndex;
        }
    }
}
