package com.lianshang.job.center.web.util;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.lite.internal.schedule.JobScheduleController;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.job.MyDataFlowJob;
import com.lianshang.job.center.web.job.MySimpleJob;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import com.lianshang.job.center.web.service.NameSpaceConfigurationService;
import javax.sql.DataSource;
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

	public static final String SPLIT_ = "___";

	private static NameSpaceConfigurationService nameSpaceConfigurationService;
	private static JobCoreConfigurationService jobCoreConfigurationService = null;
	private static DataSource dataSource = null;
	/**
	 * 初始化SimpleJob
	 */
	public static void initSimpleJob(JobCoreConfigurationDto jobCoreConfigurationDto, Integer nameSpaceId) {

		String jobName = jobCoreConfigurationDto.getApplicationName() + "___" + jobCoreConfigurationDto.getJobName();

		// 定义SIMPLE类型配置
		SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(
			getJobCoreConfiguration(jobCoreConfigurationDto, jobName),
			MySimpleJob.class.getCanonicalName());

		// 定义Lite作业根配置
		LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();

		CoordinatorRegistryCenter coordinatorRegistryCenter = null;

		if(null != nameSpaceId) {
			coordinatorRegistryCenter = nameSpaceConfigurationService.getById(nameSpaceId)
				.getCoordinatorRegistryCenter();
		}

		//构建任务调度job
		buildJobScheduler(jobCoreConfigurationDto, simpleJobRootConfig, coordinatorRegistryCenter);
	}

	/**
	 * 构建任务调度job
	 */
	private static void buildJobScheduler(JobCoreConfigurationDto jobCoreConfigurationDto,
		LiteJobConfiguration simpleJobRootConfig, CoordinatorRegistryCenter coordinatorRegistryCenter) {

		try {
			JobScheduler jobScheduler = null;
			// 定义日志数据库事件溯源配置
			JobEventConfiguration jobEventRdbConfig = null;
			if(Boolean.TRUE.equals(jobCoreConfigurationDto.getEventLog())) {
				jobEventRdbConfig = new JobEventRdbConfiguration(dataSource);
				jobScheduler = new JobScheduler(coordinatorRegistryCenter, simpleJobRootConfig, jobEventRdbConfig);
			} else {
				jobScheduler = new JobScheduler(coordinatorRegistryCenter, simpleJobRootConfig);
			}

			jobScheduler.init();

			JobRegistry jobRegistry = JobRegistry.getInstance();
			if(null != jobRegistry) {
				JobScheduleController jobScheduleController = jobRegistry.getJobScheduleController
					(simpleJobRootConfig.getJobName());

				if(null != jobScheduleController){
					jobScheduleController.rescheduleJob
						(jobCoreConfigurationDto.getCron());
				}
			}


		} catch(Exception ex) {
			log.info("error:{}", ex);
		}

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
		LiteJobConfiguration dataFlowJobRootConfig = LiteJobConfiguration.newBuilder(dataflowJobConfiguration).build();

		CoordinatorRegistryCenter coordinatorRegistryCenter = null;

		if(null != nameSpaceId) {
			coordinatorRegistryCenter = nameSpaceConfigurationService.getById(nameSpaceId)
				.getCoordinatorRegistryCenter();
		}

		//构建任务调度job
		buildJobScheduler(jobCoreConfigurationDto, dataFlowJobRootConfig, coordinatorRegistryCenter);
	}

	/**
	 * job核心配置
	 */
	private static JobCoreConfiguration getJobCoreConfiguration(JobCoreConfigurationDto jobCoreConfigurationDto,
		String jobName) {
		log.info("description==>{}", jobCoreConfigurationDto.getDescription());
		// 定义作业核心配置
		JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration
			.newBuilder(jobName, jobCoreConfigurationDto.getCron(),
				jobCoreConfigurationDto.getShardingTotalCount()).description(jobCoreConfigurationDto.getDescription())
			.failover(jobCoreConfigurationDto.getFailover()).jobParameter(jobCoreConfigurationDto.getJobParameter())
			.misfire(jobCoreConfigurationDto.getMisfire())
			.shardingItemParameters(jobCoreConfigurationDto.getShardingItemParameters()).build();
		return jobCoreConfiguration;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		nameSpaceConfigurationService = applicationContext.getBean(NameSpaceConfigurationService.class);
		jobCoreConfigurationService = applicationContext.getBean(JobCoreConfigurationService.class);
		dataSource = applicationContext.getBean(DataSource.class);
	}

	/**
	 * 刷新数据库配置
	 */
	public static void freshJobItem(JobCoreConfiguration jobCoreConfiguration) {

		String[] jobNameList = jobCoreConfiguration.getJobName().split(SPLIT_);
		String applicationName = jobNameList[0];
		String jobName = jobNameList[1];

		JobCoreConfigurationDto jobCoreConfigurationDto = jobCoreConfigurationService.getByName(applicationName,
			jobName);

		//更新
		editJob(jobCoreConfiguration, jobCoreConfigurationDto);
	}

	/**
	 * 根据命名空间和job名称删除job(逻辑删除)
	 */
	public static void disableJob(String fullJobName) {

		String[] jobNameList = fullJobName.split(SPLIT_);
		String applicationName = jobNameList[0];
		String jobName = jobNameList[1];

		JobCoreConfigurationDto jobCoreConfigurationDto = jobCoreConfigurationService.getByName(applicationName,
			jobName);

		JobCoreConfigurationDto targetCoreConfigurationDto = new JobCoreConfigurationDto();
		targetCoreConfigurationDto.setId(jobCoreConfigurationDto.getId());
		targetCoreConfigurationDto.setValidity(false);

		jobCoreConfigurationService.edit(targetCoreConfigurationDto);
	}

	/**
	 * 根据job记录
	 * @param jobCoreConfiguration
	 * @param jobCoreConfigurationDto
	 */
	private static void editJob(JobCoreConfiguration jobCoreConfiguration,
		JobCoreConfigurationDto jobCoreConfigurationDto) {

		if(null != jobCoreConfiguration) {

			jobCoreConfigurationDto.setDescription(jobCoreConfiguration.getDescription());
			jobCoreConfigurationDto.setCron(jobCoreConfiguration.getCron());
			jobCoreConfigurationDto.setJobParameter(jobCoreConfiguration.getJobParameter());
			jobCoreConfigurationDto.setFailover(jobCoreConfiguration.isFailover());
			jobCoreConfigurationDto.setMisfire(jobCoreConfiguration.isMisfire());
			jobCoreConfigurationDto.setShardingItemParameters(jobCoreConfiguration.getShardingItemParameters());
			jobCoreConfigurationDto.setShardingTotalCount(jobCoreConfiguration.getShardingTotalCount());
			jobCoreConfigurationDto.setValidity(true);
			jobCoreConfigurationService.edit(jobCoreConfigurationDto);

		}
	}

}
