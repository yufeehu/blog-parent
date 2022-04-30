package com.hyh.blog.dos;

import lombok.Data;

/**
 * @author huyuhui
 * 归档
 */
@Data
public class Archives {

    private Integer year;

    private Integer month;

    /**
     * 文章数量
     */
    private Long count;
}
