/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.remote;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2018年7月28日 下午12:03:53
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
public class UserLoginClientTests {

	@Autowired
	UserLoginClient userLoginClient;
	
	@Test
	public void testVerify(){

		JSONObject json = userLoginClient.verify("ekozhan", "888888");
		System.out.println(json);
	}

	@Test
	public void testLoadList(){
        JSONObject list = userLoginClient.loadList(1, 10, "");
        System.out.println(list);
    }
}
