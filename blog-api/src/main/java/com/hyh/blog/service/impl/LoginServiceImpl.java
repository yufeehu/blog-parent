package com.hyh.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.service.LoginService;
import com.hyh.blog.service.SysUserService;
import com.hyh.blog.util.JwtUtils;
import com.hyh.blog.vo.ErrorCode;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.param.LoginParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author huyuhui
 */
@Slf4j
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
        log.info("加密前******==>{},{}",account,password);
        if(StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String secretPwd = DigestUtils.md5Hex(password + salt);
        log.info("用户名：{},密码：{}",account,secretPwd);
        SysUser user = sysUserService.findUser(account, secretPwd);
        if(user == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //登录成功，使用JWT生成token，将token和用户信息缓存到redis中,返回token
        String token = jwtUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        log.info("token====>{}",token);
        redisTemplate.delete("TOKEN_"+token);
        return Result.success("退出登录成功！");
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断账号是否存在，如果存在则返回账号已被注册
         * 3.账号不存在，则注册账号
         * 4.生成token
         * 5.token存入redis,并返回token
         * 6.加上事务，以上过程中间出现问题，注册用户都要回滚
         */
        String account = loginParam.getAccount();
        String nickname = loginParam.getNickname();
        String password = loginParam.getPassword();
        if(StringUtils.isBlank(account)
                || StringUtils.isBlank(nickname)
                || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser judgeUser = sysUserService.findUserByAccount(account);
        if(judgeUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),"账号已经被注册！");
        }
        SysUser user = new SysUser();
        user.setAccount(account);
        user.setNickname(nickname);
        user.setPassword(DigestUtils.md5Hex(password+salt));
        user.setCreateDate(System.currentTimeMillis());
        user.setLastLogin(System.currentTimeMillis());
        user.setAvatar("/static/img/logo.b3a48c0.png");
        user.setAdmin(1);
        user.setDeleted(0);
        user.setSalt(salt);
        user.setStatus("");
        user.setEmail("");
        sysUserService.saveUser(user);
        String token = jwtUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(user),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
