package com.lianshang.job.center.web.service;


import com.lianshang.job.center.web.dto.NameSpaceConfigurationDto;

/**
 * zookeeper配置管理 service
 */
public interface NameSpaceConfigurationService {

	/**
	 * 添加空间配置
	 * @param nameSpaceConfigurationDto
	 */
	public void save(NameSpaceConfigurationDto nameSpaceConfigurationDto);

	/**
	 * 根据id查询对象
	 * @param id
	 * @return
	 */
	public NameSpaceConfigurationDto getById(Integer id);

	/**
	 * 根据名称查询namespace
	 * @param namespace
	 * @return
	 */
	public NameSpaceConfigurationDto getByName(String namespace);

	/**
	 * 获取默认空间
	 * @return
	 */
	public NameSpaceConfigurationDto getDefault();

	/**
	 * 编辑
	 * @param nameSpaceConfigurationDto
	 */
	public void edit(NameSpaceConfigurationDto nameSpaceConfigurationDto);

}
