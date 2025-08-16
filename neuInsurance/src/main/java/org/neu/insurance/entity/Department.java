package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门实体类
 */
@Data
public class Department {
    private Long id;
    private String deptCode;           // 部门编码
    private String deptName;           // 部门名称
    private Long parentId;             // 父部门ID
    private Integer level;             // 层级
    private String leader;             // 负责人
    private String phone;              // 联系电话
    private String email;              // 邮箱
    private Integer sortOrder;         // 排序
    private Integer status;            // 状态：1-正常，0-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
    
    // 子部门（数据库映射时使用）
    private List<Department> children;
} 