package com.hyh.blog.service;

import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.param.LoginParam;

/**
 * @author huyuhui
 */
public interface LoginService {


    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 退出登录
     * @param token
     * @return
     */
    Result logout(String token);
}
