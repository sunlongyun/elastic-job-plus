package com.lianshang.job.center.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lianshang.job.center.service.response.LsCloudResponse;
import com.lianshang.job.center.web.config.RestTemplateConfigUtil;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午10:45
 */
@Slf4j
public class MySimpleJob implements SimpleJob{

	private volatile RestTemplate restTemplate;

	@Override
	public void execute(ShardingContext shardingContext) {
		//这个jobName 是应用名称+beanName
		String contextJobName = shardingContext.getJobName();
		String[] nameList = contextJobName.split("___");

		String applicationName = nameList[0];
		String jobName = nameList[1];

		String parameter = shardingContext.getShardingParameter();
		String jobParameter = shardingContext.getJobParameter();
		int totalCount = shardingContext.getShardingTotalCount();
		int item = shardingContext.getShardingItem();
		log.info("发送请求---------{},thread:{}",new Date(),Thread.currentThread());
		log.info("jobName=>{}", contextJobName);
		log.info("shardingParameter=>{}", parameter);
		log.info("jobParameter=>{}", jobParameter);
		log.info("totalCount=>{}", totalCount);
		log.info("item=>{}", item);


		String url = "http://" + applicationName.toUpperCase() + "/task/simpleJob";
		log.info("url=>{}", url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("jobName", jobName);
		map.add("shardingTotalCount", totalCount+"");
		map.add("shardingParameter", parameter);
		map.add("shardingItem", item+"");
		map.add("taskId",(item-1)+"");
		map.add("jobParameter", jobParameter);

		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);


		if(restTemplate == null) {
			synchronized(MySimpleJob.class) {
				if(restTemplate == null) {
					restTemplate = RestTemplateConfigUtil.get();
				}
			}
		}

		ResponseEntity<LsCloudResponse> responseResponseEntity = restTemplate
			.postForEntity(url, request, LsCloudResponse.class);

		log.info("jobName:{} 响应结果:{}", jobName, responseResponseEntity);
	}
}
