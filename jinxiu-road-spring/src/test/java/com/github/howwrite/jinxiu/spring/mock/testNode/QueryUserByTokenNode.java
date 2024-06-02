package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class QueryUserByTokenNode implements Node {
    @Execute
    public UserInfo queryUserByToken(String token) {
        if (!token.startsWith("yes")) {
            throw new IllegalArgumentException("illegal user");
        }
        return new UserInfo(token.substring(3));
    }
}
