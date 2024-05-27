package com.github.howwrite.jinxiu.spring;

import com.github.howwrite.jinxiu.core.LetUsGo;
import com.github.howwrite.jinxiu.core.executor.PipelineExecutorProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 流水线执行器提供者注册器
 *
 * @see LetUsGo#register(PipelineExecutorProvider)
 */
@Component
public class PipelineExecutorProviderRegister {

    @Resource
    private PipelineExecutorProvider pipelineExecutorProvider;

    @PostConstruct
    public void init() {
        LetUsGo.register(pipelineExecutorProvider);
    }
}
