package com.hyh.blog.service;

import com.hyh.blog.dao.pojo.SysUser;

public interface SysUserService {

    SysUser findUserById(Long authorId);
}
