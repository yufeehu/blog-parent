package com.hyh.blog.service;

import com.hyh.blog.vo.CategoryVo;
import com.hyh.blog.vo.Result;

/**
 * @author huyuhui
 */
public interface CategoryService {
    /**
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);

    /**
     * @return
     */
    Result findAll();

    /**
     * @return
     */
    Result findAllDetail();

    /**
     * @param id categoryId
     * @return
     */
    Result categoriesDetailById(Long id);
}
