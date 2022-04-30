package com.hyh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyh.blog.dao.mapper.ArticleBodyMapper;
import com.hyh.blog.dao.mapper.ArticleMapper;
import com.hyh.blog.dao.pojo.Article;
import com.hyh.blog.dao.pojo.ArticleBody;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.dos.Archives;
import com.hyh.blog.service.ArticleService;
import com.hyh.blog.service.CategoryService;
import com.hyh.blog.service.SysUserService;
import com.hyh.blog.service.TagService;
import com.hyh.blog.vo.*;
import com.hyh.blog.vo.param.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huyuhui
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private TagService tagService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private ArticleBodyMapper articleBodyMapper;

    @Resource
    private CategoryService categoryService;

    @Override
    public Result listArticlePage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPageNum(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Article::getCreateDate, Article::getWeight);
        //以时间和权重(是否置顶)进行排序
        Page<Article> articlePage = articleMapper.selectPage(page, wrapper);
        List<Article> records = articlePage.getRecords();
        //处理从数据库查询到结果，将Article实体类的数据转化为前端所需要ArticleVo实体类，这里需要自定义copyList方法
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return Result.success(articleVoList);
    }

    @Override
    public Result getHotArticles(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper
            .select(Article::getId,Article::getTitle)
            .orderByDesc(Article::getViewCounts)
            .last("limit "+limit);
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result getNewArticles(int limit) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .select(Article::getId,Article::getTitle)
                .orderByDesc(Article::getCreateDate)
                .last("limit "+limit);
        List<Article> articles = articleMapper.selectList(wrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. 根据id查询 文章信息
         * 2. 根据bodyId和categoryid 去做关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article,true,true,true,true);
        return Result.success(articleVo);
    }

    /**
     * @param records
     * @return
     */
    private List<ArticleVo> copyList(List<Article> records, Boolean isTag, Boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, Boolean isTag, Boolean isAuthor,Boolean isBody,Boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }

    /**
     * Article实体类属性值复制到ArticleVo实体类
     *
     * @param article 数据库查询到的文章结果集
     * @return
     */
    private ArticleVo copy(Article article, Boolean isTag, Boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        //将Article和ArticleVo相同的属性赋值到ArticleVo中(类型和属性名一致才可以)
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        if(isTag){
            Long id = article.getId();
            List<TagVo> tagVo = tagService.findTagByArticleId(id);
            articleVo.setTags(tagVo);
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            //根据article表中的作者id去查询sysUser表的作者昵称
            SysUser sysUser = sysUserService.findUserById(authorId);
            articleVo.setAuthor(sysUser.getNickname());
        }
        return articleVo;
    }


    /**
     *
     * @param article
     * @param isTag 是否显示标文章签
     * @param isAuthor 是否显示文章作者
     * @param isBody 是否显示文章内容
     * @param isCategory 是否显示文章类型
     * @return
     */
    private ArticleVo copy(Article article, Boolean isTag, Boolean isAuthor,Boolean isBody,Boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        //将Article和ArticleVo相同的属性赋值到ArticleVo中(类型和属性名一致才可以)
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        if(isTag){
            Long id = article.getId();
            List<TagVo> tagVo = tagService.findTagByArticleId(id);
            articleVo.setTags(tagVo);
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            //根据article表中的作者id去查询sysUser表的作者昵称
            SysUser sysUser = sysUserService.findUserById(authorId);
            articleVo.setAuthor(sysUser.getNickname());
        }
        if(isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if(isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategorys(findCategoryById(categoryId));
        }
        return articleVo;
    }

    private CategoryVo findCategoryById(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
