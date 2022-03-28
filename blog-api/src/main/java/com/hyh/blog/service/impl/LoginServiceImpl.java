package com.hyh.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.service.LoginService;
import com.hyh.blog.service.SysUserService;
import com.hyh.blog.util.JwtUtils;
import com.hyh.blog.vo.ErrorCode;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.param.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author huyuhui
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private SysUserService sysUserService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    private final String salt = "hyhblog!@#";

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询 是否存在
         * 3. 如果不存在 登录失败
         * 4. 如果存在 ，使用jwt 生成token 返回给前端
         * 5. token放入redis当中，redis  token：user信息 设置过期时间
         *  (登录认证的时候 先认证token字符串是否合法，去redis认证是否存在)
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if(StringUtils.isNotBlank(account) || StringUtils.isNotBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String secretPwd = DigestUtils.md5Hex(password + salt);
        SysUser user = sysUserService.findUser(account, secretPwd);
        if(user == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //登录成功，使用JWT生成token，将token和用户信息缓存到redis中,返回token
        String token = jwtUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }
}
