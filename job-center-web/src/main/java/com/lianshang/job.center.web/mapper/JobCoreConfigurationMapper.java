package com.lianshang.job.center.web.mapper;

import com.lianshang.job.center.web.entity.JobCoreConfiguration;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * job配置
 */
public interface JobCoreConfigurationMapper {

	/**
	 * 添加job配置
	 */
	public int insert(JobCoreConfiguration jobCoreConfiguration);

	/**
	 * 刷新任务配置
	 */
	public int update(JobCoreConfiguration jobCoreConfiguration);

	/**
	 * 根据id查询
	 */
	public JobCoreConfiguration getById(Integer id);

	/**
	 * 根据job名字查询
	 */
	public JobCoreConfiguration getByName(@Param("applicationName") String applicationName,
		@Param("jobName") String jobName);

	/**
	 * @return
	 * 获取所有有效的job列表
	 */
	public List<JobCoreConfiguration> getAllList();
}
