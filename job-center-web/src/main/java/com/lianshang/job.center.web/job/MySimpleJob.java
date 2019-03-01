package com.lianshang.job.center.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lianshang.job.center.service.beans.ShardInfo;
import com.lianshang.job.center.service.response.LsCloudResponse;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午10:45
 */
@Slf4j
public class MySimpleJob implements SimpleJob{

	@Autowired
	private RestTemplate restTemplate;
	@Override
	public void execute(ShardingContext shardingContext) {
		//这个jobName 是应用名称+beanName
		String contextJobName = shardingContext.getJobName();
		String[] nameList = contextJobName.split("\\/");

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

//		ShardInfo shardInfo = new ShardInfo();
//		shardInfo.setJobName(jobName);
//		shardInfo.setJobParameter(jobParameter);
//		shardInfo.setShardingItem(item);
//		shardInfo.setShardingParameter(parameter);
//		shardInfo.setShardingTotalCount(totalCount);
//
//		String url = "http://" + applicationName.toUpperCase() + "/task/simpleJob";
//
//		ResponseEntity<LsCloudResponse> responseResponseEntity = restTemplate
//			.postForEntity(url, shardInfo, LsCloudResponse.class);
//
//		log.info("jobName:{} 响应结果:{}", jobName, responseResponseEntity);
	}
}
