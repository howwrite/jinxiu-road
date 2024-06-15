package com.github.howwrite.jinxiu.core.annotation;


import java.lang.annotation.*;

/**
 * 表示某一个值是线程安全的，在并行时对于标注此注解的参数会并行处理
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SafeValue {
}
