package org.neu.insurance.dto;

import lombok.Data;
import org.neu.insurance.entity.Admin;

/**
 * 登录响应DTO
 * 包含用户信息和JWT token
 */
@Data
public class LoginResponseDTO {
    private Admin user;           // 用户信息
    private String token;         // JWT token
    private String tokenType;     // token类型，固定为"Bearer"
    private Long expiresIn;       // token过期时间（秒）
    
    public LoginResponseDTO(Admin user, String token) {
        this.user = user;
        this.token = token;
        this.tokenType = "Bearer";
        this.expiresIn = 24L * 60 * 60; // 24小时，单位：秒
    }
} 