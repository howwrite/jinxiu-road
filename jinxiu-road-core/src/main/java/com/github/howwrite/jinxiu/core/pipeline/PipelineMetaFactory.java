package com.github.howwrite.jinxiu.core.pipeline;

import com.github.howwrite.jinxiu.core.model.PipelineMetaBuildParam;

import javax.annotation.Nonnull;

public interface PipelineMetaFactory {
    PipelineMeta buildPipelineMeta(@Nonnull PipelineMetaBuildParam pipelineMetaBuildParam);
}
