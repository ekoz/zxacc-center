/*
 * Power by www.xiaoi.com
 */
package com.zhengxinacc.common.security;

import com.alibaba.fastjson.JSON;
import com.zhengxinacc.common.redis.RedisRepository;
import com.zhengxinacc.system.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * copy from https://github.com/coderliguoqing/vans/blob/master/vans-common-util/src/main/java/cn/com/guoqing/vans/common/security/JwtTokenUtil.java
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2019/1/13 17:21
 */
@Service
public class TokenAuthenticationService {

    public static final String TOKEN_TYPE_BEARER = "Bearer";

    @Getter
    @Setter
    private String header = "Authorization";
    @Getter
    @Setter
    private String secret = "defaultSecret";
    @Getter
    @Setter
    private Long expiration = 604800L;
    @Getter
    @Setter
    private String authPath = "auth";
    @Getter
    @Setter
    private String md5Key = "randomKey";

    @Autowired
    RedisRepository redisRepository;

    /**
     * 从token中获取用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken( String token ){
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 从token获取jwt的发布时间
     * @param token
     * @return
     */
    public Date getIssueAtDateFromToken(String token){
        return getClaimFromToken(token).getIssuedAt();
    }

    /**
     * 从token获取失效时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取jwt的接收者
     * @param token
     * @return
     */
    public String getAudienceFromToken(String token){
        return getClaimFromToken(token).getAudience();
    }

    /**
     * 获取使用的jwt claim
     * @param token
     * @param key
     * @return
     */
    public String getPrivateClaimFromToken(String token, String key){
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取md5 key
     * @param token
     * @return
     */
    public String getMd5KeyFromToken(String token){
        return getPrivateClaimFromToken(token, md5Key);
    }

    /**
     * 获取jwt的payload部分
     * @param token
     * @return
     */
    public Claims getClaimFromToken(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 验证token是否过期，true为过期，false为没过期
     * @param token
     * @return
     */
    public Boolean isTokenExpired(String token){
        final Date expriation = getExpirationDateFromToken(token);
        String userName = getUsernameFromToken(token);
        String redisToken = redisRepository.get("user_auth_token_" + userName);
        return expriation.before(new Date()) && token.equals(redisToken);
    }

    /**
     * 解析token是否正确，不正确抛出异常
     * @param token
     * @throws JwtException
     */
    public void parseToken(String token) throws JwtException {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 生成token（通过用户名和签名时候用的随机数）
     * @param userDetails
     * @param randomKey
     * @return
     */
    public String generateToken(UserDetails userDetails, String randomKey){
        Map<String, Object> claims = new HashMap<>();
        claims.put(md5Key, randomKey);
        String token = doGenerateToken(claims, userDetails);
        //将生成的token存入redis做唯一性校验
        redisRepository.setExpire("user_auth_token_" + userDetails.getUsername(), token, expiration);
        redisRepository.setExpire("user_auth_info_" + userDetails.getUsername(), JSON.toJSONString(userDetails), expiration );
        return token;
    }

    /**
     * 生成token
     * @param claims
     * @param userDetails
     * @return
     */
    private String doGenerateToken(Map<String, Object> claims, UserDetails userDetails){
        final Date createDate = new Date();
        final Date expirationDate = new Date(createDate.getTime() + expiration*1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(createDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 失效token
     * @param token
     */
    public void deleteToken(String token){
        String userName = getUsernameFromToken(token);
        redisRepository.del("user_auth_token_" + userName);
        redisRepository.del("user_auth_info_" + userName);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public User getUserDetails(String token){
        String userName = getUsernameFromToken(token);
        String user = redisRepository.get("user_auth_info_" + userName);
        return JSON.toJavaObject(JSON.parseObject(user), User.class);
    };



    public static String getTokenTypeBearer() {
        return TOKEN_TYPE_BEARER;
    }
}
