package com.wojiushiwo.spring.security.demo.controller;

import com.wojiushiwo.spring.security.demo.service.MethodSecurityControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class BizpageController {

    @Autowired
    private MethodSecurityControlService securityControlService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public String index(String username, String password) {
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
     * 日志管理
     */
    @GetMapping("/syslog")
    public String showOrder() {
        return "/syslog.html";
    }

    /**
     * 用户管理
     *
     * @return
     */
    @GetMapping("/sysuser")
    public String addOrder() {
        //测试 preAuthorize 可以正常访问该方法
//        System.out.println(securityControlService.testPreAuthorize());

        //测试 postAuthorize 可以正常访问该方法
//        System.out.println(securityControlService.testPostAuthorize());

        //测试 preFilter
//        List<Integer> ids= new ArrayList<Integer>(){{
//            add(1);
//            add(2);
//            add(3);
//            add(4);
//        }};
//        //这里会输出2 4
//        securityControlService.testPreFilter(ids,null);

        //测试postFilter 当前登录用户是admin 因此过滤掉不符合条件数据，返回结果只有admin
//        securityControlService.testPostFilter().forEach(System.out::println);
        return "/sysuser.html";
    }

    /**
     * 具体业务一
     *
     * @return
     */
    @GetMapping("/biz1")
    public String updateOrder() {
        return "/biz1.html";
    }

    /**
     * 具体业务二
     *
     * @return
     */
    @GetMapping("/biz2")
    public String deleteOrder() {
        return "/biz2.html";
    }
}