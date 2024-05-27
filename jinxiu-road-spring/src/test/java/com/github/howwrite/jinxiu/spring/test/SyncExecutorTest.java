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

public class SyncExecutorTest extends BaseJinxiuSpringTest {

    @Test
    public void testCreateUserInfo() {
        String username = "new western city";
        TestCreateUserRequest initValue = new TestCreateUserRequest();
        initValue.setUsername(username);
        initValue.setBirthday(LocalDate.of(1998, 5, 1));
        Object createUser = LetUsGo.go("syncCreateUserInfo", initValue);
        TestUserInfo userInfo = (TestUserInfo) createUser;
        Assert.assertEquals(username, userInfo.getUsername());
        Assert.assertEquals(LocalDateTime.of(2023, 2, 4, 19, 0), userInfo.getRegisterTime());
        Assert.assertEquals(LocalDate.of(1998, 5, 1), userInfo.getBirthday());
    }

    @Test
    public void testCreateUserInfo_exception() {
        String username = "new western city";
        TestCreateUserRequest initValue = new TestCreateUserRequest();
        initValue.setBirthday(LocalDate.now());
        initValue.setUsername(username);
        try {
            Object createUser = LetUsGo.go("syncCreateUserInfo", initValue);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ExecuteTargetException);
            Assert.assertEquals("birthday year is not 1998", ((ExecuteTargetException) e).getTarget().getMessage());
        }
    }
}
