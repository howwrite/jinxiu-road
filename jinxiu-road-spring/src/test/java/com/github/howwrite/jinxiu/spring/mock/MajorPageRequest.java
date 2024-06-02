package com.github.howwrite.jinxiu.spring.mock;

import lombok.Data;

@Data
public class MajorPageRequest {
    /**
     * 登录token
     */
    private String loginToken;
    /**
     * 经纬度
     */
    private Double longitude;
    private Double latitude;
}
