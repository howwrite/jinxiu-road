package com.github.howwrite.jinxiu.core.runtime;

import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;

import javax.annotation.Nonnull;

public interface PipelineRuntimeFactory {
    PipelineRuntime buildPipelineRuntime(@Nonnull PipelineMeta pipelineMeta, @Nonnull Object initValue);
}
