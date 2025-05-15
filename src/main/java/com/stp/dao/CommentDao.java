package com.stp.dao;

import com.stp.domain.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    // 获取某个景点的所有评论
//    @Select("SELECT * FROM comments WHERE spot_id = #{spotId}")
    @Select("select c.id,c.content,c.created_at,u.username from comments c join users u on c.user_id = u.id where spot_id = #{spotId}")
    @Result(property = "createdAt", column = "created_at")
    List<Comment> findCommentsBySpotId(Long spotId);

    // 插入评论
    @Insert("INSERT INTO comments (content, spot_id, created_at,updated_at) VALUES (#{content}, #{spotId}, NOW(),NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id") // 使用自增主键
//    @Results(id = "commentsMap",value = {
//            @Result(property = "createdAt", column = "created_at")
////            @Result(property = "updateAt", column = "update_at")
//    })
    int insertComment(Comment comment);

    // 删除评论
    @Delete("DELETE FROM comments WHERE id = #{id}")
    int deleteComment(Long id);
}
