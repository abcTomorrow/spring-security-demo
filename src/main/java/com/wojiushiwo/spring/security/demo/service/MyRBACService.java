package com.wojiushiwo.spring.security.demo.service;

import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname MyRBACService
 * @Description 自定义鉴权规则 服务
 * @Date 2020/10/12 3:51 下午
 * @Created by wojiushiwo
 */
@Component
public class MyRBACService {

    private PathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 判断某用户是否有访问当前request请求资源的权限
     *
     * @param request
     * @param authentication
     * @return
     */
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {

            MyUserDetails myUserDetails = (MyUserDetails) principal;

            String requestURI = request.getRequestURI();

            //这里 不再使用 ROLE_ 角色 全部替换成了权限。
            //如数据库配置某用户的访问权限是/sysuser、/syslog,而此时访问的请求是/sysuser 则可以访问
            return myUserDetails.getAuthorities().stream().anyMatch(s -> pathMatcher.match(requestURI, s.getAuthority()));
        }

        return false;

    }

}
