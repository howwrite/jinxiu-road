package com.github.howwrite.jinxiu.core;

import com.github.howwrite.jinxiu.core.exception.BuildException;
import com.github.howwrite.jinxiu.core.executor.PipelineExecutor;
import com.github.howwrite.jinxiu.core.executor.PipelineExecutorProvider;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流水线启动器，可通过{@link #go(String, Object)}获取对应流水线并执行获取执行结果
 */
public class LetUsGo {

    private static final Map<String, PipelineMeta> PIPELINE_META_MAP = new ConcurrentHashMap<>();

    /**
     * 流水线执行器提供者
     */
    private static PipelineExecutorProvider PIPELINE_EXECUTOR_PROVIDER;

    public static Object go(String pipelineName, Object initValue) {
        PipelineMeta pipelineMeta = Objects.requireNonNull(PIPELINE_META_MAP.get(pipelineName));
        PipelineExecutor pipelineExecutor = PIPELINE_EXECUTOR_PROVIDER.getPipelineExecutor(pipelineMeta);
        return pipelineExecutor.go(pipelineMeta, initValue);
    }

    public static void register(PipelineMeta pipelineMeta) {
        String pipelineMetaName = pipelineMeta.getName();
        if (PIPELINE_META_MAP.containsKey(pipelineMetaName)) {
            throw new BuildException("Duplicate registration pipelines, name:" + pipelineMetaName);
        }
        PIPELINE_META_MAP.put(pipelineMetaName, pipelineMeta);
    }

    public static void register(@Nonnull PipelineExecutorProvider pipelineExecutorProvider) {
        if (PIPELINE_EXECUTOR_PROVIDER != null) {
            throw new BuildException("Duplicate registration pipeline executor provider, existClass:" + PIPELINE_EXECUTOR_PROVIDER.getClass().getName());
        }
        PIPELINE_EXECUTOR_PROVIDER = pipelineExecutorProvider;
    }
}
