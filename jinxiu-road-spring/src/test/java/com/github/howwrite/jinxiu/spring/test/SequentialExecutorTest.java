package com.github.howwrite.jinxiu.spring.test;

import com.github.howwrite.jinxiu.core.LetUsGo;
import com.github.howwrite.jinxiu.core.exception.ExecuteTargetException;
import com.github.howwrite.jinxiu.spring.BaseJinxiuSpringTest;
import com.github.howwrite.jinxiu.spring.mock.TestCreateUserRequest;
import com.github.howwrite.jinxiu.spring.mock.TestUserInfo;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SequentialExecutorTest extends BaseJinxiuSpringTest {
    @Test
    public void testCreateUser() {
        TestCreateUserRequest initValue = new TestCreateUserRequest();
        initValue.setUsername("test");
        initValue.setBirthday(LocalDate.of(1998, 1, 1));
        Object createUser = LetUsGo.go("createUser", initValue);
        TestUserInfo userInfo = (TestUserInfo) createUser;
        Assert.assertEquals("test", userInfo.getUsername());
        Assert.assertEquals(LocalDateTime.of(2023, 2, 4, 19, 0), userInfo.getRegisterTime());
        Assert.assertEquals(LocalDate.of(1998, 1, 1), userInfo.getBirthday());
    }

    @Test
    public void testCreateUserInfo() {
        String username = "new western city";
        TestCreateUserRequest initValue = new TestCreateUserRequest();
        initValue.setBirthday(LocalDate.of(1998, 1, 1));
        initValue.setUsername(username);
        Object createUser = LetUsGo.go("createUserInfo", initValue);
        TestUserInfo userInfo = (TestUserInfo) createUser;
        Assert.assertEquals(username, userInfo.getUsername());
        Assert.assertEquals(LocalDateTime.of(2023, 2, 4, 19, 0), userInfo.getRegisterTime());
        Assert.assertEquals(LocalDate.of(1998, 1, 1), userInfo.getBirthday());
    }

    @Test
    public void testCreateUserInfo_exception() {
        try {
            String username = "";
            TestCreateUserRequest initValue = new TestCreateUserRequest();
            initValue.setUsername(username);
            initValue.setBirthday(LocalDate.of(1998, 1, 1));
            Object createUser = LetUsGo.go("createUserInfo", initValue);
            Assert.fail();
        } catch (ExecuteTargetException e) {
            Throwable target = e.getTarget();
            Assert.assertEquals(RuntimeException.class, target.getClass());
            Assert.assertEquals("username is empty", target.getMessage());
        }
    }
}
