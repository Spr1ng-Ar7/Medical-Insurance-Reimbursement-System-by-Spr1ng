package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    private Long id;
    private String username;           // 用户名
    private String password;           // 密码
    private String realName;           // 真实姓名
    private String email;              // 邮箱
    private String phone;              // 手机号
    private String role;               // 角色：admin/doctor/operator等
    private String department;         // 部门
    private Integer status;            // 状态：1-正常，0-禁用
    private LocalDateTime lastLoginTime; // 最后登录时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;             // 备注
} 