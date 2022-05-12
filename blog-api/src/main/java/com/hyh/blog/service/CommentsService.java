package com.hyh.blog.service;

import com.hyh.blog.dao.pojo.CommentParam;
import com.hyh.blog.vo.Result;

/**
 * @author huyuhui
 */
public interface CommentsService {
    /**
     * 根据文章id查询评论列表
     * @param articleId 文章id
     * @return
     */
    Result commentsByArticleId(Long articleId);

    /**
     * 添加评论
     * @param param 评论的参数
     * @return
     */
    Result insertComment(CommentParam param);
}
