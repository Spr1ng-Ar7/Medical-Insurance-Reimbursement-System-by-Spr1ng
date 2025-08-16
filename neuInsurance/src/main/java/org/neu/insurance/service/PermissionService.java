package org.neu.insurance.service;

import org.neu.insurance.entity.Permission;
import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    /**
     * 获取权限树
     */
    List<Permission> getPermissionTree();
    
    /**
     * 获取用户权限树
     */
    List<Permission> getUserPermissionTree(Long userId);
    
    /**
     * 获取角色权限树
     */
    List<Permission> getRolePermissionTree(Long roleId);
    
    /**
     * 根据ID获取权限
     */
    Permission getPermissionById(Long id);
    
    /**
     * 根据编码获取权限
     */
    Permission getPermissionByCode(String permissionCode);
    
    /**
     * 获取所有权限列表
     */
    List<Permission> getAllPermissions();
    
    /**
     * 获取根权限列表
     */
    List<Permission> getRootPermissions();
    
    /**
     * 根据父权限ID获取子权限
     */
    List<Permission> getPermissionsByParentId(Long parentId);
    
    /**
     * 根据资源类型获取权限
     */
    List<Permission> getPermissionsByResourceType(String resourceType);
    
    /**
     * 新增权限
     */
    void addPermission(Permission permission);
    
    /**
     * 更新权限
     */
    void updatePermission(Permission permission);
    
    /**
     * 删除权限
     */
    void deletePermission(Long id);
    
    /**
     * 批量删除权限
     */
    void batchDeletePermissions(List<Long> ids);
    
    /**
     * 更新权限状态
     */
    void updatePermissionStatus(Long id, Integer status);
    
    /**
     * 检查权限编码是否唯一
     */
    boolean isPermissionCodeUnique(String permissionCode, Long excludeId);
    
    /**
     * 检查权限路径是否唯一
     */
    boolean isResourcePathUnique(String resourcePath, String httpMethod, Long excludeId);
    
    /**
     * 获取权限的所有子权限ID
     */
    List<Long> getAllChildIds(Long parentId);
    
    /**
     * 获取权限的所有父权限ID
     */
    List<Long> getAllParentIds(Long permissionId);
    
    /**
     * 验证权限树结构
     */
    boolean validatePermissionTree();
    
    /**
     * 重新计算权限层级
     */
    void recalculatePermissionLevels();
} 