package com.stp.controller;

import com.stp.dao.BlogCommentDao;
import com.stp.dao.BlogDao;
import com.stp.domain.*;
import com.stp.service.BlogCommentService;
import com.stp.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BlogController {

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogCommentService blogCommentService;

    @Autowired
    private BlogCommentDao blogCommentDao;

    //获取分页的博客列表

    @PostMapping("/getBlogCard")
    @ResponseBody
    public Map<String, Object> getBlogCard(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "3") int size) {

        // 计算分页偏移量
        int offset = (page - 1) * size;

        // 获取博客列表
        List<Blog> blogs = blogDao.getPagedBlogCard(offset, size);

//        System.out.println(blogs.toString());
        // 获取博客总数
        int totalRecords = blogDao.getAllBlogCount();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // 返回数据封装
        Map<String, Object> response = new HashMap<>();
        response.put("blogs", blogs);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);

        return response;
    }

    // 根据博客 ID 动态生成页面
    @RequestMapping("/blog/{id}")
    public String getBlog(@PathVariable("id") Long id, Model model) {
        // 获取景点数据
        Blog blog = blogService.getBlogById(id);

        if (blog == null) {
            System.out.println("Error: Blog with ID " + id + " not found!");
            return "error";  // 如果找不到景点，则返回错误页面
        }

        // 获取评论数据
        List<BlogComment> comments = blogCommentService.getCommentsByBlogId(id);

        // 将数据传递给前端
        model.addAttribute("blog", blog);
        model.addAttribute("comments", comments);

        // 返回景点详情页面
        return "blog";  // 返回 blog.html 页面
    }

    //博客详情页评论获取
    @RequestMapping("/blog/{blogId}/comments")
    @ResponseBody
    public List<BlogComment> getCommentsByBlogId(@PathVariable("blogId") Long blogId){
        List<BlogComment> blogComment = blogCommentDao.getCommentsByBlogId(blogId);
//        logger.info(blogComment.toString());
        return blogCommentDao.getCommentsByBlogId(blogId);
    }

    //博客详情页提交评论
    @PostMapping("/submit-blogComment")
    @ResponseBody
    public String submitComment(@RequestParam String content, @RequestParam String blogId, @CookieValue("userId") String userId) {
        // 获取博客数据
        Blog blog = blogDao.getBlogById(Long.parseLong(blogId));
        User user = new User();
        if (blog == null) {
            return "error";  // 如果找不到博客，则返回失败响应
        }

        int num = Integer.parseInt(userId);
        user.setId(num);
        // 创建评论对象并保存
        BlogComment blogComment = new BlogComment();
        blogComment.setContent(content);
        blogComment.setBlog(blog);  // 使用 Blog 对象
        blogComment.setUser(user);

        // 保存评论
        try {
            blogCommentService.addComment(blogComment);  // 调用 Service 保存评论
            return "success";  // 返回成功响应
        } catch (Exception e) {
            System.out.println(e);
            return "error";  // 返回失败响应
        }
    }

    //发表博客
    @RequestMapping( "/publish_blog")
    @ResponseBody
    public ResponseEntity<Object> submitBlog(Blog blog, @RequestParam("image") MultipartFile image,@CookieValue(value = "userId",defaultValue = "-1") String userId) throws IOException {
        // 处理文件上传
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择一张图片上传");
        }
        System.out.println(blog);
        //图片储存路径

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        String uploadDir = "D:\\JiLin_STP\\src\\main\\resources\\static\\image\\homepage\\blog"; // 直接使用绝对路径
        Path filePath = Paths.get(uploadDir,fileName);

        // 在上传图片的方法中
        String uploadDir1 = new ClassPathResource("static/image/homepage/blog").getFile().getAbsolutePath();
        Path filePath1 = Paths.get(uploadDir1, fileName);
        // 获取当前时间
        Date timestamp = new Date(); // 获取当前日期时间对象
        blog.setCreateTime(timestamp); // 将 timestamp 设置为 Date 类型

        try{
            // 创建目标文件夹（如果不存在）
            Files.createDirectories(filePath.getParent());
            Files.createDirectories(filePath1.getParent());
            // 保存文件到服务器
            Files.copy(image.getInputStream(), filePath);
            Files.copy(image.getInputStream(), filePath1);
            // 从 cookie 获取 userId
            if (userId.equals("-1")) {
                return ResponseEntity.badRequest().body("未登录");
            }

            String imageUrl = "/image/homepage/blog/" + fileName;
            blog.setAuthorId(Integer.parseInt(userId));
            blog.setImageUrl(imageUrl);  // 设置图片 URL

//            blog.setContent(blog.getContent());
//            blog.setTitle(blog.getTitle());

//            // 确保博客内容的编码正确，避免乱码
            blog.setContent(new String(blog.getContent().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            blog.setTitle(new String(blog.getTitle().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            // 将博客插入数据库
            blogService.addBlog(blog);

            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("newImageUrl", imageUrl);
            resultMap.put("responseCode", "200");

            return ResponseEntity.ok(resultMap);

        } catch (IOException e) {
            return ResponseEntity.status(450).body("博客创建失败");
        }
    }
    @RequestMapping("/getBlogWithAuthor")
    public String getBlogWithAuthor(@RequestParam("blogId") Integer blogId, Model model) {
        Blog blog = blogService.getBlogWithAuthorName(blogId);
        model.addAttribute("blog", blog);
        return "blog";  // 返回到详细页面，显示博客和作者的名称
    }


    @RequestMapping("/createBlog")
    public String createBlog(Model model) {
        // 返回博客创建页面
        model.addAttribute("blog", new Blog());
        return "publish_blog"; // 返回给前端页面
    }
}
