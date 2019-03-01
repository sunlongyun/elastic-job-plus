package com.lianshang.job.center.service.jobTaskInterface;

import com.lianshang.job.center.service.beans.ShardInfo;
import java.util.List;

/**
 * 流式数据分片类型任务
 */
public interface DataFlowJob<T> {

	/**
	 * 抓取数据列表
	 * @param shardInfo
	 * @return
	 */
	List<T> fetchData(ShardInfo shardInfo);

	/**
	 * 处理数据.
	 *
	 * @param shardInfo 分片上下文
	 * @param data 待处理数据集合
	 */
	void processData(ShardInfo shardInfo, List<T> data);
}
