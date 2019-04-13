/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.security;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/4/13 13:19
 */
@Slf4j
@Component(value="failureHandler")
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("登录失败，原因 [%s]", exception.getMessage());
        String msg = exception.getMessage();

        // 账号或密码错误
        int result = 0;
        if (exception instanceof SessionAuthenticationException) {
            // session 限制, 用户重复登录
            result = -1;
        } else if (exception instanceof BadCredentialsException) {
            // 账号或密码错误
            result = -2;
        }

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("type", result);
        out.println(json.toString());
    }
}
