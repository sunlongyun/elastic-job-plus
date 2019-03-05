package com.lianshang.job.center.web.controller.request;

import lombok.Data;

/**
 * 描述:
 * 业务端通知的job信息
 * @AUTHOR 孙龙云
 * @date 2019-02-28 下午5:01
 */
@Data
public class JobInfo {

	/**
	 * job的空间(和远程业务应用名称相同)
	 */
	private String namespace;
	/**
	 * 任务名称(和远程业务的bean名称相同)
	 */
	private String jobName;
	/**
	 * 任务类型
	 */
	private String jobType;
	/**
	 * 是否开启事件追踪
	 */
	private Boolean eventLog = false;
}
