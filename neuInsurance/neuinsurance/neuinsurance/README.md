# 医保报销系统前端

基于 Vue 3 + TypeScript + Element Plus + Pure Admin Lite 构建的医保报销系统前端项目。

## 项目架构

### 技术栈
- **框架**: Vue 3 + TypeScript
- **UI库**: Element Plus
- **模板**: Pure Admin Lite
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **构建工具**: Vite
- **代码规范**: ESLint + Prettier

### 目录结构
```
src/
├── api/                    # API接口
│   ├── auth.ts            # 认证相关
│   ├── patient.ts         # 患者管理
│   ├── drug.ts            # 药品管理
│   ├── medical-order.ts   # 医疗订单
│   ├── settlement.ts      # 结算管理
│   └── system.ts          # 系统管理
├── components/            # 公共组件
│   └── Common/           # 通用组件
│       ├── BaseTable.vue # 基础表格
│       └── BaseForm.vue  # 基础表单
├── router/               # 路由配置
│   └── modules/         # 路由模块
│       ├── patient.ts   # 患者管理路由
│       ├── drug.ts      # 药品管理路由
│       ├── medical-order.ts # 医疗订单路由
│       ├── settlement.ts # 结算管理路由
│       └── system.ts    # 系统管理路由
├── views/               # 页面组件
│   ├── patient/        # 患者管理页面
│   ├── drug/           # 药品管理页面
│   ├── medical-order/  # 医疗订单页面
│   ├── settlement/     # 结算管理页面
│   └── system/         # 系统管理页面
├── store/              # 状态管理
│   └── modules/       # Store模块
│       └── patient.ts # 患者管理Store
├── utils/             # 工具函数
│   ├── constants.ts   # 常量定义
│   └── format.ts      # 格式化工具
└── types/             # 类型定义
    └── index.ts       # 全局类型
```

## 功能模块

### 1. 患者管理
- 患者列表查询
- 患者信息新增/编辑/删除
- 患者详情查看
- 批量操作

### 2. 药品管理
- 药品列表查询
- 药品信息管理
- 药品分类统计
- Excel数据导入

### 3. 医疗订单
- 订单列表查询
- 订单创建/编辑
- 订单审核流程
- 订单统计分析

### 4. 结算管理
- 结算列表查询
- 结算处理
- 结算统计报表
- 数据导出

### 5. 系统管理
- 管理员管理
- 角色权限管理
- 菜单管理
- 系统配置

## 开发指南

### 环境要求
- Node.js >= 16
- pnpm >= 7

### 安装依赖
```bash
pnpm install
```

### 开发模式
```bash
pnpm dev
```

### 构建生产版本
```bash
pnpm build
```

### 代码检查
```bash
pnpm lint
```

### 代码格式化
```bash
pnpm format
```

## 组件使用

### BaseTable 基础表格组件
```vue
<template>
  <BaseTable
    :data="tableData"
    :loading="loading"
    :show-selection="true"
    @search="handleSearch"
    @selection-change="handleSelectionChange"
  >
    <!-- 搜索区域 -->
    <template #search="{ searchForm }">
      <el-form-item label="名称">
        <el-input v-model="searchForm.name" placeholder="请输入名称" />
      </el-form-item>
    </template>

    <!-- 工具栏 -->
    <template #toolbar-left>
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </template>

    <!-- 表格列 -->
    <el-table-column prop="name" label="名称" />
    <el-table-column prop="createTime" label="创建时间" />

    <!-- 操作列 -->
    <template #action="{ row }">
      <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
      <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
    </template>
  </BaseTable>
</template>
```

### BaseForm 基础表单组件
```vue
<template>
  <BaseForm
    v-model="formData"
    :rules="rules"
    @submit="handleSubmit"
    @cancel="handleCancel"
  >
    <el-form-item label="名称" prop="name">
      <el-input v-model="formData.name" placeholder="请输入名称" />
    </el-form-item>
    
    <el-form-item label="描述" prop="description">
      <el-input
        v-model="formData.description"
        type="textarea"
        placeholder="请输入描述"
      />
    </el-form-item>
  </BaseForm>
</template>
```

## API接口规范

### 请求格式
```typescript
// GET请求
const result = await getList({ page: 1, size: 10, name: "搜索关键词" });

// POST请求
const result = await createItem({ name: "名称", description: "描述" });

// PUT请求
const result = await updateItem(id, { name: "新名称" });

// DELETE请求
const result = await deleteItem(id);
```

### 响应格式
```typescript
interface ApiResponse<T> {
  code: number;      // 状态码
  message: string;   // 消息
  data: T;          // 数据
  success: boolean;  // 是否成功
}

interface PageResult<T> {
  records: T[];      // 数据列表
  total: number;     // 总数
  size: number;      // 每页大小
  current: number;   // 当前页
}
```

## 状态管理

### Store使用示例
```typescript
import { usePatientStore } from "@/store/modules/patient";

const patientStore = usePatientStore();

// 获取患者列表
await patientStore.fetchPatientList({ page: 1, size: 10 });

// 获取患者详情
await patientStore.fetchPatientDetail(1);

// 获取状态
const patientList = patientStore.getPatientList;
const loading = patientStore.getLoading;
```

## 路由配置

### 路由模块示例
```typescript
export default {
  path: "/patient",
  redirect: "/patient/list",
  meta: {
    icon: "ep:user",
    title: "患者管理",
    rank: 1
  },
  children: [
    {
      path: "/patient/list",
      name: "PatientList",
      component: () => import("@/views/patient/list.vue"),
      meta: {
        title: "患者列表",
        showParent: true
      }
    }
  ]
} as RouteConfigsTable;
```

## 开发规范

### 命名规范
- 组件名：PascalCase（如：PatientList）
- 文件名：kebab-case（如：patient-list.vue）
- 变量名：camelCase（如：patientList）
- 常量名：UPPER_SNAKE_CASE（如：API_BASE_URL）

### 代码规范
- 使用 TypeScript 进行类型检查
- 使用 ESLint 进行代码检查
- 使用 Prettier 进行代码格式化
- 遵循 Vue 3 Composition API 规范

### 提交规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式化
- refactor: 代码重构
- test: 测试相关
- chore: 构建过程或辅助工具的变动

## 部署说明

### 构建
```bash
pnpm build
```

### 部署到服务器
将 `dist` 目录下的文件部署到 Web 服务器即可。

### 环境变量配置
```bash
# 开发环境
VITE_API_BASE_URL=http://localhost:8080/api

# 生产环境
VITE_API_BASE_URL=https://api.example.com/api
```

## 常见问题

### 1. 跨域问题
开发环境下，Vite 会自动代理 API 请求到后端服务器。

### 2. 权限控制
使用路由守卫和权限指令进行权限控制。

### 3. 数据缓存
使用 Pinia 进行状态管理，避免重复请求。

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本发布
- 完成基础框架搭建
- 实现患者管理模块
- 实现药品管理模块
- 实现医疗订单模块
- 实现结算管理模块
- 实现系统管理模块
