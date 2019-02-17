package com.zhengxinacc.mgr.remote;

import com.alibaba.fastjson.JSONObject;
import com.zhengxinacc.mgr.config.FeignClientConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
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
}