package com.stp.service;

import com.stp.domain.Blog;

import java.util.List;

public interface BlogService {
    void addBlog(Blog blog);
    List<Blog> getAllPublished();
    Blog getBlogById(Long id);
    List<Blog> getBlogsByAuthorId(Integer authorId);
    int updateBlog(Blog blog);
    int deleteBlog(Integer id);
    List<Blog> getBlogList();
    int getTotalBlogCount();
    List<Blog> getPagedBlogs(int page,int size);

    List<Blog> getALLBlogCard();

    Blog getBlogWithAuthorName(Integer blogId);
}
