package com.stp.controller;

import com.stp.dao.CommentDao;
import com.stp.dao.ScenicSpotDao;
import com.stp.dao.UserDao;
import com.stp.domain.Comment;
import com.stp.domain.ScenicSpot;
import com.stp.domain.User;
import com.stp.service.CommentService;
import com.stp.service.ScenicSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class ScenicSpotController {
    private static final Logger logger = LoggerFactory.getLogger(ScenicSpotController.class);

    @Autowired
    private ScenicSpotDao scenicSpotDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Autowired
    private CommentService commentService;

    // 根据景点 ID 动态生成页面
    @RequestMapping("/scenic-spot/{id}")
    public String getScenicSpot(@PathVariable("id") Long id, Model model) {
        // 获取景点数据
        ScenicSpot spot = scenicSpotService.getScenicSpotById(id);

        if (spot == null) {
            return "error";  // 如果找不到景点，则返回错误页面
        }

        // 获取评论数据
        List<Comment> comments = commentService.getCommentsBySpotId(id);

        // 将数据传递给前端
        model.addAttribute("spot", spot);
        model.addAttribute("comments", comments);

        // 返回景点详情页面
        return "scenic_spot";  // 返回 scenic-spot.html 页面
    }


    //主页景点卡片
    @PostMapping("/getScenicSpotCard")
    @ResponseBody
    public Map<String, Object> getScenicSpotCard(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "3") int size, Model model){


        // 计算偏移量
        int offset = (page - 1) * size;

        // 获取分页数据
        List<ScenicSpot> scenicSpotsList = scenicSpotDao.getPagedScenicSpotCard(offset, size);

        // 获取总记录数
        int totalRecords = scenicSpotDao.getAllScenicSpotCount();
        int totalPages = (int) Math.ceil((double) totalRecords / size);  // 计算总页数

        // 构建返回的 Map 数据结构
        Map<String, Object> response = new HashMap<>();
        // 将分页数据和总页数添加到模型
        response.put("scenicSpots", scenicSpotsList);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);

        return response;
//        return scenicSpotDao.getPagedScenicSpotCard(offset, size);
//        return scenicSpotsList;
    }


    //景点详情页评论获取
    @RequestMapping("/scenic-spot/{spotId}/comments")
    @ResponseBody
    public List<Comment> getCommentsBySpotId(@PathVariable("spotId") Long spotId){
        List<Comment> comment = commentDao.findCommentsBySpotId(spotId);
        logger.info(comment.toString());
        return commentDao.findCommentsBySpotId(spotId);
    }

    // 景点详情页提交评论
    @PostMapping("/submit-comment")
    @ResponseBody
    public String submitComment(@RequestParam String content, @RequestParam String spotId, @CookieValue("userId") String userId) {
        // 获取景点数据
        ScenicSpot spot = scenicSpotDao.selectScenicSpotById(Long.parseLong(spotId));
        User user = new User();
        if (spot == null) {
            return "error";  // 如果找不到景点，则返回失败响应
        }

        int num = Integer.parseInt(userId);
        user.setId(num);
        // 创建评论对象并保存
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setScenicSpot(spot);  // 使用 ScenicSpot 对象
        comment.setUser(user);

        // 保存评论
        try {
            commentService.addComment(comment);  // 调用 Service 保存评论
            return "success";  // 返回成功响应
        } catch (Exception e) {
            return "error";  // 返回失败响应
        }
    }


    //动态地图部分的景点获取


    @RequestMapping("/apiList")
    @ResponseBody
    public List<Map<String,Object>> listScenicSpots(){
        List<ScenicSpot> spots = scenicSpotDao.selectAllSpots();
        return spots.stream().map(spot -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", spot.getId());
            map.put("name", spot.getName());
            map.put("longitude", spot.getLongitude());
            map.put("latitude", spot.getLatitude());
            map.put("imageUrl", spot.getImageUrl());
            map.put("openTime",spot.getOpenTime());
            return map;
        }).collect(Collectors.toList());

    }
}
