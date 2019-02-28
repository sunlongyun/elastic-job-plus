package com.lianshang.job.center.web.util;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.job.MyDataflowJob;
import com.lianshang.job.center.web.job.MySimpleJob;
import com.lianshang.job.center.web.service.ZookeeperConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 描述: job管理工具类
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 下午12:46
 */
@Component
@Slf4j
public class JobUtil implements ApplicationContextAware {

	private static ZookeeperConfigurationService zookeeperConfigurationService;

	/**
	 * 初始化SimpleJob
	 */
	public static void initSimpleJob(JobCoreConfigurationDto jobCoreConfigurationDto) {
		String jobName = jobCoreConfigurationDto.getApplicationName() + "_" + jobCoreConfigurationDto.getJobName();

		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(
			getJobCoreConfiguration(jobCoreConfigurationDto,
				jobName),
			MySimpleJob.class.getCanonicalName());
		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
		new JobScheduler(zookeeperConfigurationService.getDefault().getCoordinatorRegistryCenter(),
			simpleJobRootConfig).init();
	}


	/**
	 * 初始化DataflowJob
	 */
	public static void initDataflowJob(JobCoreConfigurationDto jobCoreConfigurationDto) {

		String jobName = jobCoreConfigurationDto.getApplicationName() + ":" + jobCoreConfigurationDto.getJobName();

		// 定义SIMPLE类型配置
		DataflowJobConfiguration dataflowJobConfiguration = new DataflowJobConfiguration(
			getJobCoreConfiguration(jobCoreConfigurationDto, jobName),
			MyDataflowJob.class.getCanonicalName(), false);

		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(dataflowJobConfiguration).build();
		new JobScheduler(zookeeperConfigurationService.getDefault().getCoordinatorRegistryCenter(), simpleJobRootConfig)
			.init();
	}

	/**
	 * job核心配置
	 */
	private static JobCoreConfiguration getJobCoreConfiguration(JobCoreConfigurationDto jobCoreConfigurationDto,
		String jobName) {
		log.info("description==>{}", jobCoreConfigurationDto.getDescription());
		// 定义作业核心配置
		return JobCoreConfiguration
			.newBuilder(jobName, jobCoreConfigurationDto.getCron(),
				jobCoreConfigurationDto.getShardingTotalCount()).description(jobCoreConfigurationDto.getDescription())
			.failover(jobCoreConfigurationDto.getFailover()).jobParameter(jobCoreConfigurationDto.getJobParameter())
			.misfire(jobCoreConfigurationDto.getMisfire())
			.shardingItemParameters(jobCoreConfigurationDto.getShardingItemParameters()).build();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		zookeeperConfigurationService = applicationContext.getBean(ZookeeperConfigurationService.class);
	}
}
