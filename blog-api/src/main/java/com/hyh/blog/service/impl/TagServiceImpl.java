package com.hyh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyh.blog.dao.mapper.TagMapper;
import com.hyh.blog.dao.pojo.Tag;
import com.hyh.blog.service.TagService;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.TagVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author huyuhui
 */
@Slf4j
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

    @Override
    public Result hotTags(int limit) {
        List<Long> tagsId = tagMapper.findHotTagsId(limit);
        //如果标签为空，则返回一个空集合
        if(tagsId.isEmpty()){
            return Result.success(Collections.emptyList());
        }
        List<Tag> tags = tagMapper.selectBatchIds(tagsId);
//        log.info("tagsId=>{},{}",tagsId,tags);
        return Result.success(tags);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(wrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = this.tagMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(tags);
    }

    @Override
    public Result tagsDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
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
