package com.zhengxinacc.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
/**
 * 使用 exclude 后会有如下报错，直接从 pom.xml 中不引入 security 包，zuul 没必要加上 security 验证
 * @author eko.zhan
 * @date 2019/2/16 15:03
 * Parameter 0 of method setObjectPostProcessor in org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter required a bean of type 'org.springframework.security.config.annotation.ObjectPostProcessor' that could not be found.
 * @SpringBootApplication(exclude = {
 * 		org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class
 * })
 */
public class ZxaccZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZxaccZuulApplication.class, args);
	}
}
