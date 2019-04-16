/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.config;

import com.zhengxinacc.common.util.EncryptUtils;
import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
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
    private static final String NULL_STRING = "null";

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
//        log.debug("token is {}", token);
//        log.debug("token is {} ", EncryptUtils.decodeBase64(token));
        if (StringUtils.isNotBlank(token) && !NULL_STRING.equals(token)){
            template.header(HttpHeaders.AUTHORIZATION, EncryptUtils.decodeBase64(token));
        }
    }
}
