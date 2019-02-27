package com.lianshang.job.center.web.mapper;

import com.lianshang.job.center.web.entity.ZookeeperConfiguration;

/**
 * zookeeper配置管理mapper
 */
public interface ZookeeperConfigurationMapper {

	public int insert(ZookeeperConfiguration zookeeperConfiguration);

	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public ZookeeperConfiguration getById(Integer id);

	/**
	 * 根据名称查询namespace
	 * @param namespace
	 * @return
	 */
	public ZookeeperConfiguration getByName(String namespace);

	/**
	 * 获取默认空间
	 * @return
	 */
	public ZookeeperConfiguration getDefault();
}
