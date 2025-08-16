package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 统一用户实体类
 * 整合：管理员、医生、护士、财务等所有用户类型
 */
@Data
public class Admin {
    private Long id;
    private String username;           // 用户名
    private String password;           // 密码
    private String realName;           // 真实姓名
    private String email;              // 邮箱
    private String phone;              // 电话
    private String avatar;             // 头像URL
    private String userType;           // 用户类型：admin-管理员/doctor-医生/nurse-护士/finance-财务/operator-操作员
    private String role;               // 角色：SUPER_ADMIN/ADMIN/DOCTOR/NURSE/FINANCE/OPERATOR
    private String department;         // 部门
    private String position;           // 职位
    private String idCard;             // 身份证号
    private String gender;             // 性别：男/女
    private String birthDate;          // 出生日期
    private String address;            // 地址
    private Integer status;            // 状态：1-正常，0-禁用
    private Long departmentId;         // 部门ID
    private LocalDateTime lastLoginTime; // 最后登录时间
    private String lastLoginIp;        // 最后登录IP
    private Integer loginFailCount;    // 登录失败次数
    private LocalDateTime lockedTime;  // 锁定时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
    
    // 扩展字段，用于特殊用户类型
    private String licenseNumber;      // 执业证号（医生）
    private String specialty;          // 专业特长（医生）
    private String title;              // 职称：主任医师/副主任医师/主治医师等
    private String workSchedule;       // 工作班次（护士）
    private String certification;      // 资格证书
} 