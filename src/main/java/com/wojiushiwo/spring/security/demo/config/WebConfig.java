package com.wojiushiwo.spring.security.demo.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author wojiushiwo
 */
//@Configuration
//@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {

//    @Override
//    protected void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/index").setViewName("/index.html");
//    }
}
