package com.wojiushiwo.spring.security.demo.config;

import com.wojiushiwo.spring.security.demo.auth.filter.CaptchaCodeFilter;
import com.wojiushiwo.spring.security.demo.handler.MyLogoutSuccessHandler;
import com.wojiushiwo.spring.security.demo.handler.MySuccessHandler;
import com.wojiushiwo.spring.security.demo.service.auth.MyUserDetailsService;
import com.wojiushiwo.spring.security.demo.smscode.config.SmsCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author wojiushiwo
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] IGNORE_PATH = {
            "/login.html",
            "/login",
            "/logout.html",
            "/kaptcha",
            "/smscode"};
    @Autowired
    private MySuccessHandler successHandler;
    @Autowired
    private MyLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private CaptchaCodeFilter captchaCodeFilter;

    @Autowired
    private SmsCodeSecurityConfig smsCodeSecurityConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //在UsernamePasswordAuthenticationFilter过滤器前加上验证码过滤器
        http.addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/myLogout")//自定义登出url，默认是/logout
                .deleteCookies("JSESSIONID")//登出时 删除指定名称的cookie
//                .logoutSuccessUrl("/logout.html")//指定登出后跳转的页面，l默认是oginPage配置项指定的页面
                .logoutSuccessHandler(logoutSuccessHandler)//logoutSuccessHandler不能与logoutSuccessUrl一起用
                .and().csrf().disable() //禁用跨站csrf攻击防御
                .formLogin()
                .loginPage("/login.html")//用户未登录时，访问任何资源都转跳到该路径，即登录页面
                .loginProcessingUrl("/login")//登录表单form中action的地址，也就是处理认证请求的路径
                .usernameParameter("username")///登录表单form中用户名输入框input的name名，不修改的话默认是username
                .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password
                .defaultSuccessUrl("/index")//登录认证成功后默认转跳的路径
                .and().apply(smsCodeSecurityConfig)//添加短信验证过滤器配置
                .and()
                .authorizeRequests()
                .antMatchers(IGNORE_PATH).permitAll()//不需要通过登录验证就可以被访问的资源路径
                .antMatchers("/index").authenticated()
                .anyRequest().access("@myRBACService.hasPermission(request,authentication)") //使用权限表达式 进行鉴权
//                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login.html")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy());


    }

    //这个方法用来测试方法级别权限表达式--method表达式安全控制
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable() //禁用跨站csrf攻击防御
//                .formLogin()
//                .loginPage("/login.html")//用户未登录时，访问任何资源都转跳到该路径，即登录页面
//                .loginProcessingUrl("/login")//登录表单form中action的地址，也就是处理认证请求的路径
//                .usernameParameter("username")///登录表单form中用户名输入框input的name名，不修改的话默认是username
//                .passwordParameter("password")//form中密码输入框input的name名，不修改的话默认是password
//                .defaultSuccessUrl("/index")//登录认证成功后默认转跳的路径
//                .and()
//                .authorizeRequests()
//                .antMatchers("/login.html", "/login").permitAll()//不需要通过登录验证就可以被访问的资源路径
//                .antMatchers("/index").authenticated()
//                .anyRequest().authenticated()//这里 只要登录成功 即可用拥有访问接口的权限
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                .invalidSessionUrl("/login.html")
//                .maximumSessions(1)
//                .maxSessionsPreventsLogin(false)
//                .expiredSessionStrategy(new CustomExpiredSessionStrategy());
//
//
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("root")
//                .password(passwordEncoder().encode("root"))
//                .roles("admin")
////                .authorities("sys:user", "sys:log","biz1","biz2")
//                .and()
//                .withUser("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("user")
//                .and()
//                //配置BCrypt加密
//                .passwordEncoder(passwordEncoder());
        //设置从数据库读取用户信息
        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //将项目中静态资源路径开放出来
        web.ignoring().antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "**/favicon.ico");
    }
}
