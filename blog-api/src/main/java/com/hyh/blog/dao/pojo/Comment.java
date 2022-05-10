package com.hyh.blog.dao.pojo;

import lombok.Data;

/**
 * @author huyuhui
 * 评论留言
 */
@Data
public class Comment {

    private Long id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Long createDate;

    /**
     * 评论的文章id
     */
    private Long articleId;

    /**
     * 评论的人的id
     */
    private Long authorId;

    /**
     * 盖楼功能，对评论的评论进行回复
     */
    private Long parentId;

    /**
     * 回复谁的评论
     */
    private Long toUid;

    /**
     * 评论的第几层
     */
    private Integer level;
}
