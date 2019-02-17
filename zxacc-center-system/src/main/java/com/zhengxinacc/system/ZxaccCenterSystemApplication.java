package com.zhengxinacc.system;

import java.util.Arrays;

import com.zhengxinacc.common.security.AuthenticationTokenFilter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.zhengxinacc.system", "com.zhengxinacc.common.security", "com.zhengxinacc.common.redis"})
@EnableEurekaClient
@EnableScheduling
@Slf4j
public class ZxaccCenterSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZxaccCenterSystemApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

//            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
//                log.debug(beanName);
            }
        };
    }

}
