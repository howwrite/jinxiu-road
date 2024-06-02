## 快速开始

本项目本身不依赖任何其他框架，核心代码都在`jinxiu-road-core`模块中。<br/>
默认提供了基于spring boot框架的实现，`jinxiu-road-spring`，本quick start也是基于spring boot实现来说明，毕竟大家的项目基本都是基于spring
boot。<br/>
样例代码我一会写一下。。

### 1. 引入依赖

```xml

<dependency>
    <groupId>com.github.howwrite</groupId>
    <artifactId>jinxiu-road-spring</artifactId>
    <version>1.0</version>
</dependency>
```

建议使用最新版本 https://central.sonatype.com/artifact/com.github.howwrite/jinxiu-road-spring

### 2. 在启动器声明启用注解

```java
import com.github.howwrite.jinxiu.spring.EnableJinxiu;

@EnableJinxiu
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

+ 启动器声明`@EnableJinxiu`注解。

### 3. 编写执行节点(Node)

例如编写一个通过经纬度查询天气信息的节点。

```java
import com.github.howwrite.jinxiu.core.node.Node;
import com.github.howwrite.jinxiu.core.annotation.Execute;

@Component
public class QueryWeatherNode implements Node {

    @Execute
    public WeatherInfo queryWeather(Double latitude, Double longitude) {
        // ... 调用天气api
        // 将天气结果返回
        return new WeatherInfo("2023-02-04", "多云", 5, 20);
    }
}
```

1. node节点要继承`Node`接口。
2. 类中有且仅有一个public方法声明`@Execute`注解，它就是这个节点执行的方法。
3. 将类注入spring容器中。

### 4. 声明流水线(Pipeline)

```java
import com.github.howwrite.jinxiu.core.pipeline.PipelineMetaFactory;
import com.github.howwrite.jinxiu.core.pipeline.PipelineMeta;

@Configuration
public class DefaultConfiguration {
    @Resource
    private PipelineMetaFactory pipelineMetaFactory;

    @Bean
    public PipelineMeta queryMajorPagePipeline() {
        PipelineMetaBuildParam pipelineMetaBuildParam = PipelineMetaBuildParam.builder()
                .name("queryMajorPage")
                .initValueType(MajorPageRequest.class)
                .nodeTypes(QueryUserByTokenNode.class,
                        QueryUserTaskInfosNode.class,
                        QueryWeatherNode.class,
                        BuildMajorPageResponseNode.class)
                .returnValueNodeType(BuildResponse.class)
                .build();
        return pipelineMetaFactory.buildPipelineMeta(pipelineMetaBuildParam);
    }
}
```

1. `name`表示当前流水线的名称，后续调用也是通过name调用。
2. `initValueType`声明初始值的类型，调用时需要传入这个类型的对象。
3. `nodeTypes`声明流水线中执行节点的类型，有前后顺序。
4. `returnValueNodeType`表示有返回值的node类型，当此类型的node执行完成后它的结果即是返回值。
5. 将pipelineMeta对象注入spring容器。

### 5. 调用

```java

@RestController
@RequestMapping
public class Controller {

    @GetMapping("/queryMajorPage")
    public Response<MajorPageResponse> queryMajorPage(@RequestParam String token,
                                                      @RequestParam Double latitude, @RequestParam Double longitude) {
        MajorPageRequest initValue = new MajorPageRequest();
        initValue.setToken(token);
        initValue.setLatitude(latitude);
        initValue.setLongitude(longitude);
        return Response.ok((MajorPageResponse) LetUsGo.go("queryMajorPage", initValue));
    }
}
```

1. 使用`LetUsGo.go()`声明要调用的pipeline名字，以及初始值对象。

## 并行执行

pipeline默认是串行执行，提供默认执行实现，仅需在spring容器中增加以下组件实现，即可实现对名字是async开头的pipeline并行执行，其他pipeline还是串行执行。

```java

@Configuration
public class DefaultConfiguration {
    @Bean
    public PipelineExecutorProvider autoSyncExecutorProvider(PipelineRuntimeFactory pipelineRuntimeFactory) {
        ASyncPipelineExecutor aSyncPipelineExecutor = new ASyncPipelineExecutor(pipelineRuntimeFactory);
        SequentialPipelineExecutor sequentialPipelineExecutor = new SequentialPipelineExecutor(pipelineRuntimeFactory);
        return pipelineMeta -> pipelineMeta.getName().startsWith("async") ? aSyncPipelineExecutor : sequentialPipelineExecutor;
    }
}
```

大部分场景在通过以上bean注入即可替换默认实现，因为在默认声明中增加了`@ConditionalOnMissingBean`
注解，但是在一些依赖比较复杂的项目中可能会失效，可以在`application.yml`
中配置的方式来关闭默认组件。具体见`JinxiuRoadConfiguration`。

```yaml
jinxiu:
  enable-default-pipeline-executor-provider: false
```
