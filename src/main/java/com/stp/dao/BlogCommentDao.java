package com.stp.dao;

import com.stp.domain.BlogComment;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface BlogCommentDao {
    //插入评论
    @Insert("INSERT INTO blog_comment (content, blog_id, user_id, create_time, update_time)" +
            "VALUES (#{content}, #{blog.id},#{user.id} ,NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
   @ResultMap(value = "BlogCommentMap")
    void addComment(BlogComment blogComment);

    // 根据博客ID获取评论
    @Select("SELECT b.id,b.content, b.create_time, b.update_time, b.status,u.username FROM blog_comment b JOIN users u ON b.user_id = u.id WHERE blog_id = #{blogId}")
    @Results(id = "BlogCommentMap",value = {
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "username", column = "username")  // 映射username字段
    })
    List<BlogComment> getCommentsByBlogId(long blogId);

    // 根据用户ID获取评论
    @Select("SELECT content, create_time, update_time, status, blog_id, user_id FROM blog_comment WHERE user_id = #{userId}")
    @ResultMap(value = "BlogCommentMap")
    List<BlogComment> getCommentsByUserId(long userId);

    // 删除评论
    @Update("UPDATE blog_comment SET status= 'deleted' WHERE id= #{commentId}")
    void deleteComment(int commentId);
}
