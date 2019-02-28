package com.lianshang.job.center.web.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午10:45
 */
@Slf4j
public class MySimpleJob implements SimpleJob{

	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("发送请求---------{},thread:{}",new Date(),Thread.currentThread());
	}
}
