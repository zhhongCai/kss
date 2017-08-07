package com.kss.dataprocess.configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMvcConfiguration {

    @Bean
    public HttpMessageConverters custom() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter4());
    }

}
