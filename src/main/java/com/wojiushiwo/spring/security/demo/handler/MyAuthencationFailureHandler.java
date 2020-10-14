package com.wojiushiwo.spring.security.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname MyAuthencationFailureHandler
 * @Description 自定义登录失败处理器
 * @Date 2020/10/14 10:45 下午
 * @Created by wojiushiwo
 */
@Component
public class MyAuthencationFailureHandler extends SimpleUrlAuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg = "用户名或者密码输入错误!";
        if (exception instanceof SessionAuthenticationException) {
            errorMsg = exception.getMessage();
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(errorMsg);
    }
}
