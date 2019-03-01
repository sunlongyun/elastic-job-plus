package com.lianshang.job.center.web.dto;

import java.util.Date;
import lombok.Data;

/**
 * 描述:
 * job
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午11:06
 */
@Data
public class JobCoreConfigurationDto {

	/**
	 * id
	 */
	private Integer id;
	/**
	 * 任务执行时间表达式
	 */
	private String cron;
	/**
	 * cron 定时表达式
	 */
	private String jobName;
	/**
	 * job类型  SimpleJob-简单作业;DataflowJob-流式作业
	 */
	private String jobType;
	/**
	 * 分片总数量
	 */
	private Integer shardingTotalCount = 5;
	/**
	 * 分片参数化
	 */
	private String shardingItemParameters;
	/**
	 * job自定义参数
	 */
	private String jobParameter;
	/**
	 * 是否开启任务失效转移
	 */
	private Boolean failover = false;
	/**
	 * 是否开启错过任务重新执行
	 */
	private Boolean misfire = false;
	/**
	 * 任务所在应用名称(Eureka 上应用名称)
	 */
	private String applicationName;
	/**
	 * 命名空间id
	 */
	private Integer namespaceId;
	/**
	 * 是否流式处理
	 */
	private Boolean streamingProcess = false;
	/**
	 * 是否有效
	 */
	private boolean validity;
	/**
	 * 作业任务描述
	 */
	private String description;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;

	public static enum JobType{

		SIMPLE_JOB("simple_job","简单作业类型"),
		DATA_FLOW_JOB("data_flow_job","流式作业类型");

		private String code;
		private String msg;
		JobType(String code, String msg){
			this.code = code;
			this.msg = msg;
		}
		public String code(){
			return this.code;
		}
		public String msg(){
			return this.msg;
		}
	}
}
