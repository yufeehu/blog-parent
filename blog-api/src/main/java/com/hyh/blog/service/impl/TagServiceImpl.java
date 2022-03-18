package com.hyh.blog.service.impl;

import com.hyh.blog.dao.mapper.TagMapper;
import com.hyh.blog.dao.pojo.Tag;
import com.hyh.blog.service.TagService;
import com.hyh.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huyuhui
 */
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagByArticleId(Long id) {
        List<Tag> tagList = tagMapper.findTagByArticleId(id);
        List<TagVo> tagVoList =copyList(tagList);
        return tagVoList;
    }

    private List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }
}
