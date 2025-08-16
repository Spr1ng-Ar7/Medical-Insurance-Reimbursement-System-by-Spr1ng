package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色实体类
 */
@Data
public class Role {
    private Long id;
    private String roleCode;           // 角色编码
    private String roleName;           // 角色名称
    private String description;        // 角色描述
    private Integer status;            // 状态：1-正常，0-禁用
    private Integer sortOrder;         // 排序
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
    
    // 关联权限（数据库映射时使用）
    private List<Permission> permissions;
} 