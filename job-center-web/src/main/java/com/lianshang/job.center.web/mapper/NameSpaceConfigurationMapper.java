package com.lianshang.job.center.web.mapper;

import com.lianshang.job.center.web.entity.NameSpaceConfiguration;

/**
 * zookeeper配置管理mapper
 */
public interface NameSpaceConfigurationMapper {

	public int insert(NameSpaceConfiguration nameSpaceConfiguration);

	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public NameSpaceConfiguration getById(Integer id);

	/**
	 * 根据名称查询namespace
	 * @param namespace
	 * @return
	 */
	public NameSpaceConfiguration getByName(String namespace);

	/**
	 * 获取默认空间
	 * @return
	 */
	public NameSpaceConfiguration getDefault();
}
