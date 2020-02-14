-------------------------------------------------------------------
#### 演示项目架构
![](docs/images/architecture.png) <br />

本项目基础演示部分包括基于SpringBoot的4个Dubbo微服务和一个shop-web应用，另外还包含以下几个方面：

##### 1、Docker容器化
除个别基础组件外，整个演示项目（包括MySQL、Seata、Nacos、ZipKin、SkyWalking、Dubbo服务、Web应用等）都支持容器化运行，包含了Dockerfile和相关的管理脚本，可以方便快速的运行演示应用。

##### 2、分布式事务管理
阿里云分布式事务管理GTS的开源版Seata，2019年1月开源出来，1.0.0版已经发布。相关概念、部署和使用方法参考[Seata分布式事务管理框架概览](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Seata-Distributed-Transaction-Management.md)。

Seata提供AT、TCC、Saga三种柔性事务模式，可以跨微服务和应用实现分布式事务管理，AT模式对应用几乎透明，使用方便，目前来看：
1. 性能开销还比较高；
2. 在使用Mycat、Sharding-Proxy进行分库分表时，Seata会产生不少路由到全分片执行的SQL操作，详细参考[Seata分布式事务管理框架概览](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Seata-Distributed-Transaction-Management.md)文末；

##### 3、数据库分库分表
本项目演示了使用Mycat和Sharding-Proxy进行分库分表，相关概念、部署和使用方法，参考[MyCat分库分表概览](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Sharding-Mycat-Overview-Quickstart.md)、[Sharding-Proxy分库分表概览](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Sharding-Sharding-Proxy-Overview-Quickstart.md)，这2个分库分表开源方案与阿里云DRDS对比，参考[DRDS产品概览](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Sharding-DRDS-Overview.md)。

Mycat、Sharding-Proxy和DRDS都实现了MySQL协议，成为独立的中间件，将分库分表、读写分离等数据存储的弹性伸缩方案与应用隔离，并且实现语言无关。

##### 4、APM全链路监控
演示项目支持PinPoint、SkyWalking、ZipKin三种APM工具进行全链路跟踪和性能分析，相关概念、部署和使用方法，参考[PinPoint部署和使用](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-PinPoint.md)、[SkyWalking部署和使用](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-SkyWalking.md)、[ZipKin部署和使用](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-ZipKin.md)。

三种APM工具对比：
- 使用方式：PinPoint和SkyWalking都采用javaagent方式，对应用代码几乎没有侵入性；ZipKin需要和应用打包到一起，并在应用中完成各种配置，属于强依赖关系；
- 链路跟踪能力：整体上看相差不大，基本都参照[Google Dapper](http://research.google.com/pubs/pub36356.html)，也都支持对大量主流框架的跟踪，细节上有些差异：
  - 对单次RPC调用分析，ZipKin定义的Annotations更精细，参考[ZipKin部署和使用](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-ZipKin.md)；
  - PinPoint和SkyWalking都提供将额外方法添加到调用链跟踪的功能，其中PinPoint对代码完全没有侵入性，SkyWalking则需要对方法添加注解；
  - SkyWalking支持在Span中添加自定义tag功能，利用该功能可以将方法参数值等额外信息记录到Span中，有利于问题分析；
- UI功能：PinPoint和SkyWalking UI功能比较丰富，都提供应用/服务、实例等层级的性能统计，两者各有特色；ZipKin UI功能最弱，只提供依赖关系、具体调用链查看分析；<br />
  额外的UI功能，可以读取APM工具的数据，自定义开发；
- 社区支持：ZipKin架构灵活、文档完善，社区支持度最高，Spring Cloud和Service Mesh（[istio](https://github.com/istio/)）官方提供ZipKin支持；SkyWalking是华为员工开发，已成为Apache项目；PinPoint为韩国公司开源；

-------------------------------------------------------------------
#### 运行演示项目
[package.sh](https://github.com/liuzhibin-cn/my-demo/blob/master/package.sh)为项目编译打包脚本，参数说明：
- **简单运行**：不带任何参数执行`package.sh`，仅运行Dubbo微服务和演示应用，使用单个MySQL数据库、[nacos](https://nacos.io/)注册中心，运行4个Dubbo服务和1个Web应用；
- **分库分表**：`-mycat`、`-sharding-proxy`二选一。
  - `-mycat`：使用[Mycat](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Sharding-Mycat-Overview-Quickstart.md)分库分表；
  - `-sharding-proxy`：使用[Sharding-Proxy](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Sharding-Sharding-Proxy-Overview-Quickstart.md)分库分表；
- **分布式事务**：
  - `-seata`：使用[Seata](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/Seata-Distributed-Transaction-Management.md)分布式事务管理；
- **APM全链路跟踪**：`-zipkin`、`-pinpoint`、`-skywalking`三选一。
  - `-zipkin`：使用[ZipKin](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-ZipKin.md)进行链路跟踪、性能分析；
  - `-pinpoint`：使用[PinPoint](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-PinPoint.md)进行链路跟踪、性能分析；
  - `-skywalking`：使用[SkyWalking](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/APM-SkyWalking.md)进行链路跟踪、性能分析；

例如`./package.sh -mycat -seata -zipkin`

##### 本地运行
1. 要求JDK 8+。
2. 部署nacos，用于Dubbo注册中心。参考[Nacos快速开始](https://nacos.io/zh-cn/docs/quick-start.html)即可。
3. 部署MySQL数据库。<br />
   建库脚本[sql-schema.sql](https://github.com/liuzhibin-cn/my-demo/blob/master/docs/sql-schema.sql)，是演示分库分表用的建库脚本，简单方式运行只需要其中`mydemo-dn1`单库即可。
4. 确认项目配置。<br />
   项目配置都在[parent pom.xml](https://github.com/liuzhibin-cn/my-demo/blob/master/pom.xml)中，包括数据库连接信息、nacos地址等。
5. 编译打包。使用`package.sh`，Windows环境装了git bash就可以运行。
6. 运行演示项目。<br />
   按依赖关系依次启动Dubbo服务和Web应用:
   ```sh
   java -jar item-service\target\item-service-0.0.1-SNAPSHOT.jar
   java -jar stock-service\target\stock-service-0.0.1-SNAPSHOT.jar
   java -jar user-service\target\user-service-0.0.1-SNAPSHOT.jar
   java -jar order-service\target\order-service-0.0.1-SNAPSHOT.jar
   java -jar shop-web\target\shop-web-0.0.1-SNAPSHOT.jar
   ```
7. 运行演示应用，查看相关结果。
   - 演示应用：[localhost:8090/shop](http://localhost:8090/shop)
   - Nacos：[localhost:8848/nacos](http://localhost:8848/nacos)，登录用户/密码：nacos/nacos
   - ZipKin：[localhost:9411/zipkin](http://localhost:9411/zipkin/)
   - Mycat：数据端口`8066`、管理端口`9066`，都可以用MySQL客户端登录访问

##### Docker容器运行
使用Docker容器运行演示项目非常简单，基础组件无需自行部署、配置，直接运行容器即可。<br />
本项目支持Docker容器运行的组件：所有Dubbo服务和shop-web应用、MySQL、Nacos、Seata Server、Mycat Server、ZipKin Server、SkyWalking Server，其它组件没有制作Docker镜像。
1. 创建Docker NetWork：`docker network create mydemo`
2. 基础组件构建Docker镜像，运行Docker容器。<br />
   相关脚本和Dockerfile在[docker](docker/)目录中，每个基础组件一个子目录，其中`build.sh`构建Docker镜像，`run.sh`启动运行Docker容器，都不需要任何参数。<br />
   注意按基础组件的依赖关系依次启动Docker容器：`mysql -> mycat/nacos/zipkin/skywalking -> seata`。
3. 演示用Dubbo服务和Web应用构建Docker镜像、运行Docker容器。<br />
   1. 先参考`package.sh`，编译打包；
   2. 使用[docker/mydemo.sh](docker/mydemo.sh)管理Docker镜像和容器，其操作对象为所有Dubbo服务和shop-web应用，参数说明：
      - `-build`：构建Docker镜像；
      - `-run`：运行Docker容器；
      - `-stop`：停止Docker容器；
      - `-rm`：删除Docker容器（需要先停止Docker容器）；
      - `-rmi`：删除Docker镜像（需要先停止Docker容器）；
4. 运行演示应用，查看相关结果。
   - 演示应用：[localhost:18090/shop](http://localhost:18090/shop)
   - Nacos：[localhost:18848/nacos](http://localhost:18848/nacos)，登录用户/密码：nacos/nacos
   - ZipKin：[localhost:19411/zipkin](http://localhost:19411/zipkin/)
   - Mycat：数据端口`18066`、管理端口`19066`，都可以用MySQL客户端登录访问

例如：
```sh
./package.sh -mycat -seata -zipkin # 编译打包
./docker/mydemo.sh -build -run     # 构建Docker镜像、运行Docker容器
./package.sh -mycat -seata -zipkin # 编译打包：不使用Seata
./docker/mydemo.sh -stop -rm -rmi -build -run # 重新构建Docker镜像、运行Docker容器
```

Docker容器：<br />
![](docs/images/docker-containers.png)

容器资源使用情况：<br />
![](docs/images/docker-stats.png)

##### 运行效果
shop-web日志输出：<br />
![](docs/images/shopweb-out.png)

order-service日志输出：<br />
![](docs/images/order-service-out.png)

ZipKin：<br />
![](https://richie-leo.github.io/ydres/img/10/120/1013/screen-trace-detail-sql.png)

PinPoint：<br />
![](https://richie-leo.github.io/ydres/img/10/120/1012/pinpoint-screen-trace-mixed-view.png)