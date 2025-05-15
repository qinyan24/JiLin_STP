package com.stp.service.impl;

import com.stp.dao.BlogCommentDao;
import com.stp.domain.BlogComment;
import com.stp.service.BlogCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {
    @Autowired
    private BlogCommentDao blogCommentDao;


    @Override
    public void addComment(BlogComment blogComment) {
        if (blogComment.getStatus() == null) {
            blogComment.setStatus("public"); // 默认状态
        }
        blogCommentDao.addComment(blogComment);
    }

    @Override
    public List<BlogComment> getCommentsByBlogId(long blogId) {
        return blogCommentDao.getCommentsByBlogId(blogId);
    }

    @Override
    public List<BlogComment> getCommentsByUserId(long userId) {
        return blogCommentDao.getCommentsByUserId(userId);
    }

    @Override
    public void deleteComment(int commentId) {
        blogCommentDao.deleteComment(commentId);
    }

}
