package com.wojiushiwo.spring.security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BizpageController {

    /**
     *  登录
      */
    @PostMapping("/login")
    public String index(String username,String password) {
        return "index";
    }

    /**
     * 登录成功之后的首页
     */
    @GetMapping("/index")
    public String index() {
        return "/index.html";
    }

    /**
     *  日志管理
     */
    @GetMapping("/syslog")
    public String showOrder() {
        return "/syslog.html";
    }

    /**
     * 用户管理
     * @return
     */
    @GetMapping("/sysuser")
    public String addOrder() {
        return "/sysuser.html";
    }

    /**
     * 具体业务一
     * @return
     */
    @GetMapping("/biz1")
    public String updateOrder() {
        return "/biz1.html";
    }

    /**
     * 具体业务二
     * @return
     */
    @GetMapping("/biz2")
    public String deleteOrder() {
        return "/biz2.html";
    }
}