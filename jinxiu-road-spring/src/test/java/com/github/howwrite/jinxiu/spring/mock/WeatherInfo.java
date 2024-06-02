package com.github.howwrite.jinxiu.spring.mock;

import lombok.Data;

/**
 * 天气信息
 */
@Data
public class WeatherInfo {
    private final String dateStr;
    private final String weatherName;
    private final Integer minTemperature;
    private final Integer maxTemperature;
}
