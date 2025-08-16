# Pure Admin 组件库重构完成说明

## 概述

本次重构使用 [Pure Admin 组件库](https://pure-admin.cn/pages/components/#pure-admin-table) 对前端页面进行了全面改造，充分利用了 `@pureadmin/table` 的强大功能，提升了开发效率和用户体验。

## 使用的 Pure Admin 组件

### 1. PureTable 组件
- **依赖**: `@pureadmin/table`
- **功能**: 功能强大的表格组件，支持多种高级特性
- **主要特性**:
  - 内置搜索功能，支持多种搜索类型
  - 内置工具栏，支持自定义按钮
  - 内置分页功能，支持多种分页样式
  - 支持列显示/隐藏控制
  - 支持列拖拽排序
  - 支持表格全屏显示
  - 支持数据导出
  - 支持树形表格
  - 支持虚拟滚动

### 2. PureTableBar 组件
- **位置**: `@/components/RePureTableBar/`
- **功能**: 表格工具栏组件
- **特性**:
  - 列显示控制
  - 列拖拽排序
  - 表格密度控制
  - 全屏切换
  - 刷新功能

### 3. RePerms 组件
- **位置**: `@/components/RePerms/`
- **功能**: 权限控制组件
- **特性**:
  - 基于权限显示/隐藏内容
  - 支持多种权限验证方式

### 4. ReSegmented 组件
- **位置**: `@/components/ReSegmented/`
- **功能**: 分段控制器组件
- **特性**:
  - 支持多种分段样式
  - 支持自定义选项

## 重构的页面

### 1. 患者管理模块
- **页面**: `src/views/patient/list.vue`
- **重构内容**:
  - 使用 PureTable 组件替换原有表格
  - 配置表格列定义
  - 使用头部按钮配置
  - 集成搜索功能
  - 支持批量操作
  - 自定义操作列模板

### 2. 药品管理模块
- **页面**: `src/views/drug/list.vue`
- **重构内容**:
  - 使用 PureTable 组件重构整个列表页面
  - 配置药品类型、价格、状态等自定义列模板
  - 集成新增/编辑对话框
  - 支持导入和统计功能
  - 使用头部按钮配置

### 3. 医疗订单模块
- **页面**: `src/views/medical-order/list.vue`
- **重构内容**:
  - 使用 PureTable 组件重构列表页面
  - 配置金额显示模板
  - 配置状态标签模板
  - 支持订单审核流程
  - 集成批量操作功能

## PureTable 组件使用示例

### 基础配置
```vue
<template>
  <PureTable
    row-key="id"
    :loading="loading"
    :columns="columns"
    :data="tableData"
    :pagination="pagination"
    :headerButtons="headerButtons"
    :tableToolbar="true"
    :search="true"
    :searchInfo="searchInfo"
    @page-size-change="handleSizeChange"
    @page-current-change="handleCurrentChange"
    @selection-change="handleSelectionChange"
    @search="handleSearch"
  >
    <!-- 工具栏 -->
    <template #toolbar>
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </template>

    <!-- 自定义列模板 -->
    <template #status="{ row }">
      <el-tag :type="getStatusTag(row.status)">
        {{ getStatusText(row.status) }}
      </template>

    <!-- 操作列 -->
    <template #operation="{ row }">
      <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
    </template>
  </PureTable>
</template>
```

### 表格列配置
```typescript
const columns = ref([
  {
    type: "selection",
    width: 55,
    align: "center",
    hide: false
  },
  {
    label: "名称",
    prop: "name",
    minWidth: 120,
    hide: false
  },
  {
    label: "状态",
    prop: "status",
    width: 100,
    slot: "status", // 使用自定义模板
    hide: false
  },
  {
    label: "操作",
    fixed: "right",
    width: 200,
    slot: "operation", // 使用自定义模板
    hide: false
  }
]);
```

### 头部按钮配置
```typescript
const headerButtons = ref([
  {
    type: "primary",
    label: "新增",
    icon: "Plus",
    click: () => handleAdd()
  },
  {
    type: "danger",
    label: "批量删除",
    icon: "Delete",
    disabled: () => selectedRows.value.length === 0,
    click: () => handleBatchDelete()
  }
]);
```

## PureTable 高级功能

### 1. 搜索功能
```typescript
// 搜索信息配置
const searchInfo = reactive({
  name: "",
  status: undefined,
  dateRange: []
});

// 搜索处理
const handleSearch = (params: any) => {
  Object.assign(searchInfo, params);
  pagination.currentPage = 1;
  fetchData();
};
```

### 2. 分页功能
```typescript
// 分页配置
const pagination = reactive({
  total: 0,
  pageSize: 10,
  currentPage: 1,
  background: true
});

// 分页事件处理
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchData();
};

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  fetchData();
};
```

### 3. 选择功能
```typescript
// 选择变化处理
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection;
};
```

## 重构优势

### 1. 功能强大
- **内置搜索**: 支持多种搜索类型，无需手动实现
- **内置分页**: 支持多种分页样式和配置
- **内置工具栏**: 支持自定义按钮和操作
- **列控制**: 支持列显示/隐藏和拖拽排序
- **全屏显示**: 支持表格全屏查看

### 2. 开发效率高
- **配置化开发**: 通过配置即可实现复杂功能
- **模板化**: 支持自定义列模板，灵活展示数据
- **事件驱动**: 统一的事件处理机制
- **类型支持**: 完整的 TypeScript 支持

### 3. 用户体验好
- **响应式设计**: 自适应不同屏幕尺寸
- **交互友好**: 丰富的交互反馈
- **性能优化**: 支持虚拟滚动，大数据量性能优秀
- **主题适配**: 支持多种主题切换

### 4. 维护性强
- **组件化**: 功能模块化，便于维护
- **标准化**: 统一的API和配置方式
- **扩展性**: 支持自定义扩展和插件

## 使用建议

### 1. 列配置最佳实践
- 使用 `slot` 属性实现自定义列模板
- 合理设置列宽度，避免内容溢出
- 使用 `hide` 属性控制列显示/隐藏
- 使用 `fixed` 属性固定重要列

### 2. 搜索配置最佳实践
- 合理配置搜索字段类型
- 使用 `searchInfo` 统一管理搜索状态
- 实现搜索重置功能
- 优化搜索性能

### 3. 分页配置最佳实践
- 合理设置每页显示数量
- 使用 `background` 属性美化分页样式
- 实现分页状态保持
- 优化分页性能

### 4. 工具栏配置最佳实践
- 使用 `headerButtons` 配置头部按钮
- 使用 `toolbar` 插槽添加自定义工具栏
- 合理设置按钮权限控制
- 优化按钮布局

## 注意事项

### 1. 依赖安装
确保项目中已正确安装 Pure Admin 相关依赖：
```bash
pnpm add @pureadmin/table @pureadmin/utils
```

### 2. 类型定义
- 使用 TypeScript 时注意类型定义
- 合理使用 `any` 类型，避免过度使用
- 为自定义数据定义接口类型

### 3. 性能优化
- 大数据量时考虑使用虚拟滚动
- 合理使用列显示/隐藏功能
- 优化搜索和分页性能

### 4. 样式兼容
- 注意与现有样式的兼容性
- 合理使用主题变量
- 确保响应式布局正常

## 总结

通过使用 Pure Admin 组件库重构前端页面，我们实现了：

1. **功能大幅增强** - 获得了强大的表格功能
2. **开发效率显著提升** - 配置化开发，减少重复代码
3. **用户体验明显改善** - 更丰富的交互和更好的性能
4. **维护成本大幅降低** - 标准化组件，便于维护和扩展

这次重构充分利用了 Pure Admin 组件库的优势，为项目的长期发展奠定了坚实的基础。后续可以在此基础上继续探索更多高级功能，如虚拟滚动、数据导出、树形表格等。 