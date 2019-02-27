package com.lianshang.job.center.web.service.impl;

import com.lianshang.job.center.web.dto.ZookeeperConfigurationDto;
import com.lianshang.job.center.web.entity.ZookeeperConfiguration;
import com.lianshang.job.center.web.mapper.ZookeeperConfigurationMapper;
import com.lianshang.job.center.web.service.ZookeeperConfigurationService;
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
public class ZookeeperConfigurationServiceImpl implements ZookeeperConfigurationService {

	@Autowired
	private ZookeeperConfigurationMapper zookeeperConfigurationMapper;

	@Override
	public void save(ZookeeperConfigurationDto zookeeperConfigurationDto) {
		if(null == zookeeperConfigurationDto) {
			throw new RuntimeException("zookeeperConfigurationDto不能为空");
		}
		ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration();
		BeanUtils.copyProperties(zookeeperConfigurationDto, zookeeperConfiguration);
		int r = zookeeperConfigurationMapper.insert(zookeeperConfiguration);
	}

	@Override
	public ZookeeperConfigurationDto getById(Integer id) {

		ZookeeperConfiguration zookeeperConfiguration = zookeeperConfigurationMapper.getById(id);
		return entityToDto(zookeeperConfiguration);
	}

	/**
	 * entity转DTO
	 */
	private ZookeeperConfigurationDto entityToDto(ZookeeperConfiguration zookeeperConfiguration) {
		if(null != zookeeperConfiguration) {
			ZookeeperConfigurationDto zookeeperConfigurationDto = new ZookeeperConfigurationDto();
			BeanUtils.copyProperties(zookeeperConfiguration, zookeeperConfigurationDto);
			return zookeeperConfigurationDto;
		}
		return null;
	}

	@Override
	public ZookeeperConfigurationDto getByName(String namespace) {

		ZookeeperConfiguration zookeeperConfiguration = zookeeperConfigurationMapper.getByName(namespace);
		return entityToDto(zookeeperConfiguration);
	}

	@Override
	public ZookeeperConfigurationDto getDefault() {

		ZookeeperConfiguration zookeeperConfiguration = zookeeperConfigurationMapper.getDefault();
		return entityToDto(zookeeperConfiguration);
	}
}
