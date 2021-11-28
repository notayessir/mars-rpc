## mars.xml 文件说明

mars.xml 文件参考了 Spring 的 XML Scheme Authoring 章节，自定义了部分配置参数；然后再结合 Spring  namespace 将参数注册到 Spring 管理的容器中；mars.xml 的配置分为 3 个部分，首先看下服务提供者的配置。

### 服务提供者

#### mars:provider

该标签下的属性用于服务提供者，其下有 2 个子标签，即注册中心的端口、Netty 服务器暴露的端口。

##### mars:registry

使用的注册中心，该子标签有 2 个属性：

- registry：注册中心框架
- host：注册中心 ip
- port：注册中心端口

##### mars:protocol

Netty 服务器监听的端口，该标签下有 2 个属性：

- protocol：使用的协议，目前只支持 Netty
- host：Netty 服务器 ip
- port：Netty 服务器监听的端口

像下面这样，为服务提供者设置了使用 ZooKeeper 作为注册中心，并使 Netty 监听 5669 端口：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mars="https://github.com/luckyMax-dev/scheme/tree/master/mars"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        https://github.com/luckyMax-dev/scheme/tree/master/mars
        https://github.com/luckyMax-dev/scheme/tree/master/mars/mars.xsd">

    <mars:provider>
        <mars:registry registry="ZOOKEEPER" host="127.0.0.1" port="2181"/>
        <mars:protocol protocol="MARS" host="127.0.0.1" port="5669"/>
    </mars:provider>

</beans>
```

### 服务消费者

#### mars:consumer

该标签下的属性用于服务消费者，其下有 3 个子标签，分别是注册中心、集群策略、序列化框架。

##### mars:discovery

使用的注册中心，该子标签有 2 个属性：

- registry：注册中心框架
- host：注册中心 ip
- port：注册中心端口

##### mars:cluster

全局的集群策略，若需要特别定制，使用 RPCReference 注解。该标签和 RPCReference 中声明的属性意义一致，也就是当 RPCReference 中的属性没有配置时，会使用该标签的配置，当该标签的属性没有配置时，会使用代码里默认构造器的配置。

- strategy：集群策略
- balanceStrategy：负载均衡策略
- timeout：超时时间
- retry：集群策略 FAILOVER 的重试次数
- forkingNumber：集群容错策略为 FORKING 时，指定并发数

##### mars:serialize

指定服务消费端的序列化框架。当前只实现了 fastjaon 序列化框架，后续会拓展；该子标签有 1 个属性，用于指定序列化框架：

- serialization：指定序列化框架，如 FASTJSON

像下面这样，指定全局集群策略为 FORKING、负载均衡为 LEAST_ACTIVE、序列化框架 FASTJSON：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mars="https://github.com/luckyMax-dev/scheme/tree/master/mars"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        https://github.com/luckyMax-dev/scheme/tree/master/mars
        https://github.com/luckyMax-dev/scheme/tree/master/mars/mars.xsd">
    
    <mars:consumer>
        <mars:discovery registry="ZOOKEEPER" host="127.0.0.1" port="2181"/>
        <mars:cluster strategy="FORKING" forkingNumber="5" timeout="6" balanceStrategy="LEAST_ACTIVE"/>
        <mars:serialize serialization="FASTJSON"/>
    </mars:consumer>
    
</beans>
```

### 通用标签

#### mars:common

通用配置里目前只有线程池子标签，mars-rpc 中，服务消费者发起请求和服务提供者处理请求均使用同一个线程池，子标签可以设置该线程池的大小。

##### mars:executor

该标签下有 3 个属性，用于指定线程池队列长度、线程最小个数、线程最大个数，该标签为选填：

- coreSize：空闲线程数
- maximumSize：最大线程数
- queueSize：任务队列长度，若短时间内，队列满了就会使用拒绝策略

像下面这样，指定空闲线程数为 8，最大线程数为 16，任务队列长度 1024：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:mars="https://github.com/luckyMax-dev/scheme/tree/master/mars"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        https://github.com/luckyMax-dev/scheme/tree/master/mars
        https://github.com/luckyMax-dev/scheme/tree/master/mars/mars.xsd">
  
    <!-- 省略服务消费者活服务提供者配置 -->

    <mars:common>
        <mars:executor coreSize="8" maximumSize="16" queueSize="1024"/>
    </mars:common>

</beans>
```

