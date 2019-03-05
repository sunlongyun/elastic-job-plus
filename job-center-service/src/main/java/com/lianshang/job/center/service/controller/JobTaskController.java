package com.lianshang.job.center.service.controller;

import com.lianshang.job.center.service.beans.ProcessDataInfo;
import com.lianshang.job.center.service.beans.ShardInfo;
import com.lianshang.job.center.service.listener.ClientJobTaskListener;
import com.lianshang.job.center.service.jobTaskInterface.DataFlowJob;
import com.lianshang.job.center.service.jobTaskInterface.SimpleJob;
import com.lianshang.job.center.service.response.LsCloudResponse;
import com.lianshang.job.center.service.utils.GenericsUtils;
import com.lianshang.job.center.service.utils.JsonUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 回调任务
 *
 * @AUTHOR 孙龙云
 * @date 2019-03-01 上午11:20
 */
@RestController
@RequestMapping("/task")
@Slf4j
public class JobTaskController {

	/**
	 * 简单任务调用
	 */
	@RequestMapping("/simpleJob")
	public LsCloudResponse simpleJob(ShardInfo shardInfo) {

		Optional<SimpleJob> simpleJobOptional = ClientJobTaskListener.simpleJobMap.entrySet().stream()
			.filter(obj -> obj.getKey().equals(shardInfo.getJobName())).findFirst().map(a -> a.getValue());

		if(!simpleJobOptional.isPresent()) {
			return LsCloudResponse.fail("未找到对应的任务实现");
		}

		try {
			simpleJobOptional.ifPresent(simpleJob -> {
				simpleJob.execute(shardInfo);
			});
			return LsCloudResponse.success();
		} catch(Exception ex) {
			log.error("请求失败", ex);
			return LsCloudResponse.fail(ex.getCause().getMessage());
		}

	}

	/**
	 * 流式任务获取数据
	 */
	@RequestMapping("/dataFlow-fetchData")
	public LsCloudResponse dataFlowFetchData(ShardInfo shardInfo) {


		Optional<DataFlowJob> dataFlowJobOptional = ClientJobTaskListener.dataFlowJobMap.entrySet().stream()
			.filter(obj -> obj.getKey().equals(shardInfo.getJobName())).findFirst().map(a -> a.getValue());

		if(!dataFlowJobOptional.isPresent()) {
			return LsCloudResponse.fail("未找到对应的任务实现");
		}
		try {
			return LsCloudResponse.success(dataFlowJobOptional.get().fetchData(shardInfo));
		} catch(Exception ex) {
			log.error("请求失败", ex);
			return LsCloudResponse.fail(ex.getCause().getMessage());
		}
	}

	/**
	 * 处理流式数据
	 */
	@RequestMapping("/dataFlow-processData")
	public LsCloudResponse processData(@RequestBody ProcessDataInfo processDataInfo) {

		ShardInfo shardInfo = processDataInfo.getShardInfo();
		List data = processDataInfo.getData();

		Optional<DataFlowJob> dataFlowJobOptional = ClientJobTaskListener.dataFlowJobMap.entrySet().stream()
			.filter(obj -> obj.getKey().equals(shardInfo.getJobName())).findFirst().map(a -> a.getValue());

		if(!dataFlowJobOptional.isPresent()) {
			return LsCloudResponse.fail("未找到对应的任务实现");
		}
		DataFlowJob dataFlowJob = dataFlowJobOptional.get();
		try {
			if(null != data) {
				//获取泛型
				Class genericsClazz = GenericsUtils.getSuperClassGenricType(dataFlowJob.getClass());
				//转换list数据
				List dataList = (List) data.stream()
					.map(a -> JsonUtils.json2Object(JsonUtils.object2JsonString(a), genericsClazz))
					.collect(Collectors.toList());

				dataFlowJob.processData(shardInfo, dataList);
			}

			return LsCloudResponse.success();
		} catch(Exception ex) {
			log.error("请求失败", ex);
			return LsCloudResponse.fail(ex.getCause().getMessage());
		}

	}

	public JobTaskController() {
		log.info("JobTaskController初始化---------");
	}
}
