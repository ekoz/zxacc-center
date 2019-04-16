package com.zhengxinacc.mgr.remote;

import com.alibaba.fastjson.JSONObject;
import com.zhengxinacc.mgr.config.FeignClientConfiguration;
import com.zhengxinacc.system.domain.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年7月28日 下午1:44:16
 * @version 1.0
 */
@FeignClient(name = "zxacc-zuul", /*configuration = FeignClientConfiguration.class,*/ fallback = UserLoginClientFallback.class)
public interface UserLoginClient {
	/**
	 * 登录验证
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/api-system/user/verify", method=RequestMethod.POST)
	JSONObject verify(@RequestParam("username") String username, @RequestParam("password") String password);

    /**
     * https://segmentfault.com/a/1190000018313243
     * https://github.com/OpenFeign/feign#headers
     * https://stackoverflow.com/questions/50237709/how-to-send-bearer-authorization-token-using-spring-boot-and-feignclient
     * https://blog.arnoldgalovics.com/passing-headers-with-spring-cloud-feign/
     * 根据用户名获取用户信息
     * @param username
     * @param token
     * @return
     */
    @RequestMapping(value="/api-system/user/findByUsername", method=RequestMethod.POST)
//    TODO 用 @Headers 无法登陆
//    @Headers("Authorization: {token}")
//    User findByUsername(@RequestParam("username") String username, @Param("token") String token);
    User findByUsername(@RequestParam("username") String username, @RequestHeader("Authorization") String token);

	/**
	 * 获取用户列表
	 * @param page
	 * @param limit
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value="/api-system/user/loadList", method=RequestMethod.POST)
	JSONObject loadList(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("keyword") String keyword);
}

class UserLoginClientFallback implements UserLoginClient {

    @Override
    public JSONObject verify(String username, String password) {
        return new JSONObject();
    }

    @Override
    public JSONObject loadList(Integer page, Integer limit, String keyword) {
        return new JSONObject();
    }

    @Override
    public User findByUsername(String username, String token){
        return null;
    }
}