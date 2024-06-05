package com.github.howwrite.jinxiu.core.node;

@FunctionalInterface
public interface NodeInstanceProvider {
    <T extends Node> T findNodeByType(Class<T> nodeType);
}
