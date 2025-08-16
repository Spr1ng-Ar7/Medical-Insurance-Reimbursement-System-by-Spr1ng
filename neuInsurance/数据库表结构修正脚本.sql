-- 数据库表结构修正脚本
-- 用于修正 sys_admin 表结构，使其与业务代码匹配

USE neuinsurance;

-- 1. 为 sys_admin 表添加缺失的字段
ALTER TABLE sys_admin 
ADD COLUMN user_type VARCHAR(20) COMMENT '用户类型：admin-管理员/doctor-医生/nurse-护士/finance-财务/operator-操作员' AFTER avatar,
ADD COLUMN role VARCHAR(50) COMMENT '角色：SUPER_ADMIN/ADMIN/DOCTOR/NURSE/FINANCE/OPERATOR' AFTER user_type,
ADD COLUMN department VARCHAR(100) COMMENT '部门' AFTER role,
ADD COLUMN position VARCHAR(50) COMMENT '职位' AFTER department,
ADD COLUMN id_card VARCHAR(18) COMMENT '身份证号' AFTER position,
ADD COLUMN gender VARCHAR(10) COMMENT '性别：男/女' AFTER id_card,
ADD COLUMN birth_date VARCHAR(20) COMMENT '出生日期' AFTER gender,
ADD COLUMN address VARCHAR(200) COMMENT '地址' AFTER birth_date,
ADD COLUMN license_number VARCHAR(50) COMMENT '执业证号（医生）' AFTER remark,
ADD COLUMN specialty VARCHAR(100) COMMENT '专业特长（医生）' AFTER license_number,
ADD COLUMN title VARCHAR(50) COMMENT '职称：主任医师/副主任医师/主治医师等' AFTER specialty,
ADD COLUMN work_schedule VARCHAR(50) COMMENT '工作班次（护士）' AFTER title,
ADD COLUMN certification VARCHAR(100) COMMENT '资格证书' AFTER work_schedule;

-- 2. 添加索引
ALTER TABLE sys_admin 
ADD INDEX idx_user_type (user_type),
ADD INDEX idx_role (role),
ADD INDEX idx_department (department);

-- 3. 更新现有管理员数据的字段
UPDATE sys_admin SET 
    user_type = 'admin',
    role = 'SUPER_ADMIN',
    department = '系统管理部',
    position = '系统管理员',
    gender = '男',
    title = '系统管理员'
WHERE username = 'admin';

UPDATE sys_admin SET 
    user_type = 'admin',
    role = 'BUSINESS_ADMIN',
    department = '业务管理部',
    position = '业务管理员',
    gender = '男',
    title = '业务管理员'
WHERE username = 'manager';

-- 4. 验证表结构
DESCRIBE sys_admin;

-- 5. 验证数据
SELECT id, username, real_name, user_type, role, department, position, status FROM sys_admin; 