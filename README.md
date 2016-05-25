# dubbo thrift协议扩展
`
(thrift2(?),thrift093(建议在系统里都使用这个协议),swift(暂时不支持)),以原生协议提供服务.

thrift0.9.1之后开始支持多路复用;这里我新增的部分没有使用这一特性,所以理论上是支持从thrift0.8.0--thrift0.9.3之间的所有版本,
但是 依然建议最好用thrift0.9.3.

暂时支持thrift 二进制编码的形式: TBinaryProtocol

`
## kill port :
* lsof -i tcp:28088 | awk ' NR > 1 {print $2}' | xargs kill -9


## 使用方法:

### 服务端
```

pom.xml 添加 2个依赖就可以了:

<dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.5.3</version>
            <scope>provided</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring</artifactId>
                </exclusion>
            </exclusions>
 </dependency>


 <dependency>

     <groupId>cn.jpush</groupId>
     <artifactId>dubbo-rpc-thrift</artifactId>
     <version>1.0.0</version>

 </dependency>


已经更名为 jpush-dubbo-plugins



<dubbo:protocol id="thrift093" name="thrift093" />
<bean id="helloService" class="cn.jpush.dubbo.thritf093.HelloServiceImpl" />
<dubbo:service interface="cn.jpush.dubbo.thritf093.HelloService$Iface"
	ref="helloService" protocol="thrift093">
</dubbo:service>
```
### 客户端
`必须使用 TBinary 协议 和 TFramedTransport `


## 协议支持:
* thrift >= 0.8.0(暂时 0.8.0  0.9.3 都支持)

## 新增 thrift2,thrift093,协议
* thrift2 用netty 重写编码解码 连接的部分
* thrift093 在thrift 0.9.3版本的基础上
* swift协议暂时由于jdk编码问题还没加入进来测试,但是这个恐怕是最好的组合,无论用不用dubbo,swift都极大的简化了

## 新增 container

## 注意:

* 客户端对thrift 版本的升级必须联系服务端;尽量保持一致
* swift已经支持对thrift的开发进行了改造,可以以注解方式进行thrift开发,可以抛弃thrift文件了
没时间研究 去兼容
* 0.8.* 只支持一个端口 一个服务
* 0.9.1 开始支持多路复用,开始支持同一个端口暴漏多个服务;
* Thrift多路复用协议的原理:

```
TMultiplexedProtocol:
增加的部分就是:

 serviceName:methodName 替代 传输头部里的methodName

```
这样就可以在不改变就的协议的基础上 新增了对多Service的支持.


* 现在发布在maven 第三方库测试 http://maven.jpushoa.com/nexus/content/repositories/thirdparty



## 以供参考:
服务端代码   供写客户端参考
```java

TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(port);
org.apache.thrift.server.THsHaServer.Args thhsArgs = new THsHaServer.Args(tnbSocketTransport);
thhsArgs.processor(processor);
thhsArgs.transportFactory(new TFramedTransport.Factory());
thhsArgs.protocolFactory(new TBinaryProtocol.Factory());


```


## changed :

> 更改了 artifactId : dubbo-plugins

    <groupId>cn.jpush</groupId>
    <artifactId>dubbo-plugins</artifactId>

## todo list 未完成
* thrift093协议 dubbo客户端未完成?(优先级最低)
* 一些断开 重连重试 负载均衡源码查看,并实现thrift093协议的断开重连重试
* 和spring 高版本兼容测试; 目前已知的是 spring3 和 spring4应该都没问题的;具体的版本兼容待测试; 建议4.0.9
* URL 的各种参数的产生和传递和使用
* 写一个demo项目 以后关于dubbo的项目 都以这套配置为原型
* 监控项目:(部署两套 dubbo官方的一套,第三方的一套)
* 多路复用的实现
* SOA后期优化,dubbo项目部署优化 ( 容器部署 和 jar包共享 )




## dubbo + thrift
```
<dubbo:service interface="cn.jpush.dubbo.thritf093.HelloService$Iface"
		ref="helloService" protocol="thrift093">
		<dubbo:parameter key="thrift_server_type" value="TNonblockingServer"></dubbo:parameter>
	</dubbo:service>
```


## NEW COMMON

* dubbo thrift java 的客户端没写 感觉没必要  java调用java 我特默就不用thrift了.
