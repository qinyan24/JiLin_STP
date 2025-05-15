package com.stp.controller;

import com.stp.dao.ScenicSpotDao;
import com.stp.domain.ScenicSpot;
import com.stp.domain.User;
import com.stp.service.ScenicSpotService;
import com.stp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private ScenicSpotService scenicSpotService;

    @Autowired
    private ScenicSpotDao scenicSpotDao;

    @ResponseBody
    @GetMapping("/homepage1")  // 后端接收请求的 URL
    public Map<String, Object> handleGoHomeRequest() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);  // 请求成功，返回成功标志
        return response;
    }

    @RequestMapping ("/myself1")  // 后端接收请求的 URL
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


    //动态地图页面
    @RequestMapping("/summary_attractions")
    public String showSummaryAttraction(Model model) {
        // 获取所有景点数据
        List<ScenicSpot> attractions = scenicSpotService.getAllSpots();
//        System.out.println("获取到的景点数据：" + attractions);
        // 将景点数据添加到模型中
        model.addAttribute("attractions", attractions);

        // 返回视图名称 (Thymeleaf 页面)
        return "summary_attractions";
    }

    //手动更新景点经纬度
    @GetMapping("/update_coordinates")
    @ResponseBody
    public String updateCoordinates(){
        scenicSpotService.updateScenicSpotCoordinates();
        return "景点经纬度更新完成！";
    }
}
