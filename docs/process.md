## 启动与调用流程

### 启动流程

#### 解析 mars.xml

借助于 Spring 的 NamespaceHandlerSupport 机制，获取用户的 mars.xml 配置文件，将 xml 信息转换成对应的配置 bean。

#### 服务引用/注册

借助于 Spring 的 ContextRefreshedEvent 事件，mars-rpc 中实现了 2 个类对该事件进行监听，分别是：

1. RPCDetectListener：遍历 Spring 管理的 bean，扫描每个 bean 内声明的字段是否带有特定的注解；先于 StartupListener；
2. StartupListener：将 RPCDetectListener 扫描到的注解根据服务提供者配置暴露到注册中心，或者根据服务消费者配置引用服务；

下图简单描述了 mars-rpc 的启动流程：

![init](https://github.com/luckyMax-dev/mars-rpc/blob/master/docs/image/init.jpeg)

### 调用流程

完整的调用流程包含消费者发起调用请求，与提供者处理请求，下面是消费者的具体流程：

1. 透明的使用接口声明的方法；
2. 使用 JDK Proxy 或 CGLib 进行远程调用；
3. 将调用的接口、方法信息转为 Invocation 对象，包装成 FutureTask 提交至线程池阻塞（根据集群策略设置超时时间以及负载均衡策略）；
4. 将 Invocation 对象序列化成字节，传输至服务提供者；

服务提供者收到请求之后，有以下流程：

1. 读取帧头部，检查魔数是否正确，数据长度是否在允许的范围内；
2. 根据头部的序列化框架标识，反序列化成 Invocation 对象，解析出需要调用的接口方法；
3. 包装成 Callable 任务提交至线程池，获取执行结果；
4. 将结果序列化成字节，传输至服务消费者；

服务消费者收到响应之后，有以下流程：

1. 反序列化成调用结果并返回给上层；

下图简单描述调用过程：

![invoke](https://github.com/luckyMax-dev/mars-rpc/blob/master/docs/image/invoke.jpeg)