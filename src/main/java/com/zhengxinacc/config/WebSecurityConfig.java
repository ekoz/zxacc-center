/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.zhengxinacc.system.user.service.UserService;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年10月21日 下午1:45:36
 * @version 1.0
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;
	@Autowired
    private MyAuthenticationProvider provider;
	@Autowired
	private MyAuthenticationSuccessHandler successHandler;
	@Autowired
	private MyAccessDeniedHandler accessDeniedHandler;
	
	/**
	 * 设置拦截规则，设置默认登录页面以及登录成功后的跳转页面 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/about").permitAll()
				.antMatchers("/user/loadData", "/user/save").authenticated()
				.antMatchers("/exam/task/**", "/exam/exec/**").hasAnyRole("ADMIN", "EXAM", "USER")
				.antMatchers("/exam/**").hasAnyRole("ADMIN", "EXAM")
				.antMatchers("/user/**", "/role/**", "/menu/**", "/permission/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated() //任何请求,登录后可以访问
			//注意顺序
			.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/main", true) //默认进入main，注意，设置为true后不会进入上一个页面了，具体在  successHandler 中根据条件跳转
				.failureUrl("/login?error")
				.permitAll()
				.successHandler(successHandler) //登录页面用户任意访问
			.and()
			.logout()
				.permitAll() //注销行为任意访问
			.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
			;
			
		//http://blog.csdn.net/u012373815/article/details/55047285
		http.csrf().disable();
		
		//X-Frame-Options default is deny
		//https://docs.spring.io/spring-security/site/docs/current/reference/html/headers.html#headers-frame-options
		http.headers().frameOptions().sameOrigin();
		
		super.configure(http);
	}
	
	/**
	 * 设置用户
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("zxacc").password("zxacc123$").roles("ADMIN");
		//auth.userDetailsService(userService);
		auth.authenticationProvider(provider);
	}
	
	/**
	 * 设置静态资源不被拦截
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/DATAS/**", "/plugins/**");
		super.configure(web);
	}
}
