package com.github.howwrite.jinxiu.core.globalValue;

/**
 * pipeline级别全局变量提供者
 */
public interface GlobalValueProvider {
    default boolean match(String pipelineName) {
        return true;
    }

    GlobalValueMeta<?> generateGlobalValueMeta(String pipelineName);
}
