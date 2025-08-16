<template>
  <div class="medical-order-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>医疗订单管理</span>
          <el-button type="primary" icon="Plus" @click="handleAdd">
            新增订单
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-form">
        <el-form :model="searchForm" inline>
          <el-form-item label="订单号">
            <el-input 
              v-model="searchForm.orderNo" 
              placeholder="请输入订单号" 
              clearable 
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="患者姓名">
            <el-input 
              v-model="searchForm.patientName" 
              placeholder="请输入患者姓名" 
              clearable 
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="订单类型">
            <el-select 
              v-model="searchForm.orderType" 
              placeholder="请选择订单类型" 
              clearable 
              style="width: 130px"
            >
              <el-option label="门诊" value="门诊" />
              <el-option label="住院" value="住院" />
              <el-option label="急诊" value="急诊" />
              <el-option label="体检" value="体检" />
            </el-select>
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择状态" 
              clearable 
              style="width: 130px"
            >
              <el-option label="已取消" value="0" />
              <el-option label="待结算" value="1" />
              <el-option label="已结算" value="2" />
              <el-option label="已支付" value="3" />
              <el-option label="待审核" value="4" />
              <el-option label="已拒绝" value="5" />
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
          <el-button type="primary" icon="Plus" @click="handleAdd">新增订单</el-button>
          <el-button 
            type="danger" 
            icon="Delete"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button 
            type="success" 
            icon="Check"
            :disabled="selectedRows.length === 0"
            @click="handleBatchApprove"
          >
            批量审核
          </el-button>
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
        :data="orderList" 
        border 
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="orderNo" label="订单号" min-width="180">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.orderNo || '-' }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="120" />
        <el-table-column prop="orderType" label="订单类型" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="getOrderTypeColor(row.orderType)" 
              size="small"
            >
              <el-icon style="margin-right: 4px;">
                <User v-if="row.orderType === '门诊'" />
                <House v-if="row.orderType === '住院'" />
                <Warning v-if="row.orderType === '急诊'" />
                <Document v-else />
              </el-icon>
              {{ row.orderType || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="diagnosis" label="诊断" min-width="150">
          <template #default="{ row }">
            <el-tooltip :content="row.diagnosis" placement="top" :disabled="!row.diagnosis">
              <span class="text-ellipsis">{{ row.diagnosis || '-' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="主治医生" width="120" />
        <el-table-column prop="departmentName" label="科室" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">
            <span class="amount">¥{{ formatAmount(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualReimbursement" label="报销金额" width="120">
          <template #default="{ row }">
            <span class="reimbursement">¥{{ formatAmount(row.actualReimbursement) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)">
              {{ getStatusText(row.status) }}
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
            <el-button 
              v-if="[1, 4].includes(row.status)" 
              type="success" 
              link 
              size="small" 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="row.status === 4" 
              type="warning" 
              link 
              size="small" 
              @click="handleApprove(row)"
            >
              审核
            </el-button>
            <el-button 
              :type="row.status === 0 ? 'success' : 'warning'" 
              link 
              size="small" 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 0 ? '激活' : '取消' }}
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

    <!-- 审核对话框 -->
    <el-dialog v-model="approveDialogVisible" title="审核医疗订单" width="600px">
      <el-form :model="approveForm" label-width="100px">
        <el-form-item label="订单号">
          <el-input :value="currentOrder?.orderNo" readonly />
        </el-form-item>
        <el-form-item label="患者姓名">
          <el-input :value="currentOrder?.patientName" readonly />
        </el-form-item>
        <el-form-item label="审核结果" required>
          <el-radio-group v-model="approveForm.result">
            <el-radio :label="2">通过</el-radio>
            <el-radio :label="5">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input
            v-model="approveForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="approveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleApproveConfirm" :loading="approveLoading">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { User, House, Warning, Document } from "@element-plus/icons-vue";
import { 
  getMedicalOrderList, 
  deleteMedicalOrder, 
  batchDeleteMedicalOrder, 
  updateOrderStatus,
  type MedicalOrder,
  type MedicalOrderQueryParams
} from "../../api/medical-order";

const router = useRouter();

const loading = ref(false);
const selectedRows = ref<MedicalOrder[]>([]);
const orderList = ref<MedicalOrder[]>([]);

// 搜索表单
const searchForm = reactive({
  orderNo: '',
  patientName: '',
  orderType: '',
  status: '',
  keyword: ''
});

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  background: true
});

// 审核相关
const approveDialogVisible = ref(false);
const approveLoading = ref(false);
const currentOrder = ref<MedicalOrder | null>(null);
const approveForm = reactive({
  result: 2,
  remark: ''
});

// 格式化金额
const formatAmount = (amount?: number | null) => {
  if (amount === undefined || amount === null || isNaN(Number(amount))) {
    return '0.00';
  }
  return Number(amount).toFixed(2);
};

// 获取订单类型颜色
const getOrderTypeColor = (type: string) => {
  const colorMap: Record<string, "primary" | "success" | "warning" | "info"> = {
    "门诊": "primary",
    "住院": "success",
    "急诊": "warning",
    "体检": "info"
  };
  return colorMap[type] || "info";
};

// 获取状态颜色
const getStatusColor = (status: number) => {
  const colorMap: Record<number, "primary" | "success" | "warning" | "danger" | "info"> = {
    0: "info",     // 已取消
    1: "warning",  // 待结算
    2: "success",  // 已结算
    3: "primary",  // 已支付
    4: "warning",  // 待审核
    5: "danger"    // 已拒绝
  };
  return colorMap[status] || "info";
};

// 获取状态文本
const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: "已取消",
    1: "待结算", 
    2: "已结算",
    3: "已支付",
    4: "待审核",
    5: "已拒绝"
  };
  return statusMap[status] || "未知";
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

// 获取医疗订单列表
const fetchOrderList = async () => {
  loading.value = true;
  try {
    const requestParams: MedicalOrderQueryParams = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      orderNo: searchForm.orderNo || undefined,
      patientId: undefined,
      status: searchForm.status || undefined,
      keyword: searchForm.keyword || undefined
    };

    const result = await getMedicalOrderList(requestParams);
    orderList.value = result.records || [];
    pagination.total = result.total || 0;
  } catch (error: any) {
    console.error("获取订单列表失败:", error);
    ElMessage.error(error?.message || "获取订单列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchOrderList();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    orderNo: '',
    patientName: '',
    orderType: '',
    status: '',
    keyword: ''
  });
  pagination.currentPage = 1;
  fetchOrderList();
};

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchOrderList();
};

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  fetchOrderList();
};

// 选择处理
const handleSelectionChange = (rows: MedicalOrder[]) => {
  selectedRows.value = rows;
};

// 新增订单
const handleAdd = () => {
  router.push("/medical-order/add");
};

// 查看订单
const handleView = (row: MedicalOrder) => {
  router.push(`/medical-order/detail/${row.id}`);
};

// 编辑订单
const handleEdit = (row: MedicalOrder) => {
  router.push(`/medical-order/edit/${row.id}`);
};

// 审核订单
const handleApprove = (row: MedicalOrder) => {
  currentOrder.value = row;
  approveForm.result = 2;
  approveForm.remark = '';
  approveDialogVisible.value = true;
};

// 确认审核
const handleApproveConfirm = async () => {
  if (!currentOrder.value) return;
  
  approveLoading.value = true;
  try {
    await updateOrderStatus(currentOrder.value.id!, String(approveForm.result));
    ElMessage.success("审核成功");
    approveDialogVisible.value = false;
    fetchOrderList();
  } catch (error: any) {
    ElMessage.error(error?.message || "审核失败");
  } finally {
    approveLoading.value = false;
  }
};

// 切换状态
const handleToggleStatus = async (row: MedicalOrder) => {
  try {
    const newStatus = row.status === 0 ? 1 : 0;
    const statusText = newStatus === 0 ? '取消' : '激活';
    
    await ElMessageBox.confirm(
      `确定要${statusText}订单 "${row.orderNo}" 吗？`,
      "确认操作",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await updateOrderStatus(row.id!, String(newStatus));
    ElMessage.success(`订单已${statusText}`);
    fetchOrderList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "状态更新失败");
    }
  }
};

// 删除订单
const handleDelete = async (row: MedicalOrder) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除订单 "${row.orderNo}" 吗？`,
      "确认删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await deleteMedicalOrder(row.id!);
    ElMessage.success("删除成功");
    fetchOrderList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "删除失败");
    }
  }
};

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的订单");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个订单吗？`,
      "确认批量删除",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    const ids = selectedRows.value.map(row => row.id!);
    await batchDeleteMedicalOrder(ids);
    ElMessage.success("批量删除成功");
    selectedRows.value = [];
    fetchOrderList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量删除失败");
    }
  }
};

// 批量审核
const handleBatchApprove = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要审核的订单");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要批量审核选中的 ${selectedRows.value.length} 个订单吗？`,
      "确认批量审核",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    for (const row of selectedRows.value) {
      if (row.status === 4) {
        await updateOrderStatus(row.id!, "2");
      }
    }
    ElMessage.success("批量审核成功");
    selectedRows.value = [];
    fetchOrderList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量审核失败");
    }
  }
};

// 导出数据
const handleExport = () => {
  ElMessage.info("导出功能开发中");
  // TODO: 实现导出功能
};

onMounted(() => {
  fetchOrderList();
});
</script>

<style scoped>
.medical-order-list {
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

.amount {
  color: #e6a23c;
  font-weight: bold;
}

.reimbursement {
  color: #67c23a;
  font-weight: bold;
}

.text-ellipsis {
  display: inline-block;
  max-width: 130px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:deep(.el-tag) {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}
</style> 