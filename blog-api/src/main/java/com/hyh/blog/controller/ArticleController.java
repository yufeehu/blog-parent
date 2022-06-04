package com.hyh.blog.controller;

import com.hyh.blog.common.aop.LogAnnotation;
import com.hyh.blog.service.ArticleService;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.param.ArticleParam;
import com.hyh.blog.vo.param.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huyuhui
 */
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    //自定义注解，标识需要记录的日志操作
    @LogAnnotation(module = "文章",operation = "获取文章列表")
    public Result articles(@RequestBody PageParams pageParams){
        return articleService.listArticlePage(pageParams);
    }

    @PostMapping("hot")
    public Result hotArticles(){
        //最热门的文章，限制前5条
        int limit = 5;
        return articleService.getHotArticles(limit);
    }

    @PostMapping("new")
    public Result newArticles(){
        //最新的文章，限制前5条
        int limit = 5;
        return articleService.getNewArticles(limit);
    }

    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 未登录时不能发布文章，需要在拦截器添加该路径
     * @param articleParam
     * @return
     */
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
