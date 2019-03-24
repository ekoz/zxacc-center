/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.discovery.converters.Auto;
import com.zhengxinacc.common.redis.RedisRepository;
import com.zhengxinacc.common.security.TokenAuthenticationService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.zhengxinacc.common.util.SystemKeys;
import com.zhengxinacc.mgr.remote.UserClient;
import com.zhengxinacc.mgr.util.LogUtils;
import com.zhengxinacc.system.domain.User;
import org.springframework.util.Base64Utils;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月24日 上午10:32:53
 * @version 1.0
 */
@Slf4j
@Component(value="successHandler")
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler  {

	@Autowired
	UserClient userClient;
	@Resource
    RedisRepository redisRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		String username = authentication.getName();
        // 登陆成功后，将 user_auth_token_*** 存放在 HttpHeaders.AUTHORIZATION 中
        String token = TokenAuthenticationService.TOKEN_TYPE_BEARER + " " + redisRepository.get("user_auth_token_" + username);
//        response.setHeader(HttpHeaders.AUTHORIZATION, token);

        response.addCookie(new Cookie("token", new String(Base64Utils.encode(token.getBytes()))));
		User user = userClient.findByUsername(username);
		log.debug(user.toString());
		LogUtils.infoLogin(user, LogUtils.getRemoteIp(request));

		super.onAuthenticationSuccess(request, response, authentication);
		
	}
}