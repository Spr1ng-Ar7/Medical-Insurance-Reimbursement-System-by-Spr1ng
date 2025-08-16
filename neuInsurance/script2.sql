create table if not exists disease
(
    id               bigint auto_increment comment '疾病ID'
        primary key,
    icd_code         varchar(20)                        not null comment '国际ICD编码',
    disease_code     varchar(50)                        not null comment '疾病编码',
    disease_name     varchar(200)                       not null comment '疾病名称',
    disease_category varchar(50)                        null comment '疾病分类',
    description      text                               null comment '疾病描述',
    status           int      default 1                 null comment '状态：1-正常，0-停用',
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by        varchar(50)                        null comment '创建人',
    update_by        varchar(50)                        null comment '更新人',
    remark           varchar(500)                       null comment '备注',
    constraint uk_disease_code
        unique (disease_code),
    constraint uk_icd_code
        unique (icd_code)
)
    comment '疾病编码表';

create index idx_disease_category
    on disease (disease_category);

create table if not exists drug
(
    id             bigint auto_increment comment '药品ID'
        primary key,
    drug_code      varchar(50)                             not null comment '药品编码',
    drug_name      varchar(200)                            not null comment '医保目录名称',
    trade_name     varchar(200)                            null comment '商品名',
    drug_type      varchar(10)                             not null comment '药品类型：甲类/乙类/丙类',
    self_pay_ratio decimal(5, 4) default 0.0000            null comment '自付比例',
    specification  varchar(200)                            null comment '规格',
    unit           varchar(20)                             null comment '单位',
    price          decimal(10, 2)                          null comment '支付标准（价格）',
    manufacturer   varchar(200)                            null comment '生产企业',
    status         int           default 1                 null comment '状态：1-正常，0-停用',
    create_time    datetime      default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    datetime      default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by      varchar(50)                             null comment '创建人',
    update_by      varchar(50)                             null comment '更新人',
    remark         varchar(500)                            null comment '备注',
    constraint uk_drug_code
        unique (drug_code)
)
    comment '药品表' collate = utf8mb4_unicode_ci;

create index idx_drug_type
    on drug (drug_type);

create index idx_manufacturer
    on drug (manufacturer);

create table if not exists expense_detail
(
    id                   bigint auto_increment comment '明细ID'
        primary key,
    patient_id           bigint                                   not null comment '患者ID',
    order_id             bigint                                   not null comment '医疗订单ID',
    item_type            varchar(20)                              not null comment '项目类型：drug-药品/service-服务/equipment-设备',
    item_id              bigint                                   not null comment '项目ID',
    item_code            varchar(50)                              null comment '项目编码',
    item_name            varchar(200)                             not null comment '项目名称',
    specification        varchar(200)                             null comment '规格型号',
    unit                 varchar(20)                              null comment '计量单位',
    quantity             int            default 1                 null comment '数量',
    unit_price           decimal(10, 2) default 0.00              null comment '单价',
    total_amount         decimal(10, 2) default 0.00              null comment '总金额',
    reimbursement_ratio  decimal(5, 2)  default 0.00              null comment '报销比例',
    reimbursement_amount decimal(10, 2) default 0.00              null comment '报销金额',
    self_pay_amount      decimal(10, 2) default 0.00              null comment '自付金额',
    service_type         varchar(50)                              null comment '服务类型（当itemType为service时）',
    department           varchar(100)                             null comment '科室',
    doctor               varchar(50)                              null comment '医生',
    expense_date         datetime                                 null comment '费用发生日期',
    status               tinyint        default 1                 null comment '状态：1-正常，2-已报销，0-作废',
    create_time          datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_time          datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by            varchar(50)                              null comment '创建人',
    update_by            varchar(50)                              null comment '更新人',
    remark               varchar(500)                             null comment '备注'
)
    comment '费用明细表' collate = utf8mb4_unicode_ci;

create index idx_expense_date
    on expense_detail (expense_date);

create index idx_item_type
    on expense_detail (item_type);

create index idx_order_id
    on expense_detail (order_id);

create index idx_patient_id
    on expense_detail (patient_id);

create index idx_status
    on expense_detail (status);

create table if not exists hospital
(
    id             bigint auto_increment comment '医院ID'
        primary key,
    hospital_code  varchar(50)                        not null comment '医院编码',
    hospital_name  varchar(100)                       not null comment '医院名称',
    hospital_level varchar(10)                        not null comment '医院等级：一级/二级/三级',
    hospital_type  varchar(20)                        null comment '医院类型：公立/私立',
    address        varchar(200)                       null comment '地址',
    province       varchar(50)                        null comment '省份',
    city           varchar(50)                        null comment '城市',
    district       varchar(50)                        null comment '区县',
    phone          varchar(20)                        null comment '联系电话',
    contact_person varchar(50)                        null comment '联系人',
    email          varchar(100)                       null comment '邮箱',
    website        varchar(200)                       null comment '网站',
    description    varchar(500)                       null comment '描述',
    status         int      default 1                 null comment '状态：1-正常，0-停用',
    create_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by      varchar(50)                        null comment '创建人',
    update_by      varchar(50)                        null comment '更新人',
    remark         varchar(500)                       null comment '备注',
    constraint uk_hospital_code
        unique (hospital_code)
)
    comment '医院表';

create table if not exists medical_equipment
(
    id                bigint auto_increment comment '设备项目ID'
        primary key,
    finance_category  varchar(10)                        null comment '财务分类',
    equipment_code    varchar(50)                        not null comment '项目编码',
    national_code     varchar(100)                       null comment '国家编码',
    equipment_name    varchar(200)                       not null comment '项目名称',
    equipment_content text                               null comment '项目内容',
    exclude_content   text                               null comment '除外内容',
    unit_type         varchar(20)                        null comment '计价单位',
    price             decimal(10, 2)                     null comment '价格',
    category          varchar(50)                        null comment '设备类别',
    status            int      default 1                 null comment '状态：1-正常，0-停用',
    create_time       datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by         varchar(50)                        null comment '创建人',
    update_by         varchar(50)                        null comment '更新人',
    remark            text                               null comment '说明',
    constraint uk_equipment_code
        unique (equipment_code)
)
    comment '医疗器械设备表';

create index idx_category
    on medical_equipment (category);

create index idx_finance_category
    on medical_equipment (finance_category);

create table if not exists medical_order
(
    id                   bigint auto_increment comment '订单ID'
        primary key,
    order_no             varchar(50)                              not null comment '订单编号',
    patient_id           bigint                                   not null comment '患者ID',
    patient_name         varchar(50)                              null comment '患者姓名',
    patient_id_card      varchar(18)                              null comment '患者身份证',
    hospital_id          bigint                                   null comment '医院ID',
    hospital_name        varchar(100)                             null comment '医院名称',
    hospital_level       varchar(20)                              null comment '医院等级',
    doctor_id            bigint                                   null comment '医生ID',
    doctor_name          varchar(50)                              null comment '医生姓名',
    department_name      varchar(100)                             null comment '科室名称',
    diagnosis            text                                     null comment '诊断信息',
    order_type           varchar(20)                              null comment '订单类型：门诊/住院/急诊',
    visit_time           datetime                                 null comment '就诊时间',
    discharge_time       datetime                                 null comment '出院时间（住院）',
    stay_days            int                                      null comment '住院天数',
    total_amount         decimal(10, 2) default 0.00              null comment '总费用',
    drug_amount          decimal(10, 2) default 0.00              null comment '药品费用',
    treatment_amount     decimal(10, 2) default 0.00              null comment '诊疗费用',
    service_amount       decimal(10, 2) default 0.00              null comment '服务设施费用',
    other_amount         decimal(10, 2) default 0.00              null comment '其他费用',
    category_a_amount    decimal(10, 2) default 0.00              null comment '甲类药品费用',
    category_b_amount    decimal(10, 2) default 0.00              null comment '乙类药品费用',
    category_c_amount    decimal(10, 2) default 0.00              null comment '丙类药品费用',
    deductible           decimal(10, 2) default 0.00              null comment '起付线',
    reimbursement_ratio  decimal(5, 4)  default 0.0000            null comment '报销比例',
    reimbursable_amount  decimal(10, 2) default 0.00              null comment '可报销金额',
    actual_reimbursement decimal(10, 2) default 0.00              null comment '实际报销金额',
    self_pay_amount      decimal(10, 2) default 0.00              null comment '自付金额',
    settlement_no        varchar(50)                              null comment '结算单号',
    settlement_time      datetime                                 null comment '结算时间',
    approval_result      varchar(50)                              null comment '审批结果',
    approval_remark      varchar(500)                             null comment '审批备注',
    reject_reason        varchar(500)                             null comment '拒绝原因',
    status               tinyint        default 1                 null comment '状态：1-待结算，2-已结算，3-已支付，0-已取消，4-待审批，5-已拒绝',
    create_time          datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_time          datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by            varchar(50)                              null comment '创建人',
    update_by            varchar(50)                              null comment '更新人',
    remark               varchar(500)                             null comment '备注',
    constraint uk_order_no
        unique (order_no),
    constraint uk_settlement_no
        unique (settlement_no)
)
    comment '医疗订单表（整合结算功能）' collate = utf8mb4_unicode_ci;

create index idx_department_name
    on medical_order (department_name);

create index idx_doctor_id
    on medical_order (doctor_id);

create index idx_hospital_id
    on medical_order (hospital_id);

create index idx_order_type
    on medical_order (order_type);

create index idx_patient_id
    on medical_order (patient_id);

create index idx_settlement_time
    on medical_order (settlement_time);

create index idx_status
    on medical_order (status);

create index idx_visit_time
    on medical_order (visit_time);

create table if not exists medical_order_item
(
    id                  bigint auto_increment comment '明细ID'
        primary key,
    order_id            bigint                                   not null comment '订单ID',
    item_type           varchar(20)                              not null comment '项目类型：drug-药品/service-服务/equipment-设备',
    item_id             bigint                                   not null comment '关联项目ID',
    item_code           varchar(50)                              null comment '项目编码',
    item_name           varchar(200)                             not null comment '项目名称',
    specification       varchar(200)                             null comment '规格',
    unit                varchar(20)                              null comment '单位',
    quantity            decimal(10, 2) default 1.00              null comment '数量',
    unit_price          decimal(10, 2) default 0.00              null comment '单价',
    total_amount        decimal(10, 2) default 0.00              null comment '总金额',
    self_pay_ratio      decimal(5, 4)  default 0.0000            null comment '自付比例',
    self_pay_amount     decimal(10, 2) default 0.00              null comment '自付金额',
    reimbursable_amount decimal(10, 2) default 0.00              null comment '可报销金额',
    category            varchar(20)                              null comment '类别：甲类/乙类/丙类',
    service_type        varchar(50)                              null comment '服务类型（当itemType为service时）',
    department          varchar(100)                             null comment '科室',
    doctor              varchar(50)                              null comment '医生',
    service_time        datetime                                 null comment '服务时间',
    status              tinyint        default 1                 null comment '状态：1-正常，0-取消',
    create_time         datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_time         datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    remark              varchar(500)                             null comment '备注'
)
    comment '医疗订单明细表' collate = utf8mb4_unicode_ci;

create index idx_item_id
    on medical_order_item (item_id);

create index idx_item_type
    on medical_order_item (item_type);

create index idx_order_id
    on medical_order_item (order_id);

create index idx_status
    on medical_order_item (status);

create table if not exists medical_service
(
    id                  bigint auto_increment comment '服务ID'
        primary key,
    service_code        varchar(50)                              not null comment '项目编码',
    national_code       varchar(100)                             null comment '国家编码',
    service_name        varchar(200)                             not null comment '项目名称',
    service_type        varchar(50)                              null comment '服务类型：诊疗/检查/手术/设施/化验/护理',
    category            varchar(50)                              null comment '服务类别：门诊/住院/急诊/手术室/ICU等',
    service_content     text                                     null comment '项目内容',
    exclude_content     text                                     null comment '除外内容',
    unit_type           varchar(20)                              null comment '计价单位：次/天/小时/项等',
    price               decimal(10, 2) default 0.00              null comment '价格',
    reimbursement_ratio decimal(5, 2)  default 0.00              null comment '报销比例',
    department          varchar(100)                             null comment '科室',
    doctor_level        varchar(50)                              null comment '医生级别：主治/副主任/主任',
    reimbursable        tinyint        default 1                 null comment '是否可报销：1-可报销，0-不可报销',
    insurance_category  varchar(20)                              null comment '医保分类：甲类/乙类/丙类',
    status              tinyint        default 1                 null comment '状态：1-正常，0-停用',
    create_time         datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_time         datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by           varchar(50)                              null comment '创建人',
    update_by           varchar(50)                              null comment '更新人',
    remark              text                                     null comment '说明',
    equipment_type      varchar(100)                             null comment '设备类型（用于检查类服务）',
    surgery_level       varchar(50)                              null comment '手术级别（用于手术类服务）',
    nursing_level       varchar(50)                              null comment '护理级别（用于护理类服务）',
    duration            int                                      null comment '服务时长（分钟）',
    anesthesia_type     varchar(50)                              null comment '麻醉类型（用于手术类服务）',
    constraint uk_national_code
        unique (national_code),
    constraint uk_service_code
        unique (service_code)
)
    comment '统一医疗服务表' collate = utf8mb4_unicode_ci;

create index idx_category
    on medical_service (category);

create index idx_department
    on medical_service (department);

create index idx_service_name
    on medical_service (service_name);

create index idx_service_type
    on medical_service (service_type);

create index idx_status
    on medical_service (status);

create table if not exists menu
(
    id          bigint auto_increment comment '菜单ID'
        primary key,
    parent_id   bigint       default 0                 null comment '父菜单ID，根菜单为0',
    menu_name   varchar(50)                            not null comment '菜单名称',
    menu_code   varchar(50)                            not null comment '菜单编码',
    menu_path   varchar(200) default ''                null comment '菜单路径',
    component   varchar(255)                           null comment '组件路径',
    icon        varchar(100) default '#'               null comment '菜单图标',
    order_num   int          default 0                 null comment '显示顺序',
    menu_type   char         default ''                null comment '菜单类型：M(目录)、C(菜单)、F(按钮)',
    visible     char         default '0'               null comment '是否显示：0(显示)、1(隐藏)',
    perms       varchar(100)                           null comment '权限标识',
    query       varchar(255)                           null comment '路由参数',
    is_frame    int          default 1                 null comment '是否为外链：0(否)、1(是)',
    is_cache    int          default 0                 null comment '是否缓存：0(缓存)、1(不缓存)',
    status      char         default '0'               null comment '状态：1-正常，0-停用',
    create_time datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by   varchar(64)  default ''                null comment '创建者',
    update_by   varchar(64)  default ''                null comment '更新者',
    remark      varchar(500) default ''                null comment '备注',
    constraint uk_menu_code
        unique (menu_code)
)
    comment '菜单表' collate = utf8mb4_unicode_ci;

create index idx_menu_type
    on menu (menu_type);

create index idx_parent_id
    on menu (parent_id);

create index idx_status
    on menu (status);

create table if not exists patient
(
    id               bigint auto_increment comment '患者ID'
        primary key,
    patient_id       varchar(50)                        not null comment '患者编号',
    name             varchar(50)                        not null comment '姓名',
    id_card          varchar(18)                        not null comment '身份证号',
    gender           varchar(10)                        null comment '性别',
    birth_date       date                               null comment '出生日期',
    phone            varchar(20)                        null comment '联系电话',
    address          varchar(255)                       null comment '地址',
    insurance_type   varchar(50)                        null comment '医保类型：城镇职工/城乡居民等',
    insurance_number varchar(50)                        null comment '医保号',
    status           tinyint  default 1                 null comment '状态：1-正常，0-注销',
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    remark           varchar(500)                       null comment '备注',
    constraint uk_id_card
        unique (id_card),
    constraint uk_patient_id
        unique (patient_id)
)
    comment '患者表' collate = utf8mb4_unicode_ci;

create index idx_insurance_type
    on patient (insurance_type);

create index idx_name
    on patient (name);

create index idx_status
    on patient (status);

create table if not exists reimbursement_level
(
    id                bigint auto_increment comment '报销等级ID'
        primary key,
    level_code        varchar(50)                              not null comment '等级编码',
    level_name        varchar(100)                             not null comment '等级名称',
    insurance_type    varchar(20)                              not null comment '医保类型：城镇职工/城乡居民等',
    hospital_level    varchar(10)                              not null comment '医院等级：一级/二级/三级',
    min_amount        decimal(10, 2) default 0.00              null comment '最低费用',
    max_amount        decimal(10, 2)                           null comment '最高费用',
    deductible        decimal(10, 2) default 0.00              null comment '起付线',
    max_reimbursement decimal(10, 2)                           null comment '最高报销额度',
    category_a_rate   decimal(5, 4)  default 0.0000            null comment '甲类药品报销比例',
    category_b_rate   decimal(5, 4)  default 0.0000            null comment '乙类药品报销比例',
    category_c_rate   decimal(5, 4)  default 0.0000            null comment '丙类药品报销比例',
    treatment_rate    decimal(5, 4)  default 0.0000            null comment '诊疗费用报销比例',
    service_rate      decimal(5, 4)  default 0.0000            null comment '服务设施费用报销比例',
    status            int            default 1                 null comment '状态：1-正常，0-禁用',
    effective_time    datetime                                 null comment '生效时间',
    expire_time       datetime                                 null comment '失效时间',
    create_time       datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by         varchar(50)                              null comment '创建人',
    update_by         varchar(50)                              null comment '更新人',
    remark            text                                     null comment '备注',
    constraint uk_level_code
        unique (level_code)
)
    comment '报销等级配置表' collate = utf8mb4_unicode_ci;

create index idx_insurance_hospital
    on reimbursement_level (insurance_type, hospital_level);

create table if not exists role_menu
(
    id          bigint auto_increment comment 'ID'
        primary key,
    role_id     bigint                             not null comment '角色ID',
    menu_id     bigint                             not null comment '菜单ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_role_menu
        unique (role_id, menu_id)
)
    comment '角色菜单关联表' collate = utf8mb4_unicode_ci;

create table if not exists sys_admin
(
    id               bigint auto_increment comment '管理员ID'
        primary key,
    username         varchar(50)                        not null comment '用户名',
    password         varchar(100)                       not null comment '密码',
    real_name        varchar(50)                        null comment '真实姓名',
    email            varchar(100)                       null comment '邮箱',
    phone            varchar(20)                        null comment '电话',
    avatar           varchar(200)                       null comment '头像URL',
    user_type        varchar(20)                        null comment '用户类型：admin-管理员/doctor-医生/nurse-护士/finance-财务/operator-操作员',
    role             varchar(50)                        null comment '角色：SUPER_ADMIN/ADMIN/DOCTOR/NURSE/FINANCE/OPERATOR',
    department       varchar(100)                       null comment '部门',
    position         varchar(50)                        null comment '职位',
    id_card          varchar(18)                        null comment '身份证号',
    gender           varchar(10)                        null comment '性别：男/女',
    birth_date       varchar(20)                        null comment '出生日期',
    address          varchar(200)                       null comment '地址',
    status           int      default 1                 null comment '状态：1-正常，0-禁用',
    department_id    bigint                             null comment '部门ID',
    last_login_time  datetime                           null comment '最后登录时间',
    last_login_ip    varchar(50)                        null comment '最后登录IP',
    login_fail_count int      default 0                 null comment '登录失败次数',
    locked_time      datetime                           null comment '锁定时间',
    create_time      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by        varchar(50)                        null comment '创建人',
    update_by        varchar(50)                        null comment '更新人',
    remark           varchar(500)                       null comment '备注',
    license_number   varchar(50)                        null comment '执业证号（医生）',
    specialty        varchar(100)                       null comment '专业特长（医生）',
    title            varchar(50)                        null comment '职称：主任医师/副主任医师/主治医师等',
    work_schedule    varchar(50)                        null comment '工作班次（护士）',
    certification    varchar(100)                       null comment '资格证书',
    constraint uk_username
        unique (username)
)
    comment '管理员表' collate = utf8mb4_unicode_ci;

create index idx_department_id
    on sys_admin (department_id);

create table if not exists sys_admin_role
(
    id          bigint auto_increment comment 'ID'
        primary key,
    admin_id    bigint                             not null comment '管理员ID',
    role_id     bigint                             not null comment '角色ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_admin_role
        unique (admin_id, role_id)
)
    comment '管理员角色关联表' collate = utf8mb4_unicode_ci;

create table if not exists sys_department
(
    id          bigint auto_increment comment '部门ID'
        primary key,
    dept_code   varchar(50)                        not null comment '部门编码',
    dept_name   varchar(100)                       not null comment '部门名称',
    parent_id   bigint   default 0                 null comment '父部门ID',
    level       int      default 1                 null comment '层级',
    leader      varchar(50)                        null comment '负责人',
    phone       varchar(20)                        null comment '联系电话',
    email       varchar(100)                       null comment '邮箱',
    sort_order  int      default 0                 null comment '排序',
    status      int      default 1                 null comment '状态：1-正常，0-禁用',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by   varchar(50)                        null comment '创建人',
    update_by   varchar(50)                        null comment '更新人',
    remark      varchar(500)                       null comment '备注',
    constraint uk_dept_code
        unique (dept_code)
)
    comment '部门表' collate = utf8mb4_unicode_ci;

create table if not exists sys_permission
(
    id              bigint auto_increment comment '权限ID'
        primary key,
    permission_code varchar(100)                       not null comment '权限编码',
    permission_name varchar(100)                       not null comment '权限名称',
    resource_type   varchar(20)                        not null comment '资源类型：menu-菜单，button-按钮，api-接口',
    resource_path   varchar(200)                       null comment '资源路径',
    http_method     varchar(10)                        null comment 'HTTP方法：GET,POST,PUT,DELETE',
    parent_id       bigint   default 0                 null comment '父权限ID',
    level           int      default 1                 null comment '层级',
    sort_order      int      default 0                 null comment '排序',
    icon            varchar(100)                       null comment '图标',
    status          int      default 1                 null comment '状态：1-正常，0-禁用',
    create_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by       varchar(50)                        null comment '创建人',
    update_by       varchar(50)                        null comment '更新人',
    remark          varchar(500)                       null comment '备注',
    constraint uk_permission_code
        unique (permission_code)
)
    comment '权限表' collate = utf8mb4_unicode_ci;

create table if not exists sys_role
(
    id          bigint auto_increment comment '角色ID'
        primary key,
    role_code   varchar(50)                        not null comment '角色编码',
    role_name   varchar(100)                       not null comment '角色名称',
    description varchar(500)                       null comment '角色描述',
    status      int      default 1                 null comment '状态：1-正常，0-禁用',
    sort_order  int      default 0                 null comment '排序',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    create_by   varchar(50)                        null comment '创建人',
    update_by   varchar(50)                        null comment '更新人',
    remark      varchar(500)                       null comment '备注',
    constraint uk_role_code
        unique (role_code)
)
    comment '角色表' collate = utf8mb4_unicode_ci;

create table if not exists sys_role_permission
(
    id            bigint auto_increment comment 'ID'
        primary key,
    role_id       bigint                             not null comment '角色ID',
    permission_id bigint                             not null comment '权限ID',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_role_permission
        unique (role_id, permission_id)
)
    comment '角色权限关联表' collate = utf8mb4_unicode_ci;

-- ==================== 系统管理数据初始化 ====================

-- 1. 部门数据
INSERT INTO sys_department (dept_code, dept_name, parent_id, level, leader, phone, email, sort_order, status, create_by, remark) VALUES 
('ROOT', '医保管理局', 0, 1, '张局长', '010-12345678', 'admin@insurance.gov.cn', 1, 1, 'system', '根部门'),
('ADMIN', '系统管理部', 1, 2, '李部长', '010-12345679', 'admin@insurance.gov.cn', 1, 1, 'system', '系统管理部门'),
('BUSINESS', '业务管理部', 1, 2, '王部长', '010-12345680', 'business@insurance.gov.cn', 2, 1, 'system', '业务管理部门'),
('MEDICAL', '医疗服务部', 1, 2, '赵部长', '010-12345681', 'medical@insurance.gov.cn', 3, 1, 'system', '医疗服务管理部门'),
('FINANCE', '财务管理部', 1, 2, '钱部长', '010-12345682', 'finance@insurance.gov.cn', 4, 1, 'system', '财务管理部门');

-- 2. 管理员数据
INSERT INTO sys_admin (username, password, real_name, email, phone, user_type, role, department, position, gender, title, status, department_id, create_by, remark) VALUES 
('admin', 'admin456', '超级管理员', 'admin@insurance.gov.cn', '13800000000', 'admin', 'SUPER_ADMIN', '系统管理部', '系统管理员', '男', '系统管理员', 1, 2, 'system', '系统默认管理员'),
('manager', 'manager456', '业务管理员', 'manager@insurance.gov.cn', '13800000001', 'admin', 'BUSINESS_ADMIN', '业务管理部', '业务管理员', '男', '业务管理员', 1, 3, 'system', '业务管理员'),
('doctor', 'doctor456', '医生管理员', 'doctor@insurance.gov.cn', '13800000002', 'doctor', 'DOCTOR_ADMIN', '医疗服务部', '医生管理员', '男', '主任医师', 1, 4, 'system', '医生管理员'),
('finance', 'finance456', '财务管理员', 'finance@insurance.gov.cn', '13800000003', 'finance', 'FINANCE_ADMIN', '财务管理部', '财务管理员', '女', '财务管理员', 1, 5, 'system', '财务管理员');

-- 3. 角色数据
INSERT INTO sys_role (role_code, role_name, description, status, sort_order, create_by, remark) VALUES 
('SUPER_ADMIN', '超级管理员', '系统超级管理员，拥有所有权限', 1, 1, 'system', '系统内置角色'),
('ADMIN', '系统管理员', '系统管理员，拥有系统管理权限', 1, 2, 'system', '系统内置角色'),
('BUSINESS_ADMIN', '业务管理员', '业务管理员，拥有业务管理权限', 1, 3, 'system', '系统内置角色'),
('DOCTOR_ADMIN', '医生管理员', '医生管理员，拥有医疗服务管理权限', 1, 4, 'system', '系统内置角色'),
('FINANCE_ADMIN', '财务管理员', '财务管理员，拥有财务管理权限', 1, 5, 'system', '系统内置角色'),
('OPERATOR', '操作员', '普通操作员，拥有基本操作权限', 1, 6, 'system', '系统内置角色');

-- 4. 权限数据
INSERT INTO sys_permission (permission_code, permission_name, resource_type, resource_path, parent_id, level, sort_order, icon, status, create_by, remark) VALUES 
-- 系统管理模块
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
('SYSTEM:MENU', '菜单管理', 'menu', '/system/menu', 1, 2, 3, 'menu', 1, 'system', '菜单管理菜单'),
('SYSTEM:MENU:LIST', '菜单列表', 'api', '/api/menu/list', 12, 3, 1, '', 1, 'system', '菜单列表权限'),
('SYSTEM:MENU:ADD', '新增菜单', 'button', '/api/menu', 12, 3, 2, '', 1, 'system', '新增菜单权限'),
('SYSTEM:MENU:UPDATE', '修改菜单', 'button', '/api/menu/*', 12, 3, 3, '', 1, 'system', '修改菜单权限'),
('SYSTEM:MENU:DELETE', '删除菜单', 'button', '/api/menu/*', 12, 3, 4, '', 1, 'system', '删除菜单权限'),
('SYSTEM:DEPT', '部门管理', 'menu', '/system/dept', 1, 2, 4, 'dept', 1, 'system', '部门管理菜单'),
('SYSTEM:DEPT:LIST', '部门列表', 'api', '/api/dept/list', 17, 3, 1, '', 1, 'system', '部门列表权限'),
('SYSTEM:DEPT:ADD', '新增部门', 'button', '/api/dept', 17, 3, 2, '', 1, 'system', '新增部门权限'),
('SYSTEM:DEPT:UPDATE', '修改部门', 'button', '/api/dept/*', 17, 3, 3, '', 1, 'system', '修改部门权限'),
('SYSTEM:DEPT:DELETE', '删除部门', 'button', '/api/dept/*', 17, 3, 4, '', 1, 'system', '删除部门权限'),

-- 业务管理模块
('BUSINESS', '业务管理', 'menu', '/business', 0, 1, 2, 'business', 1, 'system', '业务管理菜单'),
('BUSINESS:DRUG', '药品管理', 'menu', '/business/drug', 22, 2, 1, 'drug', 1, 'system', '药品管理菜单'),
('BUSINESS:DRUG:LIST', '药品列表', 'api', '/api/drug/list', 23, 3, 1, '', 1, 'system', '药品列表权限'),
('BUSINESS:DRUG:ADD', '新增药品', 'button', '/api/drug', 23, 3, 2, '', 1, 'system', '新增药品权限'),
('BUSINESS:DRUG:UPDATE', '修改药品', 'button', '/api/drug/*', 23, 3, 3, '', 1, 'system', '修改药品权限'),
('BUSINESS:DRUG:DELETE', '删除药品', 'button', '/api/drug/*', 23, 3, 4, '', 1, 'system', '删除药品权限'),
('BUSINESS:PATIENT', '患者管理', 'menu', '/business/patient', 22, 2, 2, 'patient', 1, 'system', '患者管理菜单'),
('BUSINESS:PATIENT:LIST', '患者列表', 'api', '/api/patient/list', 28, 3, 1, '', 1, 'system', '患者列表权限'),
('BUSINESS:PATIENT:ADD', '新增患者', 'button', '/api/patient', 28, 3, 2, '', 1, 'system', '新增患者权限'),
('BUSINESS:PATIENT:UPDATE', '修改患者', 'button', '/api/patient/*', 28, 3, 3, '', 1, 'system', '修改患者权限'),
('BUSINESS:PATIENT:DELETE', '删除患者', 'button', '/api/patient/*', 28, 3, 4, '', 1, 'system', '删除患者权限'),
('BUSINESS:ORDER', '医疗订单', 'menu', '/business/order', 22, 2, 3, 'order', 1, 'system', '医疗订单管理菜单'),
('BUSINESS:ORDER:LIST', '订单列表', 'api', '/api/order/list', 33, 3, 1, '', 1, 'system', '订单列表权限'),
('BUSINESS:ORDER:ADD', '新增订单', 'button', '/api/order', 33, 3, 2, '', 1, 'system', '新增订单权限'),
('BUSINESS:ORDER:UPDATE', '修改订单', 'button', '/api/order/*', 33, 3, 3, '', 1, 'system', '修改订单权限'),
('BUSINESS:ORDER:DELETE', '删除订单', 'button', '/api/order/*', 33, 3, 4, '', 1, 'system', '删除订单权限'),
('BUSINESS:REIMBURSEMENT', '报销等级管理', 'menu', '/business/reimbursement', 22, 2, 4, 'reimbursement', 1, 'system', '报销等级管理菜单'),
('BUSINESS:REIMBURSEMENT:LIST', '报销等级列表', 'api', '/api/reimbursement/list', 38, 3, 1, '', 1, 'system', '报销等级列表权限'),
('BUSINESS:REIMBURSEMENT:ADD', '新增报销等级', 'button', '/api/reimbursement', 38, 3, 2, '', 1, 'system', '新增报销等级权限'),
('BUSINESS:REIMBURSEMENT:UPDATE', '修改报销等级', 'button', '/api/reimbursement/*', 38, 3, 3, '', 1, 'system', '修改报销等级权限'),
('BUSINESS:REIMBURSEMENT:DELETE', '删除报销等级', 'button', '/api/reimbursement/*', 38, 3, 4, '', 1, 'system', '删除报销等级权限'),

-- 医疗服务模块
('MEDICAL', '医疗服务', 'menu', '/medical', 0, 1, 3, 'medical', 1, 'system', '医疗服务菜单'),
('MEDICAL:SERVICE', '服务项目管理', 'menu', '/medical/service', 43, 2, 1, 'service', 1, 'system', '服务项目管理菜单'),
('MEDICAL:SERVICE:LIST', '服务项目列表', 'api', '/api/service/list', 44, 3, 1, '', 1, 'system', '服务项目列表权限'),
('MEDICAL:SERVICE:ADD', '新增服务项目', 'button', '/api/service', 44, 3, 2, '', 1, 'system', '新增服务项目权限'),
('MEDICAL:SERVICE:UPDATE', '修改服务项目', 'button', '/api/service/*', 44, 3, 3, '', 1, 'system', '修改服务项目权限'),
('MEDICAL:SERVICE:DELETE', '删除服务项目', 'button', '/api/service/*', 44, 3, 4, '', 1, 'system', '删除服务项目权限'),
('MEDICAL:EQUIPMENT', '医疗器械管理', 'menu', '/medical/equipment', 43, 2, 2, 'equipment', 1, 'system', '医疗器械管理菜单'),
('MEDICAL:EQUIPMENT:LIST', '医疗器械列表', 'api', '/api/equipment/list', 49, 3, 1, '', 1, 'system', '医疗器械列表权限'),
('MEDICAL:EQUIPMENT:ADD', '新增医疗器械', 'button', '/api/equipment', 49, 3, 2, '', 1, 'system', '新增医疗器械权限'),
('MEDICAL:EQUIPMENT:UPDATE', '修改医疗器械', 'button', '/api/equipment/*', 49, 3, 3, '', 1, 'system', '修改医疗器械权限'),
('MEDICAL:EQUIPMENT:DELETE', '删除医疗器械', 'button', '/api/equipment/*', 49, 3, 4, '', 1, 'system', '删除医疗器械权限'),
('MEDICAL:DISEASE', '疾病管理', 'menu', '/medical/disease', 43, 2, 3, 'disease', 1, 'system', '疾病管理菜单'),
('MEDICAL:DISEASE:LIST', '疾病列表', 'api', '/api/disease/list', 54, 3, 1, '', 1, 'system', '疾病列表权限'),
('MEDICAL:DISEASE:ADD', '新增疾病', 'button', '/api/disease', 54, 3, 2, '', 1, 'system', '新增疾病权限'),
('MEDICAL:DISEASE:UPDATE', '修改疾病', 'button', '/api/disease/*', 54, 3, 3, '', 1, 'system', '修改疾病权限'),
('MEDICAL:DISEASE:DELETE', '删除疾病', 'button', '/api/disease/*', 54, 3, 4, '', 1, 'system', '删除疾病权限'),

-- 财务管理模块
('FINANCE', '财务管理', 'menu', '/finance', 0, 1, 4, 'finance', 1, 'system', '财务管理菜单'),
('FINANCE:SETTLEMENT', '结算管理', 'menu', '/finance/settlement', 59, 2, 1, 'settlement', 1, 'system', '结算管理菜单'),
('FINANCE:SETTLEMENT:LIST', '结算列表', 'api', '/api/settlement/list', 60, 3, 1, '', 1, 'system', '结算列表权限'),
('FINANCE:SETTLEMENT:ADD', '新增结算', 'button', '/api/settlement', 60, 3, 2, '', 1, 'system', '新增结算权限'),
('FINANCE:SETTLEMENT:UPDATE', '修改结算', 'button', '/api/settlement/*', 60, 3, 3, '', 1, 'system', '修改结算权限'),
('FINANCE:SETTLEMENT:DELETE', '删除结算', 'button', '/api/settlement/*', 60, 3, 4, '', 1, 'system', '删除结算权限'),
('FINANCE:EXPENSE', '费用明细管理', 'menu', '/finance/expense', 59, 2, 2, 'expense', 1, 'system', '费用明细管理菜单'),
('FINANCE:EXPENSE:LIST', '费用明细列表', 'api', '/api/expense/list', 65, 3, 1, '', 1, 'system', '费用明细列表权限'),
('FINANCE:EXPENSE:ADD', '新增费用明细', 'button', '/api/expense', 65, 3, 2, '', 1, 'system', '新增费用明细权限'),
('FINANCE:EXPENSE:UPDATE', '修改费用明细', 'button', '/api/expense/*', 65, 3, 3, '', 1, 'system', '修改费用明细权限'),
('FINANCE:EXPENSE:DELETE', '删除费用明细', 'button', '/api/expense/*', 65, 3, 4, '', 1, 'system', '删除费用明细权限');

-- 5. 菜单数据
INSERT INTO menu (parent_id, menu_name, menu_code, menu_path, component, icon, order_num, menu_type, visible, perms, status, create_by, remark) VALUES 
-- 根菜单
(0, '系统管理', 'system', '/system', NULL, 'system', 1, 'M', '0', NULL, '0', 'system', '系统管理目录'),
(0, '业务管理', 'business', '/business', NULL, 'business', 2, 'M', '0', NULL, '0', 'system', '业务管理目录'),
(0, '医疗服务', 'medical', '/medical', NULL, 'medical', 3, 'M', '0', NULL, '0', 'system', '医疗服务目录'),
(0, '财务管理', 'finance', '/finance', NULL, 'finance', 4, 'M', '0', NULL, '0', 'system', '财务管理目录'),

-- 系统管理子菜单
(1, '管理员管理', 'admin', 'admin', 'system/admin/index', 'admin', 1, 'C', '0', 'system:admin:list', '0', 'system', '管理员管理菜单'),
(1, '角色管理', 'role', 'role', 'system/role/index', 'role', 2, 'C', '0', 'system:role:list', '0', 'system', '角色管理菜单'),
(1, '菜单管理', 'menu', 'menu', 'system/menu/index', 'menu', 3, 'C', '0', 'system:menu:list', '0', 'system', '菜单管理菜单'),
(1, '部门管理', 'dept', 'dept', 'system/dept/index', 'dept', 4, 'C', '0', 'system:dept:list', '0', 'system', '部门管理菜单'),

-- 业务管理子菜单
(2, '药品管理', 'drug', 'drug', 'business/drug/index', 'drug', 1, 'C', '0', 'business:drug:list', '0', 'system', '药品管理菜单'),
(2, '患者管理', 'patient', 'patient', 'business/patient/index', 'patient', 2, 'C', '0', 'business:patient:list', '0', 'system', '患者管理菜单'),
(2, '医疗订单', 'order', 'order', 'business/order/index', 'order', 3, 'C', '0', 'business:order:list', '0', 'system', '医疗订单管理菜单'),
(2, '报销等级管理', 'reimbursement', 'reimbursement', 'business/reimbursement/index', 'reimbursement', 4, 'C', '0', 'business:reimbursement:list', '0', 'system', '报销等级管理菜单'),

-- 医疗服务子菜单
(3, '服务项目管理', 'service', 'service', 'medical/service/index', 'service', 1, 'C', '0', 'medical:service:list', '0', 'system', '服务项目管理菜单'),
(3, '医疗器械管理', 'equipment', 'equipment', 'medical/equipment/index', 'equipment', 2, 'C', '0', 'medical:equipment:list', '0', 'system', '医疗器械管理菜单'),
(3, '疾病管理', 'disease', 'disease', 'medical/disease/index', 'disease', 3, 'C', '0', 'medical:disease:list', '0', 'system', '疾病管理菜单'),

-- 财务管理子菜单
(4, '结算管理', 'settlement', 'settlement', 'finance/settlement/index', 'settlement', 1, 'C', '0', 'finance:settlement:list', '0', 'system', '结算管理菜单'),
(4, '费用明细管理', 'expense', 'expense', 'finance/expense/index', 'expense', 2, 'C', '0', 'finance:expense:list', '0', 'system', '费用明细管理菜单');

-- 6. 管理员角色关联
INSERT INTO sys_admin_role (admin_id, role_id) VALUES 
(1, 1),  -- admin -> SUPER_ADMIN
(2, 3),  -- manager -> BUSINESS_ADMIN
(3, 4),  -- doctor -> DOCTOR_ADMIN
(4, 5);  -- finance -> FINANCE_ADMIN

-- 7. 角色权限关联（超级管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id) 
SELECT 1, id FROM sys_permission WHERE status = 1;

-- 系统管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES 
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6),  -- 系统管理
(2, 7), (2, 8), (2, 9), (2, 10), (2, 11),        -- 角色管理
(2, 12), (2, 13), (2, 14), (2, 15), (2, 16),      -- 菜单管理
(2, 17), (2, 18), (2, 19), (2, 20), (2, 21);      -- 部门管理

-- 业务管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES 
(3, 22), (3, 23), (3, 24), (3, 25), (3, 26), (3, 27),  -- 药品管理
(3, 28), (3, 29), (3, 30), (3, 31), (3, 32),            -- 患者管理
(3, 33), (3, 34), (3, 35), (3, 36), (3, 37),            -- 医疗订单
(3, 38), (3, 39), (3, 40), (3, 41), (3, 42);            -- 报销等级

-- 医生管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES 
(4, 43), (4, 44), (4, 45), (4, 46), (4, 47), (4, 48),  -- 服务项目管理
(4, 49), (4, 50), (4, 51), (4, 52), (4, 53),            -- 医疗器械管理
(4, 54), (4, 55), (4, 56), (4, 57), (4, 58);            -- 疾病管理

-- 财务管理员权限
INSERT INTO sys_role_permission (role_id, permission_id) VALUES 
(5, 59), (5, 60), (5, 61), (5, 62), (5, 63), (5, 64),  -- 结算管理
(5, 65), (5, 66), (5, 67), (5, 68), (5, 69);            -- 费用明细管理

-- 8. 角色菜单关联
-- 超级管理员拥有所有菜单
INSERT INTO role_menu (role_id, menu_id) 
SELECT 1, id FROM menu WHERE status = '0';

-- 系统管理员菜单
INSERT INTO role_menu (role_id, menu_id) VALUES 
(2, 1), (2, 5), (2, 6), (2, 7), (2, 8);

-- 业务管理员菜单
INSERT INTO role_menu (role_id, menu_id) VALUES 
(3, 2), (3, 9), (3, 10), (3, 11), (3, 12);

-- 医生管理员菜单
INSERT INTO role_menu (role_id, menu_id) VALUES 
(4, 3), (4, 13), (4, 14), (4, 15);

-- 财务管理员菜单
INSERT INTO role_menu (role_id, menu_id) VALUES 
(5, 4), (5, 16), (5, 17);

-- 9. 报销等级配置初始数据
INSERT INTO reimbursement_level (level_code, level_name, insurance_type, hospital_level, min_amount, max_amount, deductible, max_reimbursement, category_a_rate, category_b_rate, category_c_rate, treatment_rate, service_rate, status, effective_time, create_by, remark) VALUES 
('LEVEL_1', '一级报销', '城镇职工', '一级', 0, 30000, 1300, 30000, 0.9500, 0.8500, 0.0000, 0.9000, 0.8000, 1, '2024-01-01 00:00:00', 'system', '城镇职工一级医院报销标准'),
('LEVEL_2', '二级报销', '城镇职工', '二级', 0, 40000, 1800, 40000, 0.9000, 0.8000, 0.0000, 0.8500, 0.7500, 1, '2024-01-01 00:00:00', 'system', '城镇职工二级医院报销标准'),
('LEVEL_3', '三级报销', '城镇职工', '三级', 0, 50000, 2300, 50000, 0.8500, 0.7500, 0.0000, 0.8000, 0.7000, 1, '2024-01-01 00:00:00', 'system', '城镇职工三级医院报销标准'),
('LEVEL_4', '居民一级报销', '城乡居民', '一级', 0, 20000, 500, 20000, 0.8000, 0.7000, 0.0000, 0.7500, 0.6500, 1, '2024-01-01 00:00:00', 'system', '城乡居民一级医院报销标准'),
('LEVEL_5', '居民二级报销', '城乡居民', '二级', 0, 30000, 800, 30000, 0.7500, 0.6500, 0.0000, 0.7000, 0.6000, 1, '2024-01-01 00:00:00', 'system', '城乡居民二级医院报销标准'),
('LEVEL_6', '居民三级报销', '城乡居民', '三级', 0, 40000, 1200, 40000, 0.7000, 0.6000, 0.0000, 0.6500, 0.5500, 1, '2024-01-01 00:00:00', 'system', '城乡居民三级医院报销标准');

-- 10. 药品初始数据（基于Excel数据）
INSERT INTO drug (drug_code, drug_name, trade_name, drug_type, self_pay_ratio, specification, unit, price, manufacturer, status, create_by, remark) VALUES 
('D001', '奥比利利', '维建乐', '丙类', 1.0000, '奥比地平12.5mg,阿立瑞钠75mg,利比昔50mg', '盒', 2712.60, '爱尔兰Fournier Laboratories Ireland Limited', 1, 'system', ''),
('D002', '达塞布韦瑞', '易奇瑞', '丙类', 1.0000, '0.25g', '盒', 235.80, '爱尔兰AbbVie Ireland NL B.V.', 1, 'system', ''),
('D003', '格卡瑞加派合他药', '艾诺全', '丙类', 1.0000, '格卡瑞加0.1g,派合他药40mg', '盒', 4750.00, '爱尔兰Fournier Laboratories Ireland Limited', 1, 'system', ''),
('D004', '达诺瑞韦钠', '艾诺卫', '丙类', 1.0000, '0.1g', '瓶', 6665.99, '歌礼药业(浙江)有限公司', 1, 'system', ''),
('D005', '华夏2号无蛋白氨酸食品固体饮料(奶粉)', '', '丙类', 1.0000, '400g', '罐', 96.00, '上海津佳食品有限公司', 1, 'system', ''),
('D006', '阿司匹林肠溶片', '拜阿司匹林', '甲类', 0.0000, '100mg*30片', '盒', 15.50, '拜耳医药保健有限公司', 1, 'system', '经典解热镇痛药'),
('D007', '硝苯地平缓释片', '拜新同', '乙类', 0.1000, '30mg*7片', '盒', 45.80, '拜耳医药保健有限公司', 1, 'system', '钙通道阻滞剂'),
('D008', '格列吡嗪缓释片', '瑞易宁', '乙类', 0.1000, '5mg*20片', '盒', 28.90, '辉瑞制药有限公司', 1, 'system', '降糖药'),
('D009', '奥美拉唑肠溶胶囊', '洛赛克', '乙类', 0.1000, '20mg*14粒', '盒', 52.60, '阿斯利康制药有限公司', 1, 'system', '质子泵抑制剂'),
('D010', '氨氯地平片', '络活喜', '乙类', 0.1000, '5mg*7片', '盒', 18.70, '辉瑞制药有限公司', 1, 'system', '钙通道阻滞剂');

-- 11. 医疗服务项目初始数据（基于Excel数据）
INSERT INTO medical_service (service_code, national_code, service_name, service_content, exclude_content, unit_type, price, category, status, create_by, remark) VALUES 
('110200001-1', '001102000010300-110200001-1', '门诊诊查费(医师)', '指医护人员提供(技术劳务)的诊疗服务', '', '次', 3.00, '诊查费', 1, 'system', ''),
('110200001-1/1', '001102000010300-110200001-1/1', '门诊诊查费(传染病、发热或心理门诊医师)', '', '', '次', 3.90, '诊查费', 1, 'system', ''),
('110200001-1c', '001102000010300-110200001-1c', '门诊诊查费(6岁及以下)(医师)', '', '', '次', 8.00, '诊查费', 1, 'system', ''),
('110200001-1/1c', '001102000010300-110200001-1/1c', '门诊诊查费(传染病、发热或心理门诊)(6岁及以下)(医师)', '', '', '次', 10.40, '诊查费', 1, 'system', ''),
('110200001-2', '001102000010300-110200001-2', '门诊诊查费(主治医师)', '指医护人员提供(技术劳务)的诊疗服务', '', '次', 6.00, '诊查费', 1, 'system', ''),
('110100008', '001101000080100-110100008', '血常规', '包括白细胞计数及分类、红细胞计数、血红蛋白测定、血细胞比容、血小板计数', '', '次', 25.00, '化验检查', 1, 'system', ''),
('110100009', '001101000090100-110100009', '尿常规', '包括尿蛋白、尿糖、尿酮体、尿胆原、胆红素、亚硝酸盐、白细胞酯酶', '', '次', 10.00, '化验检查', 1, 'system', ''),
('220101001', '002201010010100-220101001', 'X线摄影', '胸部正位片', '', '次', 35.00, '影像检查', 1, 'system', ''),
('220201001', '002202010010100-220201001', 'CT检查', '头部CT平扫', '', '次', 120.00, '影像检查', 1, 'system', ''),
('330101001', '003301010010100-330101001', '心电图检查', '常规心电图', '', '次', 15.00, '功能检查', 1, 'system', '');

-- 12. 医疗器械设备初始数据（基于Excel数据）  
INSERT INTO medical_equipment (finance_category, equipment_code, national_code, equipment_name, equipment_content, exclude_content, unit_type, price, category, status, create_by, remark) VALUES 
('G', '300000000/1', '453300000010000-300000000/1', '使用腹膜镜加收', '', '', '台次', 150.00, '内镜设备', 1, 'system', ''),
('G', '300000000/2', '453300000010000-300000000/2', '使用关节镜加收', '', '', '台次', 250.00, '内镜设备', 1, 'system', ''),
('G', '300000000/3', '453300000010000-300000000/3', '使用胸腔镜加收', '', '', '台次', 500.00, '内镜设备', 1, 'system', ''),
('G', '300000000/4', '453300000010000-300000000/4', '使用胆道镜加收', '', '', '台次', 400.00, '内镜设备', 1, 'system', ''),
('G', '300000000/5', '453300000010000-300000000/5', '使用手术显微镜加收', '', '', '台次', 200.00, '显微设备', 1, 'system', ''),
('G', '400000001', '454000000010000-400000001', 'CT增强扫描加收', '', '', '次', 80.00, 'CT设备', 1, 'system', ''),
('G', '400000002', '454000000020000-400000002', 'MRI增强扫描加收', '', '', '次', 120.00, 'MRI设备', 1, 'system', ''),
('G', '400000003', '454000000030000-400000003', '超声引导下穿刺活检加收', '', '', '次', 180.00, '超声设备', 1, 'system', ''),
('G', '400000004', '454000000040000-400000004', '数字化X线摄影(DR)加收', '', '', '次', 50.00, 'X线设备', 1, 'system', ''),
('G', '400000005', '454000000050000-400000005', '全自动生化分析仪加收', '', '', '项', 10.00, '检验设备', 1, 'system', '');

-- 13. 疾病编码初始数据（基于Excel数据）
INSERT INTO disease (icd_code, disease_code, disease_name, disease_category, description, status, create_by, remark) VALUES 
('A00.051', 'GDXHL', '古典型霍乱', '霍乱', '古典型霍乱疾病', 1, 'system', ''),
('A00.052', 'ZXDXHL', '中型[典型]霍乱', '霍乱', '中型典型霍乱疾病', 1, 'system', ''),
('A00.053', 'ZXBFXHGXHL', '重型[暴发型或干性]霍乱', '霍乱', '重型暴发型或干性霍乱疾病', 1, 'system', ''),
('A00.151', 'QXFDXHGXHL', '轻型[非典型]霍乱', '霍乱', '轻型非典型霍乱疾病', 1, 'system', ''),
('A00.152', 'AETHL', '埃尔托霍乱', '霍乱', '埃尔托霍乱疾病', 1, 'system', ''),
('I10.001', 'BFXGXY', '原发性高血压', '循环系统疾病', '原发性高血压疾病', 1, 'system', ''),
('E11.901', 'ⅡXTNB', 'Ⅱ型糖尿病', '内分泌疾病', 'Ⅱ型糖尿病疾病', 1, 'system', ''),
('K25.901', 'WKY', '胃溃疡', '消化系统疾病', '胃溃疡疾病', 1, 'system', ''),
('J44.101', 'MXZS', '慢性阻塞性肺疾病', '呼吸系统疾病', '慢性阻塞性肺疾病', 1, 'system', ''),
('N18.601', 'MXSSY', '慢性肾脏病', '泌尿系统疾病', '慢性肾脏病', 1, 'system', '');

-- 14. 医院初始数据
INSERT INTO hospital (hospital_code, hospital_name, hospital_level, hospital_type, address, province, city, district, phone, contact_person, email, status, create_by, remark) VALUES 
('H001', '中国医科大学附属第一医院', '三级', '公立', '沈阳市和平区南京北街155号', '辽宁省', '沈阳市', '和平区', '024-83283333', '张主任', 'contact@cmu1h.com', 1, 'system', '三级甲等医院'),
('H002', '沈阳市第一人民医院', '三级', '公立', '沈阳市大东区清真街1号', '辽宁省', '沈阳市', '大东区', '024-24736435', '李主任', 'contact@sy1h.com', 1, 'system', '三级甲等医院'),
('H003', '沈阳和平区中心医院', '二级', '公立', '沈阳市和平区长白二街16号', '辽宁省', '沈阳市', '和平区', '024-23454567', '王主任', 'contact@hpzx.com', 1, 'system', '二级甲等医院'),
('H004', '沈阳市社区卫生服务中心', '一级', '公立', '沈阳市铁西区建设大路32号', '辽宁省', '沈阳市', '铁西区', '024-25556789', '刘主任', 'contact@community.com', 1, 'system', '一级医院'),
('H005', '北京协和医院', '三级', '公立', '北京市东城区帅府园1号', '北京市', '北京市', '东城区', '010-69156114', '赵主任', 'contact@pumch.cn', 1, 'system', '三级甲等医院'),
('H006', '上海瑞金医院', '三级', '公立', '上海市黄浦区瑞金二路197号', '上海市', '上海市', '黄浦区', '021-64370045', '钱主任', 'contact@ruijin.com', 1, 'system', '三级甲等医院');

-- 15. 患者初始数据
INSERT INTO patient (patient_id, name, id_card, gender, birth_date, phone, address, insurance_type, insurance_number, status, remark) VALUES 
('P001', '张三', '110101199001011234', '男', '1990-01-01', '13800000001', '北京市朝阳区', '城镇职工', 'YB001234567', 1, '测试患者1'),
('P002', '李四', '110101199002022345', '女', '1990-02-02', '13800000002', '北京市海淀区', '城乡居民', 'YB002345678', 1, '测试患者2'),
('P003', '王五', '110101199003033456', '男', '1990-03-03', '13800000003', '北京市西城区', '城镇职工', 'YB003456789', 1, '测试患者3'),
('P004', '赵六', '220101199004044567', '女', '1990-04-04', '13800000004', '沈阳市和平区', '城乡居民', 'YB004567890', 1, '测试患者4'),
('P005', '钱七', '310101199005055678', '男', '1990-05-05', '13800000005', '上海市浦东新区', '城镇职工', 'YB005678901', 1, '测试患者5');

-- 16. 医疗订单示例数据
INSERT INTO medical_order (order_no, patient_id, patient_name, patient_id_card, hospital_id, hospital_name, hospital_level, doctor_name, department_name, diagnosis, order_type, visit_time, total_amount, drug_amount, treatment_amount, service_amount, category_a_amount, category_b_amount, category_c_amount, deductible, reimbursement_ratio, reimbursable_amount, actual_reimbursement, self_pay_amount, status, create_by, remark) VALUES 
('MO202507170001', 1, '张三', '110101199001011234', 1, '中国医科大学附属第一医院', '三级', '王医生', '内科', '高血压', '门诊', '2025-07-17 09:00:00', 256.80, 90.00, 166.80, 0.00, 50.00, 40.00, 0.00, 2300, 0.8000, 166.80, 133.44, 123.36, 1, 'system', '门诊治疗'),
('MO202507170002', 2, '李四', '110101199002022345', 2, '沈阳市第一人民医院', '三级', '李医生', '内分泌科', '糖尿病', '门诊', '2025-07-17 10:30:00', 420.50, 200.00, 220.50, 0.00, 80.00, 120.00, 0.00, 1200, 0.6500, 220.50, 143.33, 277.17, 1, 'system', '糖尿病复查'),
('MO202507170003', 3, '王五', '110101199003033456', 3, '沈阳和平区中心医院', '二级', '张医生', '消化科', '胃溃疡', '住院', '2025-07-16 08:00:00', 2850.00, 800.00, 2050.00, 0.00, 300.00, 500.00, 0.00, 1800, 0.8000, 2050.00, 1640.00, 1210.00, 2, 'system', '住院治疗'),
('MO202507170004', 4, '赵六', '220101199004044567', 4, '沈阳市社区卫生服务中心', '一级', '刘医生', '全科', '感冒', '门诊', '2025-07-17 14:00:00', 85.60, 35.60, 50.00, 0.00, 35.60, 0.00, 0.00, 500, 0.7500, 85.60, 64.20, 21.40, 1, 'system', '普通感冒'),
('MO202507170005', 5, '钱七', '310101199005055678', 5, '北京协和医院', '三级', '周医生', '呼吸科', '肺炎', '急诊', '2025-07-17 02:30:00', 1280.00, 480.00, 800.00, 0.00, 200.00, 280.00, 0.00, 2300, 0.8000, 1080.00, 864.00, 416.00, 2, 'system', '急诊肺炎治疗');

-- 17. 医疗订单明细示例数据
INSERT INTO medical_order_item (order_id, item_type, item_id, item_code, item_name, specification, unit, quantity, unit_price, total_amount, self_pay_ratio, self_pay_amount, reimbursable_amount, category, department, doctor, service_time, status, remark) VALUES 
-- 订单1的明细
(1, 'drug', 6, 'D006', '阿司匹林肠溶片', '100mg*30片', '盒', 2, 15.50, 31.00, 0.0000, 0.00, 31.00, '甲类', '内科', '王医生', '2025-07-17 09:00:00', 1, ''),
(1, 'drug', 7, 'D007', '硝苯地平缓释片', '30mg*7片', '盒', 1, 45.80, 45.80, 0.1000, 4.58, 41.22, '乙类', '内科', '王医生', '2025-07-17 09:00:00', 1, ''),
(1, 'service', 1, '110200001-2', '门诊诊查费(主治医师)', '', '次', 1, 6.00, 6.00, 0.0000, 0.00, 6.00, '', '内科', '王医生', '2025-07-17 09:00:00', 1, ''),
(1, 'service', 6, '110100008', '血常规', '', '次', 1, 25.00, 25.00, 0.0000, 0.00, 25.00, '', '内科', '王医生', '2025-07-17 09:00:00', 1, ''),
-- 订单2的明细
(2, 'drug', 8, 'D008', '格列吡嗪缓释片', '5mg*20片', '盒', 3, 28.90, 86.70, 0.1000, 8.67, 78.03, '乙类', '内分泌科', '李医生', '2025-07-17 10:30:00', 1, ''),
(2, 'service', 2, '110200001-1/1', '门诊诊查费(传染病、发热或心理门诊医师)', '', '次', 1, 3.90, 3.90, 0.0000, 0.00, 3.90, '', '内分泌科', '李医生', '2025-07-17 10:30:00', 1, ''),
(2, 'service', 6, '110100008', '血常规', '', '次', 1, 25.00, 25.00, 0.0000, 0.00, 25.00, '', '内分泌科', '李医生', '2025-07-17 10:30:00', 1, '');

COMMIT;


