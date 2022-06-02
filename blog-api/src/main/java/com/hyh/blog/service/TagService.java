package com.hyh.blog.service;

import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagByArticleId(Long id);

    Result hotTags(int limit);

    Result findAll();

    Result findAllDetail();
}
