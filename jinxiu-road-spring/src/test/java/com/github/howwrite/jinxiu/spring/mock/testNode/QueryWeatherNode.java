package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.annotation.Param;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.WeatherInfo;
import org.springframework.stereotype.Component;

@Component
public class QueryWeatherNode implements Node {

    @Execute
    public WeatherInfo queryWeather(@Param(valueKey = "latitude") Double lat, @Param(valueKey = "longitude") Double lon) {
        if (lon.equals(119.98) && lat.equals(30.28)) {
            return new WeatherInfo("2023-02-04", "晴", 5, 20);
        }
        return null;
    }
}
