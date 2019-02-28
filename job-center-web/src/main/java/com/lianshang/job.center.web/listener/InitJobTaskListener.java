package com.lianshang.job.center.web.listener;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.lianshang.job.center.web.job.MySimpleJob;
import com.lianshang.job.center.web.service.ZookeeperConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 描述: 初始化job
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-27 下午4:55
 */
@Component
@Slf4j
public class InitJobTaskListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ZookeeperConfigurationService zookeeperConfigurationService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null) {

			log.info("容器初始化完成--------");

			int num =2;
			for(int i=0;i<num;i++){
				final String x = i+"";
				new Thread(new Runnable() {
					@Override
					public void run() {
						initJob(x);
					}
				}).start();
			}

		}
	}

	/**
	 * 初始化job
	 */
	private void initJob(String jobName){
		// 定义作业核心配置
		JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, "0/30 * * * * ?",
			1).build();
		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig,
			MySimpleJob.class.getCanonicalName());
		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
		new JobScheduler(zookeeperConfigurationService.getDefault().getCoordinatorRegistryCenter(),
			simpleJobRootConfig).init();
	}
}
