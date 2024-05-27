package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.TestCreateUserRequest;
import org.springframework.stereotype.Component;

@Component
public class CheckUserRequestNode implements Node {
    @Execute
    public void checkUserRequest(TestCreateUserRequest userRequest) {
        if (userRequest == null) {
            throw new RuntimeException("request is null");
        }
    }
}
