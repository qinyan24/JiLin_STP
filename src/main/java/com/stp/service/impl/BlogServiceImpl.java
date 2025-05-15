package com.stp.service.impl;

import com.stp.dao.BlogDao;
import com.stp.domain.Blog;
import com.stp.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Override
    public void addBlog(Blog blog) {
       blogDao.addBlog(blog);
    }

    @Override
    public List<Blog> getAllPublished() {
        return blogDao.findAllPublished();
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogDao.getBlogById(id);
    }

    @Override
    public List<Blog> getBlogsByAuthorId(Integer authorId) {
        return blogDao.findByAuthorId(authorId);
    }

    @Override
    public int updateBlog(Blog blog) {
        return blogDao.updateBlog(blog);
    }

    @Override
    public int deleteBlog(Integer id) {
        return blogDao.deleteBlog(id);
    }

    @Override
    public List<Blog> getBlogList() {
        return blogDao.getBlogList();
    }

    @Override
    public List<Blog> getPagedBlogs(int page, int size) {
        int offset = page * size;
        return blogDao.getPagedBlogCard(offset, size);
    }

    @Override
    public int getTotalBlogCount() {
        return blogDao.getAllBlogCount();
    }

    @Override
    public List<Blog> getALLBlogCard(){
        return  blogDao.getALLBlogCard();
    }

    @Override
    public Blog getBlogWithAuthorName(Integer blogId) {
        return blogDao.getBlogWithAuthorName(blogId);
    }


}
