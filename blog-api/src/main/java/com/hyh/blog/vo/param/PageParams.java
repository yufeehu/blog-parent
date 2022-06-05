package com.hyh.blog.vo.param;

import lombok.Data;

/**
 * 与前端交互的分页的参数
 * @author huyuhui
 */
@Data
public class PageParams {
    /**
     * 当前页数
     */
    private int pageNum = 1;

    /**
     * 每页条数
     */
    private int pageSize = 10;

    /**
     * 文章分类ID
     */
    private Long categoryId;


    /**
     * 文章标签ID
     */
    private Long tagId;

    private String year;

    private String month;

    public String getMonth(){
        if(this.month != null && this.month.length() == 1){
            return "0"+this.month;
        }else{
            return this.month;
        }
    }
}
