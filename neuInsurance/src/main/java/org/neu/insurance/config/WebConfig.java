package org.neu.insurance.config;

import org.neu.insurance.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private PermissionInterceptor permissionInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册权限拦截器
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/api/**")  // 拦截所有API请求
                .excludePathPatterns(        // 排除不需要权限验证的路径
                    "/api/auth/login",
                    "/api/admin/login",      // 添加管理员登录路径
                    "/api/auth/logout",
                    "/api/auth/refresh",
                    "/error",
                    "/favicon.ico",
                    "/static/**",
                    "/public/**"
                );
    }
} 