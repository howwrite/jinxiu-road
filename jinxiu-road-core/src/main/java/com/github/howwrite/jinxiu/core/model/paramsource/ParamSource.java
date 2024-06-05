package com.github.howwrite.jinxiu.core.model.paramsource;


import com.github.howwrite.jinxiu.core.runtime.NodeRuntime;

/**
 * 参数来源
 */
public interface ParamSource {
    /**
     * 用于表述node执行方法中的参数是从哪里获取
     *
     * @param initValue    管道执行初始值
     * @param nodeRuntimes 之前节点的返回值
     * @return 通过初始值或者之前节点返回值获取的当前参数值
     */
    Object getParam(Object initValue, NodeRuntime[] nodeRuntimes);
}
