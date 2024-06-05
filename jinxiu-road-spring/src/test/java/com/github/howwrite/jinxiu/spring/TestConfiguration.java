package com.github.howwrite.jinxiu.spring;

import com.github.howwrite.jinxiu.core.executor.ASyncPipelineExecutor;
import com.github.howwrite.jinxiu.core.executor.PipelineExecutorProvider;
import com.github.howwrite.jinxiu.core.executor.SequentialPipelineExecutor;
import com.github.howwrite.jinxiu.core.model.PipelineMetaBuildParam;
import com.github.howwrite.jinxiu.core.node.NodeInstanceProvider;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMetaFactory;
import com.github.howwrite.jinxiu.spring.mock.MajorPageRequest;
import com.github.howwrite.jinxiu.spring.mock.TestCreateUserRequest;
import com.github.howwrite.jinxiu.spring.mock.testNode.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootConfiguration
public class TestConfiguration {
    @Resource
    private PipelineMetaFactory pipelineMetaFactory;

    @Bean
    public PipelineMeta testCreateUser() {
        PipelineMetaBuildParam pipelineMetaBuildParam = PipelineMetaBuildParam.builder()
                .initValueType(TestCreateUserRequest.class)
                .name("createUser")
                .returnValueNodeType(SaveUserInfoNode.class)
                .nodeTypes(CheckUserRequestNode.class, CheckUserInfoNode.class, RecallOtherUserInfoNode.class, BuildUserInfoNode.class,
                        SaveUserInfoNode.class)
                .build();

        return pipelineMetaFactory.buildPipelineMeta(pipelineMetaBuildParam);
    }

    @Bean
    public PipelineMeta testCreateUserInfo() {
        PipelineMetaBuildParam pipelineMetaBuildParam = PipelineMetaBuildParam.builder()
                .initValueType(TestCreateUserRequest.class)
                .name("createUserInfo")
                .returnValueNodeType(BuildUserInfoNode.class)
                .nodeTypes(CheckUserRequestNode.class, CheckUserInfoNode.class, RecallOtherUserInfoNode.class, BuildUserInfoNode.class,
                        SaveUserInfoNode.class)
                .build();

        return pipelineMetaFactory.buildPipelineMeta(pipelineMetaBuildParam);
    }


    @Bean
    public PipelineMeta testSyncCreateUserInfo() {
        PipelineMetaBuildParam pipelineMetaBuildParam = PipelineMetaBuildParam.builder()
                .initValueType(TestCreateUserRequest.class)
                .name("asyncCreateUserInfo")
                .returnValueNodeType(BuildUserInfoNode.class)
                .nodeTypes(CheckUserRequestNode.class, CheckUserInfoNode.class, RecallOtherUserInfoNode.class, BuildUserInfoNode.class,
                        SaveUserInfoNode.class)
                .build();

        return pipelineMetaFactory.buildPipelineMeta(pipelineMetaBuildParam);
    }

    @Bean
    public PipelineMeta testMajorPage() {
        PipelineMetaBuildParam pipelineMetaBuildParam = PipelineMetaBuildParam.builder()
                .initValueType(MajorPageRequest.class)
                .name("majorPage")
                .returnValueNodeType(BuildMajorPageResponseNode.class)
                .nodeTypes(QueryUserByTokenNode.class, QueryUserTaskInfosNode.class, QueryWeatherNode.class, BuildMajorPageResponseNode.class)
                .build();

        return pipelineMetaFactory.buildPipelineMeta(pipelineMetaBuildParam);
    }

    @Bean
    public PipelineMeta testAsyncMajorPage() {
        PipelineMetaBuildParam pipelineMetaBuildParam = PipelineMetaBuildParam.builder()
                .initValueType(MajorPageRequest.class)
                .name("asyncMajorPage")
                .returnValueNodeType(BuildMajorPageResponseNode.class)
                .nodeTypes(QueryUserByTokenNode.class, QueryUserTaskInfosNode.class, QueryWeatherNode.class, BuildMajorPageResponseNode.class)
                .build();

        return pipelineMetaFactory.buildPipelineMeta(pipelineMetaBuildParam);
    }

    @Bean
    public PipelineExecutorProvider autoSyncExecutorProvider(NodeInstanceProvider nodeInstanceProvider) {
        ASyncPipelineExecutor aSyncPipelineExecutor = new ASyncPipelineExecutor(nodeInstanceProvider);
        SequentialPipelineExecutor sequentialPipelineExecutor = new SequentialPipelineExecutor(nodeInstanceProvider);
        return pipelineMeta -> pipelineMeta.getName().startsWith("async") ? aSyncPipelineExecutor : sequentialPipelineExecutor;
    }
}
