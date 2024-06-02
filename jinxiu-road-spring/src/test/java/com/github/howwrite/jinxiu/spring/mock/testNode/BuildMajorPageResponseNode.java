package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.MajorPageResponse;
import com.github.howwrite.jinxiu.spring.mock.TaskInfo;
import com.github.howwrite.jinxiu.spring.mock.UserInfo;
import com.github.howwrite.jinxiu.spring.mock.WeatherInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuildMajorPageResponseNode implements Node {

    @Execute
    public MajorPageResponse buildMajorPageResponse(UserInfo userInfo, List<TaskInfo> taskInfos, WeatherInfo weatherInfo) {
        return new MajorPageResponse(userInfo, weatherInfo, taskInfos);
    }
}
