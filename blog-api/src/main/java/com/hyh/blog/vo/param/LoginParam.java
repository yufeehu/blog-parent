package com.hyh.blog.vo.param;

import lombok.Data;

/**
 * 存储前端获取的账户和密码
 * @author huyuhui
 */
@Data
public class LoginParam {
    private String account;

    private String password;

    private String nickname;
}
