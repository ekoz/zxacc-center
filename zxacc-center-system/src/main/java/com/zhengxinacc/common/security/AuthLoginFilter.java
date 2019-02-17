/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/2/17 14:03
 */
@Slf4j
public class AuthLoginFilter extends AbstractAuthenticationProcessingFilter {

    public AuthLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // JSON反序列化成 AccountCredentials
        List<String> list = IOUtils.readLines(request.getInputStream(), "UTF-8");
        String authorization = request.getHeader("Authorization");
        Enumeration<String> headerNames = request.getHeaderNames();
//        String headerName = headerNames.nextElement();
        log.debug("===================================");
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            log.debug("1. " + headerName);
            log.debug("2. " + request.getHeader(headerName));
        }
        log.debug("===================================");

        log.debug("===================================");
        log.debug(authorization);
        log.debug(String.valueOf(list.size()));
        list.forEach(s -> log.debug(s));
        log.debug("===================================");
        AccountCredentials creds = new ObjectMapper().readValue(request.getInputStream(), AccountCredentials.class);

        // 返回一个验证令牌
        return getAuthenticationManager()
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                creds.getUsername(),
                                creds.getPassword()
                        )
                );
    }
}

class AccountCredentials {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}