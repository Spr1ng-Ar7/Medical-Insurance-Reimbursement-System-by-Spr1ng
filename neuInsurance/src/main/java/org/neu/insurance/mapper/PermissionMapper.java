package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.Permission;
import java.util.List;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper {
    
    /**
     * 查询用户的所有权限
     */
    List<Permission> selectUserPermissions(@Param("userId") Long userId);
    
    /**
     * 根据ID查询权限
     */
    Permission selectPermissionById(@Param("id") Long id);
    
    /**
     * 查询所有权限
     */
    List<Permission> selectAllPermissions();
    
    /**
     * 查询所有有效权限（用于构建权限树）
     */
    List<Permission> selectAllValidPermissions();
    
    /**
     * 根据角色ID查询权限
     */
    List<Permission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据父权限ID查询子权限
     */
    List<Permission> selectPermissionsByParentId(@Param("parentId") Long parentId);
    
    /**
     * 查询根权限（顶级权限）
     */
    List<Permission> selectRootPermissions();
    
    /**
     * 根据权限编码查询权限
     */
    Permission selectPermissionByCode(@Param("permissionCode") String permissionCode);
    
    /**
     * 根据资源路径查询权限
     */
    List<Permission> selectPermissionsByResourcePath(@Param("resourcePath") String resourcePath);
    
    /**
     * 根据资源类型查询权限
     */
    List<Permission> selectPermissionsByResourceType(@Param("resourceType") String resourceType);
    
    /**
     * 查询权限的子权限数量
     */
    int countChildrenByParentId(@Param("parentId") Long parentId);
    
    /**
     * 查询权限的层级
     */
    int getPermissionLevel(@Param("permissionId") Long permissionId);
    
    /**
     * 新增权限
     */
    int insertPermission(Permission permission);
    
    /**
     * 更新权限
     */
    int updatePermission(Permission permission);
    
    /**
     * 删除权限
     */
    int deletePermissionById(@Param("id") Long id);
    
    /**
     * 批量删除权限
     */
    int batchDeletePermissions(@Param("ids") List<Long> ids);
    
    /**
     * 更新权限状态
     */
    int updatePermissionStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 检查权限编码是否存在
     */
    int countByPermissionCode(@Param("permissionCode") String permissionCode, @Param("excludeId") Long excludeId);
    
    /**
     * 检查权限路径是否存在
     */
    int countByResourcePath(@Param("resourcePath") String resourcePath, @Param("httpMethod") String httpMethod, @Param("excludeId") Long excludeId);
    
    /**
     * 获取权限的所有子权限ID
     */
    List<Long> selectAllChildIds(@Param("parentId") Long parentId);
    
    /**
     * 获取权限的所有父权限ID
     */
    List<Long> selectAllParentIds(@Param("permissionId") Long permissionId);
}
