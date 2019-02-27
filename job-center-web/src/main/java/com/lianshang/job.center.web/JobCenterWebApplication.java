package com.lianshang.job.center.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.lianshang.job.center.web.mapper")
public class JobCenterWebApplication {

  public static void main(String[] args) {

    SpringApplication.run(JobCenterWebApplication.class, args);
  }

}
