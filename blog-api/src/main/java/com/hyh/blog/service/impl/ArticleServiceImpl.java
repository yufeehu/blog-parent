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
         * 1. ??????id?????? ????????????
         * 2.??????bodyId???categoryid ??????????????????
         * 3.??????????????????
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
         * 1. ???????????? ?????? ??????Article??????
         * 2. ??????id  ?????????????????????
         * 3. ??????  ????????????????????? ??????????????????
         * 4. body ???????????? article bodyId
         */
        //????????????????????????
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
        //?????????????????????MP????????????????????????????????????id,?????????????????????id???????????????
        this.articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        //??????
        if(tags != null){
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        //??????
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);
        //???????????????????????????id
        article.setBodyId(articleBody.getId());
        //?????????????????????id??????????????????????????????
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
     * Article???????????????????????????ArticleVo?????????
     *
     * @param article ????????????????????????????????????
     * @return
     */
    private ArticleVo copy(Article article, Boolean isTag, Boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        //???Article???ArticleVo????????????????????????ArticleVo???(?????????????????????????????????)
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        if(isTag){
            Long id = article.getId();
            List<TagVo> tagVo = tagService.findTagByArticleId(id);
            articleVo.setTags(tagVo);
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            //??????article???????????????id?????????sysUser??????????????????
            SysUser sysUser = sysUserService.findUserById(authorId);
            articleVo.setAuthor(sysUser.getNickname());
        }
        return articleVo;
    }


    /**
     *
     * @param article
     * @param isTag ????????????????????????
     * @param isAuthor ????????????????????????
     * @param isBody ????????????????????????
     * @param isCategory ????????????????????????
     * @return
     */
    private ArticleVo copy(Article article, Boolean isTag, Boolean isAuthor,Boolean isBody,Boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        //???Article???ArticleVo????????????????????????ArticleVo???(?????????????????????????????????)
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        if(isTag){
            Long id = article.getId();
            List<TagVo> tagVo = tagService.findTagByArticleId(id);
            articleVo.setTags(tagVo);
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            //??????article???????????????id?????????sysUser??????????????????
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
