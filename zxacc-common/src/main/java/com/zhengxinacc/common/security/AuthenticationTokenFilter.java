/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.common.security;

import com.zhengxinacc.system.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/1/13 17:42
 */
@Slf4j
public class AuthenticationTokenFilter extends GenericFilterBean {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader==null || !authHeader.startsWith(tokenAuthenticationService.TOKEN_TYPE_BEARER)) {
            chain.doFilter(request, response);
            return;
        }

        final String authToken = StringUtils.substring(authHeader, 7);
        log.debug(authToken);
        String username = StringUtils.isNotBlank(authToken) ? tokenAuthenticationService.getUsernameFromToken(authToken) : null;

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && !tokenAuthenticationService.isTokenExpired(authToken)) {
            log.debug(username);
            User user = tokenAuthenticationService.getUserDetails(authToken);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
