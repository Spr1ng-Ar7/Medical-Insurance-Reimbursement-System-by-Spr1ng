package org.neu.insurance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnectionTest {
    
    // 数据库连接配置
    private static final String URL = "jdbc:mysql://localhost:3306/neuinsurance?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    
    public static void main(String[] args) {
        System.out.println("=== 数据库连接测试 ===");
        
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 建立连接
            System.out.println("正在连接数据库...");
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✓ 数据库连接成功！");
            
            // 检查表是否存在
            checkTables(connection);
            
            // 检查管理员数据
            checkAdminData(connection);
            
            // 关闭连接
            connection.close();
            System.out.println("数据库连接已关闭");
            
        } catch (Exception e) {
            System.err.println("✗ 数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void checkTables(Connection connection) throws Exception {
        System.out.println("\n=== 数据库表检查 ===");
        
        String[] tables = {
            "sys_admin", "sys_department", "sys_role", "sys_permission", 
            "sys_admin_role", "sys_role_permission", "reimbursement_level",
            "drug", "medical_service", "medical_equipment", "disease", "hospital"
        };
        
        Statement stmt = connection.createStatement();
        
        for (String table : tables) {
            try {
                String sql = "SELECT COUNT(*) FROM " + table;
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("✓ " + table + " 表存在，记录数: " + count);
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("✗ " + table + " 表不存在或查询失败: " + e.getMessage());
            }
        }
        
        stmt.close();
    }
    
    private static void checkAdminData(Connection connection) throws Exception {
        System.out.println("\n=== 管理员数据检查 ===");
        
        Statement stmt = connection.createStatement();
        String sql = "SELECT id, username, real_name, email, status FROM sys_admin";
        
        try {
            ResultSet rs = stmt.executeQuery(sql);
            
            boolean hasData = false;
            while (rs.next()) {
                if (!hasData) {
                    System.out.println("✓ 管理员数据:");
                    hasData = true;
                }
                
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String realName = rs.getString("real_name");
                String email = rs.getString("email");
                int status = rs.getInt("status");
                
                System.out.println("  - ID: " + id + 
                                 ", 用户名: " + username + 
                                 ", 姓名: " + realName + 
                                 ", 邮箱: " + email + 
                                 ", 状态: " + status);
            }
            
            if (!hasData) {
                System.out.println("✗ 管理员表为空，没有初始数据");
            }
            
            rs.close();
        } catch (Exception e) {
            System.out.println("✗ 查询管理员数据失败: " + e.getMessage());
        }
        
        stmt.close();
    }
} 