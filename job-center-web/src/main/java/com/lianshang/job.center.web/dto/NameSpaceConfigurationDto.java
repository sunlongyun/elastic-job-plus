package com.lianshang.job.center.web.dto;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import java.util.Date;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-27 下午4:20
 */
@Data
public class NameSpaceConfigurationDto {

	/**
	 * 主键id
	 */
	private Integer id;
	/**
	 * zookeeper集群列表,逗号隔开
	 */
	private String serverLists;
	/**
	 * 命名空间
	 */
	private String nameSpace;
	/**
	 * 等待重试的间隔时间的初始值 单位：毫秒
	 */
	private Integer baseSleepTimeMilliseconds = 10000;
	/**
	 * 等待重试的间隔时间的最大值
	 */
	private Integer maxSleepTimeMilliseconds = 3000;
	/**
	 * 最大重试次数
	 */
	private Integer maxRetries = 3;
	/**
	 * 会话超时时间
	 */
	private Integer sessionTimeoutMilliseconds = 60000;
	/**
	 * 连接超时时间
	 */
	private Integer connectionTimeoutMilliSeconds = 15000;
	/**
	 * 连接Zookeeper的权限令牌 缺省为不需要权限验证
	 */
	private String digest;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 是否有效
	 */
	private boolean validity;



	/**
	 * 获取当当的空间配置
	 */
	public ZookeeperConfiguration getDangDangConfiguration() {
		ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(serverLists, nameSpace);
		BeanUtils.copyProperties(this, zookeeperConfiguration);
		return zookeeperConfiguration;
	}

	/**
	 * 获取注册中心
	 * @return
	 */
	public CoordinatorRegistryCenter getCoordinatorRegistryCenter(){
		CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(getDangDangConfiguration());
		regCenter.init();
		return regCenter;
	}
}
