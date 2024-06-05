package com.github.howwrite.jinxiu.spring;

import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.core.node.NodeInstanceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * node节点实例提供者，是通过spring容器中获取节点实例
 * 将node类型与实例在一个Map中缓存
 */
@RequiredArgsConstructor
public class SpringNodeInstanceProvider implements NodeInstanceProvider {
    private final ApplicationContext applicationContext;
    private final Map<Class<? extends Node>, Node> nodeMap = new ConcurrentHashMap<>();

    @Override
    public <T extends Node> T findNodeByType(Class<T> nodeType) {
        return (T) nodeMap.computeIfAbsent(nodeType, applicationContext::getBean);
    }
}
