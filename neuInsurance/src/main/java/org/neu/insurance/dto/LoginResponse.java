package org.neu.insurance.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import org.neu.insurance.entity.Admin;
import org.neu.insurance.entity.Role;
import org.neu.insurance.entity.Permission;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    
    private String token;            // 访问令牌
    private String refreshToken;     // 刷新令牌
    private Long expireTime;         // 过期时间戳
    private String username;         // 用户名
    private String realName;         // 真实姓名
    private String avatar;           // 头像
    private String email;            // 邮箱
    private String phone;            // 电话
    private String departmentName;   // 部门名称
    private LocalDateTime lastLoginTime; // 最后登录时间
    private List<String> roles;      // 角色列表
    private List<String> permissions; // 权限列表
    
    // 默认构造函数
    public LoginResponse() {}
    
    // 构造函数：用于AdminServiceImpl中的调用
    public LoginResponse(String token, Admin admin, List<Role> roles, List<Permission> permissions, Long expireTime) {
        this.token = token;
        this.expireTime = expireTime;
        this.username = admin.getUsername();
        this.realName = admin.getRealName();
        this.avatar = admin.getAvatar();
        this.email = admin.getEmail();
        this.phone = admin.getPhone();
        this.departmentName = admin.getDepartment();
        this.lastLoginTime = admin.getLastLoginTime();
        
        // 转换角色列表为字符串列表
        if (roles != null) {
            this.roles = roles.stream()
                    .map(Role::getRoleName)
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // 转换权限列表为字符串列表
        if (permissions != null) {
            this.permissions = permissions.stream()
                    .map(Permission::getPermissionCode)
                    .collect(java.util.stream.Collectors.toList());
        }
    }
} 