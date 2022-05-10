package com.hyh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyh.blog.dao.mapper.SysUserMapper;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.service.SysUserService;
import com.hyh.blog.service.TokenService;
import com.hyh.blog.vo.ErrorCode;
import com.hyh.blog.vo.LoginUserVo;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author huyuhui
 * Transactional 开启事务
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private TokenService tokenService;

    @Override
    public UserVo findUserVoById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        //如果查询不到该文章的作者信息,默认佚名
        if(Objects.isNull(sysUser)){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("佚名");
        }
        UserVo userVo = new UserVo();
        userVo.setAvatar(sysUser.getAvatar());
        userVo.setNickname(sysUser.getNickname());
        userVo.setId(sysUser.getId());
        return userVo;
    }

    @Override
    public SysUser findUserById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        //如果查询不到该文章的作者信息,默认佚名
        if(Objects.isNull(sysUser)){
            sysUser = new SysUser();
            sysUser.setNickname("佚名");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(SysUser::getAccount,account)
                .eq(SysUser::getPassword,password)
                //查询用户账号,id,是否管理员,昵称
                .select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname)
                .last("limit 1");
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * token合法性校验
         * 1、是否为空 ，解析是否成功，redis是否存在
         * 2、如果校验失败，返回错误
         * 3、如果成功，返回对应结果 LoginUserVo
         */
        SysUser user = tokenService.checkToken(token);
        if(Objects.isNull(user)){
            return Result.fail(ErrorCode.TOKEN_ILLEGAL.getCode(),ErrorCode.TOKEN_ILLEGAL.getMsg());
        }
        LoginUserVo userVo = new LoginUserVo();
        userVo.setId(user.getId());
        userVo.setAccount(user.getAccount());
        userVo.setAvatar(user.getAvatar());
        userVo.setNickname(user.getNickname());
        return Result.success(userVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getAccount,account)
                .last("limit 1");
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public void saveUser(SysUser user) {
        sysUserMapper.insert(user);
    }
}
