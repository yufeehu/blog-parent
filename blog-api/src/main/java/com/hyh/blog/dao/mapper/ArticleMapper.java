package com.hyh.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyh.blog.dao.pojo.Article;
import com.hyh.blog.dos.Archives;

import java.util.List;

/**
 * @author huyuhui
 */
public interface ArticleMapper extends BaseMapper<Article> {


    /**
     * 根据年，月对文章进行归档
     * @return
     */
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month);
}
