package com.stp.service.impl;

import com.stp.dao.CommentDao;
import com.stp.domain.Comment;
import com.stp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public void addComment(Comment comment) {
        // 调用 DAO 层的保存方法
        commentDao.insertComment(comment);
    }

    @Override
    public List<Comment> getCommentsBySpotId(Long spotId) {
        return commentDao.findCommentsBySpotId(spotId);
    }
}
