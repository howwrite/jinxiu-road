package com.github.howwrite.jinxiu.core.executor;

import com.github.howwrite.jinxiu.core.exception.ExecuteTargetException;
import com.github.howwrite.jinxiu.core.model.ExecutorErrorResult;
import com.github.howwrite.jinxiu.core.model.paramsource.ParamSource;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.runtime.PipelineRuntime;
import com.github.howwrite.jinxiu.core.runtime.PipelineRuntimeFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

public abstract class BasePipelineExecutor implements PipelineExecutor {

    protected final PipelineRuntimeFactory pipelineRuntimeFactory;

    protected BasePipelineExecutor(PipelineRuntimeFactory pipelineRuntimeFactory) {
        this.pipelineRuntimeFactory = pipelineRuntimeFactory;
    }

    public Object invokeNode(PipelineRuntime runtime, int index, Runnable runAfter, Runnable runFinally,
                             Consumer<ExecuteTargetException> exceptionConsumer) {
        NodeMeta nodeMeta = runtime.getPipelineMeta().getNodes()[index];
        ParamSource[] paramSources = nodeMeta.getParamSources();
        Object[] params = new Object[paramSources.length];
        for (int i = 0; i < paramSources.length; i++) {
            params[i] = paramSources[i].getParam(runtime.getInitValue(), runtime.getNodeRuntimes());
        }
        try {
            Object result = nodeMeta.getExecuteMethod().invoke(runtime.getNodeRuntimes()[index].getNodeInstance(), params);
            runtime.recordForwardReturnValues(index, result);
            runAfter.run();
            return result;
        } catch (InvocationTargetException e) {
            exceptionConsumer.accept(new ExecuteTargetException(e.getTargetException()));
            return new ExecutorErrorResult(e.getTargetException());
        } catch (IllegalAccessException e) {
            exceptionConsumer.accept(new ExecuteTargetException(e));
            return new ExecutorErrorResult(e);
        } finally {
            runFinally.run();
        }
    }
}
