<template>
  <div class="settlement-detail">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button type="info" plain @click="handleBack">
              <el-icon><ArrowLeft /></el-icon>
              返回列表
            </el-button>
            <span class="page-title">结算详情</span>
          </div>
          <div class="header-right">
            <el-tag :type="getStatusColor(settlementData.status)" size="large">
              {{ getStatusText(settlementData.status) }}
            </el-tag>
          </div>
        </div>
      </template>

      <div v-loading="loading" class="detail-content">
        <!-- 基本信息 -->
        <div class="info-section">
          <h3 class="section-title">
            <el-icon><Document /></el-icon>
            基本信息
          </h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-descriptions :column="1" border>
                <el-descriptions-item label="订单号">
                  <span class="order-no">{{ settlementData.orderNo || '-' }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="结算单号">
                  <span class="settlement-no">{{ settlementData.settlementNo || '-' }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="患者姓名">
                  <span class="patient-name">{{ settlementData.patientName || '-' }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="身份证号">
                  <span class="id-card">{{ maskIdCard(settlementData.patientIdCard) }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="医院名称">
                  {{ settlementData.hospitalName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="医院等级">
                  <el-tag :type="getHospitalLevelColor(settlementData.hospitalLevel)" size="small">
                    {{ settlementData.hospitalLevel || '-' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
            <el-col :span="12">
              <el-descriptions :column="1" border>
                <el-descriptions-item label="科室">
                  {{ settlementData.departmentName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="医生">
                  {{ settlementData.doctorName || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="订单类型">
                  <el-tag :type="getOrderTypeColor(settlementData.orderType)" size="small">
                    {{ settlementData.orderType || '-' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="就诊时间">
                  {{ formatDateTime(settlementData.visitTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="结算时间">
                  {{ formatDateTime(settlementData.settlementTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="诊断信息">
                  <el-text :truncated="true" style="max-width: 200px;">
                    {{ settlementData.diagnosis || '-' }}
                  </el-text>
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>
        </div>

        <!-- 费用明细 -->
        <div class="info-section">
          <h3 class="section-title">
            <el-icon><Money /></el-icon>
            费用明细
          </h3>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-card class="amount-card total-amount">
                <div class="amount-item">
                  <div class="amount-label">总费用</div>
                  <div class="amount-value total">¥{{ formatAmount(settlementData.totalAmount) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="amount-card reimbursable-amount">
                <div class="amount-item">
                  <div class="amount-label">可报销金额</div>
                  <div class="amount-value reimbursable">¥{{ formatAmount(settlementData.reimbursableAmount) }}</div>
                </div>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card class="amount-card actual-reimbursement">
                <div class="amount-item">
                  <div class="amount-label">实际报销</div>
                  <div class="amount-value reimbursement">¥{{ formatAmount(settlementData.actualReimbursement) }}</div>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-row :gutter="20" style="margin-top: 20px;">
            <el-col :span="12">
              <el-descriptions title="费用分类" :column="2" border>
                <el-descriptions-item label="药品费用">
                  ¥{{ formatAmount(settlementData.drugAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="诊疗费用">
                  ¥{{ formatAmount(settlementData.treatmentAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="服务费用">
                  ¥{{ formatAmount(settlementData.serviceAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="其他费用">
                  ¥{{ formatAmount(settlementData.otherAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="甲类药品">
                  ¥{{ formatAmount(settlementData.categoryAAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="乙类药品">
                  ¥{{ formatAmount(settlementData.categoryBAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="丙类药品">
                  ¥{{ formatAmount(settlementData.categoryCAmount) }}
                </el-descriptions-item>
                <el-descriptions-item label="自付金额">
                  <span class="self-pay">¥{{ formatAmount(settlementData.selfPayAmount) }}</span>
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
            <el-col :span="12">
              <el-descriptions title="报销参数" :column="1" border>
                <el-descriptions-item label="起付线">
                  ¥{{ formatAmount(settlementData.deductible) }}
                </el-descriptions-item>
                <el-descriptions-item label="报销比例">
                  {{ formatPercent(settlementData.reimbursementRatio) }}
                </el-descriptions-item>
                <el-descriptions-item label="报销计算">
                  <div class="calculation-formula">
                    <div>可报销 = 总费用 - 起付线</div>
                    <div>实际报销 = 可报销金额 × 报销比例</div>
                    <div>自付金额 = 总费用 - 实际报销</div>
                  </div>
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>
        </div>

        <!-- 结算状态与操作 -->
        <div class="info-section">
          <h3 class="section-title">
            <el-icon><Setting /></el-icon>
            结算状态与操作
          </h3>
          <div class="status-operations">
            <div class="status-info">
              <el-steps :active="getStatusStep()" direction="horizontal" finish-status="success">
                <el-step title="待结算" description="订单创建完成" />
                <el-step title="已结算" description="结算金额计算完成" />
                <el-step title="已支付" description="支付流程完成" />
              </el-steps>
            </div>
            <div class="operation-buttons">
              <el-button 
                v-if="settlementData.status === 1" 
                type="success" 
                @click="handleSettle"
                :loading="operationLoading"
              >
                <el-icon><Check /></el-icon>
                确认结算
              </el-button>
              <el-button 
                v-if="settlementData.status === 2" 
                type="warning" 
                @click="handlePayment"
                :loading="operationLoading"
              >
                <el-icon><CreditCard /></el-icon>
                模拟支付
              </el-button>
              <el-button 
                v-if="[1, 2].includes(settlementData.status)" 
                type="info" 
                @click="handleRecalculate"
              >
                                 <el-icon><Setting /></el-icon>
                重新计算
              </el-button>
              <el-button 
                type="primary" 
                @click="handleEdit"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button 
                type="danger" 
                @click="handleCancel"
              >
                <el-icon><Close /></el-icon>
                取消结算
              </el-button>
            </div>
          </div>
        </div>

        <!-- 审批信息 -->
        <div class="info-section" v-if="settlementData.approvalResult || settlementData.rejectReason">
          <h3 class="section-title">
            <el-icon><UserFilled /></el-icon>
            审批信息
          </h3>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="审批结果" v-if="settlementData.approvalResult">
              <el-tag :type="settlementData.approvalResult === '通过' ? 'success' : 'danger'">
                {{ settlementData.approvalResult }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="审批备注" v-if="settlementData.approvalRemark">
              {{ settlementData.approvalRemark }}
            </el-descriptions-item>
            <el-descriptions-item label="拒绝原因" v-if="settlementData.rejectReason">
              <el-text type="danger">{{ settlementData.rejectReason }}</el-text>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 支付记录 -->
        <div class="info-section" v-if="paymentRecords.length > 0">
          <h3 class="section-title">
            <el-icon><CreditCard /></el-icon>
            支付记录
          </h3>
          <el-table :data="paymentRecords" border>
            <el-table-column prop="paymentNo" label="支付单号" width="180" />
            <el-table-column prop="paymentType" label="支付方式" width="120">
              <template #default="{ row }">
                <el-tag size="small">{{ getPaymentTypeText(row.paymentType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="paymentAmount" label="支付金额" width="120">
              <template #default="{ row }">
                <span class="payment-amount">¥{{ formatAmount(row.paymentAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="paymentTime" label="支付时间" width="160">
              <template #default="{ row }">
                {{ formatDateTime(row.paymentTime) }}
              </template>
            </el-table-column>
            <el-table-column prop="paymentStatus" label="支付状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.paymentStatus === 'SUCCESS' ? 'success' : 'danger'" size="small">
                  {{ row.paymentStatus === 'SUCCESS' ? '成功' : '失败' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="transactionId" label="交易号" min-width="150" />
            <el-table-column prop="remarks" label="备注" min-width="150" />
          </el-table>
        </div>

        <!-- 操作日志 -->
        <div class="info-section">
          <h3 class="section-title">
            <el-icon><Clock /></el-icon>
            操作日志
          </h3>
          <el-timeline>
            <el-timeline-item
              v-for="log in auditLogs"
              :key="log.id"
              :timestamp="formatDateTime(log.operateTime)"
              :type="getLogType(log.action)"
            >
              <div class="log-content">
                <div class="log-action">{{ log.description }}</div>
                <div class="log-operator">操作人：{{ log.operator }}</div>
                <div class="log-details" v-if="log.details">{{ log.details }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>

        <!-- 操作记录 -->
        <div class="info-section">
          <h3 class="section-title">
            <el-icon><Calendar /></el-icon>
            时间记录
          </h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="创建时间">
              {{ formatDateTime(settlementData.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间">
              {{ formatDateTime(settlementData.updateTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="创建人">
              {{ settlementData.createBy || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="更新人">
              {{ settlementData.updateBy || '-' }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
    </el-card>

    <!-- 支付对话框 -->
    <el-dialog v-model="paymentDialogVisible" title="模拟支付" width="600px">
      <el-form :model="paymentForm" label-width="100px">
        <el-form-item label="结算单号">
          <el-input :value="settlementData.orderNo" readonly />
        </el-form-item>
        <el-form-item label="支付金额">
          <el-input :value="`¥${formatAmount(settlementData.actualReimbursement)}`" readonly />
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
          <el-input :value="`¥${formatAmount(settlementData.totalAmount)}`" readonly />
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
import { useRouter, useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { 
  ArrowLeft, Document, Money, Setting, Check, CreditCard, 
  Edit, Close, UserFilled, Clock, Calendar 
} from "@element-plus/icons-vue";
import { 
  getSettlementDetail, 
  simulatePayment,
  completeSettlement,
  recalculateSettlement,
  getPaymentRecords,
  getSettlementAuditLog,
  type Settlement 
} from "../../api/settlement";

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const operationLoading = ref(false);
const settlementData = ref<Settlement>({} as Settlement);
const paymentRecords = ref<any[]>([]);
const auditLogs = ref<any[]>([]);

// 支付相关
const paymentDialogVisible = ref(false);
const paymentLoading = ref(false);
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

// 预览计算结果
const previewReimbursableAmount = computed(() => {
  const reimbursable = (settlementData.value.totalAmount || 0) - recalculateForm.deductible;
  return Math.max(0, reimbursable);
});

const previewActualReimbursement = computed(() => {
  return previewReimbursableAmount.value * recalculateForm.reimbursementRatio;
});

const previewSelfPayAmount = computed(() => {
  return (settlementData.value.totalAmount || 0) - previewActualReimbursement.value;
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

// 掩码身份证号
const maskIdCard = (idCard?: string) => {
  if (!idCard) return '-';
  if (idCard.length < 10) return idCard;
  return idCard.substring(0, 6) + '****' + idCard.substring(idCard.length - 4);
};

// 获取状态颜色
const getStatusColor = (status?: number) => {
  const colorMap: Record<number, "primary" | "success" | "warning" | "danger" | "info"> = {
    1: "warning",  // 待结算
    2: "success",  // 已结算
    3: "primary"   // 已支付
  };
  return colorMap[status || 1] || "info";
};

// 获取状态文本
const getStatusText = (status?: number) => {
  const statusMap: Record<number, string> = {
    1: "待结算",
    2: "已结算",
    3: "已支付"
  };
  return statusMap[status || 1] || "未知";
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

// 获取状态步骤
const getStatusStep = () => {
  const statusStepMap: Record<number, number> = {
    1: 0,  // 待结算
    2: 1,  // 已结算
    3: 2   // 已支付
  };
  return statusStepMap[settlementData.value.status || 1] || 0;
};

// 获取支付方式文本
const getPaymentTypeText = (type: string) => {
  const typeMap: Record<string, string> = {
    'bank_transfer': '银行转账',
    'online_payment': '在线支付',
    'cash': '现金支付',
    'batch_payment': '批量支付'
  };
  return typeMap[type] || type;
};

// 获取日志类型
const getLogType = (action: string) => {
  const typeMap: Record<string, "primary" | "success" | "warning" | "danger" | "info"> = {
    'CREATE': 'primary',
    'CALCULATE': 'info',
    'APPROVE': 'success',
    'REJECT': 'danger',
    'PAYMENT': 'warning'
  };
  return typeMap[action] || 'primary';
};

// 获取结算详情
const fetchSettlementDetail = async () => {
  loading.value = true;
  try {
    const id = Number(route.params.id);
    if (!id) {
      ElMessage.error("结算ID无效");
      router.push("/settlement/list");
      return;
    }

    const detail = await getSettlementDetail(id);
    settlementData.value = detail;

    // 获取支付记录
    try {
      paymentRecords.value = await getPaymentRecords(id);
    } catch (error) {
      console.warn("获取支付记录失败:", error);
    }

    // 获取审计日志
    try {
      auditLogs.value = await getSettlementAuditLog(id);
    } catch (error) {
      console.warn("获取审计日志失败:", error);
    }
  } catch (error: any) {
    console.error("获取结算详情失败:", error);
    ElMessage.error(error?.message || "获取结算详情失败");
    router.push("/settlement/list");
  } finally {
    loading.value = false;
  }
};

// 返回列表
const handleBack = () => {
  router.push("/settlement/list");
};

// 结算处理
const handleSettle = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要对订单 "${settlementData.value.orderNo}" 进行结算吗？`,
      "确认结算",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    operationLoading.value = true;
    await completeSettlement(settlementData.value.id!);
    ElMessage.success("结算成功");
    fetchSettlementDetail();
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "结算失败");
    }
  } finally {
    operationLoading.value = false;
  }
};

// 支付处理
const handlePayment = () => {
  paymentForm.paymentType = 'bank_transfer';
  paymentForm.bankAccount = '';
  paymentForm.remarks = '';
  paymentDialogVisible.value = true;
};

// 确认支付
const handlePaymentConfirm = async () => {
  paymentLoading.value = true;
  try {
    await simulatePayment(settlementData.value.id!, {
      paymentType: paymentForm.paymentType,
      bankAccount: paymentForm.bankAccount,
      remarks: paymentForm.remarks
    });
    
    ElMessage.success("支付成功");
    paymentDialogVisible.value = false;
    fetchSettlementDetail();
  } catch (error: any) {
    ElMessage.error(error?.message || "支付失败");
  } finally {
    paymentLoading.value = false;
  }
};

// 重新计算
const handleRecalculate = () => {
  recalculateForm.deductible = settlementData.value.deductible || 0;
  recalculateForm.reimbursementRatio = settlementData.value.reimbursementRatio || 0;
  recalculateDialogVisible.value = true;
};

// 确认重新计算
const handleRecalculateConfirm = async () => {
  recalculateLoading.value = true;
  try {
    await recalculateSettlement(settlementData.value.id!, {
      deductible: recalculateForm.deductible,
      reimbursementRatio: recalculateForm.reimbursementRatio
    });
    
    ElMessage.success("重新计算成功");
    recalculateDialogVisible.value = false;
    fetchSettlementDetail();
  } catch (error: any) {
    ElMessage.error(error?.message || "重新计算失败");
  } finally {
    recalculateLoading.value = false;
  }
};

// 编辑
const handleEdit = () => {
  router.push(`/settlement/edit/${settlementData.value.id}`);
};

// 取消结算
const handleCancel = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要取消订单 "${settlementData.value.orderNo}" 的结算吗？`,
      "确认取消",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    );
    
    // 这里可以调用取消结算的API
    ElMessage.success("结算已取消");
    router.push("/settlement/list");
  } catch (error: any) {
    if (error !== "cancel") {
      ElMessage.error(error?.message || "取消失败");
    }
  }
};

onMounted(() => {
  fetchSettlementDetail();
});
</script>

<style scoped>
.settlement-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.detail-content {
  min-height: 400px;
}

.info-section {
  margin-bottom: 32px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 2px solid var(--el-border-color-light);
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.order-no, .settlement-no {
  font-family: 'Courier New', monospace;
  color: var(--el-color-primary);
  font-weight: bold;
}

.patient-name {
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.id-card {
  font-family: 'Courier New', monospace;
  color: var(--el-text-color-regular);
}

.amount-card {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.amount-item {
  text-align: center;
  padding: 8px;
}

.amount-label {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 8px;
}

.amount-value {
  font-size: 24px;
  font-weight: bold;
  font-family: 'Arial', sans-serif;
}

.amount-value.total {
  color: #e6a23c;
}

.amount-value.reimbursable {
  color: #909399;
}

.amount-value.reimbursement {
  color: #67c23a;
}

.self-pay {
  color: #f56c6c;
  font-weight: bold;
}

.calculation-formula {
  font-size: 13px;
  color: var(--el-text-color-regular);
  line-height: 1.6;
}

.calculation-formula div {
  margin-bottom: 4px;
}

.status-operations {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.status-info {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.operation-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.payment-amount {
  color: #67c23a;
  font-weight: bold;
}

.log-content {
  padding: 8px 0;
}

.log-action {
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.log-operator {
  font-size: 13px;
  color: var(--el-text-color-regular);
  margin-bottom: 4px;
}

.log-details {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

:deep(.el-descriptions__label) {
  font-weight: 600;
}

:deep(.el-card__body) {
  padding: 16px;
}

:deep(.amount-card .el-card__body) {
  padding: 20px;
}

:deep(.el-tag) {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

:deep(.el-steps) {
  margin: 20px 0;
}

:deep(.el-timeline-item__content) {
  padding-bottom: 16px;
}
</style> 