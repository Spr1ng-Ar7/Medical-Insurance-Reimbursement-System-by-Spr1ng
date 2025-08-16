package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.RoleDTO;
import org.neu.insurance.entity.Role;
import org.neu.insurance.entity.Permission;
import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
    
    /**
     * 分页查询角色列表
     */
    PageResponse<Role> getRoleList(String roleName, String roleCode, Integer status, PageRequest pageRequest);
    
    /**
     * 根据ID获取角色详情
     */
    Role getRoleById(Long id);
    
    /**
     * 新增角色
     */
    void addRole(RoleDTO role);
    
    /**
     * 更新角色信息
     */
    void updateRole(RoleDTO role);
    
    /**
     * 删除角色
     */
    void deleteRole(Long id);
    
    /**
     * 批量删除角色
     */
    void batchDeleteRole(List<Long> ids);
    
    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 获取所有有效角色列表
     */
    List<Role> getAllRoles();
    
    /**
     * 获取角色的权限列表
     */
    List<Permission> getRolePermissions(Long id);
    
    /**
     * 为角色分配权限
     */
    void assignPermissions(Long id, List<Long> permissionIds);
    
    /**
     * 验证角色编码是否唯一
     */
    boolean isRoleCodeUnique(String roleCode, Long excludeId);
} 