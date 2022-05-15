package com.hyh.blog.dao.pojo;

import lombok.Data;

/**
 * @author huyuhui
 */
@Data
public class ArticleTag {

    private Long id;

    private Long articleId;

    private Long tagId;
}
