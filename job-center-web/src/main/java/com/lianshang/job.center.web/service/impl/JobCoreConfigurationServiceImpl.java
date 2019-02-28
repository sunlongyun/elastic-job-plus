package com.lianshang.job.center.web.service.impl;

import com.lianshang.job.center.web.dto.JobCoreConfigurationDto;
import com.lianshang.job.center.web.entity.JobCoreConfiguration;
import com.lianshang.job.center.web.mapper.JobCoreConfigurationMapper;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午11:07
 */
@Service
public class JobCoreConfigurationServiceImpl implements JobCoreConfigurationService {

	@Autowired
	private JobCoreConfigurationMapper jobCoreConfigurationMapper;

	@Override
	public void save(JobCoreConfigurationDto jobCoreConfigurationDto) {

		if(null == jobCoreConfigurationDto) {
			throw new RuntimeException("JobCoreConfigurationDto 对象不能为空");
		}

		jobCoreConfigurationMapper.insert(dtoToEntity(jobCoreConfigurationDto));

	}

	@Override
	public void edit(JobCoreConfigurationDto jobCoreConfigurationDto) {

		if(null == jobCoreConfigurationDto) {
			throw new RuntimeException("JobCoreConfigurationDto 对象不能为空");
		}
		jobCoreConfigurationMapper.update(dtoToEntity(jobCoreConfigurationDto));

	}

	@Override
	public JobCoreConfigurationDto getById(Integer id) {
		JobCoreConfiguration jobCoreConfiguration = jobCoreConfigurationMapper.getById(id);
		return entityToDto(jobCoreConfiguration);
	}

	@Override
	public JobCoreConfigurationDto getByName(String applicationName, String jobName) {
		JobCoreConfiguration jobCoreConfiguration = jobCoreConfigurationMapper.getByName(applicationName, jobName);
		return entityToDto(jobCoreConfiguration);
	}

	@Override
	public List<JobCoreConfigurationDto> getAllList() {

		return jobCoreConfigurationMapper.getAllList().stream().map(this::entityToDto).collect(
			Collectors.toList());
	}

	/**
	 * dto转entity
	 */
	private JobCoreConfiguration dtoToEntity(JobCoreConfigurationDto dto) {
		if(null != dto) {
			JobCoreConfiguration jobCoreConfiguration = new JobCoreConfiguration();
			BeanUtils.copyProperties(dto, jobCoreConfiguration);
			return jobCoreConfiguration;
		}
		return null;
	}

	/**
	 * entity转dto
	 */
	private JobCoreConfigurationDto entityToDto(JobCoreConfiguration jobCoreConfiguration) {
		if(null != jobCoreConfiguration) {
			JobCoreConfigurationDto jobCoreConfigurationDto = new JobCoreConfigurationDto();
			BeanUtils.copyProperties(jobCoreConfiguration, jobCoreConfigurationDto);
			return jobCoreConfigurationDto;
		}
		return null;
	}
}
