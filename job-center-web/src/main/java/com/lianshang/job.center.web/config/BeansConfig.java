package com.lianshang.job.center.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-03-01 下午12:06
 */
@Configuration
public class BeansConfig {

	/**
	 * 创建 RestTemplate
	 * @return
	 */
	@ConditionalOnMissingBean
	@Bean("jobRestTemplate")
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}
