package com.hyh.blog.service;

import com.hyh.blog.vo.CategoryVo;

/**
 * @author huyuhui
 */
public interface CategoryService {
    /**
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);
}
