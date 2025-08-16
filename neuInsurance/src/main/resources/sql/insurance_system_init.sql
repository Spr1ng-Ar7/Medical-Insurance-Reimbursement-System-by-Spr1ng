-- 医保报销系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS neu_insurance CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE neu_insurance;

-- 1. 部门表
CREATE TABLE IF NOT EXISTS sys_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    dept_code VARCHAR(50) NOT NULL COMMENT '部门编码',
    dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    level INT DEFAULT 1 COMMENT '层级',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_dept_code (dept_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2. 管理员表
CREATE TABLE IF NOT EXISTS sys_admin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    avatar VARCHAR(200) COMMENT '头像URL',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    department_id BIGINT COMMENT '部门ID',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    login_fail_count INT DEFAULT 0 COMMENT '登录失败次数',
    locked_time DATETIME COMMENT '锁定时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_username (username),
    KEY idx_department_id (department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 3. 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
    description VARCHAR(500) COMMENT '角色描述',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 4. 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    permission_name VARCHAR(100) NOT NULL COMMENT '权限名称',
    resource_type VARCHAR(20) NOT NULL COMMENT '资源类型：menu-菜单，button-按钮，api-接口',
    resource_path VARCHAR(200) COMMENT '资源路径',
    http_method VARCHAR(10) COMMENT 'HTTP方法：GET,POST,PUT,DELETE',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    level INT DEFAULT 1 COMMENT '层级',
    sort_order INT DEFAULT 0 COMMENT '排序',
    icon VARCHAR(100) COMMENT '图标',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_permission_code (permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 5. 管理员角色关联表
CREATE TABLE IF NOT EXISTS sys_admin_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    admin_id BIGINT NOT NULL COMMENT '管理员ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_admin_role (admin_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员角色关联表';

-- 6. 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 7. 报销等级配置表
CREATE TABLE IF NOT EXISTS reimbursement_level (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报销等级ID',
    level_code VARCHAR(50) NOT NULL COMMENT '等级编码',
    level_name VARCHAR(100) NOT NULL COMMENT '等级名称',
    insurance_type VARCHAR(20) NOT NULL COMMENT '医保类型：城镇职工/城乡居民等',
    hospital_level VARCHAR(10) NOT NULL COMMENT '医院等级：一级/二级/三级',
    min_amount DECIMAL(10,2) DEFAULT 0 COMMENT '最低费用',
    max_amount DECIMAL(10,2) COMMENT '最高费用',
    deductible DECIMAL(10,2) DEFAULT 0 COMMENT '起付线',
    max_reimbursement DECIMAL(10,2) COMMENT '最高报销额度',
    category_a_rate DECIMAL(5,4) DEFAULT 0 COMMENT '甲类药品报销比例',
    category_b_rate DECIMAL(5,4) DEFAULT 0 COMMENT '乙类药品报销比例',
    category_c_rate DECIMAL(5,4) DEFAULT 0 COMMENT '丙类药品报销比例',
    treatment_rate DECIMAL(5,4) DEFAULT 0 COMMENT '诊疗费用报销比例',
    service_rate DECIMAL(5,4) DEFAULT 0 COMMENT '服务设施费用报销比例',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    effective_time DATETIME COMMENT '生效时间',
    expire_time DATETIME COMMENT '失效时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_level_code (level_code),
    KEY idx_insurance_hospital (insurance_type, hospital_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报销等级配置表';

-- 8. 药品表
CREATE TABLE IF NOT EXISTS drug (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '药品ID',
    drug_code VARCHAR(50) NOT NULL COMMENT '药品编码',
    drug_name VARCHAR(200) NOT NULL COMMENT '医保目录名称',
    trade_name VARCHAR(200) COMMENT '商品名',
    drug_type VARCHAR(10) NOT NULL COMMENT '药品类型：甲类/乙类/丙类',
    self_pay_ratio DECIMAL(5,4) DEFAULT 0 COMMENT '自付比例',
    specification VARCHAR(200) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位',
    price DECIMAL(10,2) COMMENT '支付标准（价格）',
    manufacturer VARCHAR(200) COMMENT '生产企业',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_drug_code (drug_code),
    KEY idx_drug_type (drug_type),
    KEY idx_manufacturer (manufacturer)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品表';

-- 9. 医疗服务项目表
CREATE TABLE IF NOT EXISTS medical_service (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '服务项目ID',
    service_code VARCHAR(50) NOT NULL COMMENT '项目编码',
    national_code VARCHAR(100) COMMENT '国家编码',
    service_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    service_content TEXT COMMENT '项目内容',
    exclude_content TEXT COMMENT '除外内容',
    unit_type VARCHAR(20) COMMENT '计价单位',
    price DECIMAL(10,2) COMMENT '价格',
    category VARCHAR(50) COMMENT '服务类别',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark TEXT COMMENT '说明',
    UNIQUE KEY uk_service_code (service_code),
    KEY idx_category (category),
    KEY idx_unit_type (unit_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医疗服务项目表';

-- 10. 医疗器械设备表
CREATE TABLE IF NOT EXISTS medical_equipment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '设备项目ID',
    finance_category VARCHAR(10) COMMENT '财务分类',
    equipment_code VARCHAR(50) NOT NULL COMMENT '项目编码',
    national_code VARCHAR(100) COMMENT '国家编码',
    equipment_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    equipment_content TEXT COMMENT '项目内容',
    exclude_content TEXT COMMENT '除外内容',
    unit_type VARCHAR(20) COMMENT '计价单位',
    price DECIMAL(10,2) COMMENT '价格',
    category VARCHAR(50) COMMENT '设备类别',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark TEXT COMMENT '说明',
    UNIQUE KEY uk_equipment_code (equipment_code),
    KEY idx_finance_category (finance_category),
    KEY idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医疗器械设备表';

-- 11. 疾病编码表
CREATE TABLE IF NOT EXISTS disease (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '疾病ID',
    icd_code VARCHAR(20) NOT NULL COMMENT '国际ICD编码',
    disease_code VARCHAR(50) NOT NULL COMMENT '疾病编码',
    disease_name VARCHAR(200) NOT NULL COMMENT '疾病名称',
    disease_category VARCHAR(50) COMMENT '疾病分类',
    description TEXT COMMENT '疾病描述',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_icd_code (icd_code),
    UNIQUE KEY uk_disease_code (disease_code),
    KEY idx_disease_category (disease_category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='疾病编码表';

-- 12. 医院表
CREATE TABLE IF NOT EXISTS hospital (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '医院ID',
    hospital_code VARCHAR(50) NOT NULL COMMENT '医院编码',
    hospital_name VARCHAR(100) NOT NULL COMMENT '医院名称',
    hospital_level VARCHAR(10) NOT NULL COMMENT '医院等级：一级/二级/三级',
    hospital_type VARCHAR(20) COMMENT '医院类型：公立/私立',
    address VARCHAR(200) COMMENT '地址',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    phone VARCHAR(20) COMMENT '联系电话',
    contact_person VARCHAR(50) COMMENT '联系人',
    email VARCHAR(100) COMMENT '邮箱',
    website VARCHAR(200) COMMENT '网站',
    description VARCHAR(500) COMMENT '描述',
    status INT DEFAULT 1 COMMENT '状态：1-正常，0-停用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    remark VARCHAR(500) COMMENT '备注',
    UNIQUE KEY uk_hospital_code (hospital_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医院表';

-- 插入初始数据
-- 部门数据
INSERT INTO sys_department (dept_code, dept_name, parent_id, level, leader, phone, email, sort_order, status, create_by, remark) VALUES 
('ROOT', '医保管理局', 0, 1, '张局长', '010-12345678', 'admin@insurance.gov.cn', 1, 1, 'system', '根部门'),
('ADMIN', '系统管理部', 1, 2, '李部长', '010-12345679', 'admin@insurance.gov.cn', 1, 1, 'system', '系统管理部门'),
('BUSINESS', '业务管理部', 1, 2, '王部长', '010-12345680', 'business@insurance.gov.cn', 2, 1, 'system', '业务管理部门');

-- 管理员数据（密码是123456的MD5：e10adc3949ba59abbe56e057f20f883e）
INSERT INTO sys_admin (username, password, real_name, email, phone, status, department_id, create_by, remark) VALUES 
('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'admin@insurance.gov.cn', '13800000000', 1, 2, 'system', '系统默认管理员'),
('manager', 'e10adc3949ba59abbe56e057f20f883e', '业务管理员', 'manager@insurance.gov.cn', '13800000001', 1, 3, 'system', '业务管理员');

-- 角色数据
INSERT INTO sys_role (role_code, role_name, description, status, sort_order, create_by, remark) VALUES 
('SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 1, 1, 'system', '系统内置角色'),
('ADMIN', '系统管理员', '系统管理员，拥有系统管理权限', 1, 2, 'system', '系统内置角色'),
('BUSINESS_ADMIN', '业务管理员', '业务管理员，拥有业务管理权限', 1, 3, 'system', '系统内置角色'),
('OPERATOR', '操作员', '普通操作员，拥有基本操作权限', 1, 4, 'system', '系统内置角色');

-- 权限数据
INSERT INTO sys_permission (permission_code, permission_name, resource_type, resource_path, parent_id, level, sort_order, icon, status, create_by, remark) VALUES 
('SYSTEM', '系统管理', 'menu', '/system', 0, 1, 1, 'system', 1, 'system', '系统管理菜单'),
('SYSTEM:ADMIN', '管理员管理', 'menu', '/system/admin', 1, 2, 1, 'admin', 1, 'system', '管理员管理菜单'),
('SYSTEM:ADMIN:LIST', '管理员列表', 'api', '/api/admin/list', 2, 3, 1, '', 1, 'system', '管理员列表权限'),
('SYSTEM:ADMIN:ADD', '新增管理员', 'button', '/api/admin', 2, 3, 2, '', 1, 'system', '新增管理员权限'),
('SYSTEM:ADMIN:UPDATE', '修改管理员', 'button', '/api/admin/*', 2, 3, 3, '', 1, 'system', '修改管理员权限'),
('SYSTEM:ADMIN:DELETE', '删除管理员', 'button', '/api/admin/*', 2, 3, 4, '', 1, 'system', '删除管理员权限'),
('SYSTEM:ROLE', '角色管理', 'menu', '/system/role', 1, 2, 2, 'role', 1, 'system', '角色管理菜单'),
('SYSTEM:ROLE:LIST', '角色列表', 'api', '/api/role/list', 7, 3, 1, '', 1, 'system', '角色列表权限'),
('SYSTEM:ROLE:ADD', '新增角色', 'button', '/api/role', 7, 3, 2, '', 1, 'system', '新增角色权限'),
('SYSTEM:ROLE:UPDATE', '修改角色', 'button', '/api/role/*', 7, 3, 3, '', 1, 'system', '修改角色权限'),
('SYSTEM:ROLE:DELETE', '删除角色', 'button', '/api/role/*', 7, 3, 4, '', 1, 'system', '删除角色权限'),
('BUSINESS', '业务管理', 'menu', '/business', 0, 1, 2, 'business', 1, 'system', '业务管理菜单'),
('BUSINESS:DRUG', '药品管理', 'menu', '/business/drug', 12, 2, 1, 'drug', 1, 'system', '药品管理菜单'),
('BUSINESS:PATIENT', '患者管理', 'menu', '/business/patient', 12, 2, 2, 'patient', 1, 'system', '患者管理菜单'),
('BUSINESS:SETTLEMENT', '结算管理', 'menu', '/business/settlement', 12, 2, 3, 'settlement', 1, 'system', '结算管理菜单'),
('BUSINESS:REIMBURSEMENT', '报销等级管理', 'menu', '/business/reimbursement', 12, 2, 4, 'reimbursement', 1, 'system', '报销等级管理菜单');

-- 管理员角色关联
INSERT INTO sys_admin_role (admin_id, role_id) VALUES 
(1, 1),  -- admin -> SUPER_ADMIN
(2, 3);  -- manager -> BUSINESS_ADMIN

-- 角色权限关联（超级管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id) 
SELECT 1, id FROM sys_permission WHERE status = 1;

-- 业务管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES 
(3, 12), (3, 13), (3, 14), (3, 15), (3, 16);

-- 报销等级配置初始数据
INSERT INTO reimbursement_level (level_code, level_name, insurance_type, hospital_level, min_amount, max_amount, deductible, max_reimbursement, category_a_rate, category_b_rate, category_c_rate, treatment_rate, service_rate, status, effective_time, create_by, remark) VALUES 
('LEVEL_1', '一级报销', '城镇职工', '一级', 0, 30000, 1300, 30000, 0.9500, 0.8500, 0.0000, 0.9000, 0.8000, 1, '2024-01-01 00:00:00', 'system', '城镇职工一级医院报销标准'),
('LEVEL_2', '二级报销', '城镇职工', '二级', 0, 40000, 1800, 40000, 0.9000, 0.8000, 0.0000, 0.8500, 0.7500, 1, '2024-01-01 00:00:00', 'system', '城镇职工二级医院报销标准'),
('LEVEL_3', '三级报销', '城镇职工', '三级', 0, 50000, 2300, 50000, 0.8500, 0.7500, 0.0000, 0.8000, 0.7000, 1, '2024-01-01 00:00:00', 'system', '城镇职工三级医院报销标准'),
('LEVEL_4', '居民一级报销', '城乡居民', '一级', 0, 20000, 500, 20000, 0.8000, 0.7000, 0.0000, 0.7500, 0.6500, 1, '2024-01-01 00:00:00', 'system', '城乡居民一级医院报销标准'),
('LEVEL_5', '居民二级报销', '城乡居民', '二级', 0, 30000, 800, 30000, 0.7500, 0.6500, 0.0000, 0.7000, 0.6000, 1, '2024-01-01 00:00:00', 'system', '城乡居民二级医院报销标准'),
('LEVEL_6', '居民三级报销', '城乡居民', '三级', 0, 40000, 1200, 40000, 0.7000, 0.6000, 0.0000, 0.6500, 0.5500, 1, '2024-01-01 00:00:00', 'system', '城乡居民三级医院报销标准');

-- 药品初始数据（基于Excel数据）
INSERT INTO drug (drug_code, drug_name, trade_name, drug_type, self_pay_ratio, specification, unit, price, manufacturer, status, create_by, remark) VALUES 
('D001', '奥比利利', '维建乐', '丙类', 1.0000, '奥比地平12.5mg,阿立瑞钠75mg,利比昔50mg', '盒', 2712.60, '爱尔兰Fournier Laboratories Ireland Limited', 1, 'system', ''),
('D002', '达塞布韦瑞', '易奇瑞', '丙类', 1.0000, '0.25g', '盒', 235.80, '爱尔兰AbbVie Ireland NL B.V.', 1, 'system', ''),
('D003', '格卡瑞加派合他药', '艾诺全', '丙类', 1.0000, '格卡瑞加0.1g,派合他药40mg', '盒', 4750.00, '爱尔兰Fournier Laboratories Ireland Limited', 1, 'system', ''),
('D004', '达诺瑞韦钠', '艾诺卫', '丙类', 1.0000, '0.1g', '瓶', 6665.99, '歌礼药业(浙江)有限公司', 1, 'system', ''),
('D005', '华夏2号无蛋白氨酸食品固体饮料(奶粉)', '', '丙类', 1.0000, '400g', '罐', 96.00, '上海津佳食品有限公司', 1, 'system', ''),
('D006', '特殊医学用途婴儿配方粉苯丙酮尿症配方(1段)', '', '丙类', 1.0000, '400g', '罐', 137.28, '纽迪希亚贸易(上海)有限公司', 1, 'system', ''),
('D007', '特殊医学用途配方食品氨基酸代谢障碍配方(2段)', '', '丙类', 1.0000, '400g', '罐', 227.04, '纽迪希亚贸易(上海)有限公司', 1, 'system', ''),
('D008', '特殊医学用途非全营养配方粉苯丙酮尿症配方(3段)', '', '丙类', 1.0000, '400g', '罐', 279.84, '纽迪希亚贸易(上海)有限公司', 1, 'system', ''),
('D009', '华夏2号定粉浆', '', '丙类', 1.0000, '1000g', '袋', 38.00, '上海津佳食品有限公司', 1, 'system', ''),
('D010', '低蛋白大米', '', '丙类', 1.0000, '1000g', '袋', 82.00, '中慧(天津)医药科技有限公司', 1, 'system', '');

-- 医疗服务项目初始数据（基于Excel数据）
INSERT INTO medical_service (service_code, national_code, service_name, service_content, exclude_content, unit_type, price, category, status, create_by, remark) VALUES 
('110200001-1', '001102000010300-110200001-1', '门诊诊查费(医师)', '指医护人员提供(技术劳务)的诊疗服务', '', '次', 3.00, '诊查费', 1, 'system', ''),
('110200001-1/1', '001102000010300-110200001-1/1', '门诊诊查费(传染病、发热或心理门诊医师)', '', '', '次', 3.90, '诊查费', 1, 'system', ''),
('110200001-1c', '001102000010300-110200001-1c', '门诊诊查费(6岁及以下)(医师)', '', '', '次', 8.00, '诊查费', 1, 'system', ''),
('110200001-1/1c', '001102000010300-110200001-1/1c', '门诊诊查费(传染病、发热或心理门诊)(6岁及以下)(医师)', '', '', '次', 10.40, '诊查费', 1, 'system', ''),
('110200001-2', '001102000010300-110200001-2', '门诊诊查费(主治医师)', '指医护人员提供(技术劳务)的诊疗服务', '', '次', 6.00, '诊查费', 1, 'system', ''),
('110200001-2/1', '001102000010300-110200001-2/1', '门诊诊查费(传染病、发热或心理门诊)(主治医师)', '', '', '次', 7.80, '诊查费', 1, 'system', ''),
('110200001-2c', '001102000010300-110200001-2c', '门诊诊查费(6岁及以下)(主治医师)', '', '', '次', 10.00, '诊查费', 1, 'system', ''),
('110200001-3', '001102000010200-110200001-3', '门诊诊查费(副主任医师及以上)', '指医护人员提供(技术劳务)的诊疗服务', '', '次', 12.00, '诊查费', 1, 'system', ''),
('110200001-4', '001102000010400-110200001-4', '急诊(便民)门诊诊查费', '指只取药及开单检查，不需另外提供诊疗方案的门诊诊查服务', '', '次', 2.00, '诊查费', 1, 'system', ''),
('110200002', '001102000020000-110200002', '专家门诊诊查费', '任主任医师级职称五年以上；2、享受政府津贴的知名专家；3、名老中医；4、45岁以上博士生导师', '', '次', 30.00, '专家门诊', 1, 'system', '');

-- 医疗器械设备初始数据（基于Excel数据）  
INSERT INTO medical_equipment (finance_category, equipment_code, national_code, equipment_name, equipment_content, exclude_content, unit_type, price, category, status, create_by, remark) VALUES 
('G', '300000000/1', '453300000010000-300000000/1', '使用腹膜镜加收', '', '', '台次', 150.00, '内镜设备', 1, 'system', ''),
('G', '300000000/2', '453300000010000-300000000/2', '使用关节镜加收', '', '', '台次', 250.00, '内镜设备', 1, 'system', ''),
('G', '300000000/3', '453300000010000-300000000/3', '使用胸腔镜加收', '', '', '台次', 500.00, '内镜设备', 1, 'system', ''),
('G', '300000000/4', '453300000010000-300000000/4', '使用胆道镜加收', '', '', '台次', 400.00, '内镜设备', 1, 'system', ''),
('G', '300000000/5', '453300000010000-300000000/5', '使用手术显微镜加收', '', '', '台次', 200.00, '显微设备', 1, 'system', ''),
('G', '300000000/6', '453300000010000-300000000/6', '使用宫腔镜加收', '', '', '台次', 250.00, '内镜设备', 1, 'system', ''),
('G', '300000000/7', '453300000010000-300000000/7', '使用高频电刀', '', '', '台次', 50.00, '电外科设备', 1, 'system', ''),
('G', '300000000/8', '453300000010000-300000000/8', '使用氩气刀', '', '', '台次', 200.00, '电外科设备', 1, 'system', ''),
('G', '300000000/9', '453300000010000-300000000/9', '使用超声刀', '', '', '台次', 1000.00, '超声设备', 1, 'system', ''),
('D', '310100001', '003101000010000-310100001', '脑电图', '含诱发试验，至少8导', '', '次', 36.00, '神经电生理', 1, 'system', '脑电发生源定位加收10元，术中监测20元/小时，脑电图16导及以上加收10元');

-- 疾病编码初始数据（基于Excel数据）
INSERT INTO disease (icd_code, disease_code, disease_name, disease_category, description, status, create_by, remark) VALUES 
('A00.051', 'GDXHL', '古典型霍乱', '霍乱', '古典型霍乱疾病', 1, 'system', ''),
('A00.052', 'ZXDXHL', '中型[典型]霍乱', '霍乱', '中型典型霍乱疾病', 1, 'system', ''),
('A00.053', 'ZXBFXHGXHL', '重型[暴发型或干性]霍乱', '霍乱', '重型暴发型或干性霍乱疾病', 1, 'system', ''),
('A00.151', 'QXFDXHL', '轻型[非典型]霍乱', '霍乱', '轻型非典型霍乱疾病', 1, 'system', ''),
('A00.152', 'AETHL', '埃尔托霍乱', '霍乱', '埃尔托霍乱疾病', 1, 'system', ''),
('A00.153', 'AETXCY', '埃尔托小肠炎', '肠炎', '埃尔托小肠炎疾病', 1, 'system', ''),
('A00.901', 'HL', '霍乱 NOS', '霍乱', '霍乱疾病（未明确分类）', 1, 'system', ''),
('A01.001', 'SH', '伤寒', '伤寒', '伤寒疾病', 1, 'system', ''),
('A01.002', 'SHCJXBYZ', '伤寒杆菌性败血症', '伤寒', '伤寒杆菌引起的败血症', 1, 'system', ''),
('A01.003+', 'SHXMY', '伤寒性脑膜炎', '脑膜炎', '伤寒引起的脑膜炎', 1, 'system', ''),
('A01.051+', 'SHFY', '伤寒肺炎', '肺炎', '伤寒引起的肺炎', 1, 'system', ''),
('A01.052', 'SHFMY', '伤寒腹膜炎', '伤寒', '伤寒引起的腹膜炎', 1, 'system', ''),
('A01.053', 'ABTSBSH', '埃贝特Eberth氏长病(伤寒)', '伤寒', '埃贝特氏伤寒疾病', 1, 'system', ''),
('A01.054', 'CCXXSH', '肠出血性伤寒', '伤寒', '导致肠出血的伤寒', 1, 'system', ''),
('A01.055', 'SHXCCK', '伤寒性肠穿孔', '伤寒', '伤寒引起的肠穿孔', 1, 'system', '');

-- 医院初始数据
INSERT INTO hospital (hospital_code, hospital_name, hospital_level, hospital_type, address, province, city, district, phone, contact_person, email, status, create_by, remark) VALUES 
('H001', '中国医科大学附属第一医院', '三级', '公立', '沈阳市和平区南京北街155号', '辽宁省', '沈阳市', '和平区', '024-83283333', '张主任', 'contact@cmu1h.com', 1, 'system', '三级甲等医院'),
('H002', '沈阳市第一人民医院', '三级', '公立', '沈阳市大东区清真街1号', '辽宁省', '沈阳市', '大东区', '024-24736435', '李主任', 'contact@sy1h.com', 1, 'system', '三级甲等医院'),
('H003', '沈阳和平区中心医院', '二级', '公立', '沈阳市和平区长白二街16号', '辽宁省', '沈阳市', '和平区', '024-23454567', '王主任', 'contact@hpzx.com', 1, 'system', '二级甲等医院'),
('H004', '沈阳市社区卫生服务中心', '一级', '公立', '沈阳市铁西区建设大路32号', '辽宁省', '沈阳市', '铁西区', '024-25556789', '刘主任', 'contact@community.com', 1, 'system', '一级医院');

COMMIT; 