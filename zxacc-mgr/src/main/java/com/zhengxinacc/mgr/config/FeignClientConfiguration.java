/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.config;

import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 设置 @Configuration，会被加载一次，如果在 @FeignClient(name = "zxacc-zuul", configuration = FeignClientConfiguration.class) 再次设置，又会被加载一次
 * 否则会被默认加载两次
 * c.z.mgr.config.FeignClientConfiguration  : new BasicAuthRequesetInterceptor
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/1/18 0:06
 */
@Configuration
@Slf4j
public class FeignClientConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.debug(request.getHeader(HttpHeaders.AUTHORIZATION));
//        template.header(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
        template.header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyYW5kb21LZXkiOiJ6eGFjYyIsInN1YiI6ImVrb3poYW4iLCJleHAiOjE1NTA5OTE4ODEsImlhdCI6MTU1MDM4NzA4MX0.1ysBoP5dHR0HMAW9V92AK57-iXBeYkEiszxpYNoqu21gNODYj_LZ6AXt9erlBUyxL2ZSMurIou5ILdCidXOQ2g");
    }
}
