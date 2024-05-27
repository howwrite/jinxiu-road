package com.github.howwrite.jinxiu.core.executor;


import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;

import javax.annotation.Nonnull;

public interface PipelineExecutor {
    Object go(@Nonnull PipelineMeta pipelineMeta, @Nonnull Object initValue);
}
