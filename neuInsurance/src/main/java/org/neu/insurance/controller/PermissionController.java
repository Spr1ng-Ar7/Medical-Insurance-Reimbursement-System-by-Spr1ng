package org.neu.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.neu.insurance.dto.Result;
import org.neu.insurance.entity.Permission;
import org.neu.insurance.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@Tag(name = "权限管理", description = "权限管理相关接口")
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    
    @Autowired
    private PermissionService permissionService;
    
    
    @Operation(summary = "获取权限树")
    @GetMapping("/tree")
    public Result<List<Permission>> getPermissionTree() {
        logger.info("获取权限树");
        
        try {
            List<Permission> tree = permissionService.getPermissionTree();
            return Result.success("获取权限树成功",tree);
            
        } catch (Exception e) {
            logger.error("获取权限树失败", e);
            return Result.error("获取权限树失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "获取用户权限树")
    @GetMapping("/user/{userId}/tree")
    public Result<List<Permission>> getUserPermissionTree(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        logger.info("获取用户权限树，用户ID：{}", userId);
        
        try {
            List<Permission> tree = permissionService.getUserPermissionTree(userId);
            return Result.success("获取用户权限树成功",tree);
            
        } catch (Exception e) {
            logger.error("获取用户权限树失败，用户ID：{}", userId, e);
            return Result.error("获取用户权限树失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "获取角色权限树")
    @GetMapping("/role/{roleId}/tree")
    public Result<List<Permission>> getRolePermissionTree(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        logger.info("获取角色权限树，角色ID：{}", roleId);
        
        try {
            List<Permission> tree = permissionService.getRolePermissionTree(roleId);
            return Result.success("获取角色权限树成功",tree);
            
        } catch (Exception e) {
            logger.error("获取角色权限树失败，角色ID：{}", roleId, e);
            return Result.error("获取角色权限树失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "根据ID获取权限")
    @GetMapping("/{id}")
    public Result<Permission> getPermissionById(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        logger.info("根据ID获取权限，ID：{}", id);
        
        try {
            Permission permission = permissionService.getPermissionById(id);
            return Result.success("获取权限成功",permission);
            
        } catch (Exception e) {
            logger.error("获取权限失败，ID：{}", id, e);
            return Result.error("获取权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "根据编码获取权限")
    @GetMapping("/code/{permissionCode}")
    public Result<Permission> getPermissionByCode(
            @Parameter(description = "权限编码") @PathVariable String permissionCode) {
        logger.info("根据编码获取权限，编码：{}", permissionCode);
        
        try {
            Permission permission = permissionService.getPermissionByCode(permissionCode);
            return Result.success("获取权限成功",permission);
            
        } catch (Exception e) {
            logger.error("获取权限失败，编码：{}", permissionCode, e);
            return Result.error("获取权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "获取所有权限列表")
    @GetMapping("/list")
    public Result<List<Permission>> getAllPermissions() {
        logger.info("获取所有权限列表");
        
        try {
            List<Permission> permissions = permissionService.getAllPermissions();
            return Result.success( "获取权限列表成功",permissions);
            
        } catch (Exception e) {
            logger.error("获取权限列表失败", e);
            return Result.error("获取权限列表失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "获取根权限列表")
    @GetMapping("/root")
    public Result<List<Permission>> getRootPermissions() {
        logger.info("获取根权限列表");
        
        try {
            List<Permission> permissions = permissionService.getRootPermissions();
            return Result.success("获取根权限列表成功",permissions );
            
        } catch (Exception e) {
            logger.error("获取根权限列表失败", e);
            return Result.error("获取根权限列表失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "根据父权限ID获取子权限")
    @GetMapping("/parent/{parentId}/children")
    public Result<List<Permission>> getPermissionsByParentId(
            @Parameter(description = "父权限ID") @PathVariable Long parentId) {
        logger.info("根据父权限ID获取子权限，父权限ID：{}", parentId);
        
        try {
            List<Permission> permissions = permissionService.getPermissionsByParentId(parentId);
            return Result.success("获取子权限成功",permissions );
            
        } catch (Exception e) {
            logger.error("获取子权限失败，父权限ID：{}", parentId, e);
            return Result.error("获取子权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "根据资源类型获取权限")
    @GetMapping("/type/{resourceType}")
    public Result<List<Permission>> getPermissionsByResourceType(
            @Parameter(description = "资源类型") @PathVariable String resourceType) {
        logger.info("根据资源类型获取权限，资源类型：{}", resourceType);
        
        try {
            List<Permission> permissions = permissionService.getPermissionsByResourceType(resourceType);
            return Result.success( "获取权限成功",permissions);
            
        } catch (Exception e) {
            logger.error("获取权限失败，资源类型：{}", resourceType, e);
            return Result.error("获取权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "新增权限")
    @PostMapping
    public Result<Void> addPermission(@RequestBody Permission permission) {
        logger.info("新增权限，权限编码：{}，权限名称：{}", permission.getPermissionCode(), permission.getPermissionName());
        
        try {
            permissionService.addPermission(permission);
            return Result.success( "新增权限成功",null);
            
        } catch (Exception e) {
            logger.error("新增权限失败，权限编码：{}", permission.getPermissionCode(), e);
            return Result.error("新增权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "更新权限")
    @PutMapping("/{id}")
    public Result<Void> updatePermission(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @RequestBody Permission permission) {
        logger.info("更新权限，权限ID：{}，权限编码：{}", id, permission.getPermissionCode());
        
        try {
            permission.setId(id);
            permissionService.updatePermission(permission);
            return Result.success("更新权限成功",null );
            
        } catch (Exception e) {
            logger.error("更新权限失败，权限ID：{}", id, e);
            return Result.error("更新权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "删除权限")
    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        logger.info("删除权限，权限ID：{}", id);
        
        try {
            permissionService.deletePermission(id);
            return Result.success("删除权限成功",null);
            
        } catch (Exception e) {
            logger.error("删除权限失败，权限ID：{}", id, e);
            return Result.error("删除权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "批量删除权限")
    @DeleteMapping("/batch")
    public Result<Void> batchDeletePermissions(@RequestBody List<Long> ids) {
        logger.info("批量删除权限，权限ID列表：{}", ids);
        
        try {
            permissionService.batchDeletePermissions(ids);
            return Result.success("批量删除权限成功",null);
            
        } catch (Exception e) {
            logger.error("批量删除权限失败", e);
            return Result.error("批量删除权限失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "更新权限状态")
    @PutMapping("/{id}/status")
    public Result<Void> updatePermissionStatus(
            @Parameter(description = "权限ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Integer status) {
        logger.info("更新权限状态，权限ID：{}，状态：{}", id, status);
        
        try {
            permissionService.updatePermissionStatus(id, status);
            return Result.success( "更新权限状态成功",null);
            
        } catch (Exception e) {
            logger.error("更新权限状态失败，权限ID：{}", id, e);
            return Result.error("更新权限状态失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "检查权限编码是否唯一")
    @GetMapping("/check/code")
    public Result<Boolean> checkPermissionCodeUnique(
            @Parameter(description = "权限编码") @RequestParam String permissionCode,
            @Parameter(description = "排除的权限ID") @RequestParam(required = false) Long excludeId) {
        logger.info("检查权限编码唯一性，权限编码：{}，排除ID：{}", permissionCode, excludeId);
        
        try {
            boolean isUnique = permissionService.isPermissionCodeUnique(permissionCode, excludeId);
            return Result.success("检查权限编码唯一性成功",isUnique);
            
        } catch (Exception e) {
            logger.error("检查权限编码唯一性失败，权限编码：{}", permissionCode, e);
            return Result.error("检查权限编码唯一性失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "检查权限路径是否唯一")
    @GetMapping("/check/path")
    public Result<Boolean> checkResourcePathUnique(
            @Parameter(description = "资源路径") @RequestParam String resourcePath,
            @Parameter(description = "HTTP方法") @RequestParam String httpMethod,
            @Parameter(description = "排除的权限ID") @RequestParam(required = false) Long excludeId) {
        logger.info("检查权限路径唯一性，路径：{}，方法：{}，排除ID：{}", resourcePath, httpMethod, excludeId);
        
        try {
            boolean isUnique = permissionService.isResourcePathUnique(resourcePath, httpMethod, excludeId);
            return Result.success( "检查权限路径唯一性成功",isUnique);
            
        } catch (Exception e) {
            logger.error("检查权限路径唯一性失败，路径：{}，方法：{}", resourcePath, httpMethod, e);
            return Result.error("检查权限路径唯一性失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "获取权限的所有子权限ID")
    @GetMapping("/{parentId}/children/ids")
    public Result<List<Long>> getAllChildIds(
            @Parameter(description = "父权限ID") @PathVariable Long parentId) {
        logger.info("获取权限的所有子权限ID，父权限ID：{}", parentId);
        
        try {
            List<Long> childIds = permissionService.getAllChildIds(parentId);
            return Result.success("获取子权限ID成功",childIds);
            
        } catch (Exception e) {
            logger.error("获取子权限ID失败，父权限ID：{}", parentId, e);
            return Result.error("获取子权限ID失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "获取权限的所有父权限ID")
    @GetMapping("/{permissionId}/parents/ids")
    public Result<List<Long>> getAllParentIds(
            @Parameter(description = "权限ID") @PathVariable Long permissionId) {
        logger.info("获取权限的所有父权限ID，权限ID：{}", permissionId);
        
        try {
            List<Long> parentIds = permissionService.getAllParentIds(permissionId);
            return Result.success("获取父权限ID成功",parentIds);
            
        } catch (Exception e) {
            logger.error("获取父权限ID失败，权限ID：{}", permissionId, e);
            return Result.error("获取父权限ID失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "验证权限树结构")
    @GetMapping("/validate")
    public Result<Boolean> validatePermissionTree() {
        logger.info("验证权限树结构");
        
        try {
            boolean isValid = permissionService.validatePermissionTree();
            return Result.success( "验证权限树结构完成",isValid);
            
        } catch (Exception e) {
            logger.error("验证权限树结构失败", e);
            return Result.error("验证权限树结构失败：" + e.getMessage());
        }
    }
    
    
    @Operation(summary = "重新计算权限层级")
    @PostMapping("/recalculate-levels")
    public Result<Void> recalculatePermissionLevels() {
        logger.info("重新计算权限层级");
        
        try {
            permissionService.recalculatePermissionLevels();
            return Result.success("重新计算权限层级成功",null);
            
        } catch (Exception e) {
            logger.error("重新计算权限层级失败", e);
            return Result.error("重新计算权限层级失败：" + e.getMessage());
        }
    }
} 