package com.lianshang.job.center.service.beans;

import lombok.Data;

/**
 * 描述: 分片信息
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 下午4:13
 */
@Data
public class ShardInfo {

	/**
	 * 作业名称.
	 */
	private String jobName;

	/**
	 * 作业任务ID.
	 */
	private String taskId;

	/**
	 * 分片总数.
	 */
	private int shardingTotalCount;

	/**
	 * 作业自定义参数. 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
	 */
	private String jobParameter;

	/**
	 * 分配于本作业实例的分片项.
	 */
	private int shardingItem;

	/**
	 * 分配于本作业实例的分片参数.
	 */
	private String shardingParameter;
}
