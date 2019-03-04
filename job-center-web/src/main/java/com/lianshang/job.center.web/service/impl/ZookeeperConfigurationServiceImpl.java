package com.lianshang.job.center.web.service.impl;

import com.lianshang.job.center.web.dto.NameSpaceConfigurationDto;
import com.lianshang.job.center.web.entity.NameSpaceConfiguration;
import com.lianshang.job.center.web.mapper.NameSpaceConfigurationMapper;
import com.lianshang.job.center.web.service.NameSpaceConfigurationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-27 下午4:19
 */
@Service
public class ZookeeperConfigurationServiceImpl implements NameSpaceConfigurationService {

	@Autowired
	private NameSpaceConfigurationMapper nameSpaceConfigurationMapper;

	@Override
	public void save(NameSpaceConfigurationDto zookeeperConfigurationDto) {

		if(null == zookeeperConfigurationDto) {
			throw new RuntimeException("zookeeperConfigurationDto不能为空");
		}

		NameSpaceConfiguration zookeeperConfiguration = new NameSpaceConfiguration();
		BeanUtils.copyProperties(zookeeperConfigurationDto, zookeeperConfiguration);

		nameSpaceConfigurationMapper.insert(zookeeperConfiguration);

		zookeeperConfigurationDto.setId(zookeeperConfiguration.getId());
	}

	@Override
	public NameSpaceConfigurationDto getById(Integer id) {

		NameSpaceConfiguration zookeeperConfiguration = nameSpaceConfigurationMapper.getById(id);
		return entityToDto(zookeeperConfiguration);
	}

	/**
	 * entity转DTO
	 */
	private NameSpaceConfigurationDto entityToDto(NameSpaceConfiguration nameSpaceConfiguration) {
		if(null != nameSpaceConfiguration) {
			NameSpaceConfigurationDto zookeeperConfigurationDto = new NameSpaceConfigurationDto();
			BeanUtils.copyProperties(nameSpaceConfiguration, zookeeperConfigurationDto);
			return zookeeperConfigurationDto;
		}
		return null;
	}

	@Override
	public NameSpaceConfigurationDto getByName(String namespace) {

		NameSpaceConfiguration zookeeperConfiguration = nameSpaceConfigurationMapper.getByName(namespace);
		return entityToDto(zookeeperConfiguration);
	}

	@Override
	public NameSpaceConfigurationDto getDefault() {

		NameSpaceConfiguration zookeeperConfiguration = nameSpaceConfigurationMapper.getDefault();
		return entityToDto(zookeeperConfiguration);
	}
}
