package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RecallOtherUserInfoNode implements Node {
    @Execute(valueKey = "registerDate")
    public LocalDate recallOtherUserInfo() {
        try {
            // 模拟超时操作
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return LocalDate.of(2023, 2, 4);
    }
}
