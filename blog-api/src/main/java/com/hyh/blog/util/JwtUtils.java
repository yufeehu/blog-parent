package com.hyh.blog.util;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huyuhui
 */

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
    /**
     * 密钥
     */
    private String secret;
    /**
     * 过期时间
     */
    private long expire;

    /**
     * 生成token
     * @param userId
     * @return
     */
    public String createToken(Long userId) {
        Date secretTime = new Date(System.currentTimeMillis()+expire*1000);
        Map<String,Object> head = new HashMap<>();
        Map<String,Object> claims = new HashMap<>();
        head.put("type","JWT");
        head.put("alg","HS256");
        claims.put("userId",userId);
        claims.put("secret",secretTime);
        return Jwts.builder()
                //头部信息
                .setHeaderParams(head)
                //签发算法
                .signWith(SignatureAlgorithm.HS256,secret)
                //body数据，存放信息，比如，用户id，过期时间等;可以被解密，不能存放敏感信息
                .setClaims(claims)
                //设置签发时间
                .setIssuedAt(new Date())
                //过期时间
                .setExpiration(secretTime)
                .compact();

    }
    public Map<String, Object> checkToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
