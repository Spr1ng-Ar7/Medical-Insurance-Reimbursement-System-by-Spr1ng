package org.neu.insurance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 数据库连接测试类
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("dev")
public class DatabaseConnectionTest {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Test
    public void testDatabaseConnection() {
        try {
            // 测试数据源连接
            Connection connection = dataSource.getConnection();
            log.info("数据库连接成功！");
            log.info("数据库URL: {}", connection.getMetaData().getURL());
            log.info("数据库产品名称: {}", connection.getMetaData().getDatabaseProductName());
            log.info("数据库版本: {}", connection.getMetaData().getDatabaseProductVersion());
            connection.close();
            
        } catch (SQLException e) {
            log.error("数据库连接失败", e);
            throw new RuntimeException("数据库连接失败: " + e.getMessage());
        }
    }
    
    @Test
    public void testReimbursementLevelTable() {
        try {
            // 测试查询报销等级表
            String sql = "SELECT COUNT(*) as count FROM reimbursement_level";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            log.info("报销等级表记录数: {}", count);
            
            // 查询所有报销等级
            String selectSql = "SELECT level_code, level_name, insurance_type, hospital_level, status FROM reimbursement_level ORDER BY level_code";
            List<Map<String, Object>> levels = jdbcTemplate.queryForList(selectSql);
            
            log.info("报销等级列表:");
            for (Map<String, Object> level : levels) {
                log.info("编码: {}, 名称: {}, 医保类型: {}, 医院等级: {}, 状态: {}", 
                    level.get("level_code"), 
                    level.get("level_name"), 
                    level.get("insurance_type"), 
                    level.get("hospital_level"), 
                    level.get("status"));
            }
            
        } catch (Exception e) {
            log.error("查询报销等级表失败", e);
            throw new RuntimeException("查询报销等级表失败: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseTables() {
        try {
            // 查询所有表
            String sql = "SHOW TABLES";
            List<String> tables = jdbcTemplate.queryForList(sql, String.class);
            
            log.info("数据库中的表:");
            for (String table : tables) {
                log.info("- {}", table);
            }
            
        } catch (Exception e) {
            log.error("查询数据库表失败", e);
            throw new RuntimeException("查询数据库表失败: " + e.getMessage());
        }
    }
} 