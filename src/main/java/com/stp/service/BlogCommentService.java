package com.stp.service;

import com.stp.domain.BlogComment;

import java.util.List;

public interface BlogCommentService {
    // 添加评论
    void addComment(BlogComment blogComment);

    // 根据博客ID获取评论
    List<BlogComment> getCommentsByBlogId(long blogId);

    // 根据用户ID获取评论
    List<BlogComment> getCommentsByUserId(long userId);

    // 删除评论
    void deleteComment(int commentId);
}
