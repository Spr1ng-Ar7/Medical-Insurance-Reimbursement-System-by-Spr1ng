# Pure-Admin 组件替换指南

本文档介绍如何使用 pure-admin 封装的组件来替换 Element Plus 原生组件，以提供更好的用户体验和功能。

## 已封装的组件

### 1. ReButton - 增强按钮组件

**功能特性：**
- 支持图标和文本的快速配置
- 统一的样式和交互效果
- 更好的主题适配

**使用示例：**
```vue
<!-- 替换前 -->
<el-button type="primary" @click="handleAdd">
  <IconifyIconOffline icon="ep:plus" />
  新增患者
</el-button>

<!-- 替换后 -->
<ReButton 
  type="primary" 
  iconName="ep:plus" 
  buttonText="新增患者" 
  @click="handleAdd" 
/>
```

**属性说明：**
- `iconName`: 图标名称
- `buttonText`: 按钮文本
- `showBg`: 是否显示背景色
- `customClass`: 自定义样式类

### 2. ReTag - 增强标签组件

**功能特性：**
- 支持图标显示
- 统一的样式和颜色
- 更好的可读性

**使用示例：**
```vue
<!-- 替换前 -->
<el-tag :type="row.gender === '男' ? 'primary' : 'success'" size="small">
  <IconifyIconOffline :icon="row.gender === '男' ? 'ep:male' : 'ep:female'" />
  {{ row.gender }}
</el-tag>

<!-- 替换后 -->
<ReTag 
  :type="row.gender === '男' ? 'primary' : 'success'" 
  size="small"
  :iconName="row.gender === '男' ? 'ep:male' : 'ep:female'"
  :showIcon="true"
  :tagText="row.gender"
/>
```

**属性说明：**
- `iconName`: 图标名称
- `tagText`: 标签文本
- `showIcon`: 是否显示图标
- `customClass`: 自定义样式类

### 3. ReForm - 增强表单组件

**功能特性：**
- 支持标题显示
- 边框样式配置
- 更好的布局

**使用示例：**
```vue
<!-- 替换前 -->
<el-form
  ref="formRef"
  :model="formData"
  :rules="rules"
  label-width="100px"
  class="pure-form"
>
  <!-- 表单内容 -->
</el-form>

<!-- 替换后 -->
<ReForm
  ref="formRef"
  :model="formData"
  :rules="rules"
  label-width="100px"
  class="pure-form"
  showBorder
  showTitle
  title="患者信息"
>
  <!-- 表单内容 -->
</ReForm>
```

**属性说明：**
- `showBorder`: 是否显示边框
- `showTitle`: 是否显示标题
- `title`: 表单标题
- `customClass`: 自定义样式类

### 4. ReDialog - 对话框组件

**功能特性：**
- 支持全屏显示
- 自定义头部和底部
- 更好的交互体验

**使用示例：**
```vue
<!-- 使用 addDialog 函数 -->
import { addDialog } from "../../components";

const handleExport = () => {
  addDialog({
    title: "导出患者数据",
    width: "500px",
    draggable: true,
    closeOnClickModal: false,
    contentRenderer: () => import("../../components/Common/ExportDialog.vue"),
    beforeSure: (done, { options }) => {
      ElMessage.success("导出成功");
      done();
    }
  });
};
```

### 5. ReText - 文本省略组件

**功能特性：**
- 支持单行和多行省略
- 自动显示 Tooltip
- 响应式处理

**使用示例：**
```vue
<!-- 替换前 -->
<span>{{ row.name }}</span>

<!-- 替换后 -->
<ReText :line-clamp="1" :tippy-props="{ content: row.name }">
  {{ row.name }}
</ReText>
```

### 6. ReSegmented - 分段控制器

**功能特性：**
- 支持图标和文本
- 响应式布局
- 主题适配

**使用示例：**
```vue
<ReSegmented
  v-model="row.status"
  :options="statusOptions"
  @change="handleStatusChange(row)"
/>
```

## 组件导入方式

### 方式一：统一导入
```vue
import { 
  ReButton, 
  ReTag, 
  ReForm, 
  ReText, 
  ReSegmented, 
  addDialog 
} from "../../components";
```

### 方式二：单独导入
```vue
import { ReButton } from "../../components/ReButton";
import { ReTag } from "../../components/ReTag";
import { ReForm } from "../../components/ReForm";
```

## 替换建议

### 1. 按钮组件替换
- 所有 `el-button` 建议替换为 `ReButton`
- 特别是带有图标的按钮，使用 `iconName` 和 `buttonText` 属性

### 2. 标签组件替换
- 所有 `el-tag` 建议替换为 `ReTag`
- 需要显示图标的标签使用 `showIcon` 和 `iconName` 属性

### 3. 表单组件替换
- 复杂的表单建议使用 `ReForm`
- 添加 `showBorder` 和 `showTitle` 提升视觉效果

### 4. 对话框替换
- 使用 `addDialog` 函数替代 `el-dialog`
- 支持更丰富的配置选项

### 5. 文本显示替换
- 长文本建议使用 `ReText` 组件
- 自动处理省略和提示

## 样式定制

所有组件都支持通过 `customClass` 属性进行样式定制：

```vue
<ReButton 
  type="primary" 
  customClass="my-custom-button"
  buttonText="自定义按钮" 
/>
```

## 注意事项

1. 确保在 `main.ts` 中正确注册了所有组件
2. 新组件保持了与 Element Plus 原组件相同的 API 设计
3. 所有组件都支持 Element Plus 的原生属性
4. 建议逐步替换，避免一次性大量修改

## 扩展开发

如需开发新的封装组件，请遵循以下规范：

1. 在 `src/components/` 下创建新的组件目录
2. 使用 `withInstall` 进行组件注册
3. 提供完整的 TypeScript 类型定义
4. 在 `src/components/index.ts` 中导出新组件
5. 编写使用文档和示例 