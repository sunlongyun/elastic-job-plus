package com.lianshang.job.center.service.jobTaskInterface;

import com.lianshang.job.center.service.beans.ShardInfo;

/**
 * 简单类型分片任务
 */
public interface SimpleJob {

	/**
	 *执行分片任务
	 * @param shardInfo
	 */
	public void execute(ShardInfo shardInfo);
}
