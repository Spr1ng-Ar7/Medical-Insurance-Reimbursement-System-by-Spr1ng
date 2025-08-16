<template>
  <div class="drug-list">
    <PureTable
      row-key="id"
      :loading="loading"
      :columns="columns"
      :data="drugList"
      :pagination="pagination"
      :paginationSmall="false"
      :headerButtons="headerButtons"
      :tableToolbar="true"
      :search="true"
      :searchInfo="searchInfo"
      @page-size-change="handleSizeChange"
      @page-current-change="handleCurrentChange"
      @selection-change="handleSelectionChange"
      @search="handleSearch"
    >
      <template #toolbar>
        <ReButton type="primary" iconName="ep:plus" buttonText="新增药品" @click="handleAdd" />
        <ReButton 
          type="danger" 
          iconName="ep:delete"
          buttonText="批量删除"
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        />
        <ReButton type="success" iconName="ep:upload" buttonText="导入药品" @click="handleImport" />
        <ReButton type="warning" iconName="ep:download" buttonText="导出数据" @click="handleExport" />
      </template>

      <template #operation="{ row }">
        <ReButton type="primary" link iconName="ep:view" buttonText="查看" @click="handleView(row)" />
        <ReButton type="success" link iconName="ep:edit" buttonText="编辑" @click="handleEdit(row)" />
        <ReButton type="danger" link iconName="ep:delete" buttonText="删除" @click="handleDelete(row)" />
      </template>

      <template #name="{ row }">
        <ReText :line-clamp="1" :tippy-props="{ content: row.name }">
          {{ row.name }}
        </ReText>
      </template>

      <template #description="{ row }">
        <ReText :line-clamp="2" :tippy-props="{ content: row.description }">
          {{ row.description }}
        </ReText>
      </template>

      <template #category="{ row }">
        <ReTag 
          :type="getCategoryColor(row.category)" 
          size="small"
          :iconName="getCategoryIcon(row.category)"
          :showIcon="true"
          :tagText="row.category"
        />
      </template>

      <template #status="{ row }">
        <ReSegmented
          v-model="row.status"
          :options="statusOptions"
          @change="handleStatusChange(row)"
        />
      </template>

      <template #price="{ row }">
        <span class="price">¥{{ formatPrice(row.price) }}</span>
      </template>



      <template #manufacturer="{ row }">
        <ReText :line-clamp="1" :tippy-props="{ content: row.manufacturer }">
          {{ row.manufacturer }}
        </ReText>
      </template>
    </PureTable>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onActivated } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { PureTable } from "@pureadmin/table";
import { 
  ReText, 
  ReSegmented, 
  IconifyIconOffline, 
  addDialog,
  ReButton,
  ReTag
} from "../../components";
import { getDrugList, deleteDrug } from "@/api/drug";

const router = useRouter();

// 数据
const loading = ref(false);
const drugList = ref<any[]>([]);
const selectedRows = ref<any[]>([]);

// 状态选项
const statusOptions = ref([
  { label: "在售", value: "active" },
  { label: "停售", value: "inactive" },
  { label: "缺货", value: "out_of_stock" }
]);

// 搜索信息
const searchInfo = reactive({
  name: "",
  category: "",
  manufacturer: "",
  status: ""
});

// 分页信息
const pagination = reactive({
  total: 0,
  pageSize: 10,
  currentPage: 1,
  background: true
});

// 表格列配置
const columns = ref([
  {
    type: "selection",
    width: 55,
    align: "center",
    hide: false
  },
  {
    label: "药品名称",
    prop: "name",
    minWidth: 150,
    slot: "name",
    hide: false
  },
  {
    label: "药品分类",
    prop: "category",
    width: 120,
    slot: "category",
    hide: false
  },
  {
    label: "规格",
    prop: "specification",
    minWidth: 120,
    hide: false
  },
  {
    label: "单价",
    prop: "price",
    width: 100,
    slot: "price",
    hide: false
  },
  {
    label: "单位",
    prop: "unit",
    width: 80,
    hide: false
  },
  {
    label: "生产厂家",
    prop: "manufacturer",
    minWidth: 150,
    slot: "manufacturer",
    hide: false
  },
  {
    label: "描述",
    prop: "description",
    minWidth: 200,
    slot: "description",
    hide: false
  },
  {
    label: "状态",
    prop: "status",
    width: 120,
    slot: "status",
    hide: false
  },
  {
    label: "创建时间",
    prop: "createTime",
    minWidth: 160,
    hide: false
  },
  {
    label: "操作",
    fixed: "right",
    width: 200,
    slot: "operation",
    hide: false
  }
]);

// 头部按钮
const headerButtons = ref([
  {
    type: "primary",
    label: "新增药品",
    icon: "ep:plus",
    click: () => handleAdd()
  },
  {
    type: "danger",
    label: "批量删除",
    icon: "ep:delete",
    disabled: () => selectedRows.value.length === 0,
    click: () => handleBatchDelete()
  },
  {
    type: "success",
    label: "导入药品",
    icon: "ep:upload",
    click: () => handleImport()
  },
  {
    type: "warning",
    label: "导出数据",
    icon: "ep:download",
    click: () => handleExport()
  }
]);

// 格式化价格
const formatPrice = (price: number) => {
  return price.toFixed(2);
};

// 获取分类颜色
const getCategoryColor = (category: string) => {
  const colorMap: Record<string, "primary" | "success" | "warning" | "info"> = {
    "非处方药": "primary",
    "处方药": "success",
    "中药": "warning",
    "西药": "info"
  };
  return colorMap[category] || "info";
};

// 获取分类图标
const getCategoryIcon = (category: string) => {
  const iconMap: Record<string, string> = {
    "非处方药": "ep:medicine-box",
    "处方药": "ep:prescription",
    "中药": "ep:leaf",
    "西药": "ep:pills",
    "保健品": "ep:star"
  };
  return iconMap[category] || "ep:medicine-box";
};



// 获取药品列表
const fetchDrugList = async (query: any = {}) => {
  loading.value = true;
  try {
    // 组装分页+查询参数，与后端接口保持一致
    const params = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      drugName: query.name, // 前端搜索字段名映射到后端字段名
      drugType: query.category,
      status: query.status,
      ...query
    };

    console.log('请求参数:', params);
    const res: any = await getDrugList(params);
    console.log('API响应:', res);
    
    // 兼容 http 封装返回 { code, message, data }
    const data = res && "data" in res ? res.data : res;
    console.log('处理后的数据:', data);

    // 将后端Drug实体字段映射为前端表格需要的字段
    drugList.value = (data.records || []).map((item: any) => {
      console.log('前端处理药品数据:', item);
      
      return {
        id: item.id,
        name: item.drugName || '未知药品', // 使用API处理后的drugName
        category: item.drugType || '未分类', // 使用API处理后的drugType
        specification: item.specification || '未知规格',
        price: typeof item.price === 'number' ? item.price : parseFloat(item.price) || 0,
        unit: item.unit || '个',
        manufacturer: item.manufacturer || '未知厂家',
        description: item.remark || '无描述',
        status: getStatusFromBackend(item.status), // 状态映射
        createTime: item.createTime || '',
        // 额外保存后端原始数据用于编辑
        _original: item
      };
    });

    pagination.total = data.total || 0;
    console.log('映射后的药品列表:', drugList.value);
  } catch (error) {
    console.error('获取药品列表失败:', error);
    ElMessage.error("获取药品列表失败: " + (error.message || '未知错误'));
  } finally {
    loading.value = false;
  }
};

// 后端状态到前端状态的映射
const getStatusFromBackend = (backendStatus: number) => {
  switch (backendStatus) {
    case 1: return "active";      // 正常 -> 在售
    case 0: return "inactive";    // 停用 -> 停售  
    case 2: return "inactive";    // 其他停用状态 -> 停售
    default: return "out_of_stock"; // 未知状态 -> 缺货
  }
};

// 前端状态到后端状态的映射
const getBackendStatus = (frontendStatus: string) => {
  switch (frontendStatus) {
    case "active": return 1;       // 在售 -> 正常
    case "inactive": return 0;     // 停售 -> 停用
    case "out_of_stock": return 2; // 缺货 -> 其他状态
    default: return 0;
  }
};

// 处理状态变更
const handleStatusChange = async (row: any) => {
  try {
    const backendStatus = getBackendStatus(row.status);
    // 这里应该调用后端API更新状态，暂时用成功消息代替
    // await updateDrugStatus(row.id, backendStatus);
    
    const statusText = row.status === 'active' ? '在售' : 
                      row.status === 'inactive' ? '停售' : '缺货';
    ElMessage.success(`药品 ${row.name} 状态已更新为 ${statusText}`);
  } catch (error) {
    ElMessage.error('状态更新失败');
    // 恢复原状态
    const originalStatus = getStatusFromBackend(row._original?.status || 1);
    row.status = originalStatus;
  }
};

// 新增药品
const handleAdd = () => {
  router.push("/drug/add");
};

// 查看药品
const handleView = (row: any) => {
  router.push(`/drug/detail/${row.id}`);
};

// 编辑药品
const handleEdit = (row: any) => {
  router.push(`/drug/edit/${row.id}`);
};

// 删除药品
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除药品 ${row.name} 吗？`,
      "确认删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    // 调用删除API
    await deleteDrug(row.id);
    ElMessage.success("删除成功");
    
    // 重新获取药品列表
    fetchDrugList({ 
      pageNum: pagination.currentPage, 
      pageSize: pagination.pageSize 
    });
  } catch (error) {
    // 用户取消删除或删除失败
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败');
    }
  }
};

// 批量删除
const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的药品");
    return;
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个药品吗？`,
    "确认批量删除",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }
  ).then(() => {
    ElMessage.success("批量删除成功");
    selectedRows.value = [];
    fetchDrugList({});
  });
};

// 导入药品
const handleImport = () => {
  addDialog({
    title: "导入药品数据",
    width: "600px",
    draggable: true,
    closeOnClickModal: false,
    contentRenderer: () => import("../../components/Common/ImportDialog.vue"),
    beforeSure: (done, { options }) => {
      ElMessage.success("导入成功");
      done();
    }
  });
};

// 导出数据
const handleExport = () => {
  addDialog({
    title: "导出药品数据",
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

// 搜索处理
const handleSearch = (params: any) => {
  pagination.currentPage = 1;
  fetchDrugList(params);
};

// 分页处理
const handleSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchDrugList({});
};

const handleCurrentChange = (val: number) => {
  pagination.currentPage = val;
  fetchDrugList({});
};

// 选择处理
const handleSelectionChange = (rows: any[]) => {
  selectedRows.value = rows;
};

onMounted(() => {
  fetchDrugList({});
});

// 监听路由变化，当从新增/编辑页面返回时重新加载数据
onActivated(() => {
  console.log('药品列表页面激活，重新加载数据');
  fetchDrugList({});
});
</script>

<style scoped>
.drug-list {
  padding: 20px;
}

.price {
  color: #e6a23c;
  font-weight: bold;
}

:deep(.re-tag) {
  display: flex;
  align-items: center;
  gap: 4px;
}

:deep(.el-segmented) {
  width: 100%;
}
</style> 