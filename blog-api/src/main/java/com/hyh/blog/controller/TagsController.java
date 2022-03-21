package com.hyh.blog.controller;

import com.hyh.blog.service.TagService;
import com.hyh.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huyuhui
 */
@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("hot")
    public Result getHotTags(){
        //这里写死了，限制查询最热门的6个标签
        int limit = 6;
        return tagService.hotTags(limit);
    }

}
