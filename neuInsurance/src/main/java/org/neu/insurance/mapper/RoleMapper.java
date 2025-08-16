package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.dto.RoleDTO;
import org.neu.insurance.entity.Role;
import java.util.List;

/**
 * 角色Mapper
 */
@Mapper
public interface RoleMapper {
    
    /**
     * 根据ID查询角色
     */
    Role selectRoleById(@Param("id") Long id);
    
    /**
     * 根据ID列表批量查询角色
     */
    List<Role> selectRolesByIds(@Param("ids") List<Long> ids);
    
    /**
     * 分页查询角色列表
     */
    List<Role> selectRoleList(@Param("roleName") String roleName, 
                             @Param("roleCode") String roleCode, 
                             @Param("status") Integer status,
                             @Param("offset") int offset, 
                             @Param("pageSize") int pageSize);
    
    /**
     * 统计角色总数
     */
    int countRoles(@Param("roleName") String roleName, 
                  @Param("roleCode") String roleCode, 
                  @Param("status") Integer status);
    
    /**
     * 查询所有有效角色
     */
    List<Role> selectAllRoles();
    
    /**
     * 新增角色
     */
    int insertRole(RoleDTO role);
    
    /**
     * 更新角色
     */
    int updateRole(RoleDTO role);
    
    /**
     * 删除角色
     */
    int deleteRoleById(@Param("id") Long id);
    
    /**
     * 批量删除角色
     */
    int batchDeleteRoles(@Param("ids") List<Long> ids);
    
    /**
     * 更新角色状态
     */
    int updateRoleStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 检查角色编码是否存在
     */
    int countByRoleCode(@Param("roleCode") String roleCode, @Param("excludeId") Long excludeId);
    
    /**
     * 删除角色权限关联
     */
    int deleteRolePermissions(@Param("roleId") Long roleId);
    
    /**
     * 批量插入角色权限关联
     */
    int insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
    
    /**
     * 查询角色权限ID列表
     */
    List<Long> selectRolePermissionIds(@Param("roleId") Long roleId);
} 