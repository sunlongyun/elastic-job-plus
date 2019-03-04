package com.lianshang.job.center.web.util;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.job.MyDataFlowJob;
import com.lianshang.job.center.web.job.MySimpleJob;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import com.lianshang.job.center.web.service.NameSpaceConfigurationService;
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

	private static NameSpaceConfigurationService nameSpaceConfigurationService;
	private static JobCoreConfigurationService jobCoreConfigurationService = null;
	/**
	 * 初始化SimpleJob
	 */
	public static void initSimpleJob(JobCoreConfigurationDto jobCoreConfigurationDto, Integer nameSpaceId) {

		String jobName = jobCoreConfigurationDto.getApplicationName() + "___" + jobCoreConfigurationDto.getJobName();

		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(
			getJobCoreConfiguration(jobCoreConfigurationDto,
				jobName),
			MySimpleJob.class.getCanonicalName());
		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();

		CoordinatorRegistryCenter coordinatorRegistryCenter = null;

		if(null != nameSpaceId) {
			coordinatorRegistryCenter = nameSpaceConfigurationService.getById(nameSpaceId)
				.getCoordinatorRegistryCenter();
		}

		JobScheduler jobScheduler = new JobScheduler(coordinatorRegistryCenter, simpleJobRootConfig);
		jobScheduler.init();
		;
		jobScheduler.getSchedulerFacade();
	}


	/**
	 *初始化DataFlowJob
	 */
	public static void initDataFlowJob(JobCoreConfigurationDto jobCoreConfigurationDto, Integer nameSpaceId) {

		String jobName = jobCoreConfigurationDto.getApplicationName() + "___" + jobCoreConfigurationDto.getJobName();

		// 定义SIMPLE类型配置
		DataflowJobConfiguration dataflowJobConfiguration = new DataflowJobConfiguration(
			getJobCoreConfiguration(jobCoreConfigurationDto, jobName),
			MyDataFlowJob.class.getCanonicalName(), jobCoreConfigurationDto.getStreamingProcess());

		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(dataflowJobConfiguration).build();

		CoordinatorRegistryCenter coordinatorRegistryCenter = null;

		if(null != nameSpaceId) {
			coordinatorRegistryCenter = nameSpaceConfigurationService.getById(nameSpaceId)
				.getCoordinatorRegistryCenter();
		}

		new JobScheduler(coordinatorRegistryCenter, simpleJobRootConfig).init();
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
		nameSpaceConfigurationService = applicationContext.getBean(NameSpaceConfigurationService.class);
		jobCoreConfigurationService = applicationContext.getBean(JobCoreConfigurationService.class);
	}

	/**
	 * 刷新数据库配置
	 */
	public static void freshJobItem(JobCoreConfiguration jobCoreConfiguration) {

		//TODO 添加分布式锁
		String[] jobNameList = jobCoreConfiguration.getJobName().split("___");
		String applicationName = jobNameList[0];
		String jobName = jobNameList[1];

		JobCoreConfigurationDto jobCoreConfigurationDto = jobCoreConfigurationService.getByName(applicationName,
			jobName);

		//更新
		if(null != jobCoreConfiguration){
			jobCoreConfigurationDto.setDescription(jobCoreConfiguration.getDescription());
			jobCoreConfigurationDto.setCron(jobCoreConfiguration.getCron());
			jobCoreConfigurationDto.setJobParameter(jobCoreConfiguration.getJobParameter());
			jobCoreConfigurationDto.setFailover(jobCoreConfiguration.isFailover());
			jobCoreConfigurationDto.setMisfire(jobCoreConfiguration.isMisfire());
			jobCoreConfigurationDto.setShardingItemParameters(jobCoreConfiguration.getShardingItemParameters());
			jobCoreConfigurationDto.setShardingTotalCount(jobCoreConfiguration.getShardingTotalCount());
			jobCoreConfigurationService.edit(jobCoreConfigurationDto);
		}
	}

}
