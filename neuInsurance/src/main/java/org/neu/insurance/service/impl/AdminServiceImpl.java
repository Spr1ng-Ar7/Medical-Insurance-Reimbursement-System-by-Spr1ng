package org.neu.insurance.service.impl;

import org.apache.catalina.User;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.Admin;
import org.neu.insurance.entity.Permission;
import org.neu.insurance.mapper.AdminMapper;
import org.neu.insurance.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 统一用户管理服务实现类
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin login(String username, String password) {
        Admin admin = adminMapper.selectByUsername(username);
        if (admin != null && password.equals(admin.getPassword()) && admin.getStatus() == 1) {
            // 更新最后登录信息
            adminMapper.updateLastLoginInfo(admin.getId(), "127.0.0.1");
            return admin;
        }
        return null;
    }

    @Override
    public PageResponse<Admin> getAdminList(PageRequest pageRequest, String userType, String department, String keyword) {
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
        List<Admin> admins = adminMapper.selectByCondition(userType, department, keyword, offset, pageRequest.getPageSize());
        int total = adminMapper.countByCondition(userType, department, keyword);
        
        return new PageResponse<>(admins, (long) total, pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    public Admin getAdminById(Long id) {
        return adminMapper.selectById(id);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    @Override
    public boolean addAdmin(Admin admin) {
        // 检查用户名是否存在
        if (isUsernameExists(admin.getUsername())) {
            return false;
        }
        
        // 设置默认值
        admin.setStatus(1);
        admin.setLoginFailCount(0);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        
        return adminMapper.insert(admin) > 0;
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        admin.setUpdateTime(LocalDateTime.now());
        return adminMapper.update(admin) > 0;
    }

    @Override
    public boolean deleteAdmin(Long id) {
        return adminMapper.deleteById(id) > 0;
    }

    @Override
    public boolean batchDeleteAdmin(List<Long> ids) {
        return adminMapper.batchDelete(ids) > 0;
    }

    @Override
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        Admin admin = getAdminById(id);
        if (admin != null && oldPassword.equals(admin.getPassword())) {
            admin.setPassword(newPassword);
            admin.setUpdateTime(LocalDateTime.now());
            return adminMapper.update(admin) > 0;
        }
        return false;
    }

    @Override
    public boolean resetPassword(Long id, String newPassword) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setPassword(newPassword);
        admin.setUpdateTime(LocalDateTime.now());
        return adminMapper.update(admin) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(status);
        admin.setUpdateTime(LocalDateTime.now());
        return adminMapper.update(admin) > 0;
    }

    @Override
    public List<Admin> getAdminsByType(String userType) {
        return adminMapper.selectByUserType(userType);
    }

    @Override
    public List<Admin> getAdminsByDepartment(String department) {
        return adminMapper.selectByDepartment(department);
    }

    @Override
    public List<Admin> getAdminsByRole(String role) {
        return adminMapper.selectByRole(role);
    }

    @Override
    public boolean updateLastLoginInfo(Long id, String ip) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(ip);
        admin.setLoginFailCount(0);
        return adminMapper.update(admin) > 0;
    }

    @Override
    public boolean lockUser(Long id, boolean locked) {
        Admin admin = new Admin();
        admin.setId(id);
        if (locked) {
            admin.setLockedTime(LocalDateTime.now());
        } else {
            admin.setLockedTime(null);
            admin.setLoginFailCount(0);
        }
        admin.setUpdateTime(LocalDateTime.now());
        return adminMapper.update(admin) > 0;
    }

    @Override
    public List<String> getAllUserTypes() {
        return adminMapper.selectAllUserTypes();
    }

    @Override
    public List<String> getAllDepartments() {
        return adminMapper.selectAllDepartments();
    }

    @Override
    public List<String> getAllRoles() {
        return adminMapper.selectAllRoles();
    }

    @Override
    public boolean isUsernameExists(String username) {
        return adminMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean isEmailExists(String email) {
        return adminMapper.countByEmail(email) > 0;
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return adminMapper.countByPhone(phone) > 0;
    }

    @Override
    public Collection<Permission> getAdminPermissions(Long id) {
        // 临时解决方案：为admin用户添加超级管理员权限
        List<Permission> permissions = new ArrayList<>();
        
        // 获取用户信息
        Admin admin = getAdminById(id);
        if (admin != null && "admin".equals(admin.getUsername())) {
            // 为admin用户创建超级管理员权限
            Permission superAdminPermission = new Permission();
            superAdminPermission.setId(1L);
            superAdminPermission.setPermissionCode("SUPER_ADMIN");
            superAdminPermission.setPermissionName("超级管理员");
            superAdminPermission.setStatus(1);
            permissions.add(superAdminPermission);
        }
        
        return permissions;
    }

    @Override
    public List<String> getUserRoles(Long userId) {
       Admin user = adminMapper.selectById(userId);
       return user != null ? Arrays.asList(user.getRole()) : null;
    }
}