/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.system.config;

import com.zhengxinacc.common.security.AuthLoginFilter;
import com.zhengxinacc.common.security.AuthenticationTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年10月21日 下午1:45:36
 * @version 1.0
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 设置拦截规则，设置默认登录页面以及登录成功后的跳转页面 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http://blog.csdn.net/u012373815/article/details/55047285
        http.authorizeRequests()
                .antMatchers("/user/verify").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll();
		http.csrf().disable()
				.addFilterBefore(new AuthLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		super.configure(http);
	}

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() {
        return new AuthenticationTokenFilter();
    }

}
