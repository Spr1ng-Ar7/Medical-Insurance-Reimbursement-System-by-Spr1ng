<template>
  <div class="patient-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>患者管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">
            新增患者
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-form">
        <el-form :model="searchForm" inline>
          <el-form-item label="患者姓名">
            <el-input 
              v-model="searchForm.name" 
              placeholder="请输入患者姓名" 
              clearable 
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="身份证号">
            <el-input 
              v-model="searchForm.idCard" 
              placeholder="请输入身份证号" 
              clearable 
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input 
              v-model="searchForm.phone" 
              placeholder="请输入手机号码" 
              clearable 
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="保险类型">
            <el-select 
              v-model="searchForm.insuranceType" 
              placeholder="请选择保险类型" 
              clearable 
              style="width: 150px"
            >
              <el-option label="城镇职工" value="城镇职工" />
              <el-option label="城乡居民" value="城乡居民" />
              <el-option label="新农合" value="新农合" />
              <el-option label="医疗救助" value="医疗救助" />
              <el-option label="商业保险" value="商业保险" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择状态" 
              clearable 
              style="width: 120px"
            >
              <el-option label="正常" :value="1" />
              <el-option label="注销" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="关键词">
            <el-input 
              v-model="searchForm.keyword" 
              placeholder="模糊搜索" 
              clearable 
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" icon="Plus" @click="handleAdd">新增患者</el-button>
          <el-button 
            type="danger" 
            icon="Delete"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出数据</el-button>
        </div>
        <div class="toolbar-right">
          <span class="selected-info" v-if="selectedRows.length > 0">
            已选择 {{ selectedRows.length }} 项
          </span>
        </div>
      </div>

      <!-- 表格 -->
      <el-table 
        v-loading="loading"
        :data="patientList" 
        border 
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="患者姓名" min-width="120" />
        <el-table-column prop="idCard" label="身份证号" min-width="180">
          <template #default="{ row }">
            {{ formatIdCard(row.idCard) }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号码" width="130">
          <template #default="{ row }">
            {{ formatPhone(row.phone) }}
          </template>
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag 
              :type="row.gender === '男' ? 'primary' : 'success'" 
              size="small"
            >
              <el-icon style="margin-right: 4px;">
                <Male v-if="row.gender === '男'" />
                <Female v-else />
              </el-icon>
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="address" label="地址" min-width="200">
          <template #default="{ row }">
            <el-tooltip :content="row.address" placement="top" :disabled="!row.address">
              <span class="text-ellipsis">{{ row.address }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="insuranceType" label="保险类型" width="120">
          <template #default="{ row }">
            <el-tag 
              :type="getInsuranceTypeColor(row.insuranceType)" 
              size="small"
            >
              {{ row.insuranceType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="insuranceNumber" label="保险号" width="150" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '注销' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="success" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button 
              :type="row.status === 1 ? 'warning' : 'success'" 
              link 
              size="small" 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '注销' : '激活' }}
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pagination.currentPage"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Male, Female } from "@element-plus/icons-vue";
import { 
  getPatientList, 
  deletePatient, 
  batchDeletePatient, 
  updateStatus,
  type Patient 
} from "@/api/patient";

const router = useRouter();

const loading = ref(false);
const selectedRows = ref<Patient[]>([]);

// 搜索表单
const searchForm = reactive({
  name: '',
  idCard: '',
  phone: '',
  insuranceType: '',
  status: undefined as number | undefined,
  keyword: ''
});

// 表格数据
const patientList = ref<Patient[]>([]);

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  background: true
});

// 格式化身份证号
const formatIdCard = (idCard: string) => {
  if (!idCard) return "";
  if (idCard.length === 18) {
    return idCard.replace(/(\d{6})(\d{8})(\d{4})/, "$1********$3");
  }
  return idCard;
};

// 格式化手机号
const formatPhone = (phone: string) => {
  if (!phone) return "";
  if (phone.length === 11) {
    return phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3");
  }
  return phone;
};

// 获取保险类型颜色
const getInsuranceTypeColor = (type: string) => {
  const colorMap: Record<string, "primary" | "success" | "warning" | "info"> = {
    "城镇职工": "primary",
    "城乡居民": "success", 
    "新农合": "warning",
    "医疗救助": "info",
    "商业保险": "info"
  };
  return colorMap[type] || "info";
};

// 格式化日期时间
const formatDateTime = (dateTime?: string) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 获取患者列表
const fetchPatientList = async () => {
  loading.value = true;
  try {
    const requestParams = {
      page: pagination.currentPage,
      size: pagination.pageSize,
      name: searchForm.name || undefined,
      idCard: searchForm.idCard || undefined,
      phone: searchForm.phone || undefined,
      insuranceType: searchForm.insuranceType || undefined,
      status: searchForm.status,
      keyword: searchForm.keyword || undefined
    };

    const result = await getPatientList(requestParams);
    patientList.value = result.records || [];
    pagination.total = result.total || 0;
  } catch (error: any) {
    console.error("获取患者列表失败:", error);
    ElMessage.error(error?.message || "获取患者列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchPatientList();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    name: '',
    idCard: '',
    phone: '',
    insuranceType: '',
    status: undefined,
    keyword: ''
  });
  pagination.currentPage = 1;
  fetchPatientList();
};

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchPatientList();
};

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  fetchPatientList();
};

// 选择处理
const handleSelectionChange = (rows: Patient[]) => {
  selectedRows.value = rows;
};

// 新增患者
const handleAdd = () => {
  router.push("/patient/add");
};

// 查看患者
const handleView = (row: Patient) => {
  router.push(`/patient/detail/${row.id}`);
};

// 编辑患者
const handleEdit = (row: Patient) => {
  router.push(`/patient/edit/${row.id}`);
};

// 切换状态
const handleToggleStatus = async (row: Patient) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1;
    const statusText = newStatus === 1 ? '激活' : '注销';
    
    await ElMessageBox.confirm(
      `确定要${statusText}患者 "${row.name}" 吗？`,
      "确认操作",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await updateStatus(row.id!, newStatus);
    ElMessage.success(`患者 ${row.name} 已${statusText}`);
    fetchPatientList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "状态更新失败");
    }
  }
};

// 删除患者
const handleDelete = async (row: Patient) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除患者 "${row.name}" 吗？`,
      "确认删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await deletePatient(row.id!);
    ElMessage.success("删除成功");
    fetchPatientList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "删除失败");
    }
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的患者");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个患者吗？`,
      "确认批量删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    const ids = selectedRows.value.map(row => row.id!);
    await batchDeletePatient(ids);
    ElMessage.success("批量删除成功");
    selectedRows.value = [];
    fetchPatientList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量删除失败");
    }
  }
};

// 导出数据
const handleExport = () => {
  ElMessage.info("导出功能开发中");
  // TODO: 实现导出功能
};

onMounted(() => {
  fetchPatientList();
});
</script>

<style scoped>
.patient-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
}

.toolbar-left {
  display: flex;
  gap: 12px;
}

.toolbar-right .selected-info {
  color: #606266;
  font-size: 14px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.text-ellipsis {
  display: inline-block;
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.el-tag) {
  display: flex;
  align-items: center;
  gap: 4px;
}
</style> 