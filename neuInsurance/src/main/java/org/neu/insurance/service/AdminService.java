package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.Admin;
import org.neu.insurance.entity.Permission;

import java.util.Collection;
import java.util.List;

/**
 * 统一用户管理服务接口
 * 整合：管理员、医生、护士、财务等所有用户类型管理
 */
public interface AdminService {
    
    /**
     * 用户登录
     */
    Admin login(String username, String password);
    
    /**
     * 分页查询用户列表
     */
    PageResponse<Admin> getAdminList(PageRequest pageRequest, String userType, String department, String keyword);
    
    /**
     * 根据ID查询用户
     */
    Admin getAdminById(Long id);
    
    /**
     * 根据用户名查询用户
     */
    Admin getAdminByUsername(String username);
    
    /**
     * 新增用户
     */
    boolean addAdmin(Admin admin);
    
    /**
     * 更新用户信息
     */
    boolean updateAdmin(Admin admin);
    
    /**
     * 删除用户
     */
    boolean deleteAdmin(Long id);
    
    /**
     * 批量删除用户
     */
    boolean batchDeleteAdmin(List<Long> ids);
    
    /**
     * 修改密码
     */
    boolean changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     */
    boolean resetPassword(Long id, String newPassword);
    
    /**
     * 启用/禁用用户
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 根据用户类型查询用户列表
     */
    List<Admin> getAdminsByType(String userType);
    
    /**
     * 根据部门查询用户列表
     */
    List<Admin> getAdminsByDepartment(String department);
    
    /**
     * 根据角色查询用户列表
     */
    List<Admin> getAdminsByRole(String role);
    
    /**
     * 更新最后登录信息
     */
    boolean updateLastLoginInfo(Long id, String ip);
    
    /**
     * 锁定/解锁用户
     */
    boolean lockUser(Long id, boolean locked);
    
    /**
     * 获取所有用户类型
     */
    List<String> getAllUserTypes();
    
    /**
     * 获取所有部门
     */
    List<String> getAllDepartments();
    
    /**
     * 获取所有角色
     */
    List<String> getAllRoles();
    
    /**
     * 检查用户名是否存在
     */
    boolean isUsernameExists(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean isEmailExists(String email);
    
    /**
     * 检查手机号是否存在
     */
    boolean isPhoneExists(String phone);

    Collection<Permission> getAdminPermissions(Long id);

    List<String> getUserRoles(Long userId);
} 