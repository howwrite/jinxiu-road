package com.github.howwrite.jinxiu.core.annotation;

import com.github.howwrite.jinxiu.core.pipeline.PipelineMetaFactory;

import java.lang.annotation.*;

/**
 * 注释于node节点的执行方法上
 * 一个类中只能有一个方法被此注解修饰
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Execute {
    /**
     * 当前方法返回值对应的key
     * 用于与后续节点执行方法参数匹配, {@link com.github.howwrite.jinxiu.core.component.ParamMatcher#match}
     * 默认是node类的simpleName{@link PipelineMetaFactory#buildReturnValueMetaKey}
     *
     * @return valueKey 不写则表示不做特殊标注
     */
    String valueKey() default "";
}
