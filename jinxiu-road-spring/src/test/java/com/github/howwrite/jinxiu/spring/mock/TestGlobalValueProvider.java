package com.github.howwrite.jinxiu.spring.mock;

import com.github.howwrite.jinxiu.core.globalValue.GlobalValueMeta;
import com.github.howwrite.jinxiu.core.globalValue.GlobalValueProvider;
import org.springframework.stereotype.Component;

@Component
public class TestGlobalValueProvider implements GlobalValueProvider {
    @Override
    public GlobalValueMeta<?> generateGlobalValueMeta(String pipelineName) {
        return new GlobalValueMeta<>(null, TestGlobalValue.class, TestGlobalValue::new);
    }
}
