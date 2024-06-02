package com.github.howwrite.jinxiu.core.pipeline;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.model.PipelineMetaBuildParam;
import com.github.howwrite.jinxiu.core.node.NodeMeta;

import javax.annotation.Nonnull;

public interface PipelineMetaFactory {
    /**
     * 构建pipeline元数据
     *
     * @param pipelineMetaBuildParam pipeline创建参数
     * @return pipeline元数据
     */
    PipelineMeta buildPipelineMeta(@Nonnull PipelineMetaBuildParam pipelineMetaBuildParam);

    /**
     * 构建执行节点返回值的key
     *
     * @param nodeMeta 执行节点元数据
     * @return 返回值的key
     * @see Execute#valueKey()
     */
    String buildReturnValueMetaKey(NodeMeta nodeMeta);
}
