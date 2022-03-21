package com.hyh.blog.controller;

import com.hyh.blog.service.ArticleService;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.param.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huyuhui
 */
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result articles(PageParams pageParams){
        return articleService.listArticlePage(pageParams);
    }

    @PostMapping("hot")
    public Result hotArticles(){
        //最热门的文章，限制前5条
        int limit = 5;
        return articleService.getHotArticles(limit);
    }
}
