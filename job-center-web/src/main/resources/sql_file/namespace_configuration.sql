####创建表namespace_configuration#######
CREATE TABLE `namespace_configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `server_lists` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '连接Zookeeper服务器的列表\n包括IP地址和端口号\n多个地址用逗号分隔\n如: host1:2181,host2:2181',
  `name_space` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'Zookeeper的命名空间\n',
  `base_sleep_timeMilliseconds` int(11) DEFAULT '1000' COMMENT '等待重试的间隔时间的初始值\n单位：毫秒',
  `max_sleep_timeMilliseconds` int(11) DEFAULT '3000' COMMENT '等待重试的间隔时间的最大值',
  `max_retries` smallint(3) DEFAULT '3' COMMENT '最大重试次数',
  `session_timeoutMilliseconds` int(11) DEFAULT '6000' COMMENT '会话超时时间',
  `connection_timeout_milli_seconds` int(11) DEFAULT '15000' COMMENT '连接超时时间',
  `digest` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '连接Zookeeper的权限令牌\n缺省为不需要权限验证\n',
  `validity` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效 1-有效;0-无效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='ZookeeperConfiguration 注册中心的配置';