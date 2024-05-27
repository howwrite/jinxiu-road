package com.github.howwrite.jinxiu.core.executor;


import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface PipelineExecutorProvider {
    @Nonnull
    PipelineExecutor getPipelineExecutor(PipelineMeta pipelineMeta);
}
