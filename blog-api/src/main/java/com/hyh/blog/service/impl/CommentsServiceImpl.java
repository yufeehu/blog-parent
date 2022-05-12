package com.hyh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hyh.blog.dao.mapper.CommentsMapper;
import com.hyh.blog.dao.pojo.Comment;
import com.hyh.blog.dao.pojo.CommentParam;
import com.hyh.blog.dao.pojo.SysUser;
import com.hyh.blog.service.CommentsService;
import com.hyh.blog.service.SysUserService;
import com.hyh.blog.util.UserThreadLocal;
import com.hyh.blog.vo.CommentVo;
import com.hyh.blog.vo.Result;
import com.hyh.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huyuhui
 */
@Service
public class CommentsServiceImpl implements CommentsService {

    @Resource
    private CommentsMapper commentsMapper;

    @Resource
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long articleId) {
        /**
         * 1.根据文章id查询评论列表(查询 blog_comment表)
         * 2.根据作者id，查询作者信息
         * 3.判断 level是否等于1，如果等于1则去查询它有没有子评论
         * 4.如果有子评论，根据评论id 查询（select * from blog_comments where parent_id = id)
         */
        //先根据文章id查询所有层级为1的评论列表
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        //将查询结果重新封装
        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    @Override
    public Result insertComment(CommentParam param) {
        Comment comment = new Comment();
        SysUser user = UserThreadLocal.get();
        comment.setArticleId(param.getArticleId());
        comment.setAuthorId(user.getId());
        comment.setContent(param.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setParentId(param.getParent());
        comment.setToUid(param.getToUserId());
        Long parent = param.getParent();
        if(parent == null || parent == 0){
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = param.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        commentsMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        //先将相同属性直接复制
        BeanUtils.copyProperties(comment,commentVo);
        commentVo.setId(comment.getId());
        //根据作者id查询作者的信息,存入导CommentVo的author属性
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //获取子评论
        Integer level = comment.getLevel();
        if(1 == level){
            Long id  = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            commentVo.setChildrens(commentVoList);
        }
        //给谁评论
        if(level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        return copyList(comments);
    }
}
