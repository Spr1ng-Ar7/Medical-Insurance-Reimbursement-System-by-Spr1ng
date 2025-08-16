# Vue-Pure-Admin 组件使用指南

## 📋 组件概览

Vue-Pure-Admin 提供了丰富的组件库，可以大大提升开发效率和用户体验。以下是主要组件的使用方法和优化建议。

## 🔐 权限控制组件

### 1. Auth 组件 - 路由权限控制
```vue
<template>
  <!-- 控制按钮级别的权限 -->
  <Auth :value="'system:admin:add'">
    <el-button type="primary" @click="handleAdd">新增管理员</el-button>
  </Auth>
  
  <!-- 控制菜单级别的权限 -->
  <Auth :value="['system:admin:list', 'system:admin:add']">
    <div class="admin-panel">
      <!-- 管理员管理面板 -->
    </div>
  </Auth>
</template>

<script setup lang="ts">
import { Auth } from "@/components/ReAuth";
</script>
```

### 2. Perms 组件 - 功能权限控制
```vue
<template>
  <!-- 控制功能权限 -->
  <Perms :value="'drug:add'">
    <el-button type="primary" @click="handleAddDrug">新增药品</el-button>
  </Perms>
  
  <!-- 多个权限控制 -->
  <Perms :value="['drug:add', 'drug:edit']">
    <div class="drug-actions">
      <el-button @click="handleAdd">新增</el-button>
      <el-button @click="handleEdit">编辑</el-button>
    </div>
  </Perms>
</template>

<script setup lang="ts">
import { Perms } from "@/components/RePerms";
</script>
```

## 🎨 图标组件

### 1. IconifyIconOffline - 本地图标
```vue
<template>
  <!-- 使用本地图标 -->
  <IconifyIconOffline icon="ep:medicine-box" />
  <IconifyIconOffline icon="ep:user" />
  <IconifyIconOffline icon="ep:setting" />
</template>

<script setup lang="ts">
import { IconifyIconOffline } from "@/components/ReIcon";
</script>
```

### 2. IconifyIconOnline - 在线图标
```vue
<template>
  <!-- 使用在线图标 -->
  <IconifyIconOnline icon="mdi:home" />
  <IconifyIconOnline icon="mdi:account" />
</template>

<script setup lang="ts">
import { IconifyIconOnline } from "@/components/ReIcon";
</script>
```

### 3. FontIcon - 字体图标
```vue
<template>
  <!-- 使用字体图标 -->
  <FontIcon icon="iconfont icon-user" />
  <FontIcon icon="iconfont icon-setting" />
</template>

<script setup lang="ts">
import { FontIcon } from "@/components/ReIcon";
</script>
```

## 📊 表格组件

### 1. PureTableBar - 表格工具栏
```vue
<template>
  <div class="drug-list">
    <!-- 表格工具栏 -->
    <PureTableBar
      title="药品列表"
      :columns="columns"
      :table-ref="tableRef"
      @refresh="handleRefresh"
      @fullscreen="handleFullscreen"
    />
    
    <!-- 表格 -->
    <el-table ref="tableRef" :data="drugList" :columns="columns">
      <!-- 表格列配置 -->
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { PureTableBar } from "@pureadmin/table";

const tableRef = ref();
const columns = ref([
  { label: "药品名称", prop: "drugName", hide: false },
  { label: "药品编码", prop: "drugCode", hide: false },
  { label: "药品类型", prop: "drugType", hide: false },
  { label: "价格", prop: "price", hide: false },
  { label: "状态", prop: "status", hide: false },
  { label: "操作", prop: "action", hide: false }
]);

const handleRefresh = () => {
  // 刷新数据
  fetchDrugList();
};

const handleFullscreen = (isFullscreen: boolean) => {
  // 处理全屏
  console.log("全屏状态:", isFullscreen);
};
</script>
```

## 💬 对话框组件

### 1. ReDialog - 动态对话框
```vue
<template>
  <div>
    <el-button @click="openAddDialog">新增药品</el-button>
    <el-button @click="openEditDialog">编辑药品</el-button>
  </div>
</template>

<script setup lang="ts">
import { openDialog } from "@/components/ReDialog";
import DrugForm from "./components/DrugForm.vue";

const openAddDialog = () => {
  openDialog({
    title: "新增药品",
    width: "800px",
    draggable: true,
    fullscreenIcon: true,
    contentRenderer: () => DrugForm,
    props: {
      modelValue: {
        drugCode: "",
        drugName: "",
        drugType: "",
        price: 0
      }
    },
    beforeSure: (done, { options }) => {
      // 提交前的验证
      const formData = options.props.modelValue;
      if (!formData.drugName) {
        ElMessage.error("请输入药品名称");
        return;
      }
      done();
    }
  });
};

const openEditDialog = (drug: Drug) => {
  openDialog({
    title: "编辑药品",
    width: "800px",
    draggable: true,
    fullscreenIcon: true,
    contentRenderer: () => DrugForm,
    props: {
      modelValue: drug
    },
    beforeSure: (done, { options }) => {
      // 提交前的验证
      const formData = options.props.modelValue;
      if (!formData.drugName) {
        ElMessage.error("请输入药品名称");
        return;
      }
      // 调用更新API
      updateDrug(formData).then(() => {
        ElMessage.success("更新成功");
        done();
      });
    }
  });
};
</script>
```

## 📝 文本组件

### 1. ReText - 文本省略组件
```vue
<template>
  <!-- 单行省略 -->
  <ReText truncated>
    这是一段很长的文本内容，超出部分会被省略显示
  </ReText>
  
  <!-- 多行省略 -->
  <ReText :line-clamp="2">
    这是一段很长的文本内容，可以显示多行，超出部分会被省略显示
  </ReText>
  
  <!-- 自定义提示 -->
  <ReText 
    truncated 
    :tippy-props="{ 
      content: '完整的文本内容',
      placement: 'top'
    }"
  >
    这是一段很长的文本内容
  </ReText>
</template>

<script setup lang="ts">
import { ReText } from "@/components/ReText";
</script>
```

## 🎯 优化后的页面示例

### 1. 优化后的药品列表页面
```vue
<template>
  <div class="drug-list">
    <!-- 权限控制的工具栏 -->
    <div class="toolbar">
      <div class="left">
        <Auth :value="'drug:add'">
          <el-button type="primary" @click="openAddDialog">
            <IconifyIconOffline icon="ep:plus" />
            新增药品
          </el-button>
        </Auth>
        
        <Auth :value="'drug:import'">
          <el-button type="success" @click="openImportDialog">
            <IconifyIconOffline icon="ep:upload" />
            导入药品
          </el-button>
        </Auth>
        
        <Auth :value="'drug:delete'">
          <el-button 
            type="danger" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <IconifyIconOffline icon="ep:delete" />
            批量删除
          </el-button>
        </Auth>
      </div>
      
      <div class="right">
        <el-button @click="handleRefresh">
          <IconifyIconOffline icon="ep:refresh" />
          刷新
        </el-button>
        
        <Auth :value="'drug:stats'">
          <el-button @click="handleStats">
            <IconifyIconOffline icon="ep:data-analysis" />
            统计
          </el-button>
        </Auth>
      </div>
    </div>
    
    <!-- 搜索组件 -->
    <DrugSearch
      v-model="searchParams"
      @search="handleSearch"
      @reset="handleReset"
    />
    
    <!-- 表格工具栏 -->
    <PureTableBar
      title="药品列表"
      :columns="columns"
      :table-ref="tableRef"
      @refresh="handleRefresh"
      @fullscreen="handleFullscreen"
    />
    
    <!-- 表格 -->
    <el-table
      ref="tableRef"
      v-loading="loading"
      :data="drugList"
      stripe
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      
      <el-table-column prop="drugName" label="药品名称" width="150">
        <template #default="{ row }">
          <ReText truncated :tippy-props="{ content: row.drugName }">
            {{ row.drugName }}
          </ReText>
        </template>
      </el-table-column>
      
      <el-table-column prop="drugCode" label="药品编码" width="120" />
      
      <el-table-column prop="drugType" label="药品类型" width="100">
        <template #default="{ row }">
          <el-tag :type="getDrugTypeTag(row.drugType)">
            {{ row.drugType }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">
          ¥{{ row.price?.toFixed(2) }}
        </template>
      </el-table-column>
      
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <Auth :value="'drug:view'">
            <el-button type="primary" link @click="handleView(row)">
              <IconifyIconOffline icon="ep:view" />
              查看
            </el-button>
          </Auth>
          
          <Auth :value="'drug:edit'">
            <el-button type="primary" link @click="handleEdit(row)">
              <IconifyIconOffline icon="ep:edit" />
              编辑
            </el-button>
          </Auth>
          
          <Auth :value="'drug:delete'">
            <el-button type="danger" link @click="handleDelete(row)">
              <IconifyIconOffline icon="ep:delete" />
              删除
            </el-button>
          </Auth>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <el-pagination
      :current-page="pagination.current"
      :page-size="pagination.size"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Auth } from "@/components/ReAuth";
import { IconifyIconOffline } from "@/components/ReIcon";
import { PureTableBar } from "@pureadmin/table";
import { ReText } from "@/components/ReText";
import { openDialog } from "@/components/ReDialog";
import DrugSearch from "./components/DrugSearch.vue";
import DrugForm from "./components/DrugForm.vue";
import { getDrugList, deleteDrug } from "@/api/drug";
import type { Drug, DrugQueryParams } from "@/api/drug";

const router = useRouter();

// 数据
const loading = ref(false);
const drugList = ref<Drug[]>([]);
const selectedRows = ref<Drug[]>([]);
const tableRef = ref();

// 搜索参数
const searchParams = reactive<DrugQueryParams>({
  pageNum: 1,
  pageSize: 10,
  drugName: "",
  drugType: "",
  status: undefined
});

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 表格列配置
const columns = ref([
  { label: "药品名称", prop: "drugName", hide: false },
  { label: "药品编码", prop: "drugCode", hide: false },
  { label: "药品类型", prop: "drugType", hide: false },
  { label: "规格", prop: "specification", hide: false },
  { label: "生产厂家", prop: "manufacturer", hide: false },
  { label: "单位", prop: "unit", hide: false },
  { label: "价格", prop: "price", hide: false },
  { label: "状态", prop: "status", hide: false },
  { label: "创建时间", prop: "createTime", hide: false },
  { label: "操作", prop: "action", hide: false }
]);

// 获取药品列表
const fetchDrugList = async (params: DrugQueryParams) => {
  loading.value = true;
  try {
    const result = await getDrugList(params);
    drugList.value = result.records || [];
    pagination.total = result.total || 0;
    pagination.current = result.current || 1;
    pagination.size = result.size || 10;
  } catch (error) {
    ElMessage.error("获取药品列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = (params: DrugQueryParams) => {
  Object.assign(searchParams, params);
  searchParams.pageNum = 1;
  fetchDrugList(searchParams);
};

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    if (key !== 'pageNum' && key !== 'pageSize') {
      delete searchParams[key as keyof DrugQueryParams];
    }
  });
  searchParams.pageNum = 1;
  fetchDrugList(searchParams);
};

// 刷新
const handleRefresh = () => {
  fetchDrugList(searchParams);
};

// 全屏
const handleFullscreen = (isFullscreen: boolean) => {
  console.log("全屏状态:", isFullscreen);
};

// 统计
const handleStats = () => {
  router.push("/drug/stats");
};

// 选择变化
const handleSelectionChange = (selection: Drug[]) => {
  selectedRows.value = selection;
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size;
  searchParams.pageNum = 1;
  fetchDrugList(searchParams);
};

// 当前页变化
const handleCurrentChange = (current: number) => {
  searchParams.pageNum = current;
  fetchDrugList(searchParams);
};

// 新增药品对话框
const openAddDialog = () => {
  openDialog({
    title: "新增药品",
    width: "800px",
    draggable: true,
    fullscreenIcon: true,
    contentRenderer: () => DrugForm,
    props: {
      modelValue: {
        drugCode: "",
        drugName: "",
        drugType: "",
        specification: "",
        unit: "",
        price: 0,
        manufacturer: "",
        status: 1
      }
    },
    beforeSure: (done, { options }) => {
      const formData = options.props.modelValue;
      if (!formData.drugName) {
        ElMessage.error("请输入药品名称");
        return;
      }
      // 调用新增API
      addDrug(formData).then(() => {
        ElMessage.success("新增成功");
        fetchDrugList(searchParams);
        done();
      });
    }
  });
};

// 编辑药品对话框
const handleEdit = (row: Drug) => {
  openDialog({
    title: "编辑药品",
    width: "800px",
    draggable: true,
    fullscreenIcon: true,
    contentRenderer: () => DrugForm,
    props: {
      modelValue: { ...row }
    },
    beforeSure: (done, { options }) => {
      const formData = options.props.modelValue;
      if (!formData.drugName) {
        ElMessage.error("请输入药品名称");
        return;
      }
      // 调用更新API
      updateDrug(formData).then(() => {
        ElMessage.success("更新成功");
        fetchDrugList(searchParams);
        done();
      });
    }
  });
};

// 查看药品
const handleView = (row: Drug) => {
  router.push(`/drug/detail/${row.id}`);
};

// 删除药品
const handleDelete = async (row: Drug) => {
  try {
    await ElMessageBox.confirm("确定要删除该药品吗？", "提示", {
      type: "warning"
    });
    await deleteDrug(row.id!);
    ElMessage.success("删除成功");
    fetchDrugList(searchParams);
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除失败");
    }
  }
};

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个药品吗？`,
      "提示",
      { type: "warning" }
    );
    const ids = selectedRows.value.map(row => row.id!);
    // 调用批量删除API
    ElMessage.success("批量删除成功");
    fetchDrugList(searchParams);
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("批量删除失败");
    }
  }
};

// 获取药品类型标签
const getDrugTypeTag = (type: string) => {
  switch (type) {
    case '甲类':
      return 'success';
    case '乙类':
      return 'warning';
    case '丙类':
      return 'info';
    default:
      return 'info';
  }
};

// 初始化
onMounted(() => {
  fetchDrugList(searchParams);
});
</script>

<style scoped>
.drug-list {
  padding: 16px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
}

.toolbar .left {
  display: flex;
  gap: 12px;
}

.toolbar .right {
  display: flex;
  gap: 12px;
}
</style>
```

## 🎯 使用建议

### 1. **权限控制**
- 使用 `Auth` 组件控制路由级别的权限
- 使用 `Perms` 组件控制功能级别的权限
- 在按钮、菜单、页面等各个层级都添加权限控制

### 2. **图标使用**
- 优先使用 `IconifyIconOffline` 本地图标，性能更好
- 对于特殊图标，可以使用 `IconifyIconOnline` 在线图标
- 使用 `FontIcon` 显示自定义字体图标

### 3. **表格优化**
- 使用 `PureTableBar` 提供表格工具栏功能
- 支持列显示控制、拖拽排序、全屏等功能
- 结合 `ReText` 组件处理长文本省略

### 4. **对话框优化**
- 使用 `ReDialog` 替代传统的 `el-dialog`
- 支持动态内容渲染、全屏、拖拽等功能
- 提供统一的确认、取消处理机制

### 5. **文本处理**
- 使用 `ReText` 组件处理文本省略
- 支持单行和多行省略
- 提供鼠标悬停显示完整内容的功能

## 📈 优化效果

### 功能增强
- ✅ 完整的权限控制体系
- ✅ 丰富的图标库支持
- ✅ 强大的表格工具栏
- ✅ 灵活的对话框系统
- ✅ 智能的文本处理

### 用户体验
- ✅ 统一的界面风格
- ✅ 流畅的交互体验
- ✅ 直观的操作反馈
- ✅ 便捷的功能操作

### 开发效率
- ✅ 组件复用性高
- ✅ 代码结构清晰
- ✅ 维护成本低
- ✅ 扩展性强

通过充分利用 Vue-Pure-Admin 提供的组件，可以大大提升开发效率和用户体验，让页面开发更加高效和规范！ 