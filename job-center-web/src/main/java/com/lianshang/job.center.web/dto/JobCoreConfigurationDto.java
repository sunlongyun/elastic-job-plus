package com.lianshang.job.center.web.dto;

import java.util.Date;

/**
 * 描述:
 * job
 * @AUTHOR 孙龙云
 * @date 2019-02-28 上午11:06
 */
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
	private Integer shardingTotalCount;
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
	private Boolean failover;
	/**
	 * 是否开启错过任务重新执行
	 */
	private Boolean misfire;
	/**
	 * 任务所在应用名称(Eureka 上应用名称)
	 */
	private String applicationName;
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

		SIMPLE_JOB("SimpleJob","简单作业类型"),
		DATA_FLOW_JOB("DataflowJob","流式作业类型");

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
