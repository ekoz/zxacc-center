/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.mgr.security;

import org.junit.Test;
import org.springframework.util.Base64Utils;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/3/24 17:31
 */
public class Base64Tests {
    @Test
    public void testDecode(){
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJyYW5kb21LZXkiOiJ6eGFjYyIsInN1YiI6ImVrb3poYW4iLCJleHAiOjE1NTQwMTkzNzcsImlhdCI6MTU1MzQxNDU3N30.VVKjyOoQZyBrNk69SkOv75DPSOBm_ErDZOthcV6qInNnozYZdPur8uROESv1Tx7V8M7gIOBTYBbNelh4_TMcPQ";
        String base64Str = new String(Base64Utils.encode(token.getBytes()));
        System.out.println(base64Str);
        System.out.println(new String(new String(Base64Utils.decode(base64Str.getBytes()))));
    }
}
