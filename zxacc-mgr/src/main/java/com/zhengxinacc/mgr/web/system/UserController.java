/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.web.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.alibaba.fastjson.JSONObject;
import com.zhengxinacc.common.config.BaseController;
import com.zhengxinacc.mgr.remote.UserClient;
import com.zhengxinacc.system.domain.User;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年7月30日 下午7:15:16
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	UserClient userClient;
	
	/**
	 * 获取当前用户信息
	 * @param authentication
	 * @return
	 */
	@RequestMapping("/loadData")
	public User loadData(Authentication authentication){
		return (User)authentication.getPrincipal();
	}
	
	@RequestMapping("/loadList")
	public JSONObject loadList(Integer page, Integer limit, String keyword){
		return userClient.loadList(page, limit, keyword);
	}
}
