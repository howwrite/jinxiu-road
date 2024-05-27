package com.github.howwrite.jinxiu.spring;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * spring boot的方式启动jinxiu-road的默认组件
 *
 * @see JinxiuRoadConfiguration
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JinxiuRoadConfiguration.class)
public @interface EnableJinxiu {
}
