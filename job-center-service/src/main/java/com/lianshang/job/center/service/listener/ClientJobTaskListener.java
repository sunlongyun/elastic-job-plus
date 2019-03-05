package com.lianshang.job.center.service.listener;

import com.lianshang.job.center.service.annotation.EnableEventLog;
import com.lianshang.job.center.service.jobTaskInterface.DataFlowJob;
import com.lianshang.job.center.service.jobTaskInterface.SimpleJob;
import com.lianshang.job.center.service.response.LsCloudResponse;
import com.lianshang.job.center.service.response.ResponseCodeEnum;
import com.lianshang.job.center.service.utils.JsonUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * 描述: 开启
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 下午4:08
 */
@EnableEurekaClient
@EnableDiscoveryClient
@Configuration
@Slf4j
public class ClientJobTaskListener implements ApplicationListener {

	public static final String ROOT_TAG = "application-1";
	@Autowired
	private Environment environment;

	public static final String CLOUD_CLIENT_TEMPLATE = "serverRestTemplate";
	//job 服务 名称
	private static final String JOB_SERVER_NAME = "JOB-CENTER";
	private volatile String namespace;

	private volatile RestTemplate restTemplate = null;

	private static final String JOB_SERVER_URL = "http://" + JOB_SERVER_NAME + "/job/jobNotify";
	/**
	 * 简单任务列表
	 */
	public static Map<String, SimpleJob> simpleJobMap = new HashMap<>();
	/**
	 * 流式任务列表
	 */
	public static Map<String, DataFlowJob> dataFlowJobMap = new HashMap<>();
	/**
	 * 创建 RestTemplate
	 */
	@Bean(CLOUD_CLIENT_TEMPLATE)
	@LoadBalanced
	@ConditionalOnMissingBean(name = CLOUD_CLIENT_TEMPLATE)
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {

		if(event instanceof ContextRefreshedEvent) {

			ContextRefreshedEvent contextRefreshedEvent = (ContextRefreshedEvent) event;
			ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
			log.info("applicationContext=>{}", applicationContext);

			if(ROOT_TAG.equals(applicationContext.getId())) {
				log.info("容器初始化完成--------");
				//获取命名空间
				namespace = getNamespace();

				//获取 restTemplate
				restTemplate = (RestTemplate) applicationContext.getBean(CLOUD_CLIENT_TEMPLATE);

				//获取实现 SimpleJob 的所有bean
				simpleJobMap = applicationContext.getBeansOfType(SimpleJob.class);

				//获取所有实现 DataFlowJob 接口的bean
				dataFlowJobMap = applicationContext.getBeansOfType(DataFlowJob.class);

				//向 job 服务发送消息
				simpleJobMap.forEach(this::sendSimpleJobMsg);
				dataFlowJobMap.forEach(this::sendDataFlowJobMsg);

			}
		}
	}

	/**
	 * 简单类型任务发送消息
	 */
	private void sendSimpleJobMsg(String jobName, SimpleJob simpleJob) {

		Map<String, Object> postParameters = new HashMap<>();
		postParameters.put("namespace", namespace);
		postParameters.put("jobName", jobName);
		postParameters.put("jobType", JobType.SIMPLE_JOB.code);

		EnableEventLog enableEventLog = simpleJob.getClass().getAnnotation(EnableEventLog.class);
		if(null != enableEventLog) {
			postParameters.put("eventLog", true);
		} else {
			postParameters.put("eventLog", false);
		}

		if(log.isDebugEnabled()) {
			log.debug("postParameters==>{}", postParameters);
		}

		sendPostMsg(postParameters);
	}

	/**
	 * 流式任务发送消息
	 */
	private void sendDataFlowJobMsg(String jobName, DataFlowJob dataFlowJob) {

		Map<String, Object> postParameters = new HashMap<>();
		postParameters.put("namespace", namespace);
		postParameters.put("jobName", jobName);
		postParameters.put("jobType", JobType.DATA_FLOW_JOB.code);

		EnableEventLog enableEventLog = dataFlowJob.getClass().getAnnotation(EnableEventLog.class);
		if(null != enableEventLog) {
			postParameters.put("eventLog", true);
		} else {
			postParameters.put("eventLog", false);
		}


		if(log.isDebugEnabled()) {
			log.debug("postParameters==>{}", postParameters);
		}
		sendPostMsg(postParameters);
	}


	/**
	 * 发送消息
	 */
	private void sendPostMsg(Map<String, Object> postParameters) {

		HttpEntity<String> formEntity = getStringHttpEntity(postParameters);

		try {

			LsCloudResponse lsCloudResponse = restTemplate
				.postForEntity(JOB_SERVER_URL, formEntity, LsCloudResponse.class).getBody();

			if(!ResponseCodeEnum.SUCCESS.code().equals(lsCloudResponse.getCode())) {
				throw new RuntimeException(lsCloudResponse.getMsg());
			}

		} catch(Exception ex) {
			log.error("远程服务调用失败:", ex);
		}
	}

	/**
	 * 组装请求实体
	 * @param postParameters
	 * @return
	 */
	private HttpEntity<String> getStringHttpEntity(Map<String, Object> postParameters) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(mediaType);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		String paramsJson = JsonUtils.object2JsonString(postParameters);

		return new HttpEntity<String>(paramsJson, headers);
	}

	/**
	 * 获取命名空间
	 */
	private String getNamespace() {
		String applicationName = environment.getProperty("spring.application.name");
		if(StringUtils.isEmpty(applicationName)) {
			throw new RuntimeException("无法获取应用名称");
		}
		return applicationName;
	}

	/**
	 * 枚举类型
	 */
	private enum JobType {

		SIMPLE_JOB("simple_job", "简单作业类型"),
		DATA_FLOW_JOB("data_flow_job", "流式作业类型");

		private String code;
		private String msg;

		JobType(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}

	}
}
