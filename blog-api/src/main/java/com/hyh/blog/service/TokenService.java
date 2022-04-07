package com.hyh.blog.service;

import com.hyh.blog.dao.pojo.SysUser;

/**
 * @author huyuhui
 */
public interface TokenService {
    /**
     * 校验token合法性
     * @param token
     * @return
     */
    SysUser checkToken(String token);
}
