package org.neu.insurance.dto;

import lombok.Data;

/**
 * 登录请求DTO
 */
@Data
public class LoginRequest {
    
    private String username;
    
    private String password;
    
    private String captcha;      // 验证码
    private String captchaKey;   // 验证码key
    private Boolean rememberMe;  // 记住我
} 