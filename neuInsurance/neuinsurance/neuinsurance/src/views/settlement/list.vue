<template>
  <div class="settlement-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>结算管理</span>
          <div class="header-actions">
            <el-button type="primary" icon="Plus" @click="handleCreate">
              创建结算
            </el-button>
            <el-button type="success" icon="RefreshRight" @click="handleRefresh">
              刷新
            </el-button>
          </div>
        </div>
      </template>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="stats-card">
              <div class="stats-content">
                <div class="stats-number">{{ statistics.totalCount }}</div>
                <div class="stats-label">总结算数</div>
              </div>
              <el-icon class="stats-icon" color="#409EFF">
                <Document />
              </el-icon>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card">
              <div class="stats-content">
                <div class="stats-number">¥{{ formatAmount(statistics.totalAmount) }}</div>
                <div class="stats-label">总结算金额</div>
              </div>
              <el-icon class="stats-icon" color="#67C23A">
                <Money />
              </el-icon>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card">
              <div class="stats-content">
                <div class="stats-number">¥{{ formatAmount(statistics.totalReimbursement) }}</div>
                <div class="stats-label">总报销金额</div>
              </div>
              <el-icon class="stats-icon" color="#E6A23C">
                <CreditCard />
              </el-icon>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="stats-card">
              <div class="stats-content">
                <div class="stats-number">{{ formatPercent(statistics.averageReimbursementRatio) }}</div>
                <div class="stats-label">平均报销比例</div>
              </div>
              <el-icon class="stats-icon" color="#F56C6C">
                <TrendCharts />
              </el-icon>
            </el-card>
          </el-col>
        </el-row>
      </div>

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
          <el-form-item label="结算状态">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择状态" 
              clearable 
              style="width: 130px"
            >
              <el-option label="待结算" value="1" />
              <el-option label="已结算" value="2" />
              <el-option label="已支付" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="医院等级">
            <el-select 
              v-model="searchForm.hospitalLevel" 
              placeholder="请选择医院等级" 
              clearable 
              style="width: 130px"
            >
              <el-option label="三级甲等" value="三级甲等" />
              <el-option label="三级乙等" value="三级乙等" />
              <el-option label="二级甲等" value="二级甲等" />
              <el-option label="二级乙等" value="二级乙等" />
            </el-select>
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
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
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
          <el-button type="primary" icon="Plus" @click="handleCreate">
            创建结算
          </el-button>
          <el-button 
            type="success" 
            icon="Check"
            :disabled="selectedRows.length === 0"
            @click="handleBatchSettle"
          >
            批量结算
          </el-button>
          <el-button 
            type="warning" 
            icon="CreditCard"
            :disabled="selectedRows.length === 0"
            @click="handleBatchPayment"
          >
            批量支付
          </el-button>
          <el-button type="info" icon="Download" @click="handleExport">
            导出报表
          </el-button>
        </div>
        <div class="toolbar-right">
          <span class="selected-info" v-if="selectedRows.length > 0">
            已选择 {{ selectedRows.length }} 项，
            总金额 ¥{{ formatAmount(selectedTotalAmount) }}
          </span>
          <span class="status-summary">
            待结算: <el-tag type="warning" size="small">{{ statusSummary.pending }}</el-tag>
            已结算: <el-tag type="success" size="small">{{ statusSummary.settled }}</el-tag>
            已支付: <el-tag type="primary" size="small">{{ statusSummary.paid }}</el-tag>
          </span>
        </div>
      </div>

      <!-- 结算列表 -->
      <el-table 
        v-loading="loading"
        :data="settlementList" 
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
        <el-table-column prop="hospitalName" label="医院名称" min-width="150">
          <template #default="{ row }">
            <el-tooltip :content="row.hospitalName" placement="top" :disabled="!row.hospitalName">
              <span class="text-ellipsis">{{ row.hospitalName || '-' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="hospitalLevel" label="医院等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getHospitalLevelColor(row.hospitalLevel)" size="small">
              {{ row.hospitalLevel || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderType" label="订单类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOrderTypeColor(row.orderType)" size="small">
              {{ row.orderType || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总费用" width="120">
          <template #default="{ row }">
            <span class="amount">¥{{ formatAmount(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reimbursableAmount" label="可报销金额" width="120">
          <template #default="{ row }">
            <span class="reimbursable">¥{{ formatAmount(row.reimbursableAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualReimbursement" label="实际报销" width="120">
          <template #default="{ row }">
            <span class="reimbursement">¥{{ formatAmount(row.actualReimbursement) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="selfPayAmount" label="自付金额" width="120">
          <template #default="{ row }">
            <span class="self-pay">¥{{ formatAmount(row.selfPayAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reimbursementRatio" label="报销比例" width="100">
          <template #default="{ row }">
            {{ formatPercent(row.reimbursementRatio) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="结算状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusColor(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="settlementTime" label="结算时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.settlementTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleView(row)">
              查看
            </el-button>
            <el-button 
              v-if="row.status === 1" 
              type="success" 
              link 
              size="small" 
              @click="handleSettle(row)"
            >
              结算
            </el-button>
            <el-button 
              v-if="row.status === 2" 
              type="warning" 
              link 
              size="small" 
              @click="handlePayment(row)"
            >
              支付
            </el-button>
            <el-button 
              v-if="[1, 2].includes(row.status)" 
              type="info" 
              link 
              size="small" 
              @click="handleRecalculate(row)"
            >
              重算
            </el-button>
            <el-button 
              type="danger" 
              link 
              size="small" 
              @click="handleCancel(row)"
            >
              取消
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

    <!-- 支付对话框 -->
    <el-dialog v-model="paymentDialogVisible" title="模拟支付" width="600px">
      <el-form :model="paymentForm" label-width="100px">
        <el-form-item label="结算单号">
          <el-input :value="currentSettlement?.orderNo" readonly />
        </el-form-item>
        <el-form-item label="支付金额">
          <el-input :value="`¥${formatAmount(currentSettlement?.actualReimbursement)}`" readonly />
        </el-form-item>
        <el-form-item label="支付方式" required>
          <el-radio-group v-model="paymentForm.paymentType">
            <el-radio label="bank_transfer">银行转账</el-radio>
            <el-radio label="online_payment">在线支付</el-radio>
            <el-radio label="cash">现金支付</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="银行账户" v-if="paymentForm.paymentType === 'bank_transfer'">
          <el-input 
            v-model="paymentForm.bankAccount" 
            placeholder="请输入银行账户号码"
          />
        </el-form-item>
        <el-form-item label="支付备注">
          <el-input 
            v-model="paymentForm.remarks" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入支付备注（可选）"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="paymentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePaymentConfirm" :loading="paymentLoading">
            确认支付
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 重新计算对话框 -->
    <el-dialog v-model="recalculateDialogVisible" title="重新计算结算" width="600px">
      <el-form :model="recalculateForm" label-width="120px">
        <el-form-item label="总费用">
          <el-input :value="`¥${formatAmount(currentSettlement?.totalAmount)}`" readonly />
        </el-form-item>
        <el-form-item label="起付线">
          <el-input-number 
            v-model="recalculateForm.deductible" 
            :precision="2" 
            :min="0" 
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="报销比例">
          <el-input-number 
            v-model="recalculateForm.reimbursementRatio" 
            :precision="4" 
            :min="0" 
            :max="1" 
            :step="0.01"
            style="width: 100%"
          />
        </el-form-item>
        <el-divider>预览计算结果</el-divider>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="可报销金额">
            ¥{{ formatAmount(previewReimbursableAmount) }}
          </el-descriptions-item>
          <el-descriptions-item label="实际报销金额">
            ¥{{ formatAmount(previewActualReimbursement) }}
          </el-descriptions-item>
          <el-descriptions-item label="自付金额">
            ¥{{ formatAmount(previewSelfPayAmount) }}
          </el-descriptions-item>
          <el-descriptions-item label="报销比例">
            {{ formatPercent(recalculateForm.reimbursementRatio) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="recalculateDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleRecalculateConfirm" :loading="recalculateLoading">
            确认重算
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { Document, Money, CreditCard, TrendCharts } from "@element-plus/icons-vue";
import { 
  getSettlementList, 
  getSettlementStatistics,
  simulatePayment,
  completeSettlement,
  batchSettlement,
  recalculateSettlement,
  exportSettlementReport,
  type Settlement,
  type SettlementQueryParams 
} from "../../api/settlement";

const router = useRouter();

const loading = ref(false);
const selectedRows = ref<Settlement[]>([]);
const settlementList = ref<Settlement[]>([]);
const dateRange = ref<[string, string] | []>([]);

// 统计数据
const statistics = reactive({
  totalCount: 0,
  totalAmount: 0,
  totalReimbursement: 0,
  pendingCount: 0,
  pendingAmount: 0,
  completedCount: 0,
  completedAmount: 0,
  rejectedCount: 0,
  rejectedAmount: 0,
  averageReimbursementRatio: 0
});

// 搜索表单
const searchForm = reactive({
  orderNo: '',
  patientName: '',
  status: '',
  hospitalLevel: '',
  orderType: ''
});

// 分页
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
});

// 支付相关
const paymentDialogVisible = ref(false);
const paymentLoading = ref(false);
const currentSettlement = ref<Settlement | null>(null);
const paymentForm = reactive({
  paymentType: 'bank_transfer',
  bankAccount: '',
  remarks: ''
});

// 重新计算相关
const recalculateDialogVisible = ref(false);
const recalculateLoading = ref(false);
const recalculateForm = reactive({
  deductible: 0,
  reimbursementRatio: 0
});

// 状态统计
const statusSummary = computed(() => {
  return {
    pending: settlementList.value.filter(item => item.status === 1).length,
    settled: settlementList.value.filter(item => item.status === 2).length,
    paid: settlementList.value.filter(item => item.status === 3).length
  };
});

// 选中项总金额
const selectedTotalAmount = computed(() => {
  return selectedRows.value.reduce((sum, item) => sum + (item.actualReimbursement || 0), 0);
});

// 预览计算结果
const previewReimbursableAmount = computed(() => {
  if (!currentSettlement.value) return 0;
  const reimbursable = (currentSettlement.value.totalAmount || 0) - recalculateForm.deductible;
  return Math.max(0, reimbursable);
});

const previewActualReimbursement = computed(() => {
  return previewReimbursableAmount.value * recalculateForm.reimbursementRatio;
});

const previewSelfPayAmount = computed(() => {
  if (!currentSettlement.value) return 0;
  return (currentSettlement.value.totalAmount || 0) - previewActualReimbursement.value;
});

// 格式化金额
const formatAmount = (amount?: number | null) => {
  if (amount === undefined || amount === null || isNaN(Number(amount))) {
    return '0.00';
  }
  return Number(amount).toFixed(2);
};

// 格式化百分比
const formatPercent = (ratio?: number | null) => {
  if (ratio === undefined || ratio === null || isNaN(Number(ratio))) {
    return '0.00%';
  }
  return (Number(ratio) * 100).toFixed(2) + '%';
};

// 获取医院等级颜色
const getHospitalLevelColor = (level?: string) => {
  const colorMap: Record<string, "primary" | "success" | "warning" | "info"> = {
    "三级甲等": "success",
    "三级乙等": "primary",
    "二级甲等": "warning",
    "二级乙等": "info"
  };
  return colorMap[level || ""] || "info";
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
    1: "warning",  // 待结算
    2: "success",  // 已结算
    3: "primary"   // 已支付
  };
  return colorMap[status] || "info";
};

// 获取状态文本
const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    1: "待结算",
    2: "已结算",
    3: "已支付"
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

// 获取结算列表
const fetchSettlementList = async () => {
  loading.value = true;
  try {
    const requestParams: SettlementQueryParams = {
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize,
      orderNo: searchForm.orderNo || undefined,
      patientName: searchForm.patientName || undefined,
      status: searchForm.status || undefined,
      hospitalLevel: searchForm.hospitalLevel || undefined,
      orderType: searchForm.orderType || undefined,
      startDate: (dateRange.value as [string, string])?.[0] || undefined,
      endDate: (dateRange.value as [string, string])?.[1] || undefined
    };

    const result = await getSettlementList(requestParams);
    settlementList.value = result.records || [];
    pagination.total = result.total || 0;
  } catch (error: any) {
    console.error("获取结算列表失败:", error);
    ElMessage.error(error?.message || "获取结算列表失败");
  } finally {
    loading.value = false;
  }
};

// 获取统计数据
const fetchStatistics = async () => {
  try {
         const stats = await getSettlementStatistics(
       (dateRange.value as [string, string])?.[0], 
       (dateRange.value as [string, string])?.[1]
     );
    Object.assign(statistics, stats);
  } catch (error: any) {
    console.error("获取统计数据失败:", error);
  }
};

// 搜索
const handleSearch = () => {
  pagination.currentPage = 1;
  fetchSettlementList();
  fetchStatistics();
};

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    orderNo: '',
    patientName: '',
    status: '',
    hospitalLevel: '',
    orderType: ''
  });
  dateRange.value = [];
  pagination.currentPage = 1;
  fetchSettlementList();
  fetchStatistics();
};

// 刷新
const handleRefresh = () => {
  fetchSettlementList();
  fetchStatistics();
};

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.pageSize = size;
  pagination.currentPage = 1;
  fetchSettlementList();
};

const handleCurrentChange = (current: number) => {
  pagination.currentPage = current;
  fetchSettlementList();
};

// 选择处理
const handleSelectionChange = (rows: Settlement[]) => {
  selectedRows.value = rows;
};

// 创建结算
const handleCreate = () => {
  router.push("/medical-order/list?action=create-settlement");
};

// 查看结算
const handleView = (row: Settlement) => {
  router.push(`/settlement/detail/${row.id}`);
};

// 结算处理
const handleSettle = async (row: Settlement) => {
  try {
    await ElMessageBox.confirm(
      `确定要对订单 "${row.orderNo}" 进行结算吗？`,
      "确认结算",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    await completeSettlement(row.id!);
    ElMessage.success("结算成功");
    fetchSettlementList();
    fetchStatistics();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "结算失败");
    }
  }
};

// 支付处理
const handlePayment = (row: Settlement) => {
  currentSettlement.value = row;
  paymentForm.paymentType = 'bank_transfer';
  paymentForm.bankAccount = '';
  paymentForm.remarks = '';
  paymentDialogVisible.value = true;
};

// 确认支付
const handlePaymentConfirm = async () => {
  if (!currentSettlement.value) return;
  
  paymentLoading.value = true;
  try {
    await simulatePayment(currentSettlement.value.id!, {
      paymentType: paymentForm.paymentType,
      bankAccount: paymentForm.bankAccount,
      remarks: paymentForm.remarks
    });
    
    ElMessage.success("支付成功");
    paymentDialogVisible.value = false;
    fetchSettlementList();
    fetchStatistics();
  } catch (error: any) {
    ElMessage.error(error?.message || "支付失败");
  } finally {
    paymentLoading.value = false;
  }
};

// 重新计算
const handleRecalculate = (row: Settlement) => {
  currentSettlement.value = row;
  recalculateForm.deductible = row.deductible || 0;
  recalculateForm.reimbursementRatio = row.reimbursementRatio || 0;
  recalculateDialogVisible.value = true;
};

// 确认重新计算
const handleRecalculateConfirm = async () => {
  if (!currentSettlement.value) return;
  
  recalculateLoading.value = true;
  try {
    await recalculateSettlement(currentSettlement.value.id!, {
      deductible: recalculateForm.deductible,
      reimbursementRatio: recalculateForm.reimbursementRatio
    });
    
    ElMessage.success("重新计算成功");
    recalculateDialogVisible.value = false;
    fetchSettlementList();
    fetchStatistics();
  } catch (error: any) {
    ElMessage.error(error?.message || "重新计算失败");
  } finally {
    recalculateLoading.value = false;
  }
};

// 取消结算
const handleCancel = async (row: Settlement) => {
  try {
    await ElMessageBox.confirm(
      `确定要取消订单 "${row.orderNo}" 的结算吗？`,
      "确认取消",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    // 这里可以调用取消结算的API
    ElMessage.success("结算已取消");
    fetchSettlementList();
    fetchStatistics();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "取消失败");
    }
  }
};

// 批量结算
const handleBatchSettle = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要结算的订单");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要批量结算选中的 ${selectedRows.value.length} 个订单吗？`,
      "确认批量结算",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    const orderIds = selectedRows.value.map(row => row.id!);
    await batchSettlement(orderIds);
    
    ElMessage.success("批量结算成功");
    selectedRows.value = [];
    fetchSettlementList();
    fetchStatistics();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量结算失败");
    }
  }
};

// 批量支付
const handleBatchPayment = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要支付的订单");
    return;
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要批量支付选中的 ${selectedRows.value.length} 个订单吗？总金额为 ¥${formatAmount(selectedTotalAmount.value)}`,
      "确认批量支付",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    // 模拟批量支付
    for (const row of selectedRows.value) {
      if (row.status === 2) {
        await simulatePayment(row.id!, {
          paymentType: 'batch_payment',
          remarks: '批量支付'
        });
      }
    }
    
    ElMessage.success("批量支付成功");
    selectedRows.value = [];
    fetchSettlementList();
    fetchStatistics();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "批量支付失败");
    }
  }
};

// 导出报表
const handleExport = async () => {
  try {
    const params: SettlementQueryParams = {
      pageNum: 1,
      pageSize: 10000, // 导出所有数据
      orderNo: searchForm.orderNo || undefined,
      patientName: searchForm.patientName || undefined,
      status: searchForm.status || undefined,
      hospitalLevel: searchForm.hospitalLevel || undefined,
      orderType: searchForm.orderType || undefined,
      startDate: (dateRange.value as [string, string])?.[0] || undefined,
      endDate: (dateRange.value as [string, string])?.[1] || undefined
    };
    
    await exportSettlementReport(params);
    ElMessage.success("报表导出成功");
  } catch (error: any) {
    ElMessage.error(error?.message || "导出失败");
  }
};

onMounted(() => {
  fetchSettlementList();
  fetchStatistics();
});
</script>

<style scoped>
.settlement-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  position: relative;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stats-number {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}

.stats-label {
  font-size: 14px;
  color: var(--el-text-color-regular);
}

.stats-icon {
  position: absolute;
  top: 50%;
  right: 20px;
  transform: translateY(-50%);
  font-size: 32px;
  opacity: 0.8;
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

.selected-info, .status-summary {
  color: #606266;
  font-size: 14px;
}

.status-summary .el-tag {
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

.reimbursable {
  color: #909399;
  font-weight: bold;
}

.reimbursement {
  color: #67c23a;
  font-weight: bold;
}

.self-pay {
  color: #f56c6c;
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

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.stats-card .el-card__body) {
  padding: 20px;
}
</style> 