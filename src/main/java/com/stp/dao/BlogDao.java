package com.stp.dao;

import com.stp.domain.Blog;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface BlogDao {
    // 插入博客
    @Insert("INSERT INTO blog (title, content, author_id, create_time, image_url) VALUES (#{title}, #{content}, #{authorId}, NOW(),#{imageUrl})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @ResultMap(value = "BlogMap")
    void addBlog(Blog blog);

    //查询作者名称
    @Select("SELECT b.id, b.title, b.content, b.create_time, b.image_url, u.username AS authorName " +
            "FROM blog b " +
            "JOIN users u ON b.author_id = u.id " +
            "WHERE b.id = #{blogId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "authorName", column = "authorName")
    })
    Blog getBlogWithAuthorName(@Param("blogId") Integer blogId);

    // 查询所有已发布博客
    @Select("SELECT * FROM blog WHERE status = 'published' ORDER BY create_time DESC")
    @ResultMap(value = "BlogMap")
    List<Blog> findAllPublished();

    // 通过 ID 查询博客
    @ResultMap(value = "BlogMap")
    @Select("SELECT b.id,b.title, b.content, b.author_id, b.create_time, b.update_time, b.status, b.image_url ,u.username AS authorName " +
            "FROM blog b JOIN users u ON author_id = u.id WHERE b.id = #{id}")
    Blog getBlogById(Long id);

    // 通过作者 ID 查询博客
    @Select("SELECT * FROM blog WHERE author_id = #{authorId} ORDER BY create_time DESC")
    @ResultMap(value = "BlogMap")
    List<Blog> findByAuthorId(Integer authorId);

    // 更新博客
    @Update("UPDATE blog SET title = #{title}, content = #{content}, update_time = NOW(), status = #{status} WHERE id = #{id}")
    int updateBlog(Blog blog);

    // 删除博客（逻辑删除）
    @Update("UPDATE blog SET status = 'deleted' WHERE id = #{id}")
    int deleteBlog(Integer id);

    @Select("SELECT b.id, b.title, b.content, b.image_url, u.username AS authorName " +
            "FROM blog b JOIN users u ON b.author_id = u.id " +
            "WHERE b.status = 'published' ORDER BY b.create_time DESC")
    @Results(id = "BlogMap",value = {
            @Result(property = "imageUrl", column = "image_url"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "authorName", column = "authorName"),
            @Result(property = "id",column = "id"),
            @Result(property = "title",column = "title")
    })
    List<Blog> getBlogList();



    //获取博客总数
    @Select("SELECT COUNT(*) FROM blog")
    int getAllBlogCount();

    @Select("SELECT b.id,b.title, b.content, b.author_id, b.create_time, b.update_time, b.status, b.image_url ,u.username AS authorName " +
            "FROM blog b JOIN users u ON author_id = u.id" +
            " LIMIT #{offset}, #{size}")
    @ResultMap(value = "BlogMap")
    List<Blog> getALLBlogCard();


    @Select("SELECT b.id,b.title, b.content, b.author_id, b.create_time, b.update_time, b.status, b.image_url ,u.username AS authorName " +
            " FROM blog b JOIN users u ON author_id = u.id" +
            " WHERE status = 'published' ORDER BY create_time DESC LIMIT #{size} OFFSET #{offset}")
    @ResultMap(value = "BlogMap")
    List<Blog> getPagedBlogCard(@Param("offset") int offset, @Param("size") int size);
}
