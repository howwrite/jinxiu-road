package com.github.howwrite.jinxiu.core.node;

@FunctionalInterface
public interface NodeProvider {
    <T extends Node> T findNodeByType(Class<T> nodeType);
}
