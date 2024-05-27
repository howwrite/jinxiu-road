package com.github.howwrite.jinxiu.spring.mock;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TestUserInfo {
    private String username;
    private LocalDateTime registerTime;
    private LocalDate birthday;
}
