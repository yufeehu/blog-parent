package com.hyh.blog.controller;

import com.hyh.blog.dao.pojo.CommentParam;
import com.hyh.blog.service.CommentsService;
import com.hyh.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author huyuhui
 */
@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId){
        return commentsService.commentsByArticleId(articleId);
    }

    @PostMapping("create/change")
    public Result insetComment(@RequestBody CommentParam param){
        return commentsService.insertComment(param);
    }

}
