package com.lianshang.job.center.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午10:57
 */
@Slf4j
public class MyDataflowJob implements DataflowJob {

	@Override
	public List fetchData(ShardingContext shardingContext) {
		log.info("分片:{}", shardingContext.getShardingItem());
		log.info("获取数据--------");
		return Arrays.asList(1, 2);
	}

	@Override
	public void processData(ShardingContext shardingContext, List data) {
		log.info("分片:{}", shardingContext.getShardingItem());
		log.info("消费数据:{}", data);
	}
}
