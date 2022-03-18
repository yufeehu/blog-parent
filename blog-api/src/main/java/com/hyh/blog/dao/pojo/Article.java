package com.hyh.blog.dao.pojo;

import lombok.Data;

/**
 * @author huyuhui
 */
@Data
public class Article {
    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;
    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String summary;

    /**
     * 评论数量
     */
    private int commentCounts;

    /**
     * 阅读数量
     */
    private int viewCounts;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 内容id
     */
    private Long bodyId;

    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private int weight = Article_Common;

    /**
     * 创建时间
     */
    private Long createDate;
}
