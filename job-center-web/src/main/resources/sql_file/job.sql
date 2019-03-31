CREATE TABLE `JOB_STATUS_TRACE_LOG` (
`id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`job_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`original_task_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`task_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`slave_id`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`source`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`execution_type`  varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`sharding_item`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`state`  varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`message`  varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`creation_time`  timestamp NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
INDEX `TASK_ID_STATE_INDEX` (`task_id`, `state`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
ROW_FORMAT=COMPACT;



CREATE TABLE `JOB_EXECUTION_LOG` (
`id`  varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`job_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`task_id`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`hostname`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`ip`  varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`sharding_item`  int(11) NOT NULL ,
`execution_source`  varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL ,
`failure_cause`  varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL ,
`is_success`  int(11) NOT NULL ,
`start_time`  timestamp NULL DEFAULT NULL ,
`complete_time`  timestamp NULL DEFAULT NULL ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
ROW_FORMAT=COMPACT;



CREATE TABLE `job_core_configuration` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`job_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '作业名称' ,
`cron`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'cron表达式，用于控制作业触发时间' ,
`job_type`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'job类型  SimpleJob-简单作业;DataflowJob-流式作业' ,
`sharding_total_count`  int(11) NOT NULL DEFAULT 1 COMMENT '作业分片数' ,
`sharding_item_parameters`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分片序列号和参数用等号分隔，多个键值对用逗号分隔\n分片序列号从0开始，不可大于或等于作业分片总数\n如：\n0=a,1=b,2=c' ,
`namespace_id`  int(11) NOT NULL COMMENT '命名空间id' ,
`job_parameter`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '作业自定义参数\n作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业\n例：每次获取的数据量、作业实例从数据库读取的主键等' ,
`failover`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '  是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行\n' ,
`misfire`  tinyint(1) NULL DEFAULT 1 COMMENT '是否开启错过任务重新执行\n' ,
`event_log`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启事件追踪  1-开启,0-不开启' ,
`streaming_process`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否流式处理' ,
`application_name`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '应用名称(eureka应用名称)' ,
`validity`  tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效 1-有效;0-无效' ,
`description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '作业描述信息\n' ,
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `job_name_index` (`job_name`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='作业配置'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;


CREATE TABLE `namespace_configuration` (
`id`  int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' ,
`server_lists`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '连接Zookeeper服务器的列表\n包括IP地址和端口号\n多个地址用逗号分隔\n如: host1:2181,host2:2181' ,
`name_space`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'Zookeeper的命名空间\n' ,
`base_sleep_timeMilliseconds`  int(11) NULL DEFAULT 1000 COMMENT '等待重试的间隔时间的初始值\n单位：毫秒' ,
`max_sleep_timeMilliseconds`  int(11) NULL DEFAULT 3000 COMMENT '等待重试的间隔时间的最大值' ,
`max_retries`  smallint(3) NULL DEFAULT 3 COMMENT '最大重试次数' ,
`session_timeoutMilliseconds`  int(11) NULL DEFAULT 6000 COMMENT '会话超时时间' ,
`connection_timeout_milli_seconds`  int(11) NULL DEFAULT 15000 COMMENT '连接超时时间' ,
`digest`  varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '连接Zookeeper的权限令牌\n缺省为不需要权限验证\n' ,
`validity`  tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效 1-有效;0-无效' ,
`create_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,
`update_time`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
PRIMARY KEY (`id`),
UNIQUE INDEX `namespace_index` (`name_space`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_bin
COMMENT='ZookeeperConfiguration 注册中心的配置'
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT
;



