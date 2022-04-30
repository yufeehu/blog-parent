package com.hyh.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * @author huyuhui
 */
@Data
public class ArticleVo {
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
     * 创建时间
     */
    private String createDate;
    /**
     * 作者
     */
    private String author;

    /**
     * 内容
     */
    private ArticleBodyVo body;

    /**
     * 标签
     */
    private List<TagVo> tags;

    /**
     *类别id
     */
    private CategoryVo categorys;




}
