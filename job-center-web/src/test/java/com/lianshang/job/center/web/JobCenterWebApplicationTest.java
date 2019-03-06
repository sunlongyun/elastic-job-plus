package com.lianshang.job.center.web;

import com.lianshang.job.center.web.dto.NameSpaceConfigurationDto;
import com.lianshang.job.center.web.service.JobCoreConfigurationService;
import com.lianshang.job.center.web.service.NameSpaceConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 描述:
 *
 * @AUTHOR 孙龙云
 * @date 2019-02-27 下午1:06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JobCenterWebApplicationTest {

	@Autowired
	private NameSpaceConfigurationService nameSpaceConfigurationService;
	@Autowired
	private JobCoreConfigurationService jobCoreConfigurationService;
	@Autowired
	private ZooKeeper zooKeeper;
	@Test
	public void test1() {
		NameSpaceConfigurationDto zookeeperConfigurationDto = new NameSpaceConfigurationDto();
		zookeeperConfigurationDto
			.setServerLists("zk01.lian-shang.cn:2181,zk02.lian-shang.cn:2181,zk03.lian-shang.cn:2181");
		zookeeperConfigurationDto.setNameSpace("default_namespace");
		nameSpaceConfigurationService.save(zookeeperConfigurationDto);
	}

	@Test
	public void test2() {
		NameSpaceConfigurationDto zookeeperConfigurationDto = nameSpaceConfigurationService.getDefault();
		log.info("zookeeperConfigurationDto=={}", zookeeperConfigurationDto);
	}

	@Test
	public void test3(){
		jobCoreConfigurationService.getAllList().forEach(System.out::println);
	}

	/**
	 * 客户端
	 */
	@Test
	public void test4() {
		log.info("zoo=>{}", zooKeeper);
	}


}
