package com.hyh.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.service.TokenService;
import com.hyh.blog.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author huyuhui
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Override
    public SysUser checkToken(String token) {
        log.info("token=========>{}",token);
        //如果token为空，则返回null
        if(StringUtils.isBlank(token)){
            return null;
        }
        //解析token失败
        Map<String, Object> map = jwtUtils.checkToken(token);
        if(map == null){
            return null;
        }
        //redis是否存在token
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        //如果校验成功，返回用户信息
        SysUser user = JSON.parseObject(userJson,SysUser.class);
        log.info("user=========>{}",user);
        return user;
    }
}
