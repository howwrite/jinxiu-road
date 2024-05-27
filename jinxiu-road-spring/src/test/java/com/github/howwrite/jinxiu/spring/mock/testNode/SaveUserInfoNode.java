package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.TestUserInfo;
import org.springframework.stereotype.Component;

@Component
public class SaveUserInfoNode implements Node {
    @Execute
    public TestUserInfo buildUserInfo(TestUserInfo userInfo) throws InterruptedException {
        // 模拟超时操作
        Thread.sleep(500);
        return userInfo;
    }
}
