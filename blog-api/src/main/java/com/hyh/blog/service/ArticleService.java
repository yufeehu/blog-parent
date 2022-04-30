package com.hyh.blog.service;

import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.param.PageParams;

/**
 * @author huyuhui
 */
public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams 分页参数(pageNum,pageSize)
     * @return
     */
    Result listArticlePage(PageParams pageParams);

    /**
     * 最热门文章查询，根据浏览数
     * select id,title from article order by view_counts desc limit 5
     * @param limit 限制条数
     * @return
     */
    Result getHotArticles(int limit);

    /**
     * 最新文章查询
     * @param limit
     * @return
     */
    Result getNewArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查问文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);
}
