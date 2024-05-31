<h1 align="center">
  jinxiu-road 锦绣路
</h1>

<h4 align="center">
需要编排可灵活复用<br/>无需编排可极致加速
</h4>

## 核心功能

例如实现一个主页信息查询的接口，实现通过token查询当前用户信息、查询用户任务列表、查询当前天气等逻辑节点。
![sync](readme/sync.png)

- 其中每个执行节点(node)仅需关注自身方法需要什么入参以及声明返回值，jinxiu-road会根据入参类型从前置节点以及初始值中寻找类型相同的参数传入方法。
- 每个节点可以灵活替换，在构建流水线时只需要参数和返回值能匹配成功即可构建。

## 并行执行能力

![async](readme/async.png)

- 根据参数解析依赖关系，将可以并行执行的node并行处理。
- 线程池提供默认实现，同时可自定义替换。

## 如何使用

本项目本身不依赖任何其他框架，主要代码都在`jinxiu-road-core`模块中。<br/>
默认提供了基于spring boot框架的实现，本quick star也是基于spring boot实现来说明，毕竟大家的项目基本都是基于spring
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

启动器声明`@EnableJinxiu`注解。

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
        return new WeatherInfo("2023-02-04", "多云", 10, 5);
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
                        QueryUserTaskInfos.class,
                        QueryWeatherNode.class,
                        BuildResponse.class)
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

### 5. 调用

```java

@RestController
@RequestMapping
public class MiniProgramController {

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

## 当前已知问题

- [ ] 没有依赖关系图化表达(想想是一件有点难做的事情，懒得做)。
- [ ] 扩展能力不够完善，例如对于参数选择、日志处理等部分的处理不够优雅，这个持续优化，目前api没有完全确定。
- [ ] 异常处理不够优雅，尤其是并行实现中对异常的处理，还在探索如何优雅处理。
- [ ] 节点前后依赖关系是通过参数来做的，并没有提供自定义调整的能力，预期是在创建pipelineMeta的时候可以声明依赖关系，但是没有想好怎么做。
- [ ] 还有些小问题，比如单元测试不够完善等。慢慢完善。。

## 写在最后

+ 本项目起源于与朋友的一次闲谈，想要让需要流程编排的项目能有更优雅的实现，让node编写更优雅不需要考虑太多，同时开发过程中发现可以将没有依赖关系的node并行执行。
  灵感依赖落地代码就快得多。<br/>
+ 本项目还在不断演进阶段，同时设计也并不复杂，欢迎有想法的朋友提出pr或者issue，如果能点一个star就足够我开心到想倒立🤸‍♀️。
+ 本项目遵循Apache License2.0协议，可以不受限制地将代码用在任何地方。(
  如果你们某天收到的面试者的简历中说他是这个项目的开发者的话，还望可给一个面试机会哈哈哈)
+ 至于为什么叫锦绣路，因为那是我女朋友家门口的路名👩‍❤️‍👨。

## Star History
[![Star History Chart](https://api.star-history.com/svg?repos=howwrite/jinxiu-road&type=Date)](https://star-history.com/#howwrite/jinxiu-road&Date)

