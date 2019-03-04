###创建表job_core_configuration######
CREATE TABLE `job_core_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '作业名称',
  `cron` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'cron表达式，用于控制作业触发时间',
  `job_type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'job类型  SimpleJob-简单作业;DataflowJob-流式作业',
  `sharding_total_count` int(11) NOT NULL DEFAULT '1' COMMENT '作业分片数',
  `sharding_item_parameters` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '分片序列号和参数用等号分隔，多个键值对用逗号分隔\n分片序列号从0开始，不可大于或等于作业分片总数\n如：\n0=a,1=b,2=c',
  `namespace_id` int(11) NOT NULL COMMENT '命名空间id',
  `job_parameter` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '作业自定义参数\n作业自定义参数，可通过传递该参数为作业调度的业务方法传参，用于实现带参数的作业\n例：每次获取的数据量、作业实例从数据库读取的主键等',
  `failover` tinyint(1) NOT NULL DEFAULT '0' COMMENT '  是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行\n',
  `misfire` tinyint(1) DEFAULT '1' COMMENT '是否开启错过任务重新执行\n',
  `streaming_process` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否流式处理',
  `application_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '应用名称(eureka应用名称)',
  `validity` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效 1-有效;0-无效',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '作业描述信息\n',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='作业配置';