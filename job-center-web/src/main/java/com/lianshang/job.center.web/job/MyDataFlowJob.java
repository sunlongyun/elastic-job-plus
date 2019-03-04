package com.lianshang.job.center.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.lianshang.job.center.service.beans.ProcessDataInfo;
import com.lianshang.job.center.service.beans.ShardInfo;
import com.lianshang.job.center.service.response.LsCloudResponse;
import com.lianshang.job.center.service.response.ResponseCodeEnum;
import com.lianshang.job.center.service.utils.JsonUtils;
import com.lianshang.job.center.web.config.RestTemplateConfigUtil;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
 * @date 2019-02-28 上午10:57
 */
@Slf4j
public class MyDataFlowJob implements DataflowJob {

	private RestTemplate restTemplate;

	@Override
	public List fetchData(ShardingContext shardingContext) {

		//这个jobName 是应用名称+beanName
		String contextJobName = shardingContext.getJobName();
		String[] nameList = contextJobName.split("___");

		String applicationName = nameList[0];
		String jobName = nameList[1];

		ShardInfo shardInfo = getShardInfo(shardingContext, contextJobName, jobName);

		String url = "http://" + applicationName.toUpperCase() + "/task/dataFlow-fetchData";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("jobName", jobName);
		map.add("shardingTotalCount", shardInfo.getShardingTotalCount() + "");
		map.add("shardingParameter", shardInfo.getShardingParameter());
		map.add("shardingItem", shardInfo.getShardingItem() + "");
		map.add("jobParameter", shardInfo.getJobParameter());

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
		String[] nameList = contextJobName.split("___");

		String applicationName = nameList[0];
		String jobName = nameList[1];

		ShardInfo shardInfo = getShardInfo(shardingContext, contextJobName, jobName);

		String url = "http://" + applicationName.toUpperCase() + "/task/dataFlow-processData";

		if(restTemplate == null) {
			synchronized(MySimpleJob.class) {
				if(restTemplate == null) {
					restTemplate = RestTemplateConfigUtil.get();
				}
			}
		}

		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
		headers.setContentType(mediaType);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());

		ProcessDataInfo processDataInfo = new ProcessDataInfo();
		processDataInfo.setData(data);
		processDataInfo.setShardInfo(shardInfo);
		String paramJson = JsonUtils.object2JsonString(processDataInfo);
		log.info("paramJson==>{}", paramJson);
		HttpEntity<String> request = new HttpEntity<String>(paramJson, headers);


		ResponseEntity<LsCloudResponse> responseResponseEntity = restTemplate
			.postForEntity(url, request, LsCloudResponse.class);

		log.info("jobName:{} 响应结果:{}", jobName, responseResponseEntity);

		LsCloudResponse lsCloudResponse= responseResponseEntity.getBody();
		if(null == lsCloudResponse){
			throw  new RuntimeException("服务请求异常");
		}else{
			if(!ResponseCodeEnum.SUCCESS.code().equals(lsCloudResponse.getCode())){
				throw new RuntimeException(lsCloudResponse.getMsg());
			}
		}
	}
}
