# elastic-job-plus
基于elasticjob和springcloud开发的一块分布式定时任务，实现定时任务和业务的解耦


该项目是一款基于当当的elastic-job进行升级的定时任务调度产品.项目创建的目的旨在实现任务调度工作和业务逻辑解耦,提供程序员工作效率.



一.定时任务调度器和原生的elastic-job比较,带来了那些便利?

1.业务逻辑和定时任务解耦, 业务方项目无需关心定时任务的配置以及job对象的创建过程,只需关注自己的业务逻辑

2.业务方项目写了处理业务的bean之后,会自动上报,自动注册任务到注册中心

3.通过elastic-job 管理后台对任务进行的任何修改操作,会实时刷新到数据库,保证配置信息的安全.
  定时任务调度器重启之后,用数据库的job配置信息初始化(或者刷新)任务的调度配置,不会出现恢复到项目原始配置的情况.

二.项目结构说明
项目分为两个子模块 job-center-web和job-center-service
 1.job-center-web打包成war单独部署，是任务调度服务中心，部署多个节点可以实现任务的高可用。 job-center-web是elastic-job执行任务的节点。
 2.job-center-service打包成jar，共基于springcloud开发的业务端使用。提供了两种接口SimpleJob和DataFlowJob，实现接口，完成业务逻辑。任务可自动上报给job-center-web。

三.使用说明

######################################服务端配置#################################

1.由于elastic-job是基于zookeeper开发的，在使用之前，首先要创建zookeeper集群，然后修改job-center-web 中的resource目录下的application.properties文件中zookeeper配置中心。

2.该项目是基于springcloud做的任务的上报和调度，所以使用之前必须先搭建eureka注册中心，然后修改job-center-web 中的resource目录下的application.properties文件中eureka地址。

3.install 项目  job-center-web 成war包，然后发布服务。

#########################客户端配置############################################

4.install 项目job-center-service成jar包
5.引入pom
  <dependency>
    <groupId>com.lianshang</groupId>
    <artifactId>job-center-service</artifactId>
    <version>0.0.1</version>
  </dependency>

6.继承接口实现业务逻辑

//简单任务类型
@Component
public class MyJob implements SimpleJob {
  .........
}


//流式任务类型
@Component
public class MyJob implements DataFlowJob<T> {
  .........
}

3.通过 当当的elastic-job管理后台修改任务执行时间,分片等内容

默认注册的任务执行时间是2050年(为了注册任务后不立即执行,并且任务是激活状态).
当当的elastic-job管理后台修改任务执行时间,分片等内容.修改的内容会被监听,实时刷新到任务调度器的数据库.

4.开启事件追踪功能

为了防止大量不必要的任务追踪信息入库,系统没有对job默认开启事件追踪.
如果业务需要追踪任务执行情况,请在处理任务的bean上添加注解@EnableEventLog

例如:
@Component
@EnableEventLog
@Slf4j
public class MyJob implements SimpleJob {
......
}


四.注意事项

1.修改任务的配置信心,尽量通过当当的elastic-job管理后台.
  如果直接修改数据库,可能会导致数据库里的job配置信息和注册中心不一致.
  当数据库的job配置信息和注册中心不一致时,下次重启服务器时会以数据库配置信息为准.

2.修改任务配置信息后,无需重启任何服务器,任务自动按照新的配置信息执行.

3.该项目是在当当elastic-job分布式定时任务基础上的升级版，只是简化了使用的复杂度，提高了程序员的开发效率。管理后台仍然使用elastic-job的管理后台来管理定时任务。 elastic-jo官网地址：http://elasticjob.io/



