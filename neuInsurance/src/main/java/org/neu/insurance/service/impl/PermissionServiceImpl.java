package org.neu.insurance.service.impl;

import org.neu.insurance.entity.Permission;
import org.neu.insurance.mapper.PermissionMapper;
import org.neu.insurance.service.PermissionService;
import org.neu.insurance.util.PermissionTreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Override
    public List<Permission> getPermissionTree() {
        logger.info("获取权限树");
        
        try {
            List<Permission> permissions = permissionMapper.selectAllValidPermissions();
            List<Permission> tree = PermissionTreeUtil.buildTree(permissions);
            
            logger.info("获取权限树成功，权限数量：{}", permissions.size());
            return tree;
            
        } catch (Exception e) {
            logger.error("获取权限树失败", e);
            throw new RuntimeException("获取权限树失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Permission> getUserPermissionTree(Long userId) {
        logger.info("获取用户权限树，用户ID：{}", userId);
        
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        try {
            List<Permission> permissions = permissionMapper.selectUserPermissions(userId);
            List<Permission> tree = PermissionTreeUtil.buildTree(permissions);
            
            logger.info("获取用户权限树成功，用户ID：{}，权限数量：{}", userId, permissions.size());
            return tree;
            
        } catch (Exception e) {
            logger.error("获取用户权限树失败，用户ID：{}", userId, e);
            throw new RuntimeException("获取用户权限树失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Permission> getRolePermissionTree(Long roleId) {
        logger.info("获取角色权限树，角色ID：{}", roleId);
        
        if (roleId == null) {
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        try {
            List<Permission> permissions = permissionMapper.selectPermissionsByRoleId(roleId);
            List<Permission> tree = PermissionTreeUtil.buildTree(permissions);
            
            logger.info("获取角色权限树成功，角色ID：{}，权限数量：{}", roleId, permissions.size());
            return tree;
            
        } catch (Exception e) {
            logger.error("获取角色权限树失败，角色ID：{}", roleId, e);
            throw new RuntimeException("获取角色权限树失败：" + e.getMessage());
        }
    }
    
    @Override
    public Permission getPermissionById(Long id) {
        logger.info("根据ID获取权限，ID：{}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("权限ID不能为空");
        }
        
        try {
            Permission permission = permissionMapper.selectPermissionById(id);
            if (permission == null) {
                logger.warn("权限不存在，ID：{}", id);
                throw new IllegalArgumentException("权限不存在，ID: " + id);
            }
            
            logger.info("获取权限成功，权限名称：{}", permission.getPermissionName());
            return permission;
            
        } catch (Exception e) {
            logger.error("获取权限失败，ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("获取权限失败：" + e.getMessage());
        }
    }
    
    @Override
    public Permission getPermissionByCode(String permissionCode) {
        logger.info("根据编码获取权限，编码：{}", permissionCode);
        
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            throw new IllegalArgumentException("权限编码不能为空");
        }
        
        try {
            Permission permission = permissionMapper.selectPermissionByCode(permissionCode);
            logger.info("根据编码获取权限成功，编码：{}", permissionCode);
            return permission;
            
        } catch (Exception e) {
            logger.error("根据编码获取权限失败，编码：{}", permissionCode, e);
            throw new RuntimeException("获取权限失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Permission> getAllPermissions() {
        logger.info("获取所有权限列表");
        
        try {
            List<Permission> permissions = permissionMapper.selectAllPermissions();
            logger.info("获取所有权限列表成功，权限数量：{}", permissions.size());
            return permissions;
            
        } catch (Exception e) {
            logger.error("获取所有权限列表失败", e);
            throw new RuntimeException("获取权限列表失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Permission> getRootPermissions() {
        logger.info("获取根权限列表");
        
        try {
            List<Permission> permissions = permissionMapper.selectRootPermissions();
            logger.info("获取根权限列表成功，权限数量：{}", permissions.size());
            return permissions;
            
        } catch (Exception e) {
            logger.error("获取根权限列表失败", e);
            throw new RuntimeException("获取根权限列表失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Permission> getPermissionsByParentId(Long parentId) {
        logger.info("根据父权限ID获取子权限，父权限ID：{}", parentId);
        
        try {
            List<Permission> permissions = permissionMapper.selectPermissionsByParentId(parentId);
            logger.info("获取子权限成功，父权限ID：{}，子权限数量：{}", parentId, permissions.size());
            return permissions;
            
        } catch (Exception e) {
            logger.error("获取子权限失败，父权限ID：{}", parentId, e);
            throw new RuntimeException("获取子权限失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Permission> getPermissionsByResourceType(String resourceType) {
        logger.info("根据资源类型获取权限，资源类型：{}", resourceType);
        
        if (resourceType == null || resourceType.trim().isEmpty()) {
            throw new IllegalArgumentException("资源类型不能为空");
        }
        
        try {
            List<Permission> permissions = permissionMapper.selectPermissionsByResourceType(resourceType);
            logger.info("根据资源类型获取权限成功，资源类型：{}，权限数量：{}", resourceType, permissions.size());
            return permissions;
            
        } catch (Exception e) {
            logger.error("根据资源类型获取权限失败，资源类型：{}", resourceType, e);
            throw new RuntimeException("获取权限失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void addPermission(Permission permission) {
        logger.info("新增权限，权限编码：{}，权限名称：{}", permission.getPermissionCode(), permission.getPermissionName());
        
        if (permission == null) {
            throw new IllegalArgumentException("权限信息不能为空");
        }
        
        try {
            // 验证权限编码唯一性
            if (!isPermissionCodeUnique(permission.getPermissionCode(), null)) {
                logger.warn("权限编码已存在：{}", permission.getPermissionCode());
                throw new IllegalArgumentException("权限编码已存在：" + permission.getPermissionCode());
            }
            
            // 验证权限路径唯一性
            if (permission.getResourcePath() != null && permission.getHttpMethod() != null) {
                if (!isResourcePathUnique(permission.getResourcePath(), permission.getHttpMethod(), null)) {
                    logger.warn("权限路径已存在：{} {}", permission.getHttpMethod(), permission.getResourcePath());
                    throw new IllegalArgumentException("权限路径已存在：" + permission.getHttpMethod() + " " + permission.getResourcePath());
                }
            }
            
            // 设置默认值
            if (permission.getStatus() == null) {
                permission.setStatus(1); // 默认启用
            }
            if (permission.getSortOrder() == null) {
                permission.setSortOrder(0); // 默认排序
            }
            if (permission.getLevel() == null) {
                permission.setLevel(calculatePermissionLevel(permission.getParentId()));
            }
            
            // 新增权限
            int result = permissionMapper.insertPermission(permission);
            if (result <= 0) {
                logger.error("新增权限失败，权限编码：{}", permission.getPermissionCode());
                throw new RuntimeException("新增权限失败");
            }
            
            logger.info("新增权限成功，权限ID：{}", permission.getId());
            
        } catch (Exception e) {
            logger.error("新增权限失败，权限编码：{}", permission.getPermissionCode(), e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("新增权限失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updatePermission(Permission permission) {
        logger.info("更新权限，权限ID：{}，权限编码：{}", permission.getId(), permission.getPermissionCode());
        
        if (permission == null || permission.getId() == null) {
            throw new IllegalArgumentException("权限信息或ID不能为空");
        }
        
        try {
            // 检查权限是否存在
            Permission existingPermission = getPermissionById(permission.getId());
            
            // 验证权限编码唯一性（排除当前权限）
            if (!isPermissionCodeUnique(permission.getPermissionCode(), permission.getId())) {
                logger.warn("权限编码已存在：{}", permission.getPermissionCode());
                throw new IllegalArgumentException("权限编码已存在：" + permission.getPermissionCode());
            }
            
            // 验证权限路径唯一性（排除当前权限）
            if (permission.getResourcePath() != null && permission.getHttpMethod() != null) {
                if (!isResourcePathUnique(permission.getResourcePath(), permission.getHttpMethod(), permission.getId())) {
                    logger.warn("权限路径已存在：{} {}", permission.getHttpMethod(), permission.getResourcePath());
                    throw new IllegalArgumentException("权限路径已存在：" + permission.getHttpMethod() + " " + permission.getResourcePath());
                }
            }
            
            // 更新权限
            int result = permissionMapper.updatePermission(permission);
            if (result <= 0) {
                logger.error("更新权限失败，权限ID：{}", permission.getId());
                throw new RuntimeException("更新权限失败");
            }
            
            logger.info("更新权限成功，权限ID：{}", permission.getId());
            
        } catch (Exception e) {
            logger.error("更新权限失败，权限ID：{}", permission.getId(), e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("更新权限失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deletePermission(Long id) {
        logger.info("删除权限，权限ID：{}", id);
        
        if (id == null) {
            throw new IllegalArgumentException("权限ID不能为空");
        }
        
        try {
            Permission permission = getPermissionById(id);
            
            // 检查是否有子权限
            int childCount = permissionMapper.countChildrenByParentId(id);
            if (childCount > 0) {
                logger.warn("权限有子权限，不能删除，权限ID：{}，子权限数量：{}", id, childCount);
                throw new IllegalArgumentException("权限有子权限，不能删除");
            }
            
            // 删除权限
            int result = permissionMapper.deletePermissionById(id);
            if (result <= 0) {
                logger.error("删除权限失败，权限ID：{}", id);
                throw new RuntimeException("删除权限失败");
            }
            
            logger.info("删除权限成功，权限ID：{}", id);
            
        } catch (Exception e) {
            logger.error("删除权限失败，权限ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("删除权限失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeletePermissions(List<Long> ids) {
        logger.info("批量删除权限，权限ID列表：{}", ids);
        
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("权限ID列表不能为空");
        }
        
        try {
            // 检查是否有子权限
            for (Long id : ids) {
                int childCount = permissionMapper.countChildrenByParentId(id);
                if (childCount > 0) {
                    logger.warn("权限有子权限，不能删除，权限ID：{}", id);
                    throw new IllegalArgumentException("权限ID " + id + " 有子权限，不能删除");
                }
            }
            
            // 批量删除权限
            int deletedCount = permissionMapper.batchDeletePermissions(ids);
            if (deletedCount != ids.size()) {
                logger.error("批量删除权限失败，预期删除{}条，实际删除{}条", ids.size(), deletedCount);
                throw new RuntimeException("批量删除权限失败，预期删除" + ids.size() + "条，实际删除" + deletedCount + "条");
            }
            
            logger.info("批量删除权限成功，删除数量：{}", deletedCount);
            
        } catch (Exception e) {
            logger.error("批量删除权限失败", e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("批量删除权限失败：" + e.getMessage());
        }
    }
    
    @Override
    public void updatePermissionStatus(Long id, Integer status) {
        logger.info("更新权限状态，权限ID：{}，状态：{}", id, status);
        
        if (id == null) {
            throw new IllegalArgumentException("权限ID不能为空");
        }
        
        if (status == null || (status != 0 && status != 1)) {
            throw new IllegalArgumentException("状态值无效，只能是0或1");
        }
        
        try {
            // 检查权限是否存在
            getPermissionById(id);
            
            int result = permissionMapper.updatePermissionStatus(id, status);
            if (result <= 0) {
                logger.error("更新权限状态失败，权限ID：{}", id);
                throw new RuntimeException("更新权限状态失败");
            }
            
            logger.info("更新权限状态成功，权限ID：{}，状态：{}", id, status);
            
        } catch (Exception e) {
            logger.error("更新权限状态失败，权限ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("更新权限状态失败：" + e.getMessage());
        }
    }
    
    @Override
    public boolean isPermissionCodeUnique(String permissionCode, Long excludeId) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }
        
        try {
            int count = permissionMapper.countByPermissionCode(permissionCode, excludeId);
            return count == 0;
        } catch (Exception e) {
            logger.error("检查权限编码唯一性失败，权限编码：{}", permissionCode, e);
            return false;
        }
    }
    
    @Override
    public boolean isResourcePathUnique(String resourcePath, String httpMethod, Long excludeId) {
        if (resourcePath == null || resourcePath.trim().isEmpty() || httpMethod == null || httpMethod.trim().isEmpty()) {
            return false;
        }
        
        try {
            int count = permissionMapper.countByResourcePath(resourcePath, httpMethod, excludeId);
            return count == 0;
        } catch (Exception e) {
            logger.error("检查权限路径唯一性失败，路径：{}，方法：{}", resourcePath, httpMethod, e);
            return false;
        }
    }
    
    @Override
    public List<Long> getAllChildIds(Long parentId) {
        logger.info("获取权限的所有子权限ID，父权限ID：{}", parentId);
        
        try {
            List<Long> childIds = permissionMapper.selectAllChildIds(parentId);
            logger.info("获取子权限ID成功，父权限ID：{}，子权限数量：{}", parentId, childIds.size());
            return childIds;
            
        } catch (Exception e) {
            logger.error("获取子权限ID失败，父权限ID：{}", parentId, e);
            throw new RuntimeException("获取子权限ID失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Long> getAllParentIds(Long permissionId) {
        logger.info("获取权限的所有父权限ID，权限ID：{}", permissionId);
        
        try {
            List<Long> parentIds = permissionMapper.selectAllParentIds(permissionId);
            logger.info("获取父权限ID成功，权限ID：{}，父权限数量：{}", permissionId, parentIds.size());
            return parentIds;
            
        } catch (Exception e) {
            logger.error("获取父权限ID失败，权限ID：{}", permissionId, e);
            throw new RuntimeException("获取父权限ID失败：" + e.getMessage());
        }
    }
    
    @Override
    public boolean validatePermissionTree() {
        logger.info("验证权限树结构");
        
        try {
            List<Permission> permissions = permissionMapper.selectAllValidPermissions();
            List<Permission> tree = PermissionTreeUtil.buildTree(permissions);
            boolean isValid = PermissionTreeUtil.validateTree(tree);
            
            logger.info("权限树结构验证完成，结果：{}", isValid);
            return isValid;
            
        } catch (Exception e) {
            logger.error("验证权限树结构失败", e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public void recalculatePermissionLevels() {
        logger.info("重新计算权限层级");
        
        try {
            List<Permission> permissions = permissionMapper.selectAllPermissions();
            
            for (Permission permission : permissions) {
                int level = calculatePermissionLevel(permission.getParentId());
                if (level != permission.getLevel()) {
                    permission.setLevel(level);
                    permissionMapper.updatePermission(permission);
                }
            }
            
            logger.info("重新计算权限层级完成，处理权限数量：{}", permissions.size());
            
        } catch (Exception e) {
            logger.error("重新计算权限层级失败", e);
            throw new RuntimeException("重新计算权限层级失败：" + e.getMessage());
        }
    }
    
    /**
     * 计算权限层级
     */
    private int calculatePermissionLevel(Long parentId) {
        if (parentId == null || parentId == 0) {
            return 1; // 根权限层级为1
        }
        
        try {
            int parentLevel = permissionMapper.getPermissionLevel(parentId);
            return parentLevel + 1;
        } catch (Exception e) {
            logger.warn("计算权限层级失败，父权限ID：{}", parentId, e);
            return 1; // 默认层级为1
        }
    }
} 