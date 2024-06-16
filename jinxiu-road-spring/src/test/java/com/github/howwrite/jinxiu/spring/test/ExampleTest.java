package com.github.howwrite.jinxiu.spring.test;

import com.github.howwrite.jinxiu.core.LetUsGo;
import com.github.howwrite.jinxiu.core.exception.ExecuteTargetException;
import com.github.howwrite.jinxiu.spring.BaseJinxiuSpringTest;
import com.github.howwrite.jinxiu.spring.mock.MajorPageRequest;
import com.github.howwrite.jinxiu.spring.mock.MajorPageResponse;
import org.junit.Assert;
import org.junit.Test;

public class ExampleTest extends BaseJinxiuSpringTest {
    /**
     * 串行成功
     */
    @Test
    public void happyPath() {
        // see com.github.howwrite.jinxiu.spring.TestConfiguration.testMajorPage
        MajorPageRequest request = new MajorPageRequest();
        request.setLoginToken("yeshowwrite");
        request.setLongitude(119.98);
        request.setLatitude(30.28);
        MajorPageResponse majorPage = (MajorPageResponse) LetUsGo.go("majorPage", request);

        // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryUserByTokenNode
        Assert.assertEquals("howwrite", majorPage.getUserInfo().getName());

        // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryUserTaskInfosNode
        Assert.assertEquals(2, majorPage.getTaskInfos().size());
        Assert.assertEquals("coding", majorPage.getTaskInfos().get(0).getTaskName());
        Assert.assertEquals("sleep", majorPage.getTaskInfos().get(1).getTaskName());
        Assert.assertEquals("taskCount=2", majorPage.getLog());

        // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryWeatherNode
        Assert.assertEquals("2023-02-04", majorPage.getWeatherInfo().getDateStr());
        Assert.assertEquals("晴", majorPage.getWeatherInfo().getWeatherName());
        Assert.assertEquals((Integer) 5, majorPage.getWeatherInfo().getMinTemperature());
        Assert.assertEquals((Integer) 20, majorPage.getWeatherInfo().getMaxTemperature());
    }

    /**
     * 串行异常
     */
    @Test
    public void exception() {
        // see com.github.howwrite.jinxiu.spring.TestConfiguration.testMajorPage
        MajorPageRequest request = new MajorPageRequest();
        request.setLoginToken("nohowwrite");
        request.setLongitude(119.98);
        request.setLatitude(30.28);
        try {
            MajorPageResponse majorPage = (MajorPageResponse) LetUsGo.go("majorPage", request);
            Assert.fail();
        } catch (Exception e) {
            // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryUserByTokenNode
            Assert.assertEquals(ExecuteTargetException.class, e.getClass());
            Throwable target = ((ExecuteTargetException) e).getTarget();
            Assert.assertEquals(IllegalArgumentException.class, target.getClass());
            Assert.assertEquals("illegal user", target.getMessage());
        }
    }

    /**
     * 并行
     */
    @Test
    public void async_happyPath() {
        // see com.github.howwrite.jinxiu.spring.TestConfiguration.testMajorPage
        MajorPageRequest request = new MajorPageRequest();
        request.setLoginToken("yeshowwrite");
        request.setLongitude(119.98);
        request.setLatitude(30.28);
        MajorPageResponse majorPage = (MajorPageResponse) LetUsGo.go("asyncMajorPage", request);

        // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryUserByTokenNode
        Assert.assertEquals("howwrite", majorPage.getUserInfo().getName());

        // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryUserTaskInfosNode
        Assert.assertEquals(2, majorPage.getTaskInfos().size());
        Assert.assertEquals("coding", majorPage.getTaskInfos().get(0).getTaskName());
        Assert.assertEquals("sleep", majorPage.getTaskInfos().get(1).getTaskName());
        Assert.assertEquals("taskCount=2", majorPage.getLog());

        // see com.github.howwrite.jinxiu.spring.mock.testNode.QueryWeatherNode
        Assert.assertEquals("2023-02-04", majorPage.getWeatherInfo().getDateStr());
        Assert.assertEquals("晴", majorPage.getWeatherInfo().getWeatherName());
        Assert.assertEquals((Integer) 5, majorPage.getWeatherInfo().getMinTemperature());
        Assert.assertEquals((Integer) 20, majorPage.getWeatherInfo().getMaxTemperature());
    }
}
