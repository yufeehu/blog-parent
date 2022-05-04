package com.hyh.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hyh.blog.dao.mapper.ArticleMapper;
import com.hyh.blog.dao.pojo.Article;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author huyuhui
 */
@Component
public class ThreadService {
    @Async("taskExecutor")
    public void updateViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        Article updateArtice = new Article();
        updateArtice.setViewCounts(viewCounts+1);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        //根据id更新
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个为了在多线程的环境下线程安全
        //改之前再确认这个值有没有被其他线程抢先修改，类似于CAS操作 cas加自旋，加个循环就是cas
        updateWrapper.eq(Article::getViewCounts,viewCounts);
        // update article set view_count=100 where view_count=99 and id =111
        //实体类加更新条件
        articleMapper.update(updateArtice,updateWrapper);

        try {
            Thread.sleep(5000);
            System.out.println("更新完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
