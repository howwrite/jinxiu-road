package com.github.howwrite.jinxiu.spring.mock.testNode;

import com.github.howwrite.jinxiu.core.annotation.Execute;
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.spring.mock.TaskInfo;
import com.github.howwrite.jinxiu.spring.mock.UserInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryUserTaskInfosNode implements Node {
    @Execute
    public List<TaskInfo> queryUserTaskInfos(UserInfo userInfo) {
        if (userInfo.getName().equals("howwrite")) {
            return Lists.newArrayList(new TaskInfo("coding"), new TaskInfo("sleep"));
        }
        return Lists.newArrayList();
    }
}
