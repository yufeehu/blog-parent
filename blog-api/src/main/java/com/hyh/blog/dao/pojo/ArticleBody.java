package com.hyh.blog.dao.pojo;

import lombok.Data;

/**
 * @author huyuhui
 * 文章详情
 */
@Data
public class ArticleBody {

    private Long id;
    /**
     * 文章内容(markDown)
     */
    private String content;
    /**
     * 文章内容(html)
     */
    private String contentHtml;
    /**
     * 对应文章ID
     */
    private Long articleId;
}
