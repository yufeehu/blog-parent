package com.hyh.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hyh.blog.dao.pojo.Tag;

import java.util.List;

/**
 * @author huyuhui
 */
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章id 查询标签列表,一篇文章可能有多个标签
     * 多表查询 :根据article表中的id 查询 article_tag表中的tag_id,再通过tag_id去匹配tag表的信息
     * @param id 文章id
     * @return
     */
    List<Tag> findTagByArticleId(Long id);

    /**
     * 查询文章最多的标签，从大到小排序，限制前6
     * @param limit 限制最热标签数量
     * @return
     */
    List<Long> findHotTagsId(int limit);
}
