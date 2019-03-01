package com.lianshang.job.center.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.lianshang.job.center.service.beans.ShardInfo;
import com.lianshang.job.center.service.response.LsCloudResponse;
import com.lianshang.job.center.service.response.ResponseCodeEnum;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午10:57
 */
@Slf4j
public class MyDataflowJob implements DataflowJob {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List fetchData(ShardingContext shardingContext) {

		//这个jobName 是应用名称+beanName
		String contextJobName = shardingContext.getJobName();
		String[] nameList = contextJobName.split("\\/");

		String applicationName = nameList[0];
		String jobName = nameList[1];

		ShardInfo shardInfo = getShardInfo(shardingContext, contextJobName, jobName);

		String url = "http://" + applicationName.toUpperCase() + "/task/dataFlow-fetchData";

		ResponseEntity<LsCloudResponse> responseResponseEntity = restTemplate
			.postForEntity(url, null, LsCloudResponse.class, shardInfo);

		log.info("jobName:{} 响应结果:{}", jobName, responseResponseEntity);
		if(null != responseResponseEntity) {
			Optional<LsCloudResponse> lsCloudResponseOptional = Optional.ofNullable(responseResponseEntity.getBody());
			if(lsCloudResponseOptional.isPresent()) {
				LsCloudResponse lsCloudResponse = lsCloudResponseOptional.get();
				if(ResponseCodeEnum.SUCCESS.code().equals(lsCloudResponse.getCode())) {
					return (List) lsCloudResponse.getData();
				}
			}

		}
		return null;
	}

	/**
	 * 获取分片信息详情
	 */
	private ShardInfo getShardInfo(ShardingContext shardingContext, String contextJobName, String jobName) {
		String parameter = shardingContext.getShardingParameter();
		String jobParameter = shardingContext.getJobParameter();
		int totalCount = shardingContext.getShardingTotalCount();
		int item = shardingContext.getShardingItem();
		log.info("发送请求---------{},thread:{}", new Date(), Thread.currentThread());
		log.info("jobName=>{}", contextJobName);
		log.info("shardingParameter=>{}", parameter);
		log.info("jobParameter=>{}", jobParameter);
		log.info("totalCount=>{}", totalCount);
		log.info("item=>{}", item);

		ShardInfo shardInfo = new ShardInfo();
		shardInfo.setJobName(jobName);
		shardInfo.setJobParameter(jobParameter);
		shardInfo.setShardingItem(item);
		shardInfo.setShardingParameter(parameter);
		shardInfo.setShardingTotalCount(totalCount);
		return shardInfo;
	}

	@Override
	public void processData(ShardingContext shardingContext, List data) {
		//这个jobName 是应用名称+beanName
		String contextJobName = shardingContext.getJobName();
		String[] nameList = contextJobName.split("\\/");

		String applicationName = nameList[0];
		String jobName = nameList[1];

		ShardInfo shardInfo = getShardInfo(shardingContext, contextJobName, jobName);

		String url = "http://" + applicationName.toUpperCase() + "/task/dataFlow-processData";

		ResponseEntity<LsCloudResponse> responseResponseEntity = restTemplate
			.postForEntity(url, null, LsCloudResponse.class, shardInfo,data);

		log.info("jobName:{} 响应结果:{}", jobName, responseResponseEntity);
	}
}
