package com.hyh.blog.controller;

import com.hyh.blog.service.LoginService;
import com.hyh.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huyuhui
 */
@RestController
@RequestMapping("logout")
public class LogoutController {
    @Autowired
    private LoginService loginService;

    @RequestMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}
