<template>
  <div class="facility-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>医疗器械管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">
            新增设备
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-form">
        <el-form :model="searchForm" inline>
          <el-form-item label="项目编码">
            <el-input 
              v-model="searchForm.equipmentCode" 
              placeholder="请输入项目编码" 
              clearable 
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="项目名称">
            <el-input 
              v-model="searchForm.equipmentName" 
              placeholder="请输入项目名称" 
              clearable 
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="设备类别">
            <el-select 
              v-model="searchForm.category" 
              placeholder="请选择设备类别" 
              clearable 
              style="width: 150px"
            >
              <el-option label="诊断设备" value="诊断设备" />
              <el-option label="治疗设备" value="治疗设备" />
              <el-option label="监护设备" value="监护设备" />
              <el-option label="康复设备" value="康复设备" />
              <el-option label="急救设备" value="急救设备" />
              <el-option label="手术设备" value="手术设备" />
              <el-option label="检验设备" value="检验设备" />
              <el-option label="其他设备" value="其他设备" />
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
              <el-option label="停用" :value="0" />
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
          <el-button type="primary" icon="Plus" @click="handleAdd">新增设备</el-button>
          <el-button 
            type="danger" 
            icon="Delete"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button type="success" icon="Upload" @click="handleImport">导入设备</el-button>
          <el-button type="warning" icon="Download" @click="handleExport">导出数据</el-button>
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
        :data="facilityList" 
        border 
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="equipmentCode" label="项目编码" min-width="120" />
        <el-table-column prop="equipmentName" label="项目名称" min-width="150" />
        <el-table-column prop="category" label="设备类别" width="120" />
        <el-table-column prop="unitType" label="计价单位" width="100" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            ¥{{ formatAmount(row.price) }}
          </template>
        </el-table-column>
        <el-table-column prop="financeCategory" label="财务分类" width="100" />
        <el-table-column prop="nationalCode" label="国家编码" width="120" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '停用' }}
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
              {{ row.status === 1 ? '停用' : '启用' }}
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

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="800px"
      :close-on-click-modal="false"
      @closed="resetForm"
    >
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目编码" prop="equipmentCode">
              <el-input 
                v-model="form.equipmentCode" 
                placeholder="请输入项目编码"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="equipmentName">
              <el-input v-model="form.equipmentName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备类别">
              <el-select v-model="form.category" placeholder="请选择设备类别" style="width: 100%">
                <el-option label="诊断设备" value="诊断设备" />
                <el-option label="治疗设备" value="治疗设备" />
                <el-option label="监护设备" value="监护设备" />
                <el-option label="康复设备" value="康复设备" />
                <el-option label="急救设备" value="急救设备" />
                <el-option label="手术设备" value="手术设备" />
                <el-option label="检验设备" value="检验设备" />
                <el-option label="其他设备" value="其他设备" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计价单位">
              <el-select v-model="form.unitType" placeholder="请选择计价单位" style="width: 100%">
                <el-option label="次" value="次" />
                <el-option label="个" value="个" />
                <el-option label="台" value="台" />
                <el-option label="小时" value="小时" />
                <el-option label="天" value="天" />
                <el-option label="项" value="项" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="价格">
              <el-input-number 
                v-model="form.price" 
                :precision="2" 
                :step="0.01" 
                :min="0"
                placeholder="请输入价格"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="财务分类">
              <el-select v-model="form.financeCategory" placeholder="请选择财务分类" style="width: 100%">
                <el-option label="甲类" value="甲类" />
                <el-option label="乙类" value="乙类" />
                <el-option label="丙类" value="丙类" />
                <el-option label="自费" value="自费" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="国家编码">
          <el-input v-model="form.nationalCode" placeholder="请输入国家编码" />
        </el-form-item>

        <el-form-item label="项目内容">
          <el-input 
            v-model="form.equipmentContent" 
            type="textarea" 
            :rows="3"
            placeholder="请输入项目内容"
          />
        </el-form-item>

        <el-form-item label="除外内容">
          <el-input 
            v-model="form.excludeContent" 
            type="textarea" 
            :rows="3"
            placeholder="请输入除外内容"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="正常" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="说明">
          <el-input 
            v-model="form.remark" 
            type="textarea" 
            :rows="3"
            placeholder="请输入说明"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSave" :loading="submitLoading">
            {{ isEdit ? '更新' : '保存' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import type { FormInstance, FormRules } from "element-plus";
import { 
  getMedicalEquipmentList, 
  createMedicalEquipment, 
  updateMedicalEquipment, 
  deleteMedicalEquipment,
  batchDeleteMedicalEquipments,
  changeMedicalEquipmentStatus,
  type MedicalEquipment 
} from "@/api/medical-equipment";

const loading = ref(false);
const submitLoading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref<FormInstance>();

// 搜索表单
const searchForm = reactive({
  equipmentCode: '',
  equipmentName: '',
  category: '',
  status: undefined as number | undefined,
  keyword: ''
});

// 表格数据
const facilityList = ref<MedicalEquipment[]>([]);
const selectedRows = ref<MedicalEquipment[]>([]);

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  background: true
});

// 表单数据
const form = reactive<Partial<MedicalEquipment>>({
  id: undefined,
  financeCategory: '',
  equipmentCode: '',
  nationalCode: '',
  equipmentName: '',
  equipmentContent: '',
  excludeContent: '',
  unitType: '',
  category: '',
  price: 0,
  status: 1,
  remark: ''
});

// 表单验证规则
const formRules: FormRules = {
  equipmentCode: [
    { required: true, message: '请输入项目编码', trigger: 'blur' }
  ],
  equipmentName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' }
  ]
};

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑设备' : '新增设备');

// 格式化金额
const formatAmount = (amount?: number | null) => {
  if (amount === undefined || amount === null || isNaN(Number(amount))) {
    return '0.00';
  }
  return Number(amount).toFixed(2);
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

// 获取列表数据
const fetchList = async () => {
  loading.value = true;
  try {
    const result = await getMedicalEquipmentList({
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      equipmentCode: searchForm.equipmentCode,
      equipmentName: searchForm.equipmentName,
      category: searchForm.category,
      status: searchForm.status,
      keyword: searchForm.keyword
    });
    
    facilityList.value = result.records || [];
    pagination.total = result.total || 0;
  } catch (error: any) {
    console.error('获取设备列表失败:', error);
    ElMessage.error(error?.message || "获取设备列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchList();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    equipmentCode: '',
    equipmentName: '',
    category: '',
    status: undefined,
    keyword: ''
  });
  pagination.currentPage = 1;
  fetchList();
};

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchList();
};

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  fetchList();
};

// 选择处理
const handleSelectionChange = (rows: MedicalEquipment[]) => {
  selectedRows.value = rows;
};

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: undefined,
    financeCategory: '',
    equipmentCode: '',
    nationalCode: '',
    equipmentName: '',
    equipmentContent: '',
    excludeContent: '',
    unitType: '',
    category: '',
    price: 0,
    status: 1,
    remark: ''
  });
  formRef.value?.resetFields();
};

// 新增
const handleAdd = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

// 查看
const handleView = (row: MedicalEquipment) => {
  ElMessage.info("查看功能开发中");
  // TODO: 实现查看详情功能
};

// 编辑
const handleEdit = (row: MedicalEquipment) => {
  isEdit.value = true;
  Object.assign(form, {
    id: row.id,
    financeCategory: row.financeCategory || '',
    equipmentCode: row.equipmentCode || '',
    nationalCode: row.nationalCode || '',
    equipmentName: row.equipmentName || '',
    equipmentContent: row.equipmentContent || '',
    excludeContent: row.excludeContent || '',
    unitType: row.unitType || '',
    category: row.category || '',
    price: row.price || 0,
    status: row.status ?? 1,
    remark: row.remark || ''
  });
  dialogVisible.value = true;
};

// 切换状态
const handleToggleStatus = async (row: MedicalEquipment) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1;
    await changeMedicalEquipmentStatus(row.id!, newStatus);
    ElMessage.success("状态更新成功");
    fetchList();
  } catch (error: any) {
    ElMessage.error(error?.message || "状态更新失败");
  }
};

// 删除
const handleDelete = async (row: MedicalEquipment) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除设备 "${row.equipmentName}" 吗？`,
      "确认删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await deleteMedicalEquipment(row.id!);
    ElMessage.success("删除成功");
    fetchList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "删除失败");
    }
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的设备");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个设备吗？`,
      "确认批量删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    const ids = selectedRows.value.map(row => row.id!);
    await batchDeleteMedicalEquipments(ids);
    ElMessage.success("批量删除成功");
    selectedRows.value = [];
    fetchList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量删除失败");
    }
  }
};

// 导入
const handleImport = () => {
  ElMessage.info("导入功能开发中");
  // TODO: 实现导入功能
};

// 导出
const handleExport = () => {
  ElMessage.info("导出功能开发中");
  // TODO: 实现导出功能
};

// 保存
const handleSave = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitLoading.value = true;
    
    if (isEdit.value) {
      await updateMedicalEquipment(form.id!, form as MedicalEquipment);
      ElMessage.success("更新成功");
    } else {
      await createMedicalEquipment(form as MedicalEquipment);
      ElMessage.success("新增成功");
    }
    
    dialogVisible.value = false;
    fetchList();
  } catch (error: any) {
    if (typeof error === 'object' && error !== null) {
      // 表单验证错误
      if (Object.keys(error).length > 0) {
        ElMessage.error("请完善表单信息");
      } else {
        ElMessage.error(error?.message || `${isEdit.value ? '更新' : '新增'}失败`);
      }
    }
  } finally {
    submitLoading.value = false;
  }
};

onMounted(() => {
  fetchList();
});
</script>

<style scoped>
.facility-list {
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

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
