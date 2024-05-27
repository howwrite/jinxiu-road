package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.annotation.Param;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.TestCreateUserRequest;
import com.github.howwrite.jinxiu.spring.mock.TestUserInfo;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BuildUserInfoNode implements Node {
    @Execute
    public TestUserInfo buildUserInfo(TestCreateUserRequest request, @Param(valueKey = "registerDate") LocalDate registerDate) {
        TestUserInfo testUserInfo = new TestUserInfo();
        testUserInfo.setUsername(request.getUsername());
        testUserInfo.setRegisterTime(registerDate.atTime(19, 0));
        testUserInfo.setBirthday(request.getBirthday());
        return testUserInfo;
    }
}
