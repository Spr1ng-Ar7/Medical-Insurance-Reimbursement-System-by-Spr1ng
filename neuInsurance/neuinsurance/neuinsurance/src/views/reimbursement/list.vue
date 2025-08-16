<template>
  <div class="reimbursement-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>报销等级管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">
            新增报销等级
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-form">
        <el-form :model="searchForm" inline>
          <el-form-item label="等级名称">
            <el-input 
              v-model="searchForm.levelName" 
              placeholder="请输入等级名称" 
              clearable 
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="医保类型">
            <el-select 
              v-model="searchForm.insuranceType" 
              placeholder="请选择医保类型" 
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
          <el-form-item label="医院等级">
            <el-select 
              v-model="searchForm.hospitalLevel" 
              placeholder="请选择医院等级" 
              clearable 
              style="width: 150px"
            >
              <el-option label="一级" value="一级" />
              <el-option label="二级" value="二级" />
              <el-option label="三级" value="三级" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择状态" 
              clearable 
              style="width: 120px"
            >
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
            <el-button icon="Refresh" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格 -->
      <el-table 
        v-loading="loading"
        :data="levelList" 
        border 
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="levelCode" label="等级编码" min-width="120" />
        <el-table-column prop="levelName" label="等级名称" min-width="150" />
        <el-table-column prop="insuranceType" label="医保类型" width="120" />
        <el-table-column prop="hospitalLevel" label="医院等级" width="100" />
        <el-table-column prop="deductible" label="起付线" width="120">
          <template #default="{ row }">
            ¥{{ formatAmount(row.deductible) }}
          </template>
        </el-table-column>
        <el-table-column prop="maxReimbursement" label="最高限度" width="140">
          <template #default="{ row }">
            ¥{{ formatAmount(row.maxReimbursement) }}
          </template>
        </el-table-column>
        <el-table-column prop="categoryARate" label="甲类药品比例" width="120">
          <template #default="{ row }">
            {{ formatRate(row.categoryARate) }}%
          </template>
        </el-table-column>
        <el-table-column prop="categoryBRate" label="乙类药品比例" width="120">
          <template #default="{ row }">
            {{ formatRate(row.categoryBRate) }}%
          </template>
        </el-table-column>
        <el-table-column prop="treatmentRate" label="诊疗费比例" width="120">
          <template #default="{ row }">
            {{ formatRate(row.treatmentRate) }}%
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button type="success" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="warning" link size="small" @click="handleCopy(row)">
              复制
            </el-button>
            <el-button 
              :type="row.status === 1 ? 'warning' : 'success'" 
              link 
              size="small" 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
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
          :current-page="pagination.current"
          :page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 批量操作 -->
      <div class="batch-operations" v-if="selectedRows.length > 0">
        <el-button type="warning" @click="handleBatchToggleStatus">
          批量启用/禁用
        </el-button>
        <el-button type="danger" @click="handleBatchDelete">
          批量删除
        </el-button>
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="等级编码" prop="levelCode">
              <el-input v-model="formData.levelCode" placeholder="请输入等级编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="等级名称" prop="levelName">
              <el-input v-model="formData.levelName" placeholder="请输入等级名称" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="医保类型" prop="insuranceType">
              <el-select v-model="formData.insuranceType" placeholder="请选择医保类型" style="width: 100%">
                <el-option label="城镇职工" value="城镇职工" />
                <el-option label="城乡居民" value="城乡居民" />
                <el-option label="新农合" value="新农合" />
                <el-option label="医疗救助" value="医疗救助" />
                <el-option label="商业保险" value="商业保险" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医院等级" prop="hospitalLevel">
              <el-select v-model="formData.hospitalLevel" placeholder="请选择医院等级" style="width: 100%">
                <el-option label="一级" value="一级" />
                <el-option label="二级" value="二级" />
                <el-option label="三级" value="三级" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最低费用">
              <el-input-number 
                v-model="formData.minAmount" 
                :precision="2" 
                :min="0" 
                style="width: 100%"
                placeholder="最低费用"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高费用">
              <el-input-number 
                v-model="formData.maxAmount" 
                :precision="2" 
                :min="0" 
                style="width: 100%"
                placeholder="最高费用"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="起付线">
              <el-input-number 
                v-model="formData.deductible" 
                :precision="2" 
                :min="0" 
                style="width: 100%"
                placeholder="起付线金额"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最高报销额度">
              <el-input-number 
                v-model="formData.maxReimbursement" 
                :precision="2" 
                :min="0" 
                style="width: 100%"
                placeholder="最高报销额度"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">报销比例配置</el-divider>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="甲类药品比例">
              <el-input-number 
                v-model="formData.categoryARate" 
                :precision="4" 
                :min="0" 
                :max="1"
                :step="0.01"
                style="width: 100%"
                placeholder="0-1之间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="乙类药品比例">
              <el-input-number 
                v-model="formData.categoryBRate" 
                :precision="4" 
                :min="0" 
                :max="1"
                :step="0.01"
                style="width: 100%"
                placeholder="0-1之间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="丙类药品比例">
              <el-input-number 
                v-model="formData.categoryCRate" 
                :precision="4" 
                :min="0" 
                :max="1"
                :step="0.01"
                style="width: 100%"
                placeholder="0-1之间"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="诊疗费用比例">
              <el-input-number 
                v-model="formData.treatmentRate" 
                :precision="4" 
                :min="0" 
                :max="1"
                :step="0.01"
                style="width: 100%"
                placeholder="0-1之间"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="服务设施比例">
              <el-input-number 
                v-model="formData.serviceRate" 
                :precision="4" 
                :min="0" 
                :max="1"
                :step="0.01"
                style="width: 100%"
                placeholder="0-1之间"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注">
          <el-input 
            v-model="formData.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
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
  getLevelList, 
  deleteLevel, 
  batchDeleteLevel,
  addLevel,
  updateLevel,
  updateLevelStatus,
  copyLevel,
  type ReimbursementLevel 
} from "@/api/reimbursementLevel";

const loading = ref(false);
const submitLoading = ref(false);
const dialogVisible = ref(false);
const isEdit = ref(false);
const formRef = ref<FormInstance>();

// 搜索表单
const searchForm = reactive({
  levelName: '',
  insuranceType: '',
  hospitalLevel: '',
  status: undefined as number | undefined
});

// 表格数据
const levelList = ref<ReimbursementLevel[]>([]);
const selectedRows = ref<ReimbursementLevel[]>([]);

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 表单数据
const formData = reactive<Partial<ReimbursementLevel>>({
  levelCode: '',
  levelName: '',
  insuranceType: '',
  hospitalLevel: '',
  minAmount: 0,
  maxAmount: 0,
  deductible: 0,
  maxReimbursement: 0,
  categoryARate: 0,
  categoryBRate: 0,
  categoryCRate: 0,
  treatmentRate: 0,
  serviceRate: 0,
  status: 1,
  remark: ''
});

// 表单验证规则
const formRules: FormRules = {
  levelCode: [
    { required: true, message: '请输入等级编码', trigger: 'blur' }
  ],
  levelName: [
    { required: true, message: '请输入等级名称', trigger: 'blur' }
  ],
  insuranceType: [
    { required: true, message: '请选择医保类型', trigger: 'change' }
  ],
  hospitalLevel: [
    { required: true, message: '请选择医院等级', trigger: 'change' }
  ]
};

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑报销等级' : '新增报销等级');

// 格式化金额
const formatAmount = (amount?: number | null) => {
  if (amount === undefined || amount === null || isNaN(Number(amount))) {
    return '0.00';
  }
  return Number(amount).toFixed(2);
};

// 格式化比例
const formatRate = (rate?: number | null) => {
  if (rate === undefined || rate === null || isNaN(Number(rate))) {
    return '0.00';
  }
  return (Number(rate) * 100).toFixed(2);
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
    const result = await getLevelList({
      pageNum: pagination.current,
      pageSize: pagination.size,
      levelName: searchForm.levelName,
      insuranceType: searchForm.insuranceType,
      hospitalLevel: searchForm.hospitalLevel,
      status: searchForm.status
    });
    
    levelList.value = result.records || [];
    pagination.total = result.total || 0;
  } catch (error: any) {
    console.error('获取报销等级列表失败:', error);
    ElMessage.error(error?.message || "获取报销等级列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchList();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    levelName: '',
    insuranceType: '',
    hospitalLevel: '',
    status: undefined
  });
  pagination.current = 1;
  fetchList();
};

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size;
  pagination.current = 1;
  fetchList();
};

const handleCurrentChange = (current: number) => {
  pagination.current = current;
  fetchList();
};

// 选择处理
const handleSelectionChange = (rows: ReimbursementLevel[]) => {
  selectedRows.value = rows;
};

// 新增
const handleAdd = () => {
  isEdit.value = false;
  resetForm();
  dialogVisible.value = true;
};

// 查看
const handleView = (row: ReimbursementLevel) => {
  ElMessage.info("查看功能开发中");
  // TODO: 实现查看详情功能
};

// 编辑
const handleEdit = (row: ReimbursementLevel) => {
  isEdit.value = true;
  Object.assign(formData, row);
  dialogVisible.value = true;
};

// 复制
const handleCopy = async (row: ReimbursementLevel) => {
  try {
    await copyLevel(row.id!);
    ElMessage.success("复制成功");
    fetchList();
  } catch (error: any) {
    ElMessage.error(error?.message || "复制失败");
  }
};

// 切换状态
const handleToggleStatus = async (row: ReimbursementLevel) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1;
    await updateLevelStatus(row.id!, newStatus);
    ElMessage.success("状态更新成功");
    fetchList();
  } catch (error: any) {
    ElMessage.error(error?.message || "状态更新失败");
  }
};

// 删除
const handleDelete = async (row: ReimbursementLevel) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除报销等级 "${row.levelName}" 吗？`,
      "确认删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await deleteLevel(row.id!);
    ElMessage.success("删除成功");
    fetchList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "删除失败");
    }
  }
};

// 批量切换状态
const handleBatchToggleStatus = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要操作的数据");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要批量切换选中的 ${selectedRows.value.length} 个报销等级的状态吗？`,
      "确认操作",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    // 这里需要后端支持批量操作，暂时逐个操作
    for (const row of selectedRows.value) {
      const newStatus = row.status === 1 ? 0 : 1;
      await updateLevelStatus(row.id!, newStatus);
    }
    
    ElMessage.success("批量操作成功");
    selectedRows.value = [];
    fetchList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量操作失败");
    }
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的数据");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个报销等级吗？`,
      "确认批量删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    const ids = selectedRows.value.map(row => row.id!);
    await batchDeleteLevel(ids);
    ElMessage.success("批量删除成功");
    selectedRows.value = [];
    fetchList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量删除失败");
    }
  }
};

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: undefined,
    levelCode: '',
    levelName: '',
    insuranceType: '',
    hospitalLevel: '',
    minAmount: 0,
    maxAmount: 0,
    deductible: 0,
    maxReimbursement: 0,
    categoryARate: 0,
    categoryBRate: 0,
    categoryCRate: 0,
    treatmentRate: 0,
    serviceRate: 0,
    status: 1,
    remark: ''
  });
  formRef.value?.resetFields();
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitLoading.value = true;
    
    if (isEdit.value) {
      await updateLevel(formData.id!, formData as ReimbursementLevel);
      ElMessage.success("更新成功");
    } else {
      await addLevel(formData as ReimbursementLevel);
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
.reimbursement-list {
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.batch-operations {
  margin-top: 20px;
  padding: 16px;
  background: #fff7e6;
  border: 1px solid #ffd591;
  border-radius: 6px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
