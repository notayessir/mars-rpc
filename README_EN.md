English | [中文](https://github.com/notayessir/mars-rpc/blob/master/README.md)

## Introduction

Lightweight RPC framework base on Netty and Spring, it's convenient to access in project base on Spring Boot.

Some details implemented with reference to Dubbo 2.x version. 

## Usage

1\) start zookeeper, project only support zookeeper as registry for now;

2\) import mars-rpc-core package, the package is not published on maven, should package in local maven environment;

```
<dependency>
		<groupId>com.notayessir</groupId>
		<artifactId>mars-rpc-core</artifactId>
		<version>0.0.1</version>
</dependency>
```

3\) config mars.xml

3.1\) service provider :

```
<mars:provider>
		<!--  registry -->
    <mars:registry registry="ZOOKEEPER" host="127.0.0.1" port="2181"/>
    <!--  netty server -->
    <mars:protocol protocol="MARS" host="127.0.0.1" port="5669"/>
</mars:provider>
```

3.2\) service consumer :

```
<mars:consumer>
		<!--  registry -->
    <mars:discovery registry="ZOOKEEPER" host="127.0.0.1" port="2181"/>
</mars:consumer>
```

4\) using annotation

4.1\) annotate with @EnableMars in Application.java to scan the package conponent :

```java
@EnableMars		// RPC scan
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

4.2\) service provider expose the service base on spring bean :

```java
@RPCService(rpcInterface = SignInService.class)		// expose service
@Service
public class SignInServiceImpl implements SignInService {
    // ....
}
```

4.3\) consumer refer the service and invoke the method in controller, service, conponent etc... managed by spring bean :

```java
@RestController
@RequestMapping
public class ConsumerController {

  	// reference
    @RPCReference
    SignInService signInService;

    @GetMapping("signIn")
    public ResultBean<?> signIn(String name, String pass){
      	// invoke
        SignInResult signInResult = signInService.signIn(name, pass);
        if (!signInResult.isSuccess()){
            return new ResultBean<>("1001", "fail", null);
        }
        return new ResultBean<>("1000", "success", signInResult);
    }
}
```

## Todo

- [ ] Integrated ProtoBuf、Avro serial framework
- [ ] Integrated etcd、Redis as registry
- [ ] Benchmark

## Reference

1. [API 说明](https://github.com/notayessir/mars-rpc/blob/master/docs/API.md)
2. [mars.xml 配置说明](https://github.com/notayessir/mars-rpc/blob/master/docs/mars.xml.md)
3. [第三方组件](https://github.com/notayessir/mars-rpc/blob/master/docs/component.md)
4. [调用流程](https://github.com/notayessir/mars-rpc/blob/master/docs/process.md)
5. [帧设计](https://github.com/notayessir/mars-rpc/blob/master/docs/frame.md)

