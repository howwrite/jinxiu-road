package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CheckUserInfoNode implements Node {
    @Execute
    public void checkUserInfo(String username, LocalDate birthday) {
        if (StringUtils.isBlank(username)) {
            throw new RuntimeException("username is empty");
        }
        if (birthday.getYear() != 1998) {
            throw new RuntimeException("birthday year is not 1998");
        }
    }
}
