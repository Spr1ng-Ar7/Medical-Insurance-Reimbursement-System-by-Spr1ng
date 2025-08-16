<template>
  <div class="medical-order-approve">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>医疗订单审核</span>
          <el-button @click="handleBack">返回列表</el-button>
        </div>
      </template>
      
      <!-- 搜索条件 -->
      <div class="search-form">
        <el-form :model="searchParams" inline>
          <el-form-item label="订单号">
            <el-input
              v-model="searchParams.orderNo"
              placeholder="请输入订单号"
              clearable
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="患者姓名">
            <el-input
              v-model="searchParams.patientName"
              placeholder="请输入患者姓名"
              clearable
              style="width: 150px"
            />
          </el-form-item>
          <el-form-item label="订单类型">
            <el-select v-model="searchParams.orderType" placeholder="请选择订单类型" clearable style="width: 130px">
              <el-option label="门诊" value="门诊" />
              <el-option label="住院" value="住院" />
              <el-option label="急诊" value="急诊" />
              <el-option label="体检" value="体检" />
            </el-select>
          </el-form-item>
          <el-form-item label="审核状态">
            <el-select v-model="searchParams.status" placeholder="请选择状态" clearable style="width: 130px">
              <el-option label="待审核" value="4" />
              <el-option label="已通过" value="2" />
              <el-option label="已拒绝" value="5" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleSearch">
              搜索
            </el-button>
            <el-button icon="Refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button 
            type="success" 
            icon="Check"
            :disabled="selectedRows.length === 0"
            @click="showBatchDialog"
          >
            批量审核
          </el-button>
          <el-button 
            type="info" 
            icon="Refresh"
            @click="handleRefresh"
          >
            刷新数据
          </el-button>
        </div>
        <div class="toolbar-right">
          <span class="selected-info" v-if="selectedRows.length > 0">
            已选择 {{ selectedRows.length }} 项
          </span>
          <span class="summary-info">
            待审核: <el-tag type="warning" size="small">{{ statusSummary.pending }}</el-tag>
            已通过: <el-tag type="success" size="small">{{ statusSummary.approved }}</el-tag>
            已拒绝: <el-tag type="danger" size="small">{{ statusSummary.rejected }}</el-tag>
          </span>
        </div>
      </div>
      
      <!-- 订单列表 -->
      <el-table
        v-loading="loading"
        :data="orderList"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        
        <el-table-column prop="orderNo" label="订单编号" width="180">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">
              {{ row.orderNo || '-' }}
            </el-link>
          </template>
        </el-table-column>
        
        <el-table-column prop="patientName" label="患者姓名" width="120" />
        
        <el-table-column prop="orderType" label="订单类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOrderTypeColor(row.orderType)" size="small">
              {{ row.orderType || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="departmentName" label="科室" width="120" />
        
        <el-table-column prop="doctorName" label="主治医生" width="120" />
        
        <el-table-column prop="diagnosis" label="诊断" min-width="150">
          <template #default="{ row }">
            <el-tooltip :content="row.diagnosis" placement="top" :disabled="!row.diagnosis">
              <span class="text-ellipsis">{{ row.diagnosis || '-' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">
            <span class="amount">¥{{ formatAmount(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="审核状态" width="100">
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
        
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button 
              v-if="row.status === 4" 
              type="success" 
              link 
              size="small" 
              @click="handleApprove(row)"
            >
              审核
            </el-button>
            <el-button 
              v-if="row.status === 4" 
              type="danger" 
              link 
              size="small" 
              @click="handleReject(row)"
            >
              拒绝
            </el-button>
            <el-button 
              v-if="[2, 5].includes(row.status)" 
              type="info" 
              link 
              size="small"
              @click="handleViewApprovalDetail(row)"
            >
              审核详情
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
      
      <!-- 批量审核对话框 -->
      <el-dialog v-model="batchDialogVisible" title="批量审核" width="600px">
        <el-form :model="batchForm" label-width="100px">
          <el-form-item label="审核结果">
            <el-radio-group v-model="batchForm.result">
              <el-radio :label="2">通过</el-radio>
              <el-radio :label="5">拒绝</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="审核意见">
            <el-input
              v-model="batchForm.comment"
              type="textarea"
              :rows="3"
              placeholder="请输入审核意见"
            />
          </el-form-item>
          <el-form-item>
            <el-alert
              :title="`将对 ${selectedRows.length} 个订单执行${batchForm.result === 2 ? '通过' : '拒绝'}操作`"
              type="warning"
              :closable="false"
              show-icon
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="batchDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleBatchApprove" :loading="batchLoading">
              确定
            </el-button>
          </span>
        </template>
      </el-dialog>
      
      <!-- 审核详情对话框 -->
      <el-dialog v-model="detailDialogVisible" title="审核详情" width="600px">
        <el-descriptions :column="1" border v-if="currentOrder">
          <el-descriptions-item label="订单号">
            {{ currentOrder.orderNo }}
          </el-descriptions-item>
          <el-descriptions-item label="患者姓名">
            {{ currentOrder.patientName }}
          </el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="getStatusColor(currentOrder.status)">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="审核结果" v-if="currentOrder.approvalResult">
            {{ currentOrder.approvalResult }}
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" v-if="currentOrder.approvalRemark">
            {{ currentOrder.approvalRemark }}
          </el-descriptions-item>
          <el-descriptions-item label="拒绝原因" v-if="currentOrder.rejectReason">
            <el-alert type="error" :title="currentOrder.rejectReason" :closable="false" />
          </el-descriptions-item>
          <el-descriptions-item label="审核时间" v-if="currentOrder.updateTime">
            {{ formatDateTime(currentOrder.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="detailDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { 
  getMedicalOrderList, 
  updateOrderStatus, 
  type MedicalOrder,
  type MedicalOrderQueryParams 
} from "../../api/medical-order";

const router = useRouter();

const loading = ref(false);
const orderList = ref<MedicalOrder[]>([]);
const selectedRows = ref<MedicalOrder[]>([]);

// 搜索参数
const searchParams = reactive({
  pageNum: 1,
  pageSize: 10,
  orderNo: "",
  patientName: "",
  orderType: "",
  status: "4" // 默认只显示待审核的
});

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 批量审核相关
const batchDialogVisible = ref(false);
const batchLoading = ref(false);
const batchForm = reactive({
  result: 2,
  comment: ""
});

// 审核详情
const detailDialogVisible = ref(false);
const currentOrder = ref<MedicalOrder | null>(null);

// 状态统计
const statusSummary = computed(() => {
  return {
    pending: orderList.value.filter(order => order.status === 4).length,
    approved: orderList.value.filter(order => order.status === 2).length,
    rejected: orderList.value.filter(order => order.status === 5).length
  };
});

// 格式化金额
const formatAmount = (amount?: number | null) => {
  if (amount === undefined || amount === null || isNaN(Number(amount))) {
    return '0.00';
  }
  return Number(amount).toFixed(2);
};

// 获取订单类型颜色
const getOrderTypeColor = (type?: string) => {
  const colorMap: Record<string, "primary" | "success" | "warning" | "info"> = {
    "门诊": "primary",
    "住院": "success",
    "急诊": "warning",
    "体检": "info"
  };
  return colorMap[type || ""] || "info";
};

// 获取状态颜色
const getStatusColor = (status: number) => {
  const colorMap: Record<number, "primary" | "success" | "warning" | "danger" | "info"> = {
    2: "success",  // 已通过
    4: "warning",  // 待审核
    5: "danger"    // 已拒绝
  };
  return colorMap[status] || "info";
};

// 获取状态文本
const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    2: "已通过",
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

// 获取订单列表
const fetchOrderList = async () => {
  loading.value = true;
  try {
    const requestParams: MedicalOrderQueryParams = {
      pageNum: searchParams.pageNum,
      pageSize: searchParams.pageSize,
      orderNo: searchParams.orderNo || undefined,
      patientId: undefined,
      status: searchParams.status || undefined,
      keyword: searchParams.patientName || undefined
    };

    const result = await getMedicalOrderList(requestParams);
    orderList.value = result.records || [];
    pagination.total = result.total || 0;
    pagination.current = result.current || searchParams.pageNum;
    pagination.size = result.size || searchParams.pageSize;
  } catch (error: any) {
    ElMessage.error(error?.message || "获取订单列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  searchParams.pageNum = 1;
  fetchOrderList();
};

// 重置
const handleReset = () => {
  Object.assign(searchParams, {
    pageNum: 1,
    pageSize: 10,
    orderNo: "",
    patientName: "",
    orderType: "",
    status: "4"
  });
  fetchOrderList();
};

// 刷新
const handleRefresh = () => {
  fetchOrderList();
};

// 返回
const handleBack = () => {
  router.push("/medical-order/list");
};

// 选择变化
const handleSelectionChange = (selection: MedicalOrder[]) => {
  selectedRows.value = selection;
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size;
  searchParams.pageNum = 1;
  fetchOrderList();
};

// 当前页变化
const handleCurrentChange = (current: number) => {
  searchParams.pageNum = current;
  fetchOrderList();
};

// 查看
const handleView = (row: MedicalOrder) => {
  router.push(`/medical-order/detail/${row.id}`);
};

// 审核通过
const handleApprove = async (row: MedicalOrder) => {
  try {
    const { value } = await ElMessageBox.prompt(
      "请输入审核意见",
      "审核通过",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputType: "textarea",
        inputPlaceholder: "请输入审核意见（可选）"
      }
    );
    
    await updateOrderStatus(row.id!, "2");
    ElMessage.success("审核通过成功");
    fetchOrderList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "审核失败");
    }
  }
};

// 审核拒绝
const handleReject = async (row: MedicalOrder) => {
  try {
    const { value } = await ElMessageBox.prompt(
      "请输入拒绝原因",
      "审核拒绝",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputType: "textarea",
        inputPlaceholder: "请输入拒绝原因",
        inputValidator: (val) => {
          if (!val || val.trim() === '') {
            return '拒绝原因不能为空';
          }
          return true;
        }
      }
    );
    
    await updateOrderStatus(row.id!, "5");
    ElMessage.success("审核拒绝成功");
    fetchOrderList();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "操作失败");
    }
  }
};

// 查看审核详情
const handleViewApprovalDetail = (row: MedicalOrder) => {
  currentOrder.value = row;
  detailDialogVisible.value = true;
};

// 显示批量审核对话框
const showBatchDialog = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要审核的订单");
    return;
  }
  
  // 检查是否都是待审核状态
  const pendingOrders = selectedRows.value.filter(row => row.status === 4);
  if (pendingOrders.length !== selectedRows.value.length) {
    ElMessage.warning("只能审核状态为'待审核'的订单");
    return;
  }
  
  batchForm.result = 2;
  batchForm.comment = "";
  batchDialogVisible.value = true;
};

// 批量审核
const handleBatchApprove = async () => {
  if (!batchForm.comment.trim() && batchForm.result === 5) {
    ElMessage.warning("拒绝操作必须填写审核意见");
    return;
  }
  
  batchLoading.value = true;
  try {
    const statusText = batchForm.result === 2 ? "通过" : "拒绝";
    const promises = selectedRows.value.map(row => 
      updateOrderStatus(row.id!, String(batchForm.result))
    );
    
    await Promise.all(promises);
    
    ElMessage.success(`批量审核${statusText}成功`);
    batchDialogVisible.value = false;
    selectedRows.value = [];
    fetchOrderList();
  } catch (error: any) {
    ElMessage.error(error?.message || "批量审核失败");
  } finally {
    batchLoading.value = false;
  }
};

// 初始化
onMounted(() => {
  fetchOrderList();
});
</script>

<style scoped>
.medical-order-approve {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
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

.toolbar-right {
  display: flex;
  gap: 20px;
  align-items: center;
}

.selected-info, .summary-info {
  color: #606266;
  font-size: 14px;
}

.summary-info .el-tag {
  margin-left: 8px;
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