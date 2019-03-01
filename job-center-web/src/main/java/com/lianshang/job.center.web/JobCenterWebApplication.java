package com.lianshang.job.center.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@MapperScan(basePackages="com.lianshang.job.center.web.mapper")
@EnableEurekaClient
@EnableDiscoveryClient
public class JobCenterWebApplication {

  public static void main(String[] args) {

    SpringApplication.run(JobCenterWebApplication.class, args);
  }

}
