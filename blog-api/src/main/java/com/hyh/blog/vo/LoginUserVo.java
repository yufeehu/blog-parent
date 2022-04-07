package com.hyh.blog.vo;

import lombok.Data;

/**
 * 封装登录成功后获取的用户信息
 * @author huyuhui
 */
@Data
public class LoginUserVo {

    private Long id;

    private String account;

    private String nickname;

    private String avatar;
}
