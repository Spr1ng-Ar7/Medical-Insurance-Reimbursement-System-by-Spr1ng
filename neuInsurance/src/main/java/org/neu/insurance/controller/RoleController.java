package org.neu.insurance.controller;

import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.RoleDTO;
import org.neu.insurance.entity.Role;
import org.neu.insurance.entity.Permission;
import org.neu.insurance.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "*")
public class RoleController {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
    
    @Autowired
    private RoleService roleService;
    
    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public Result<PageResponse<Role>> getRoleList(
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) Integer status,
            PageRequest pageRequest) {
        
        logger.info("分页查询角色列表，参数：roleName={}, roleCode={}, status={}, pageNum={}, pageSize={}", 
                   roleName, roleCode, status, pageRequest.getPageNum(), pageRequest.getPageSize());
        
        try {
            PageResponse<Role> response = roleService.getRoleList(roleName, roleCode, status, pageRequest);
            logger.info("分页查询角色列表成功，总记录数：{}", response.getTotal());
            return Result.success("查询成功", response);
            
        } catch (Exception e) {
            logger.error("分页查询角色列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取角色详情
     */
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable Long id) {
        logger.info("根据ID获取角色详情，ID：{}", id);
        
        try {
            Role role = roleService.getRoleById(id);
            logger.info("获取角色详情成功，角色名称：{}", role.getRoleName());
            return Result.success("查询成功", role);
            
        } catch (Exception e) {
            logger.error("获取角色详情失败，ID：{}", id, e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 新增角色
     */
    @PostMapping
    public Result<Void> addRole(@RequestBody RoleDTO roleDTO) {
        logger.info("新增角色，角色编码：{}，角色名称：{}", roleDTO.getRoleCode(), roleDTO.getRoleName());
        
        try {
            roleService.addRole(roleDTO);
            logger.info("新增角色成功，角色ID：{}", roleDTO.getId());
            return Result.success("新增成功", null);
            
        } catch (Exception e) {
            logger.error("新增角色失败，角色编码：{}", roleDTO.getRoleCode(), e);
            return Result.error("新增失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新角色信息
     */
    @PutMapping("/{id}")
    public Result<Void> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        logger.info("更新角色信息，角色ID：{}", id);
        
        try {
            roleDTO.setId(id);
            roleService.updateRole(roleDTO);
            logger.info("更新角色信息成功，角色ID：{}", id);
            return Result.success("更新成功", null);
            
        } catch (Exception e) {
            logger.error("更新角色信息失败，角色ID：{}", id, e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        logger.info("删除角色，角色ID：{}", id);
        
        try {
            roleService.deleteRole(id);
            logger.info("删除角色成功，角色ID：{}", id);
            return Result.success("删除成功", null);
            
        } catch (Exception e) {
            logger.error("删除角色失败，角色ID：{}", id, e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 批量删除角色
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDeleteRole(@RequestBody List<Long> ids) {
        logger.info("批量删除角色，角色ID列表：{}", ids);
        
        try {
            roleService.batchDeleteRole(ids);
            logger.info("批量删除角色成功，删除数量：{}", ids.size());
            return Result.success("批量删除成功", null);
            
        } catch (Exception e) {
            logger.error("批量删除角色失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用角色
     */
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        logger.info("更新角色状态，角色ID：{}，状态：{}", id, status);
        
        try {
            roleService.updateStatus(id, status);
            String statusText = status == 1 ? "启用" : "禁用";
            logger.info("更新角色状态成功，角色ID：{}，状态：{}", id, statusText);
            return Result.success(statusText + "成功", null);
            
        } catch (Exception e) {
            logger.error("更新角色状态失败，角色ID：{}", id, e);
            return Result.error("状态更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取所有有效角色列表（用于下拉选择）
     */
    @GetMapping("/all")
    public Result<List<Role>> getAllRoles() {
        logger.info("获取所有有效角色列表");
        
        try {
            List<Role> roles = roleService.getAllRoles();
            logger.info("获取所有有效角色列表成功，角色数量：{}", roles.size());
            return Result.success("查询成功", roles);
            
        } catch (Exception e) {
            logger.error("获取所有有效角色列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取角色的权限列表
     */
    @GetMapping("/{id}/permissions")
    public Result<List<Permission>> getRolePermissions(@PathVariable Long id) {
        logger.info("获取角色权限列表，角色ID：{}", id);
        
        try {
            List<Permission> permissions = roleService.getRolePermissions(id);
            logger.info("获取角色权限列表成功，角色ID：{}，权限数量：{}", id, permissions.size());
            return Result.success("查询成功", permissions);
            
        } catch (Exception e) {
            logger.error("获取角色权限列表失败，角色ID：{}", id, e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 为角色分配权限
     */
    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        logger.info("为角色分配权限，角色ID：{}，权限数量：{}", id, permissionIds != null ? permissionIds.size() : 0);
        
        try {
            roleService.assignPermissions(id, permissionIds);
            logger.info("为角色分配权限成功，角色ID：{}", id);
            return Result.success("权限分配成功", null);
            
        } catch (Exception e) {
            logger.error("为角色分配权限失败，角色ID：{}", id, e);
            return Result.error("权限分配失败：" + e.getMessage());
        }
    }
    
    /**
     * 检查角色编码是否唯一
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkRoleCodeUnique(@RequestParam String roleCode, 
                                             @RequestParam(required = false) Long excludeId) {
        logger.info("检查角色编码唯一性，角色编码：{}，排除ID：{}", roleCode, excludeId);
        
        try {
            boolean isUnique = roleService.isRoleCodeUnique(roleCode, excludeId);
            logger.info("角色编码唯一性检查完成，角色编码：{}，是否唯一：{}", roleCode, isUnique);
            return Result.success("检查完成", isUnique);
            
        } catch (Exception e) {
            logger.error("检查角色编码唯一性失败，角色编码：{}", roleCode, e);
            return Result.error("检查失败：" + e.getMessage());
        }
    }
} 