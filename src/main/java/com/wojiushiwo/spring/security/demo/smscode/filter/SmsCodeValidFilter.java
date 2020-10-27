package com.wojiushiwo.spring.security.demo.smscode.filter;

import com.wojiushiwo.spring.security.demo.constants.MyConstants;
import com.wojiushiwo.spring.security.demo.handler.MyAuthenticationFailureHandler;
import com.wojiushiwo.spring.security.demo.mapper.MyUserDetailsMapper;
import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import com.wojiushiwo.spring.security.demo.smscode.dto.SmsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @author myk 校验验证码是否正确 过滤器
 * @create 2020/10/24 上午11:21
 */
@Component
public class SmsCodeValidFilter extends OncePerRequestFilter {

    private static final String PATH = "/smslogin";


    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (PATH.equals(requestURI) && method.equalsIgnoreCase("post")) {
            try {
                this.validateSmsCode(request);
            } catch (AuthenticationException e) {
                failureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

    private void validateSmsCode(HttpServletRequest request) throws AuthenticationException {
        SmsCode smsCode = (SmsCode) request.getSession().getAttribute(MyConstants.SMS_CODE_SESSION_KEY);
        String codeInSession = smsCode.getCode();
        String codeInRequest = request.getParameter("smsCode");
        String mobileInRequest = request.getParameter("mobile");

        //用户登录时手机号不能为空
        if (Objects.isNull(mobileInRequest)) {
            throw new SessionAuthenticationException("用户登录时手机号不能为空");
        }
        //用户登录时短信验证码不能为空
        if (Objects.isNull(codeInRequest)) {
            throw new SessionAuthenticationException("用户登录时短信验证码不能为空");
        }
        //短信验证码不能为空
        if (Objects.isNull(codeInSession)) {
            throw new SessionAuthenticationException("短信验证码不能为空");
        }

        HttpSession session = request.getSession();
        if (smsCode.isExpired()) {
            //如果验证码已经过期
            session.removeAttribute(MyConstants.SMS_CODE_SESSION_KEY);
            throw new SessionAuthenticationException("短信验证码已经过期");
        }

        if (!Objects.equals(codeInRequest, codeInSession)) {
            throw new SessionAuthenticationException("短信验证码不正确");
        }
        if (!smsCode.getMobile().equals(mobileInRequest)) {
            throw new SessionAuthenticationException("短信发送目标与该手机号不一致！");
        }

        MyUserDetails userDetails = myUserDetailsMapper.findByUserMobile(mobileInRequest);
        if (Objects.isNull(userDetails)) {
            throw new SessionAuthenticationException("您输入的手机号不是系统的注册用户");
        }
        //验证过 清除验证码
//        session.removeAttribute(MyConstants.SMS_CODE_SESSION_KEY);

    }
}
