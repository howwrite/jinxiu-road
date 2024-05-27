package com.github.howwrite.jinxiu.spring.mock;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TestCreateUserRequest {
    private String username;
    private LocalDate birthday;
}
