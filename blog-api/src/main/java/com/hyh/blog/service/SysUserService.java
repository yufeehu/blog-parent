package com.hyh.blog.service;

import com.hyh.blog.dao.pojo.SysUser;

/**
 * @author huyuhui
 */
public interface SysUserService {
    /**
     * 根据作者id查询作者信息
     * @param authorId
     * @return
     */
    SysUser findUserById(Long authorId);

    /**
     * 查询用户名和密码
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);
}
