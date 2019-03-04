package com.lianshang.job.center.web.listener;

import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto.JobType;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import com.lianshang.job.center.web.util.JobUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
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
public class InitJobTaskListener implements ApplicationListener, ApplicationContextAware {

	private static final String ROOT_TAG = "application-1";
	public static final String JOB_CORE_CONFIGURATION = "job_core_configuration";
	public static final String NAMESPACE_CONFIGURATION = "namespace_configuration";

	private static DataSource dataSource;

	@Autowired
	private JobCoreConfigurationService jobCoreConfigurationService;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextRefreshedEvent) {

			ContextRefreshedEvent contextRefreshedEvent = (ContextRefreshedEvent) event;
			ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();

			if(ROOT_TAG.equals(applicationContext.getId())) {
				log.info("应用初始化完成-------");
				jobCoreConfigurationService.getAllList().forEach(this::initJob);
			}
		}
	}

	/**
	 * 实例化job
	 */
	private void initJob(JobCoreConfigurationDto jobCoreConfigurationDto) {

		if(JobType.SIMPLE_JOB.code().equals(jobCoreConfigurationDto.getJobType())) {
			JobUtil.initSimpleJob(jobCoreConfigurationDto, jobCoreConfigurationDto.getNamespaceId());
		} else if(JobType.DATA_FLOW_JOB.code().equals(jobCoreConfigurationDto.getJobType())) {
			JobUtil.initDataFlowJob(jobCoreConfigurationDto, jobCoreConfigurationDto.getNamespaceId());
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, DataSource> dataSourceMap = applicationContext.getBeansOfType(DataSource.class);
		if(null == dataSourceMap || dataSourceMap.isEmpty()) {
			throw new RuntimeException("未找到合适的数据源");
		}
		dataSource = dataSourceMap.values().iterator().next();
	}
}
