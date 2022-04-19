package com.hyh.blog.util;

import com.hyh.blog.dao.pojo.SysUser;

/**
 * @author huyuhui
 * 线程局部变量，是每一个线程所单独持有的，其他线程不能对其进行访问
 * 使用threadLocal存储用户信息
 */
public class UserThreadLocal {
    /**
     *  私有构造器，在其它类中无法声明UserThreadLocal的实例
     */
    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal();

    public static void set(SysUser user){
        LOCAL.set(user);
    }

    public static SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }

}
