package com.github.howwrite.jinxiu.spring;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@EnableJinxiu
@SpringBootTest
@RunWith(SpringRunner.class)
@ComponentScan
@Import(TestConfiguration.class)
@Ignore
public class BaseJinxiuSpringTest {

}
