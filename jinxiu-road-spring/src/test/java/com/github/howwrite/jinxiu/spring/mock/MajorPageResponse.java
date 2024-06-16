package com.github.howwrite.jinxiu.spring.mock;

import lombok.Data;

import java.util.List;

@Data
public class MajorPageResponse {
    private final UserInfo userInfo;
    private final WeatherInfo weatherInfo;
    private final List<TaskInfo> taskInfos;
    private final String log;
}
