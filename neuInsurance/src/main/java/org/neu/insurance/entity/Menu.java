package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单实体类
 */
@Data
public class Menu {
    private Long id;
    private Long parentId;            // 父菜单ID，根菜单为0
    private String menuName;          // 菜单名称
    private String menuCode;          // 菜单编码
    private String menuPath;          // 菜单路径
    private String component;         // 组件路径
    private String icon;              // 菜单图标
    private Integer orderNum;         // 显示顺序
    private String menuType;          // 菜单类型：M(目录)、C(菜单)、F(按钮)
    private String visible;           // 是否显示：0(显示)、1(隐藏)
    private String perms;             // 权限标识
    private String query;             // 路由参数
    private String isFrame;           // 是否为外链：0(否)、1(是)
    private String isCache;           // 是否缓存：0(缓存)、1(不缓存)
    private Integer status;           // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;          // 创建人
    private String updateBy;          // 更新人
    private String remark;            // 备注
    
    // 子菜单列表
    private List<Menu> children;
} 