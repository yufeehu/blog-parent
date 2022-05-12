package com.hyh.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * 封装评论列表返回结果
 * @author huyuhui
 */
@Data
public class CommentVo {

    /**
     * 分布式id，前端可能解析不了，出现精度损失，这里先转化为string
     */
    @JsonSerialize(using = ToStringSerializer.class)
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
