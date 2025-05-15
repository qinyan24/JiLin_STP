package com.stp.controller;

import com.stp.dao.BlogDao;
import com.stp.domain.Blog;
import com.stp.domain.BlogComment;
import com.stp.domain.User;
import com.stp.service.BlogCommentService;
import com.stp.service.BlogService;
import com.stp.service.UserService;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogCommentService blogCommentService;

    @Autowired
    private BlogDao blogDao;

    @Value("${file.upload-dir}")
    private String uploadDir;  // 上传文件存储路径

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    // 登录方法
    @RequestMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody User user, HttpServletResponse response) {

        if (user.getPhone() == null || user.getPhone().isEmpty() || user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("手机号或密码不能为空");
        }

        // 调用服务层登录方法，验证用户名、手机号和密码
        User loggedInUser = userService.login(user.getUsername(), user.getPhone(), user.getPassword());
        if (loggedInUser != null) {
            // 登录成功，存储用户 ID 到 cookie
            Cookie cookie = new Cookie("userId", String.valueOf(loggedInUser.getId()));  // 使用登录成功的用户 ID
            cookie.setMaxAge(60 * 60 * 24);  // 设置cookie过期时间为1天
            cookie.setPath("/");  // 设置cookie作用域为整个网站
            response.addCookie(cookie);
            return ResponseEntity.ok("登录成功");
        }

        return ResponseEntity.status(401).body("账号、手机号或密码错误");
    }

    // 注册方法
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody User user) {
        // 验证手机号是否已注册
        if (userService.register(user)) {
            return ResponseEntity.ok("注册成功");
        }
        return ResponseEntity.status(400).body("注册失败，手机号已存在");
    }


// 获取个人信息
    @RequestMapping(value = "/myself")
    public String getMyPage(@CookieValue(value = "userId", defaultValue = "-1") int userId, Model model) {
        if (userId == -1) {
            // 如果没有 cookie 或者 userId 为默认值，则表示未登录
            return "redirect:/login";  // 重定向到登录页面
        }

        User user = userService.getUserById(userId);  // 根据 userId 获取用户
        System.out.println(user.toString());
        if (user != null) {
            model.addAttribute("user", user);
            return "myself";  // 返回个人页面的视图
        } else {
            return "redirect:/login";  // 如果用户不存在，跳转到登录页面
        }
    }


    // 头像上传
    @PostMapping("/upload-avatar")
    @ResponseBody
    public ResponseEntity<Object> uploadAvatar(@RequestParam("avatar") MultipartFile file, @CookieValue(value = "userId", defaultValue = "-1") String userId) throws IOException {
        if (Integer.parseInt(userId) == -1) {
            return ResponseEntity.status(401).body("用户未登录");
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("请选择一个文件上传");
        }

        // 文件存储路径
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String uploadDir = "D:\\JiLin_STP\\src\\main\\resources\\static\\image\\users"; // 直接使用绝对路径
        Path filePath = Paths.get(uploadDir, fileName);



        // 在上传头像的方法中
        String uploadDir1 = new ClassPathResource("static/image/users").getFile().getAbsolutePath();
        Path filePath1 = Paths.get(uploadDir1, fileName);

        try {
            // 创建目标文件夹（如果不存在）
            Files.createDirectories(filePath.getParent());
            Files.createDirectories(filePath1.getParent());

            // 保存文件到服务器
            Files.copy(file.getInputStream(), filePath);
            Files.copy(file.getInputStream(), filePath1);

            // 更新用户的头像 URL
            String imageUrl = "/image/users/" + fileName;
            User user =new User();
            user.setId(Integer.parseInt(userId));
            user.setImageUrl(imageUrl);
            userService.updateAvatar(user);  // 更新数据库中的头像URL
            logger.info(userId);
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("newImageUrl",imageUrl);
            resultMap.put("responseCode","200");

            return ResponseEntity.ok(resultMap);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("头像上传失败，请重试");
        }
    }


    // 更新个人信息
    @PostMapping("/update-profile")
    @ResponseBody
    public ResponseEntity<Object> updateProfile(@RequestParam("username") String username,
                                                @RequestParam("description") String description,
                                                @CookieValue(value = "userId", defaultValue = "-1") int userId) {
        if (userId == -1) {
            return ResponseEntity.status(401).body("用户未登录");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("用户不存在");
        }

        // 更新用户名和个人简介
        user.setUsername(username);
        user.setDescription(description);
        user.setId(userId);
        userService.updateUser(user);

        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("responseCode","200");

        return ResponseEntity.ok(resultMap);
    }

    //个人页面博客
    @GetMapping("/list")
    @ResponseBody
    public Map<String, Object> getBlogs(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "5") int size,
                                        @CookieValue(value = "userId", defaultValue = "-1") int userId) {
        // 计算偏移量
//        int offset = (page - 1) * size;
//
//        // 获取分页博客数据
//        List<Blog> blogList = blogDao.getPagedBlogs(userId, offset, size);
//
//        // 获取博客总数
//        int totalRecords = blogDao.getTotalBlogCount(userId);
//        int totalPages = (size > 0) ? (int) Math.ceil((double) totalRecords / size) : 1;

        List<Blog> blogList = blogDao.findByAuthorId(userId);
        // 组装返回数据
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("blogs", blogList);
//        response.put("currentPage", page);
//        response.put("totalPages", totalPages);

        return response;
    }

    //个人页面博客跳转
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


    // 退出登录
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 删除 Cookie
        Cookie cookie = new Cookie("userId", null);
        cookie.setMaxAge(0);  // 立即删除
        cookie.setPath("/");  // 设置cookie作用域为整个网站
        response.addCookie(cookie);

        return "redirect:/login"; // 退出后跳转到登录页面
    }

    //修改密码
    @RequestMapping("/change-password")
    @ResponseBody
    public ResponseEntity<Object> changePassword(
            @RequestBody Map<String, String> requestBody,
            @CookieValue(value = "userId", defaultValue = "-1") int userId) {

        if (userId == -1) {
            return ResponseEntity.status(401).body("用户未登录");
        }

        String oldPassword = requestBody.get("oldPassword");
        String newPassword = requestBody.get("newPassword");

        if (oldPassword == null || newPassword == null) {
            return ResponseEntity.status(400).body("参数不能为空");
        }

        // 获取用户对象
        User user = userService.getPasswordById(userId);

        // 先检查 user 是否为空
        if (user == null) {
            return ResponseEntity.status(404).body("用户不存在");
        }

        // 再检查 password 是否为空，避免 NullPointerException
        if (user.getPassword() == null) {
            return ResponseEntity.status(500).body("用户密码为空，无法修改");
        }

        if (!user.getPassword().equals(oldPassword)) {
            return ResponseEntity.status(403).body("旧密码错误");
        }

        user.setId(userId);
        user.setPassword(newPassword);
        int col = userService.updatePassword(user);

        return ResponseEntity.ok("密码修改成功");
    }

}
