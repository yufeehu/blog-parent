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
}
