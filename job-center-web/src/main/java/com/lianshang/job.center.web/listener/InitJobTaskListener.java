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
			//生成表结构
			createTableIfNotExists();

			if(ROOT_TAG.equals(applicationContext.getId())) {
				log.info("应用初始化完成-------");
				jobCoreConfigurationService.getAllList().forEach(this::initJob);
			}

		}
	}

	/**
	 * 如果当前数据源,必须的表结构还不存在,则创建
	 */
	private void createTableIfNotExists() {
		//查询数据库表是否存在,不存在则创建
		Connection connection = null;
		Statement statement = null;
		try {
			connection = dataSource.getConnection();
			statement = connection.createStatement();

			//如果表 namespace_configuration,则创建一个
			createIfNotExistForNameSpaceConfiguration(statement);
			//如果表 job_core_configuration不能存在,则创建一个
			createIfNotExistForJobCoreConfiguration(statement);

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if(null != statement) {
					statement.close();
				}
				if(null != connection) {
					connection.close();
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 如果表 NAMESPACE_CONFIGURATION,则创建一个
	 */
	private void createIfNotExistForNameSpaceConfiguration(Statement statement) throws SQLException, IOException {
		String exists_namespaceConfiguration_sql = "SELECT table_name FROM information_schema.TABLES "
			+ "WHERE TABLE_NAME=\"" + NAMESPACE_CONFIGURATION + "\";";

		ResultSet namespaceConfigurationSet = statement.executeQuery(exists_namespaceConfiguration_sql);
		createTable(statement, namespaceConfigurationSet);
	}

	/**
	 * 如果表 job_core_configuration不能存在,则创建一个
	 */
	private void createIfNotExistForJobCoreConfiguration(Statement statement) throws SQLException, IOException {
		String exists_namespaceConfiguration_sql = "SELECT table_name FROM information_schema.TABLES "
			+ "WHERE TABLE_NAME=\"" + JOB_CORE_CONFIGURATION + "\";";

		ResultSet namespaceConfigurationSet = statement.executeQuery(exists_namespaceConfiguration_sql);
		createTable(statement, namespaceConfigurationSet);
	}

	private void createTable(Statement statement, ResultSet namespaceConfigurationSet)
		throws SQLException, IOException {
		if(!namespaceConfigurationSet.next()) {//不存在,则创建表结构
			File namespaceConfiguration_sql = new File("classpath:/sql_file/namespace_configuration.sql");
			List<String> readLines = FileUtils.readLines(namespaceConfiguration_sql,
				Charset.defaultCharset());

			String create_namespaceConfiguration_sql = StringUtils.join(readLines.toArray(), " ");
			statement.execute(create_namespaceConfiguration_sql);
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
