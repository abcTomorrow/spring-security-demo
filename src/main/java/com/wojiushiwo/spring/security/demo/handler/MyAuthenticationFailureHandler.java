package com.wojiushiwo.spring.security.demo.handler;

import com.wojiushiwo.spring.security.demo.mapper.MyUserDetailsMapper;
import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Classname MyAuthencationFailureHandler
 * @Description 自定义登录失败处理器
 * @Date 2020/10/14 10:45 下午
 * @Created by wojiushiwo
 */
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    Set<RequestLimitRule> rules = Collections.singleton(RequestLimitRule.of(60, TimeUnit.MINUTES, 5));
    RequestRateLimiter limiter = new InMemorySlidingWindowRequestRateLimiter(rules);
    @Autowired
    @Qualifier("myUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        //以用户名、密码登录演示多次登录失败冻结账户

        String username = request.getParameter("username");

        String errorMsg = "用户名或者密码输入错误!";
        if (exception instanceof LockedException) {
            errorMsg = "您已经多次登陆失败，账户已被锁定，请稍后再试！";
        } else {
            //计数器加1 并判断该用户是否已经触发了锁定规则
            boolean reachLimit = limiter.overLimitWhenIncremented(username);
            if(reachLimit){
                //如果触发了锁定机制 通过UserDetails告知spring security锁定账户
                MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(username);
                userDetails.setAccountNonLocked(false);
                myUserDetailsMapper.updateEnabledByUsername(userDetails);
            }
        }


        if (exception instanceof SessionAuthenticationException) {
            errorMsg = exception.getMessage();
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorMsg);
    }
}
