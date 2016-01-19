#Hello

# kill port :
* lsof -i tcp:28088 | awk ' NR > 1 {print $2}' | xargs kill -9


## 协议支持:
* thrift >= 0.8.0(暂时 0.8.0  0.9.3 都支持)




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

