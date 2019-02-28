package com.lianshang.job.center.web.service;

import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.entity.JobCoreConfiguration;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * job管理service
 */
public interface JobCoreConfigurationService {

	/**
	 * 添加job配置
	 */
	public void save(JobCoreConfigurationDto jobCoreConfigurationDto);

	/**
	 * 刷新任务配置
	 */
	public void edit(JobCoreConfigurationDto jobCoreConfigurationDto);

	/**
	 * 根据id查询
	 */
	public JobCoreConfigurationDto getById(Integer id);

	/**
	 * 根据job名字查询
	 */
	public JobCoreConfigurationDto getByName(@Param("applicationName") String applicationName,
		@Param("jobName") String jobName);

	/**
	 * @return
	 * 获取所有有效的job列表
	 */
	public List<JobCoreConfigurationDto> getAllList();
}
