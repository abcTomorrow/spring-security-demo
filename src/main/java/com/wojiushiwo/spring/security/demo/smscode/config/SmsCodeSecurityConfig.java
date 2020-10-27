package com.wojiushiwo.spring.security.demo.smscode.config;

import com.wojiushiwo.spring.security.demo.handler.MyAuthenticationFailureHandler;
import com.wojiushiwo.spring.security.demo.handler.MySuccessHandler;
import com.wojiushiwo.spring.security.demo.smscode.filter.SmsAuthenticationFilter;
import com.wojiushiwo.spring.security.demo.smscode.filter.SmsCodeValidFilter;
import com.wojiushiwo.spring.security.demo.smscode.provider.SmsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author myk
 * @create 2020/10/24 下午3:01
 */
@Configuration
public class SmsCodeSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    @Qualifier("smsUserDetailService")
    private UserDetailsService userDetailsService;

    @Autowired
    private MySuccessHandler successHandler;

    @Autowired
    private MyAuthenticationFailureHandler failureHandler;

    @Autowired
    private SmsCodeValidFilter smsCodeValidFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        SmsAuthenticationFilter authenticationFilter = new SmsAuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(failureHandler);
        authenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));

        //获取验证码提供者
        SmsAuthenticationProvider authenticationProvider = new SmsAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);

        //在用户名密码过滤器前加入短信验证码过滤器
        builder.addFilterBefore(smsCodeValidFilter, UsernamePasswordAuthenticationFilter.class);
        //在用户名密码过滤器后加入短信验证码认证授权过滤器
        builder.addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
