package com.hyh.blog;

import com.hyh.blog.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;

@SpringBootTest
class BlogApiApplicationTests {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    void JwtTest(){
        String token = jwtUtils.createToken(10L);
        System.out.println(token);
        Map<String,Object> claims = jwtUtils.checkToken(token);
        System.out.println(claims.get("userId"));
        System.out.println(claims.get("secret"));
    }

}
