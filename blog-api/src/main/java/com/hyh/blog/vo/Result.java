package com.hyh.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 结果统一封装类
 * @author huyuhui
 */
@Data
@AllArgsConstructor
public class Result {
    private boolean success;

    private int code;

    private String msg;

    private Object data;

    /**
     * 返回成功的结果
     * @param data
     * @return
     */
    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }

    /**
     * 返回失败的结果
     * @param code
     * @param msg
     * @return
     */
    public static Result fail(int code,String msg){
        return new Result(false,code,msg,null);
    }
}
