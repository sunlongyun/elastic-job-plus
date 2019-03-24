# elastic-job-plus
基于elasticjob和springcloud开发的一块分布式定时任务，实现定时任务和业务的解耦


该项目是一款基于当当的elastic-job进行升级的定时任务调度产品.项目创建的目的旨在实现任务调度工作和业务逻辑解耦,提供程序员工作效率.



一.定时任务调度器和原生的elastic-job比较,带来了那些便利?

1.业务逻辑和定时任务解耦, 业务方项目无需关心定时任务的配置以及job对象的创建过程,只需关注自己的业务逻辑

2.业务方项目写了处理业务的bean之后,会自动上报,自动注册任务到注册中心

3.通过elastic-job 管理后台对任务进行的任何修改操作,会实时刷新到数据库,保证配置信息的安全.
  定时任务调度器重启之后,用数据库的job配置信息初始化(或者刷新)任务的调度配置,不会出现恢复到项目原始配置的情况.



二.使用说明

1.引入pom(暂时需要先下载项目后install)
  <dependency>
    <groupId>com.lianshang</groupId>
    <artifactId>job-center-service</artifactId>
    <version>0.0.1</version>
  </dependency>


2.继承接口实现业务逻辑

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

3.该项目是elastic-job的升级版，旨在进一步提高工作效率，保留了全部elastic-job的原生功能。管理后台仍然使用部elastic-job提供的管理后台。
elastic-job官网地址 http://elasticjob.io/



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


三.注意事项

1.修改任务的配置信心,尽量通过当当的elastic-job管理后台.
  如果直接修改数据库,可能会导致数据库里的job配置信息和注册中心不一致.
  当数据库的job配置信息和注册中心不一致时,下次重启服务器时会以数据库配置信息为准.

2.修改任务配置信息后,无需重启任何服务器,任务自动按照新的配置信息执行.

3.该项目是在当当elastic-job分布式定时任务基础上的升级版，只是简化了使用的复杂度，提高了程序员的开发效率。管理后台仍然使用elastic-job的管理后台来管理定时任务。 elastic-jo官网地址：http://elasticjob.io/



