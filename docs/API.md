## API 说明

mars-rpc 框架里，服务消费者的 API 占了大多数，服务提供者占少数。从简单开始，我们先看看服务提供者的 API。

### 服务提供者

#### @RPCService

服务提供者只有一个注解，即 RPCService，该注解仅有一个字段 rpcInterface 用于声明某个服务实现的接口，像下面这样，我们把一个 spring 管理的 bean 所有实例方法暴露到注册中心：

```java
@RPCService(rpcInterface = SignInService.class)
@Service
public class SignInServiceImpl implements SignInService {
    //...
}
```

### 服务消费者

#### @RPCReference

该注解用于引用远程的某个服务，像下面这样，我们在 spring 管理的 bean 中引用一个服务，接着调用远程服务：

```java
@RestController
@RequestMapping
public class ConsumerController {

  	// 引用
    @RPCReference
    SignInService signInService;


    @GetMapping("signIn")
    public ResultBean<?> signIn(String name, String pass){
      	// 调用
        SignInResult signInResult = signInService.signIn(name, pass);
        if (!signInResult.isSuccess()){
            return new ResultBean<>("1001", "fail", null);
        }
        return new ResultBean<>("1000", "success", signInResult);
    }
}
```

RPCReference 注解中声明了多个属性，用于指定这个服务的所有方法都使用某个策略，即该服务全局策略，我们先来看看每个字段的意思。

##### serviceName

mars-rpc 框架是基于 Spring 的，在使用 Spring 中，同一个接口可能有多个实现，为了调用同一个接口的不同实现，需要在告诉服务提供者调用的是哪个服务，我们可以用 serviceName 声明调用哪个服务，像下面这样（记住要遵循 Spring bean 的命名规则，即驼峰命名法）：

```java
@RPCReference(serviceName = "signInService")
SignInService signInService;
```

当然，如果只有一个实现，这个属性可以不声明，不填默认会用完整的接口名去拿到对应的服务；

##### clusterStrategy

指定集群调用策略。mars-rpc 参考了 dubbo 的集群容错功能，实现了 5 种集群策略，分别是：

- FAILOVER：在调用失败时，会自动切换 Invoker 进行重试
- FAILSAFE：当调用过程中出现异常时，仅会打印异常，而不会抛出异常
- FORKING：在运行时通过线程池创建多个线程，并发调用多个服务提供者，只要有一个服务提供者成功返回了结果，其他的并发调用就取消
- FAIL_FAST：只会进行一次调用，失败后立即抛出异常
- BROADCAST：逐个调用每个服务提供者，如果其中一台报错，在循环调用结束后，会抛出异常

若不特别指定，会默认使用 FAILOVER 来进行集群容错；像下面这样，我们使用 FAIL_FAST 策略，不管成功与否，只进行一次调用：

```java
@RPCReference(clusterStrategy = Cluster.Strategy.FAIL_FAST)
SignInService signInService;
```

##### balanceStrategy

指定服务消费端的负载均衡策略。mars-rpc 参考了 dubbo 的负载均衡功能，实现了 4 种负载均衡策略，分别是：

- ROUND_ROBIN：带权重的轮询
- RANDOM：随机
- CONSISTENT_HASH：一致性哈希
- LEAST_ACTIVE：最少活跃数

若不特别指定，会默认使用 RANDOM 来进行负载均衡；像下面这样，我们使用 ROUND_ROBIN 模仿 nginx 带权重的轮询调用：

```java
@RPCReference(balanceStrategy = LoadBalance.Strategy.ROUND_ROBIN)
SignInService signInService;
```

##### timeout

指定超时时间。某些方法有响应时间的要求，我们可以通过该参数设置超时时间，默认是 3 秒；像下面这样，我们指定超时时间为 6 秒：

```java
@RPCReference(timeout = 6)
SignInService signInService;
```

##### forkingNumber

当集群容错策略为 FORKING 时，指定并发数，默认为 3。例如某个服务的提供者有 n 个，我们可以通过  forkingNumber = 2 来指定并发调用数为 2，若 forkingNumber > n，框架会将 forkingNumber 设为 n；FORKING 策略比较耗费性能，应根据场景使用。像下面这样，我们使用 FORKING 作为集群容错策略，并指定并发数为 5：

```java
@RPCReference(clusterStrategy = Cluster.Strategy.FORKING, forkingNumber = 5)
SignInService signInService;
```

##### retry

重试次数。集群容错策略 FAILOVER 的重试次数，默认为 2。

#### @RPCCustomization

该注解类似 RPCReference，RPCReference 指明了某个服务下所有方法的调用策略；RPCCustomization 指明了某个方法的调用策略，这样可以为不同方法定制不同的调用策略，RPCCustomization 中声明的属性与 RPCReference 大致一样，这里只对不同的属性进行说明：

##### methodName

某个方法名称，若方法名称相同参数不同，也会视为使用定制化策略，这个需要注意。指明要为这个方法使用特定的集群策略，像下面这样，为 methodA、methodB 使用不同的策略：

```java
@RPCCustomization(methodName = "methodA", clusterStrategy = Cluster.Strategy.FAIL_FAST, balanceStrategy = LoadBalance.Strategy.RANDOM)
@RPCCustomization(methodName = "methodB", clusterStrategy = Cluster.Strategy.FAILSAFE, balanceStrategy = LoadBalance.Strategy.ROUND_ROBIN)
@RPCReference
SignInService signInService;
```



