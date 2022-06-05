package com.hyh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyh.blog.dao.mapper.ArticleBodyMapper;
import com.hyh.blog.dao.mapper.ArticleMapper;
import com.hyh.blog.dao.mapper.ArticleTagMapper;
import com.hyh.blog.dao.pojo.Article;
import com.hyh.blog.dao.pojo.ArticleBody;
import com.hyh.blog.dao.pojo.ArticleTag;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.dos.Archives;
import com.hyh.blog.service.*;
import com.hyh.blog.util.UserThreadLocal;
import com.hyh.blog.vo.*;
import com.hyh.blog.vo.param.ArticleParam;
import com.hyh.blog.vo.param.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Resource
    private ThreadService threadService;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticlePage(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPageNum(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
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
         * 2.根据bodyId和categoryid 去做关联查询
         * 3.阅读数量更新
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article,true,true,true,true);
        threadService.updateViewCount(articleMapper,article);
        return Result.success(articleVo);
    }


    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        //获取当前用户信息
        SysUser user = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(user.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        //文章插入之后，MP在数据库中存入一个自增的id,同时会自动返回id到实体类中
        this.articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        //标签
        if(tags != null){
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        //内容
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        //内容插入完成后生成id
        article.setBodyId(articleBody.getId());
        //这里新增了内容id，需要再重新更新文章
        articleMapper.updateById(article);
        Map<String, Object> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
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
