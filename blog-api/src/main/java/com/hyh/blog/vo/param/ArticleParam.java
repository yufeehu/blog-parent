package com.hyh.blog.vo.param;

import com.hyh.blog.vo.CategoryVo;
import com.hyh.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author huyuhui
 * 发布文章传入的参数
 */
@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;

}
