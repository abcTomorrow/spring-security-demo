package com.wojiushiwo.spring.security.demo.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MethodSecurityControlService
 * @Description 测试spring security 方法级别的权限表达式注解使用
 * @Date 2020/10/13 6:53 下午
 * @Created by wojiushiwo
 */
@Component
public class MethodSecurityControlService {

    @PreAuthorize("hasAuthority('/sysuser')")
    public String testPreAuthorize() {
        return "testPreAuthorize";
    }

    //表示 拥有/biz1权限 可以访问该方法
    @PostAuthorize("hasAuthority('/biz1')")
    public String testPostAuthorize() {
        return "testPostAuthorize";
    }

    //preFilter 当方法参数只有一个集合时 filterTarget参数会被省略
    // 当ids中值%2=0的数据才会传递到方法体重
    @PreFilter(filterTarget = "ids", value = "filterObject%2==0")
    public void testPreFilter(List<Integer> ids, List<String> userNames) {
        ids.forEach(System.out::println);
    }

    //过滤掉响应中 内容不等于登录用户用户名的数据.特别适用于集合类返回值，过滤集合中不符合表达式的对象
    //filterObject 指代集合中的实体，可以是基本数据类型 也可以是POJO类，当是POJO类时，可以通过.属性的方式 访问其属性值
    @PostFilter("filterObject == authentication.name")
    public List<String> testPostFilter() {

        List<String> list = new ArrayList<>();
        list.add("common");
        list.add("admin");
        return list;
    }
}
