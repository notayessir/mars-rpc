[English](https://github.com/notayessir/mars-rpc/blob/master/README_EN.md) | 中文

## 介绍

使用 Netty 与 Spring 实现的简易轻量级 RPC 框架，可以方便的在基于 Spring Boot 的项目上使用。

该项目参考 dubbo 2.x 版本的部分设计细节实现。

## 用法

1）启动注册中心，该框架目前使用 ZooKeeper，使用该框架前应先启动 ZooKeeper

2）在 maven 中引入框架（暂未发布到 maven central，需在本地使用 maven 打包）

```
<dependency>
		<groupId>com.notayessir</groupId>
		<artifactId>mars-rpc-core</artifactId>
		<version>0.0.1</version>
</dependency>
```

3）编写 mars.xml

3.1）服务提供者加入如下配置：

```
<mars:provider>
		<!--  注册中心端口 -->
    <mars:registry registry="ZOOKEEPER" host="127.0.0.1" port="2181"/>
    <!--  Netty 服务器暴露的 ip 与端口 -->
    <mars:protocol protocol="MARS" host="127.0.0.1" port="5669"/>
</mars:provider>
```

3.2）服务消费者加入如下配置：

```
<mars:consumer>
		<!--  注册中心端口 -->
    <mars:discovery registry="ZOOKEEPER" host="127.0.0.1" port="2181"/>
</mars:consumer>
```

4）使用注解（目前只支持注解式使用）

4.1）在 Application.java 中使用 @EnableMars 来允许扫描框架组件：

```java
@EnableMars		// 允许扫描 RPC 组件
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

4.2）服务提供者在 Spring 管理的 bean 上暴露服务：

```java
@RPCService(rpcInterface = SignInService.class)		// 声明接口并暴露该服务
@Service
public class SignInServiceImpl implements SignInService {
    // ....
}
```

4.3）服务消费者在 Spring 管理的 bean 中引用服务（例如 controller、service、component），并调用：

```java
@RestController
@RequestMapping
public class ConsumerController {

  	// 远程引用
    @RPCReference
    SignInService signInService;

    @GetMapping("signIn")
    public ResultBean<?> signIn(String name, String pass){
      	// 基于接口的调用
        SignInResult signInResult = signInService.signIn(name, pass);
        if (!signInResult.isSuccess()){
            return new ResultBean<>("1001", "fail", null);
        }
        return new ResultBean<>("1000", "success", signInResult);
    }
}
```

## 待做

- [ ] 集成 ProtoBuf、Avro 的序列化框架
- [ ] 集成 etcd、Redis 的注册中心
- [ ] 基准测试

## 参考文档

1. [API 说明](https://github.com/notayessir/mars-rpc/blob/master/docs/API.md)
2. [mars.xml 配置说明](https://github.com/notayessir/mars-rpc/blob/master/docs/mars.xml.md)
3. [第三方组件](https://github.com/notayessir/mars-rpc/blob/master/docs/component.md)
4. [调用流程](https://github.com/notayessir/mars-rpc/blob/master/docs/process.md)
5. [帧设计](https://github.com/notayessir/mars-rpc/blob/master/docs/frame.md)

