package org.neu.insurance.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseCheck {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 检查数据库表是否存在
     */
    public void checkTables() {
        System.out.println("=== 数据库表检查 ===");
        
        String[] tables = {
            "sys_admin", "sys_department", "sys_role", "sys_permission", 
            "sys_admin_role", "sys_role_permission", "reimbursement_level",
            "drug", "medical_service", "medical_equipment", "disease", "hospital"
        };
        
        for (String table : tables) {
            try {
                String sql = "SELECT COUNT(*) FROM " + table;
                int count = jdbcTemplate.queryForObject(sql, Integer.class);
                System.out.println("✓ " + table + " 表存在，记录数: " + count);
            } catch (Exception e) {
                System.out.println("✗ " + table + " 表不存在或查询失败: " + e.getMessage());
            }
        }
    }
    
    /**
     * 检查管理员数据
     */
    public void checkAdminData() {
        System.out.println("\n=== 管理员数据检查 ===");
        try {
            String sql = "SELECT id, username, real_name, email, status FROM sys_admin";
            List<Map<String, Object>> admins = jdbcTemplate.queryForList(sql);
            
            if (admins.isEmpty()) {
                System.out.println("✗ 管理员表为空，没有初始数据");
            } else {
                System.out.println("✓ 管理员数据:");
                for (Map<String, Object> admin : admins) {
                    System.out.println("  - ID: " + admin.get("id") + 
                                     ", 用户名: " + admin.get("username") + 
                                     ", 姓名: " + admin.get("real_name") + 
                                     ", 邮箱: " + admin.get("email") + 
                                     ", 状态: " + admin.get("status"));
                }
            }
        } catch (Exception e) {
            System.out.println("✗ 查询管理员数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查角色数据
     */
    public void checkRoleData() {
        System.out.println("\n=== 角色数据检查 ===");
        try {
            String sql = "SELECT id, role_code, role_name, status FROM sys_role";
            List<Map<String, Object>> roles = jdbcTemplate.queryForList(sql);
            
            if (roles.isEmpty()) {
                System.out.println("✗ 角色表为空，没有初始数据");
            } else {
                System.out.println("✓ 角色数据:");
                for (Map<String, Object> role : roles) {
                    System.out.println("  - ID: " + role.get("id") + 
                                     ", 编码: " + role.get("role_code") + 
                                     ", 名称: " + role.get("role_name") + 
                                     ", 状态: " + role.get("status"));
                }
            }
        } catch (Exception e) {
            System.out.println("✗ 查询角色数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查管理员角色关联
     */
    public void checkAdminRoleData() {
        System.out.println("\n=== 管理员角色关联检查 ===");
        try {
            String sql = "SELECT ar.admin_id, ar.role_id, a.username, r.role_name " +
                        "FROM sys_admin_role ar " +
                        "LEFT JOIN sys_admin a ON ar.admin_id = a.id " +
                        "LEFT JOIN sys_role r ON ar.role_id = r.id";
            List<Map<String, Object>> adminRoles = jdbcTemplate.queryForList(sql);
            
            if (adminRoles.isEmpty()) {
                System.out.println("✗ 管理员角色关联表为空");
            } else {
                System.out.println("✓ 管理员角色关联:");
                for (Map<String, Object> adminRole : adminRoles) {
                    System.out.println("  - 管理员ID: " + adminRole.get("admin_id") + 
                                     ", 用户名: " + adminRole.get("username") + 
                                     ", 角色ID: " + adminRole.get("role_id") + 
                                     ", 角色名: " + adminRole.get("role_name"));
                }
            }
        } catch (Exception e) {
            System.out.println("✗ 查询管理员角色关联失败: " + e.getMessage());
        }
    }
    
    /**
     * 执行完整检查
     */
    public void performFullCheck() {
        checkTables();
        checkAdminData();
        checkRoleData();
        checkAdminRoleData();
    }
} 