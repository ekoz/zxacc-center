/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/1/16 18:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTests {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void test(){
        List<RedisClientInfo> clientList = redisTemplate.getClientList();
        System.out.println(clientList.size());
        for (RedisClientInfo client : clientList){
            System.out.println(client.get("eko"));
        }
        Set<String> keys = redisTemplate.keys("*");
        keys.forEach(key-> System.out.println(key));
        System.out.println(redisTemplate.opsForValue().get("eko"));



    }
}
