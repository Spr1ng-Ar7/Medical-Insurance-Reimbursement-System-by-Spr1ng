package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限实体类
 */
@Data
public class Permission {
    private Long id;
    private String permissionCode;     // 权限编码
    private String permissionName;     // 权限名称
    private String resourceType;       // 资源类型：menu-菜单，button-按钮，api-接口
    private String resourcePath;       // 资源路径
    private String httpMethod;         // HTTP方法：GET,POST,PUT,DELETE
    private Long parentId;             // 父权限ID
    private Integer level;             // 层级
    private Integer sortOrder;         // 排序
    private String icon;               // 图标
    private Integer status;            // 状态：1-正常，0-禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
    
    // 权限树相关字段
    private List<Permission> children; // 子权限列表
    private Boolean isLeaf;            // 是否为叶子节点
    private Boolean isExpanded;        // 是否展开
    private Boolean isSelected;        // 是否选中
    private Boolean isChecked;         // 是否勾选（用于角色权限分配）
    
    // 权限树构建方法
    public void addChild(Permission child) {
        if (this.children == null) {
            this.children = new java.util.ArrayList<>();
        }
        this.children.add(child);
    }
    
    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }
    
    public boolean isRoot() {
        return parentId == null || parentId == 0;
    }
    
    public String getFullPath() {
        return resourcePath != null ? resourcePath : "";
    }
    
    public String getDisplayName() {
        return permissionName != null ? permissionName : permissionCode;
    }
}