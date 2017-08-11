package com.kss.manage.configuration;

import com.kss.manage.application.annotation.AuthorizationInterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class InterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthorizationInterceptor interceptor;

    private static final Logger logger = LoggerFactory.getLogger(InterceptorConfigurerAdapter.class);

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/api/**","/login/logout");
    }
}
