package com.lianshang.job.center.web.listener;

import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto.JobType;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import com.lianshang.job.center.web.service.ZookeeperConfigurationService;
import com.lianshang.job.center.web.util.JobUtil;
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
	@Autowired
	private JobCoreConfigurationService jobCoreConfigurationService;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null) {
			log.info("容器初始化完成--------");

			jobCoreConfigurationService.getAllList().forEach(this::initJob);

		}
	}

	/**
	 * 实例化job
	 */
	private void initJob(JobCoreConfigurationDto jobCoreConfigurationDto) {

		if(JobType.SIMPLE_JOB.code().equals(jobCoreConfigurationDto.getJobType())) {
			JobUtil.initSimpleJob(jobCoreConfigurationDto);
		} else if(JobType.DATA_FLOW_JOB.code().equals(jobCoreConfigurationDto.getJobType())) {
			JobUtil.initDataflowJob(jobCoreConfigurationDto);
		}

	}

}
