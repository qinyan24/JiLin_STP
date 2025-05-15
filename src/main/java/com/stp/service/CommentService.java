package com.stp.service;

import com.stp.domain.Comment;

import java.util.List;

public interface CommentService {
    // 添加评论
    void addComment(Comment comment);

    // 获取景点的所有评论
    List<Comment> getCommentsBySpotId(Long spotId);
}
