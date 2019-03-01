package com.lianshang.job.center.web.entity;

import java.util.Date;
import lombok.Data;

/**
 * 描述: zookeeper 配置中心配置namespace等信息
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-27 下午3:41
 */
@Data
public class NameSpaceConfiguration {

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
	 * 等待重试的间隔时间的初始值
	 单位：毫秒
	 */
	private Integer baseSleepTimeMilliseconds;
	/**
	 * 等待重试的间隔时间的最大值
	 */
	private Integer maxSleepTimeMilliseconds;
	/**
	 * 最大重试次数
	 */
	private Integer maxRetries;
	/**
	 * 会话超时时间
	 */
	private Integer sessionTimeoutMilliseconds;
	/**
	 * 连接超时时间
	 */
	private Integer connectionTimeoutMilliSeconds;
	/**
	 * 连接Zookeeper的权限令牌
	 缺省为不需要权限验证
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
}
