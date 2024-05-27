package com.github.howwrite.jinxiu.core.executor;

import com.github.howwrite.jinxiu.core.exception.ExecuteTargetException;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;
import com.github.howwrite.jinxiu.core.runtime.PipelineRuntime;
import com.github.howwrite.jinxiu.core.runtime.PipelineRuntimeFactory;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SyncPipelineExecutor extends BasePipelineExecutor {
    @Nonnull
    private final ExecutorService executorService;

    public SyncPipelineExecutor(PipelineRuntimeFactory pipelineRuntimeFactory) {
        this(pipelineRuntimeFactory, buildExecutorService());
    }

    public SyncPipelineExecutor(PipelineRuntimeFactory pipelineRuntimeFactory, @Nonnull ExecutorService executorService) {
        super(pipelineRuntimeFactory);
        this.executorService = executorService;
    }

    protected static ExecutorService buildExecutorService() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                availableProcessors,
                availableProcessors * 2,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @Override
    public Object go(@Nonnull PipelineMeta pipelineMeta, @Nonnull Object initValue) {
        PipelineRuntime pipelineRuntime = pipelineRuntimeFactory.buildPipelineRuntime(pipelineMeta, initValue);
        Queue<Integer> waitExecuteNodeIndexQueue = new ConcurrentLinkedQueue<>();
        for (int i : pipelineMeta.getNoParentNodeIndexList()) {
            waitExecuteNodeIndexQueue.add(i);
        }
        AtomicInteger runningNodeNum = new AtomicInteger(0);
        List<ExecuteTargetException> exceptionList = new Vector<>();
        while (!waitExecuteNodeIndexQueue.isEmpty() || runningNodeNum.get() != 0) {
            while (waitExecuteNodeIndexQueue.isEmpty()) {
                if (runningNodeNum.get() == 0) {
                    break;
                }
            }
            if (waitExecuteNodeIndexQueue.isEmpty()) {
                break;
            }
            final Integer nodeIndex = waitExecuteNodeIndexQueue.poll();
            runningNodeNum.incrementAndGet();
            executorService.submit(() -> invokeNode(pipelineRuntime, nodeIndex, () -> {
                NodeRuntime[] nodeRuntimes = pipelineRuntime.getNodeRuntimes();
                List<Integer> childNodeIndexList = nodeRuntimes[nodeIndex].getNodeMeta().getChildNodeIndexList();
                if (childNodeIndexList != null && !childNodeIndexList.isEmpty() && exceptionList.isEmpty()) {
                    for (Integer index : childNodeIndexList) {
                        int parentNum = nodeRuntimes[index].getWaitRunParentNodeNum().decrementAndGet();
                        if (parentNum == 0) {
                            waitExecuteNodeIndexQueue.offer(index);
                        }
                    }
                }
            }, runningNodeNum::decrementAndGet, exceptionList::add));
        }
        if (!exceptionList.isEmpty()) {
            // todo 这里只会对第一个错误处理，需要考虑怎么把所有的错误都抛出
            throw exceptionList.get(0);
        }
        return pipelineRuntime.getResult();
    }
}
