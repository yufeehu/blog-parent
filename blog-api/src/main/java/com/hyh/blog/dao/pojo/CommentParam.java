package com.hyh.blog.dao.pojo;

import lombok.Data;

/**
 * @author huyuhui
 * 新增评论的参数
 */
@Data
public class CommentParam {

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论id
     */
    private Long parent;

    /**
     * 被评论用户id
     */
    private Long toUserId;
}
