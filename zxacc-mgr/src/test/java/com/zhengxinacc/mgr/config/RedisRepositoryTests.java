/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.config;

import com.google.gson.Gson;
import com.zhengxinacc.common.redis.RedisRepository;
import com.zhengxinacc.common.security.TokenAuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/1/16 19:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisRepositoryTests {

    @Autowired
    RedisRepository redisRepository;
    @Autowired
    TokenAuthenticationService tokenAuthenticationService;

    @Test
    public void testSet(){
        redisRepository.set("ekoz1", "ekoz1");
        System.out.println(redisRepository.get("ekoz1"));;
    }

    @Test
    public void testGet(){
        String user = redisRepository.get("user_auth_info_ekozhan");
        new Gson().fromJson(user, UserDetails.class);

    }

}
