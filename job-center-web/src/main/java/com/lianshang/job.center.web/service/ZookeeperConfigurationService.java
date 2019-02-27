package com.lianshang.job.center.web.service;

import com.lianshang.job.center.web.dto.ZookeeperConfigurationDto;
import com.lianshang.job.center.web.entity.ZookeeperConfiguration;

/**
 * zookeeper配置管理 service
 */
public interface ZookeeperConfigurationService {

	/**
	 * 添加空间配置
	 * @param zookeeperConfigurationDto
	 */
	public void save(ZookeeperConfigurationDto zookeeperConfigurationDto);

	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public ZookeeperConfigurationDto getById(Integer id);

	/**
	 * 根据名称查询namespace
	 * @param namespace
	 * @return
	 */
	public ZookeeperConfigurationDto getByName(String namespace);

	/**
	 * 获取默认空间
	 * @return
	 */
	public ZookeeperConfigurationDto getDefault();
}
