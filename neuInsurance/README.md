# 医保报销系统 (Medical Insurance Reimbursement System)

基于 Vue 3 + Element Plus + Spring Boot 的医保报销管理系统

## 项目架构

### 后端技术栈
- **框架**: Spring Boot 2.6.13
- **数据库**: MySQL 8.0
- **ORM**: MyBatis
- **安全**: Spring Security
- **构建工具**: Maven

### 前端技术栈
- **框架**: Vue 3.2.13
- **UI组件库**: Element Plus 2.3.8
- **路由**: Vue Router 4.2.4
- **HTTP客户端**: Axios 1.4.0
- **构建工具**: Vue CLI 5.0

## 功能模块

### 1. 基础数据管理模块
- **药品目录管理**: 甲/乙/丙类药品分类管理，自付比例配置
- **诊疗项目管理**: 检查、手术等诊疗项目维护
- **服务设施管理**: 床位费等服务设施配置
- **医院管理**: 医院等级及报销比例设置
- **数据导入**: 支持Excel批量导入功能

### 2. 患者管理模块
- **患者信息登记**: 支持身份证OCR识别
- **医保类型管理**: 城镇职工、城乡居民等分类
- **历史记录查询**: 患者就医历史追溯

### 3. 医嘱管理模块
- **诊断信息维护**: 入院、主要、其他诊断
- **医嘱开具**: 药品医嘱、诊疗医嘱、服务医嘱
- **实时费用计算**: 自付部分实时显示，医保目录匹配提示

### 4. 医保结算模块
- **自动报销计算**: 
  ```
  报销金额 = [(甲类药品总额 + 乙类可报部分) - 起付线] × 报销比例
  约束条件: ≤ 封顶线
  ```
- **费用分解展示**: 药品分类占比、诊疗项目占比、服务设施占比
- **结算单打印导出**: 支持PDF导出和打印

### 5. 统计报表模块
- **多维数据分析**: 时间、医院、病种维度统计
- **可视化展示**: 药品使用热力图、报销趋势图、费用占比图

### 6. 系统管理模块
- **RBAC权限控制**: 角色基础的访问控制
- **操作日志审计**: 关键操作日志记录
- **系统参数配置**: 报销比例、起付线、封顶线配置

## 项目结构

```
neuInsurance/
├── neuinsurance/                    # 前端项目 (Vue 3)
│   ├── src/
│   │   ├── components/             # 公共组件
│   │   │   └── Layout.vue         # 主布局组件
│   │   ├── views/                 # 页面组件
│   │   │   ├── Dashboard.vue      # 首页
│   │   │   ├── drug/              # 药品管理
│   │   │   ├── patient/           # 患者管理
│   │   │   ├── settlement/        # 医保结算
│   │   │   └── statistics/        # 统计报表
│   │   ├── router/                # 路由配置
│   │   └── main.js               # 入口文件
│   └── package.json              # 前端依赖
├── src/main/java/org/neu/insurance/  # 后端项目 (Spring Boot)
│   ├── entity/                    # 实体类
│   │   ├── Drug.java             # 药品实体
│   │   ├── Patient.java          # 患者实体
│   │   ├── Settlement.java       # 结算实体
│   │   └── ...
│   ├── controller/                # 控制层
│   │   ├── DrugController.java   # 药品管理接口
│   │   ├── PatientController.java # 患者管理接口
│   │   └── SettlementController.java # 结算接口
│   ├── service/                   # 服务层
│   │   ├── DrugService.java      # 药品服务接口
│   │   └── impl/                 # 服务实现
│   ├── dto/                       # 数据传输对象
│   │   ├── ApiResponse.java      # 统一响应格式
│   │   ├── PageRequest.java      # 分页请求
│   │   └── SettlementResult.java # 结算结果
│   └── config/                    # 配置类
│       └── WebConfig.java        # Web配置
└── src/main/resources/
    ├── application.yml            # 应用配置
    └── mapper/                    # MyBatis映射文件
```

## 数据库设计要点

### 核心表结构
- `drug`: 药品目录表
- `treatment`: 诊疗项目表  
- `service`: 服务设施表
- `hospital`: 医院信息表
- `patient`: 患者信息表
- `medical_order`: 医嘱表
- `settlement`: 结算记录表
- `user`: 用户表

## 安装与运行

### 后端启动
1. 创建数据库 `neu_insurance`
2. 修改 `application.yml` 中的数据库连接配置
3. 运行 `NeuInsuranceApplication.java`

### 前端启动
```bash
cd neuinsurance
npm install
npm run serve
```

## 开发计划

### 第一阶段 - 基础框架 ✅
- [x] 项目结构搭建
- [x] 实体类设计
- [x] 基础API接口定义
- [x] 前端路由配置
- [x] 主布局组件

### 第二阶段 - 核心功能 (计划中)
- [ ] 数据库表结构设计与创建
- [ ] 基础数据管理CRUD功能
- [ ] 患者管理功能
- [ ] 医嘱管理功能

### 第三阶段 - 结算功能 (计划中)
- [ ] 报销计算引擎
- [ ] 结算流程实现
- [ ] 结算单据生成

### 第四阶段 - 高级功能 (计划中)
- [ ] 统计报表实现
- [ ] 权限管理完善
- [ ] Excel导入导出
- [ ] 系统优化

## 报销算法说明

### 报销计算公式
```java
// 基础报销计算
BigDecimal reimbursableAmount = categoryAAmount.add(categoryBReimbursable);
BigDecimal afterDeductible = reimbursableAmount.subtract(deductible);
BigDecimal actualReimbursement = afterDeductible.multiply(reimbursementRatio);

// 封顶线限制
if (actualReimbursement.compareTo(maxAmount) > 0) {
    actualReimbursement = maxAmount;
}
```

### 不同医院等级报销比例
- **一级医院**: 90% (起付线: 200元)
- **二级医院**: 80% (起付线: 400元)  
- **三级医院**: 70% (起付线: 600元)

## 技术特点

1. **前后端分离**: 便于团队协作和技术栈升级
2. **模块化设计**: 按业务功能划分模块，易于维护
3. **响应式布局**: 支持PC端和移动端访问
4. **数据验证**: 前后端双重数据校验
5. **权限控制**: 基于角色的访问控制
6. **日志审计**: 关键操作全程记录

## 贡献指南

1. Fork 项目
2. 创建特性分支
3. 提交代码变更
4. 发起 Pull Request

## 许可证

MIT License 