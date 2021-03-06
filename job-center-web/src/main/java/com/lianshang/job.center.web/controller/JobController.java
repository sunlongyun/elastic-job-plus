package com.lianshang.job.center.web.controller;

import com.lianshang.job.center.web.controller.request.JobInfo;
import com.lianshang.job.center.web.controller.response.JobResponse;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.dto.JobCoreConfigurationDto.JobType;
import com.lianshang.job.center.web.dto.NameSpaceConfigurationDto;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import com.lianshang.job.center.web.service.NameSpaceConfigurationService;
import com.lianshang.job.center.web.util.JobUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 任务处理controller
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 下午4:54
 */
@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {

	/**
	 * 默认执行时间
	 */
	public static final String DEFAULT_CRON = "0 0 0 1 1 ? 2050";
	@Autowired
	private NameSpaceConfigurationService nameSpaceConfigurationService;
	@Autowired
	private JobCoreConfigurationService jobCoreConfigurationService;
	/**
	 * zookeeper注册中心地址
	 */
	@Value("${zookeepr.serverLists}")
	private String serverList;

	/**
	 * 业务端创建job的请求
	 */
	@RequestMapping("/jobNotify")
	public JobResponse jobNotify(@RequestBody JobInfo jobInfo) {

		//获取任务job,不存在,则进行创建
		JobCoreConfigurationDto jobCoreConfigurationDto = getJobCoreConfigurationDto(jobInfo);

		//开启任务
		if(JobType.SIMPLE_JOB.code().equals(jobInfo.getJobType())) {
			JobUtil.initSimpleJob(jobCoreConfigurationDto, jobCoreConfigurationDto.getNamespaceId());
		} else if(JobType.DATA_FLOW_JOB.code().equals(jobInfo.getJobType())) {
			JobUtil.initDataFlowJob(jobCoreConfigurationDto, jobCoreConfigurationDto.getNamespaceId());
		}

		return JobResponse.success();
	}

	private JobCoreConfigurationDto getJobCoreConfigurationDto(@RequestBody JobInfo jobInfo) {
		NameSpaceConfigurationDto spaceConfigurationDto = nameSpaceConfigurationService.getByName(jobInfo
				.getNamespace());

		if(spaceConfigurationDto == null) {//命名空间不存在,则创建
			spaceConfigurationDto = saveZookeeperConfigurationDto(jobInfo);
		} else if(!spaceConfigurationDto.isValidity()) {
			spaceConfigurationDto.setValidity(true);
			nameSpaceConfigurationService.edit(spaceConfigurationDto);
			spaceConfigurationDto = nameSpaceConfigurationService.getById(spaceConfigurationDto.getId());
		}

		//查看job是否存在
		JobCoreConfigurationDto jobCoreConfigurationDto = jobCoreConfigurationService
			.getByName(spaceConfigurationDto.getNameSpace(), jobInfo.getJobName());

		//如果不存在,则创建
		Integer jobId = createJobIfNotExist(jobInfo, spaceConfigurationDto, jobCoreConfigurationDto);
		jobCoreConfigurationDto = jobCoreConfigurationService.getById(jobId);
		if(!jobCoreConfigurationDto.isValidity()) {//无效的更新为有效
			jobCoreConfigurationDto.setValidity(true);
			jobCoreConfigurationService.edit(jobCoreConfigurationDto);
		}
		return jobCoreConfigurationDto;
	}

	//如果job不存在则创建job
	private int createJobIfNotExist(@RequestBody JobInfo jobInfo, NameSpaceConfigurationDto spaceConfigurationDto,
		JobCoreConfigurationDto jobCoreConfigurationDto) {

		if(null == jobCoreConfigurationDto) {//job不存在,创建job
			Integer jobId = saveJob(jobInfo, spaceConfigurationDto.getId());
			return jobId;
		}
		return jobCoreConfigurationDto.getId();
	}

	/**
	 * 添加命名空间
	 */
	private NameSpaceConfigurationDto saveZookeeperConfigurationDto(@RequestBody JobInfo jobInfo) {

		NameSpaceConfigurationDto nameSpaceConfigurationDto;

		nameSpaceConfigurationDto = new NameSpaceConfigurationDto();
		nameSpaceConfigurationDto.setNameSpace(jobInfo.getNamespace());
		nameSpaceConfigurationDto.setServerLists(serverList);
		nameSpaceConfigurationService.save(nameSpaceConfigurationDto);

		return nameSpaceConfigurationDto;
	}

	/**
	 * 添加任务
	 */
	private Integer saveJob(JobInfo jobInfo, Integer namespaceId) {

		JobCoreConfigurationDto jobCoreConfigurationDto;
		jobCoreConfigurationDto = new JobCoreConfigurationDto();
		jobCoreConfigurationDto.setJobName(jobInfo.getJobName());
		jobCoreConfigurationDto.setApplicationName(jobInfo.getNamespace());
		jobCoreConfigurationDto.setCron(DEFAULT_CRON);
		jobCoreConfigurationDto.setDescription(jobInfo.getNamespace() + "/" + jobInfo.getJobName());
		jobCoreConfigurationDto.setJobType(jobInfo.getJobType());
		jobCoreConfigurationDto.setNamespaceId(namespaceId);
		jobCoreConfigurationDto.setEventLog(jobInfo.getEventLog());

		jobCoreConfigurationService.save(jobCoreConfigurationDto);

		return jobCoreConfigurationDto.getId();
	}

}
