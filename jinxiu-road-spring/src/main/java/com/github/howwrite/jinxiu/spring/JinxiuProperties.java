package com.github.howwrite.jinxiu.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 默认组件开关，默认都是开
 *
 * @see JinxiuRoadConfiguration
 */
@Data
@ConfigurationProperties(prefix = "jinxiu")
public class JinxiuProperties {
    private Boolean enableDefaultPipelineExecutorProvider;
    private Boolean enableDefaultNodeProvider;
    private Boolean enableDefaultNodeMetaFactory;
    private Boolean enableDefaultPipelineMetaFactory;
    private Boolean enableDefaultParamMatcher;
}
