package org.neu.insurance.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Session配置类
 * 禁用JDBC Session存储，使用内存存储
 */
@Configuration
public class SessionConfig {
    
    /**
     * 禁用Spring Session JDBC自动配置
     * 如果不需要Session持久化，可以使用此配置
     */
    @Bean
    @ConditionalOnProperty(name = "spring.session.store-type", havingValue = "none")
    public Object disableJdbcSessionAutoConfiguration() {
        // 返回一个空对象，用于禁用JDBC Session配置
        return new Object();
    }
} 