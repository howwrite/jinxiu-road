package com.github.howwrite.jinxiu.spring;


import com.github.howwrite.jinxiu.core.LetUsGo;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * 管道元数据注册器
 * 将管道元信息构造完成后注入spring容器即可
 *
 * @see com.github.howwrite.jinxiu.core.pipeline.PipelineMetaFactory
 */
@Component
public class PipelineMetaRegister {
    @Resource
    private List<PipelineMeta> pipelineMetas;

    @PostConstruct
    public void init() {
        for (PipelineMeta pipelineMeta : pipelineMetas) {
            LetUsGo.register(pipelineMeta);
        }
    }
}
