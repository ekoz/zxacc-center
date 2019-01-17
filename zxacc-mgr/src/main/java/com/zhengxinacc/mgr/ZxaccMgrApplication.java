package com.zhengxinacc.mgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.zhengxinacc.mgr", "com.zhengxinacc.common.security", "com.zhengxinacc.common.redis"})
@EnableEurekaClient
@EnableFeignClients
public class ZxaccMgrApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZxaccMgrApplication.class, args);
	}
	
}
