package com.hyh.blog.service;

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
}
