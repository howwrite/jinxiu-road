package com.github.howwrite.jinxiu.core.executor;

import com.github.howwrite.jinxiu.core.exception.ExecuteTargetException;
import com.github.howwrite.jinxiu.core.node.NodeInstanceProvider;
import com.github.howwrite.jinxiu.core.node.NodeMeta;
import com.github.howwrite.jinxiu.core.pipeline.AsyncPipelineMetaExtend;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import com.github.howwrite.jinxiu.core.runtime.AsyncNodeRuntime;
import com.github.howwrite.jinxiu.core.runtime.AsyncPipelineRuntime;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ASyncPipelineExecutor extends BasePipelineExecutor {
    @Nonnull
    private final ExecutorService executorService;

    private final Map<PipelineMeta, AsyncPipelineMetaExtend> asyncPipelineMetaExtendMap = new ConcurrentHashMap<>();

    public ASyncPipelineExecutor(NodeInstanceProvider nodeInstanceProvider) {
        this(nodeInstanceProvider, buildExecutorService());
    }

    public ASyncPipelineExecutor(NodeInstanceProvider nodeInstanceProvider, @Nonnull ExecutorService executorService) {
        super(nodeInstanceProvider);
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
        AsyncPipelineMetaExtend asyncPipelineMetaExtend = asyncPipelineMetaExtendMap.computeIfAbsent(pipelineMeta, AsyncPipelineMetaExtend::new);
        AsyncPipelineRuntime asyncPipelineRuntime = buildAsyncPipelineRuntime(pipelineMeta, initValue, asyncPipelineMetaExtend);
        Queue<Integer> waitExecuteNodeIndexQueue = new ConcurrentLinkedQueue<>();
        for (int i : asyncPipelineRuntime.getNoParentIndexList()) {
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
            executorService.submit(() -> invokeNode(asyncPipelineRuntime, nodeIndex, () -> {
                int[] childNodeIndexes = asyncPipelineRuntime.getAsyncNodeRuntime(nodeIndex).getChildNodeIndexes();
                if (childNodeIndexes != null && childNodeIndexes.length > 0 && exceptionList.isEmpty()) {
                    for (Integer index : childNodeIndexes) {
                        int parentNum = asyncPipelineRuntime.getAsyncNodeRuntime(index).getWaitRunParentNodeNum().decrementAndGet();
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
        return asyncPipelineRuntime.getResult();
    }

    public AsyncPipelineRuntime buildAsyncPipelineRuntime(PipelineMeta pipelineMeta, Object initValue,
                                                          AsyncPipelineMetaExtend asyncPipelineMetaExtend) {
        AsyncNodeRuntime[] asyncNodeRuntimes = new AsyncNodeRuntime[pipelineMeta.getNodes().length];
        for (int i = 0; i < pipelineMeta.getNodes().length; i++) {
            NodeMeta nodeMeta = pipelineMeta.getNodes()[i];
            asyncNodeRuntimes[i] = buildAsyncNodeRuntime(nodeMeta, i, asyncPipelineMetaExtend);
        }
        return new AsyncPipelineRuntime(pipelineMeta, initValue, asyncNodeRuntimes, asyncPipelineMetaExtend.getNoParentNodeIndexesArray(),
                buildGlobalValues(pipelineMeta));
    }

    public AsyncNodeRuntime buildAsyncNodeRuntime(NodeMeta nodeMeta, int nodeIndex, AsyncPipelineMetaExtend asyncPipelineMetaExtend) {
        int[] parentNodeIndexesArray = asyncPipelineMetaExtend.getParentNodeIndexesArray()[nodeIndex];
        int[] childNodeIndexesArray = asyncPipelineMetaExtend.getChildNodeIndexesArray()[nodeIndex];
        return new AsyncNodeRuntime(nodeMeta, nodeInstanceProvider.findNodeByType(nodeMeta.getNodeClass()), childNodeIndexesArray,
                new AtomicInteger(parentNodeIndexesArray.length));
    }
}
