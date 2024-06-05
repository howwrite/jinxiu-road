package com.github.howwrite.jinxiu.core.node;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.model.paramsource.ParamSource;
import lombok.Data;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

@Data
public class NodeMeta {
    /**
     * 当前节点在管道中的下标
     */
    private final int index;

    /**
     * 节点执行类的类型
     * 在运行时会通过{@link NodeInstanceProvider}获取运行时的实例
     */
    @Nonnull
    private final Class<? extends Node> nodeClass;


    /**
     * 节点执行的具体方法
     * 需要在方法上标注{@link Execute}注解
     */
    @Nonnull
    private final Method executeMethod;

    /**
     * 节点执行方法的注解信息
     */
    @Nonnull
    private final Execute executeAnnotation;

    /**
     * 执行方法的参数的来源
     */
    private final ParamSource[] paramSources;
}
