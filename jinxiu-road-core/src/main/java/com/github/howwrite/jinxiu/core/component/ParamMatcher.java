package com.github.howwrite.jinxiu.core.component;

import com.github.howwrite.jinxiu.core.model.ValueMeta;

import java.lang.reflect.Type;

/**
 * 参数选择器
 */
public interface ParamMatcher {
    /**
     * 判断valueMeta是否可以与所需类型匹配
     * 默认实现是{@link DefaultParamMatcher} 如果需要的参数有key，那么valueMeta的key一定相同，然后提供的类型是否是所需类型的子类
     *
     * @param needValueKey      所需参数的key
     * @param needValueType     所需参数的类型
     * @param providerValueMeta 提供的值元数据
     * @return 当前提供的值能否与所需的参数类型匹配
     */
    boolean match(String needValueKey, Type needValueType, ValueMeta providerValueMeta);
}
