package com.hyh.blog.vo;

import lombok.Data;

import java.util.List;

/**
 * 封装评论列表返回结果
 * @author huyuhui
 */
@Data
public class CommentVo {

    private Long id;

    /**
     * 作者信息
     */
    private UserVo author;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 子评论（下一层评论）
     */
    private List<CommentVo> childrens;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 评论层级
     */
    private Integer level;

    /**
     * 给谁的评论
     */
    private UserVo toUser;
}
