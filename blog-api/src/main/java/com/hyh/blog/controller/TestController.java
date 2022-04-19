package com.hyh.blog.controller;

import com.hyh.blog.util.UserThreadLocal;
import com.hyh.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huyuhui
 * 测试拦截器
 */
@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        System.out.println(UserThreadLocal.get());
        return Result.success("success");
    }
}
