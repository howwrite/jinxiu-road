package com.github.howwrite.jinxiu.spring;

import com.github.howwrite.jinxiu.core.component.DefaultParamMatcher;
import com.github.howwrite.jinxiu.core.component.ParamMatcher;
import com.github.howwrite.jinxiu.core.executor.PipelineExecutorProvider;
import com.github.howwrite.jinxiu.core.executor.SequentialPipelineExecutor;
import com.github.howwrite.jinxiu.core.node.NodeInstanceProvider;
import com.github.howwrite.jinxiu.core.node.NodeMetaFactory;
import com.github.howwrite.jinxiu.core.pipeline.DefaultPipelineMetaFactory;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMetaFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 默认组件配置
 * 默认组件都是启用的
 * {@link ConditionalOnMissingBean}注解保证大多数情况可以通过将自定义其他实现注入容器的方式替换默认实现。
 * 但是在一些特殊情况下无法替换时可以通过开关配置来关闭默认实现{@link JinxiuProperties}
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(JinxiuProperties.class)
public class JinxiuRoadConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "jinxiu", name = "enable-default-pipeline-executor-provider", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public PipelineExecutorProvider pipelineExecutorProvider(NodeInstanceProvider nodeInstanceProvider) {
        SequentialPipelineExecutor sequentialPipelineExecutor = new SequentialPipelineExecutor(nodeInstanceProvider);
        return pipelineMeta -> sequentialPipelineExecutor;
    }

    @Bean
    @ConditionalOnProperty(prefix = "jinxiu", name = "enable-default-node-provider", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public NodeInstanceProvider nodeProvider(ApplicationContext applicationContext) {
        return new SpringNodeInstanceProvider(applicationContext);
    }

    @Bean
    @ConditionalOnProperty(prefix = "jinxiu", name = "enable-default-node-meta-factory", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public NodeMetaFactory nodeMetaFactory(ParamMatcher paramMatcher) {
        return new NodeMetaFactory(paramMatcher);
    }

    @Bean
    @ConditionalOnProperty(prefix = "jinxiu", name = "enable-default-paramMatcher", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public ParamMatcher paramMatcher() {
        return new DefaultParamMatcher();
    }

    @Bean
    @ConditionalOnProperty(prefix = "jinxiu", name = "enable-default-pipeline-meta-factory", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public PipelineMetaFactory pipelineMetaFactory(NodeMetaFactory nodeMetaFactory) {
        return new DefaultPipelineMetaFactory(nodeMetaFactory);
    }
}
