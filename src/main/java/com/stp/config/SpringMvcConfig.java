package com.stp.config;

import com.stp.cookieInterceptor.CookieInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;


@Configuration
@ComponentScan({"com.stp.controller","com.stp.config"})
@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer {
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/pages/");  // Thymeleaf 模板存放路径
        resolver.setSuffix(".html");  // 视图后缀
        resolver.setTemplateMode("HTML5");
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源路径映射

        // 配置 JS 文件
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // 配置 CSS 文件
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // 配置 图片 文件
        registry.addResourceHandler("/image/**")
                .addResourceLocations("classpath:/static/image/");

        // 如果你有其他的静态资源，可以继续添加
        // registry.addResourceHandler("/**")
        //         .addResourceLocations("classpath:/static/");
    }

    @Bean
    public CookieInterceptor cookieInterceptor() {
        return new CookieInterceptor();  // 创建 CookieInterceptor 实例
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 CookieInterceptor 拦截器，拦截所有路径
        registry.addInterceptor(cookieInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/static/**", "/index","/css/**", "/js/**","/image/**",
                        "/homepage","/login","/auth/login","/auth/register","/"); // 排除静态资源和登录注册路径
    }
}
