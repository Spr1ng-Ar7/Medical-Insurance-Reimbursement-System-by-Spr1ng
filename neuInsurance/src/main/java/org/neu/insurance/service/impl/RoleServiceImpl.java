package org.neu.insurance.service.impl;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.RoleDTO;
import org.neu.insurance.entity.Role;
import org.neu.insurance.entity.Permission;
import org.neu.insurance.mapper.RoleMapper;
import org.neu.insurance.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl implements RoleService {
    
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public PageResponse<Role> getRoleList(String roleName, String roleCode, Integer status, PageRequest pageRequest) {
        logger.info("分页查询角色列表，参数：roleName={}, roleCode={}, status={}, pageNum={}, pageSize={}", 
                   roleName, roleCode, status, pageRequest.getPageNum(), pageRequest.getPageSize());
        
        try {
            // 计算偏移量
            int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
            
            // 查询总数
            int total = roleMapper.countRoles(roleName, roleCode, status);
            
            // 查询数据
            List<Role> roles = roleMapper.selectRoleList(roleName, roleCode, status, offset, pageRequest.getPageSize());
            
            // 构建分页响应
            PageResponse<Role> response = new PageResponse<>();
            response.setRecords(roles);
            response.setTotal((long) total);
            response.setPageNum(pageRequest.getPageNum());
            response.setPageSize(pageRequest.getPageSize());
            response.setTotalPages((int) Math.ceil((double) total / pageRequest.getPageSize()));
            
            logger.info("分页查询角色列表成功，总记录数：{}", total);
            return response;
            
        } catch (Exception e) {
            logger.error("分页查询角色列表失败", e);
            throw new RuntimeException("查询角色列表失败：" + e.getMessage());
        }
    }
    
    @Override
    public Role getRoleById(Long id) {
        logger.info("根据ID查询角色，ID：{}", id);
        
        if (id == null) {
            logger.warn("角色ID不能为空");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        try {
            Role role = roleMapper.selectRoleById(id);
            if (role == null) {
                logger.warn("角色不存在，ID：{}", id);
                throw new IllegalArgumentException("角色不存在，ID: " + id);
            }
            
            logger.info("查询角色成功，角色名称：{}", role.getRoleName());
            return role;
            
        } catch (Exception e) {
            logger.error("查询角色失败，ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("查询角色失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void addRole(RoleDTO roleDTO) {
        logger.info("新增角色，角色编码：{}，角色名称：{}", roleDTO.getRoleCode(), roleDTO.getRoleName());
        
        if (roleDTO == null) {
            logger.warn("角色信息不能为空");
            throw new IllegalArgumentException("角色信息不能为空");
        }
        
        try {
            // 验证角色编码唯一性
            if (!isRoleCodeUnique(roleDTO.getRoleCode(), null)) {
                logger.warn("角色编码已存在：{}", roleDTO.getRoleCode());
                throw new IllegalArgumentException("角色编码已存在：" + roleDTO.getRoleCode());
            }
            
            // 设置默认值
            if (roleDTO.getStatus() == null) {
                roleDTO.setStatus(1); // 默认启用
            }
            if (roleDTO.getSortOrder() == null) {
                roleDTO.setSortOrder(0); // 默认排序
            }
            
            // 新增角色
            int result = roleMapper.insertRole(roleDTO);
            if (result <= 0) {
                logger.error("新增角色失败，角色编码：{}", roleDTO.getRoleCode());
                throw new RuntimeException("新增角色失败");
            }
            
            // 分配权限
            if (roleDTO.getPermissionIds() != null && !roleDTO.getPermissionIds().isEmpty()) {
                roleMapper.insertRolePermissions(roleDTO.getId(), roleDTO.getPermissionIds());
                logger.info("角色权限分配成功，权限数量：{}", roleDTO.getPermissionIds().size());
            }
            
            logger.info("新增角色成功，角色ID：{}", roleDTO.getId());
            
        } catch (Exception e) {
            logger.error("新增角色失败，角色编码：{}", roleDTO.getRoleCode(), e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("新增角色失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateRole(RoleDTO roleDTO) {
        logger.info("更新角色，角色ID：{}，角色编码：{}", roleDTO.getId(), roleDTO.getRoleCode());
        
        if (roleDTO == null || roleDTO.getId() == null) {
            logger.warn("角色信息或ID不能为空");
            throw new IllegalArgumentException("角色信息或ID不能为空");
        }
        
        try {
            // 检查角色是否存在
            Role existingRole = getRoleById(roleDTO.getId());
            
            // 验证角色编码唯一性（排除当前角色）
            if (!isRoleCodeUnique(roleDTO.getRoleCode(), roleDTO.getId())) {
                logger.warn("角色编码已存在：{}", roleDTO.getRoleCode());
                throw new IllegalArgumentException("角色编码已存在：" + roleDTO.getRoleCode());
            }
            
            // 更新角色
            int result = roleMapper.updateRole(roleDTO);
            if (result <= 0) {
                logger.error("更新角色失败，角色ID：{}", roleDTO.getId());
                throw new RuntimeException("更新角色失败");
            }
            
            // 更新权限
            if (roleDTO.getPermissionIds() != null) {
                // 删除原有权限
                roleMapper.deleteRolePermissions(roleDTO.getId());
                
                // 分配新权限
                if (!roleDTO.getPermissionIds().isEmpty()) {
                    roleMapper.insertRolePermissions(roleDTO.getId(), roleDTO.getPermissionIds());
                    logger.info("角色权限更新成功，权限数量：{}", roleDTO.getPermissionIds().size());
                }
            }
            
            logger.info("更新角色成功，角色ID：{}", roleDTO.getId());
            
        } catch (Exception e) {
            logger.error("更新角色失败，角色ID：{}", roleDTO.getId(), e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("更新角色失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteRole(Long id) {
        logger.info("删除角色，角色ID：{}", id);
        
        if (id == null) {
            logger.warn("角色ID不能为空");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        try {
            Role role = getRoleById(id);
            
            // 检查是否为系统角色
            if (role.getId().equals(1L)) {
                logger.warn("不能删除系统角色，角色ID：{}", id);
                throw new IllegalArgumentException("不能删除系统角色");
            }
            
            // 删除角色权限关联
            roleMapper.deleteRolePermissions(id);
            
            // 删除角色
            int result = roleMapper.deleteRoleById(id);
            if (result <= 0) {
                logger.error("删除角色失败，角色ID：{}", id);
                throw new RuntimeException("删除角色失败");
            }
            
            logger.info("删除角色成功，角色ID：{}", id);
            
        } catch (Exception e) {
            logger.error("删除角色失败，角色ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("删除角色失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteRole(List<Long> ids) {
        logger.info("批量删除角色，角色ID列表：{}", ids);
        
        if (ids == null || ids.isEmpty()) {
            logger.warn("角色ID列表不能为空");
            throw new IllegalArgumentException("角色ID列表不能为空");
        }
        
        try {
            // 批量查询角色
            List<Role> roles = roleMapper.selectRolesByIds(ids);
            
            // 批量验证
            List<Long> validIds = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();
            
            for (Role role : roles) {
                if (role == null) {
                    errorMessages.add("角色不存在，ID: " + role.getId());
                    continue;
                }
                
                if (role.getId().equals(1L)) {
                    errorMessages.add("不能删除系统角色，ID: " + role.getId());
                    continue;
                }
                
                validIds.add(role.getId());
            }
            
            // 如果有验证错误，抛出异常
            if (!errorMessages.isEmpty()) {
                logger.warn("批量删除验证失败：{}", String.join("; ", errorMessages));
                throw new IllegalArgumentException("批量删除验证失败: " + String.join("; ", errorMessages));
            }
            
            // 批量删除角色权限关联
            for (Long id : validIds) {
                roleMapper.deleteRolePermissions(id);
            }
            
            // 批量删除角色
            if (!validIds.isEmpty()) {
                int deletedCount = roleMapper.batchDeleteRoles(validIds);
                if (deletedCount != validIds.size()) {
                    logger.error("批量删除失败，预期删除{}条，实际删除{}条", validIds.size(), deletedCount);
                    throw new RuntimeException("批量删除失败，预期删除" + validIds.size() + "条，实际删除" + deletedCount + "条");
                }
            }
            
            logger.info("批量删除角色成功，删除数量：{}", validIds.size());
            
        } catch (Exception e) {
            logger.error("批量删除角色失败", e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("批量删除角色失败：" + e.getMessage());
        }
    }
    
    @Override
    public void updateStatus(Long id, Integer status) {
        logger.info("更新角色状态，角色ID：{}，状态：{}", id, status);
        
        if (id == null) {
            logger.warn("角色ID不能为空");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        if (status == null || (status != 0 && status != 1)) {
            logger.warn("状态值无效：{}", status);
            throw new IllegalArgumentException("状态值无效，只能是0或1");
        }
        
        try {
            Role role = getRoleById(id);
            
            // 检查是否为系统角色
            if (role.getId().equals(1L)) {
                logger.warn("不能禁用系统角色，角色ID：{}", id);
                throw new IllegalArgumentException("不能禁用系统角色");
            }
            
            int result = roleMapper.updateRoleStatus(id, status);
            if (result <= 0) {
                logger.error("更新角色状态失败，角色ID：{}", id);
                throw new RuntimeException("更新角色状态失败");
            }
            
            logger.info("更新角色状态成功，角色ID：{}，状态：{}", id, status);
            
        } catch (Exception e) {
            logger.error("更新角色状态失败，角色ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("更新角色状态失败：" + e.getMessage());
        }
    }
    
    @Override
    public List<Role> getAllRoles() {
        logger.info("获取所有有效角色列表");
        
        try {
            List<Role> roles = roleMapper.selectAllRoles();
            logger.info("获取所有有效角色列表成功，角色数量：{}", roles.size());
            return roles;
            
        } catch (Exception e) {
            logger.error("获取所有有效角色列表失败", e);
            throw new RuntimeException("获取角色列表失败：" + e.getMessage());
        }
    }
    
    @Override
    public boolean isRoleCodeUnique(String roleCode, Long excludeId) {
        if (roleCode == null || roleCode.trim().isEmpty()) {
            return false;
        }
        
        try {
            int count = roleMapper.countByRoleCode(roleCode, excludeId);
            return count == 0;
        } catch (Exception e) {
            logger.error("检查角色编码唯一性失败，角色编码：{}", roleCode, e);
            return false;
        }
    }
    
    @Override
    public List<Permission> getRolePermissions(Long id) {
        logger.info("获取角色权限列表，角色ID：{}", id);
        
        if (id == null) {
            logger.warn("角色ID不能为空");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        try {
            // 这里需要调用PermissionMapper来获取权限详情
            // 暂时返回空列表，后续可以完善
            logger.info("获取角色权限列表成功，角色ID：{}", id);
            return new ArrayList<>();
            
        } catch (Exception e) {
            logger.error("获取角色权限列表失败，角色ID：{}", id, e);
            throw new RuntimeException("获取角色权限列表失败：" + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void assignPermissions(Long id, List<Long> permissionIds) {
        logger.info("为角色分配权限，角色ID：{}，权限数量：{}", id, permissionIds != null ? permissionIds.size() : 0);
        
        if (id == null) {
            logger.warn("角色ID不能为空");
            throw new IllegalArgumentException("角色ID不能为空");
        }
        
        try {
            // 检查角色是否存在
            getRoleById(id);
            
            // 删除原有权限
            roleMapper.deleteRolePermissions(id);
            
            // 分配新权限
            if (permissionIds != null && !permissionIds.isEmpty()) {
                roleMapper.insertRolePermissions(id, permissionIds);
                logger.info("角色权限分配成功，角色ID：{}，权限数量：{}", id, permissionIds.size());
            }
            
        } catch (Exception e) {
            logger.error("为角色分配权限失败，角色ID：{}", id, e);
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("分配权限失败：" + e.getMessage());
        }
    }
} 