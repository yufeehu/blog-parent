package com.hyh.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author huyuhui
 */
@Data
public class ArticleVo {
    /**
     * 分布式id，前端可能解析不了，出现精度损失，这里先转化为string
     */
    @JsonSerialize(using = ToStringSerializer.class)
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
    private Integer commentCounts;

    /**
     * 阅读数量
     */
    private Integer viewCounts;

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
