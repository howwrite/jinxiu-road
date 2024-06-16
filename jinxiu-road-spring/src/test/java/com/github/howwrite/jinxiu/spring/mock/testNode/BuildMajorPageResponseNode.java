package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BuildMajorPageResponseNode implements Node {

    @Execute
    public MajorPageResponse buildMajorPageResponse(UserInfo userInfo, List<TaskInfo> taskInfos, WeatherInfo weatherInfo,
                                                    TestGlobalValue testGlobalValue) {
        return new MajorPageResponse(userInfo, weatherInfo, taskInfos, testGlobalValue.printLog());
    }
}
