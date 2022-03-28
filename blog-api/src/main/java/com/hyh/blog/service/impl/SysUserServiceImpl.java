package com.hyh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyh.blog.dao.mapper.SysUserMapper;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author huyuhui
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

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
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper();
        wrapper
                .eq(SysUser::getAccount,account)
                .eq(SysUser::getPassword,password)
                //查询用户账号,id,是否管理员,昵称
                .select(SysUser::getAccount,SysUser::getId,SysUser::getAdmin, SysUser::getNickname)
                .last("limit 1");
        return null;
    }
}
