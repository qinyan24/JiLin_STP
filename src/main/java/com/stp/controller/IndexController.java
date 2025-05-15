package com.stp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    // 处理主页请求
    @RequestMapping ("/")
    public String index() {
        return "index"; // 返回视图名index.html
    }

    // 处理登录页请求
    @RequestMapping("/login")
    public String login() {
        return "login"; // 返回视图名login.html
    }

    @RequestMapping("/homepage")
    public String homepage(){
        return "homepage";
    }
}
