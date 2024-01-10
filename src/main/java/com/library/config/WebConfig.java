package com.library.config;


import com.library.Intercepter.AuthorInterceptors;
import com.library.Intercepter.LoginInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan({"com.library.controller"})
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptors loginInterceptors;
    @Autowired
    AuthorInterceptors authorInterceptors;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptors).addPathPatterns("/**").
                excludePathPatterns("/**/login", "/**/register");

        registry.addInterceptor(authorInterceptors).addPathPatterns("/**/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET","POST","PUT","OPTIONS","DELETE","PATCH")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
