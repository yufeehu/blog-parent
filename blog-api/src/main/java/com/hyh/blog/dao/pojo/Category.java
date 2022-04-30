package com.hyh.blog.dao.pojo;

import lombok.Data;

/**
 * @author huyuhui
 * 文章分类
 */
@Data
public class Category {

    private Long id;

    private String avatar;

    private String categoryName;

    private String description;
}
