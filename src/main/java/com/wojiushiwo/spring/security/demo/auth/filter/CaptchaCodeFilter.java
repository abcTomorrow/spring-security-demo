package com.wojiushiwo.spring.security.demo.auth.filter;

import com.wojiushiwo.spring.security.demo.constants.MyConstants;
import com.wojiushiwo.spring.security.demo.handler.MyAuthencationFailureHandler;
import com.wojiushiwo.spring.security.demo.model.CaptchaImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @Classname CaptchaCodeFilter
 * @Description 验证码过滤器 用来校验用户输入验证码是否正确
 * @Date 2020/10/14 10:33 下午
 * @Created by wojiushiwo
 */
@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {

    @Autowired
    private MyAuthencationFailureHandler authencationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //必须是/login且POST请求才验证，其他的直接放行
        if (Objects.equals("/login", request.getRequestURI())
                && Objects.equals(request.getMethod(), "POST")) {
            try {
                validate(request);
            } catch (AuthenticationException e) {
                //捕获异常 交给失败处理类进行处理
                authencationFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }

        //放行
        filterChain.doFilter(request, response);

    }


    private void validate(HttpServletRequest request) throws ServletRequestBindingException {
        HttpSession session = request.getSession();

        //获取用户登录页面输入的captchaCode
        String codeInRequest = ServletRequestUtils.getStringParameter(request, "captchaCode");

        if (StringUtils.isEmpty(codeInRequest)) {
            throw new SessionAuthenticationException("验证码不能为空");
        }

        //获取session池中的验证码
        CaptchaImageVO codeInSession = (CaptchaImageVO) session.getAttribute(MyConstants.CAPATCHA_SESSION_KEY);

        if (Objects.isNull(codeInSession)) {
            throw new SessionAuthenticationException("您输入的验证码不存在");
        }

        //校验服务器session中的验证码是否过期
        if (codeInSession.isExpried()) {
            session.removeAttribute(MyConstants.CAPATCHA_SESSION_KEY);
            throw new SessionAuthenticationException("验证码已经过期");
        }

        //请求验证码校验
        if (!Objects.equals(codeInSession.getCode(), codeInRequest)) {
            throw new SessionAuthenticationException("验证码不匹配");
        }
    }
}
