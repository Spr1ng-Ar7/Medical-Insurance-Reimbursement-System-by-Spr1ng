<template>
  <div class="medical-order-detail">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>医疗订单详情</span>
          <div class="header-actions">
            <el-button v-if="[1, 4].includes(orderDetail.status)" type="primary" @click="handleEdit">
              编辑
            </el-button>
            <el-button @click="handleBack">返回</el-button>
          </div>
        </div>
      </template>
      
      <div v-loading="loading" class="detail-content">
        <!-- 基本信息 -->
        <el-descriptions title="基本信息" :column="2" border size="large">
          <el-descriptions-item label="订单编号">
            <el-tag type="primary" size="large">{{ orderDetail.orderNo || '-' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="订单类型">
            <el-tag :type="getOrderTypeColor(orderDetail.orderType)">
              <el-icon style="margin-right: 4px;">
                <User v-if="orderDetail.orderType === '门诊'" />
                <House v-if="orderDetail.orderType === '住院'" />
                <Warning v-if="orderDetail.orderType === '急诊'" />
                <Document v-else />
              </el-icon>
              {{ orderDetail.orderType || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="患者姓名">
            {{ orderDetail.patientName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="患者身份证号">
            {{ formatIdCard(orderDetail.patientIdCard) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="医院名称">
            {{ orderDetail.hospitalName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="医院等级">
            {{ orderDetail.hospitalLevel || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="科室">
            {{ orderDetail.departmentName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="主治医生">
            {{ orderDetail.doctorName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="就诊时间">
            {{ formatDateTime(orderDetail.visitTime) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusColor(orderDetail.status)">
              {{ getStatusText(orderDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(orderDetail.createTime) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(orderDetail.updateTime) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="诊断结果" :span="2">
            {{ orderDetail.diagnosis || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2" v-if="orderDetail.remark">
            {{ orderDetail.remark }}
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 费用信息 -->
        <el-descriptions title="费用信息" :column="3" border size="large" style="margin-top: 20px;">
          <el-descriptions-item label="总费用">
            <span class="amount-large">¥{{ formatAmount(orderDetail.totalAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="药品费用">
            <span class="amount">¥{{ formatAmount(orderDetail.drugAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="诊疗费用">
            <span class="amount">¥{{ formatAmount(orderDetail.treatmentAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="服务设施费用">
            <span class="amount">¥{{ formatAmount(orderDetail.serviceAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="其他费用">
            <span class="amount">¥{{ formatAmount(orderDetail.otherAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="甲类药品费用">
            <span class="amount">¥{{ formatAmount(orderDetail.categoryAAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="乙类药品费用">
            <span class="amount">¥{{ formatAmount(orderDetail.categoryBAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="丙类药品费用">
            <span class="amount">¥{{ formatAmount(orderDetail.categoryCAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="住院天数" v-if="orderDetail.orderType === '住院'">
            {{ orderDetail.stayDays || 0 }} 天
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 结算信息 -->
        <el-descriptions 
          title="结算信息" 
          :column="2" 
          border 
          size="large" 
          style="margin-top: 20px;"
          v-if="orderDetail.status >= 2"
        >
          <el-descriptions-item label="结算单号">
            {{ orderDetail.settlementNo || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="结算时间">
            {{ formatDateTime(orderDetail.settlementTime) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="起付线">
            <span class="amount">¥{{ formatAmount(orderDetail.deductible) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="报销比例">
            {{ formatPercent(orderDetail.reimbursementRatio) }}
          </el-descriptions-item>
          <el-descriptions-item label="可报销金额">
            <span class="amount">¥{{ formatAmount(orderDetail.reimbursableAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="实际报销金额">
            <span class="reimbursement-large">¥{{ formatAmount(orderDetail.actualReimbursement) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="自费金额">
            <span class="self-pay">¥{{ formatAmount(orderDetail.selfPayAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="审批结果" v-if="orderDetail.approvalResult">
            {{ orderDetail.approvalResult }}
          </el-descriptions-item>
          <el-descriptions-item label="审批备注" :span="2" v-if="orderDetail.approvalRemark">
            {{ orderDetail.approvalRemark }}
          </el-descriptions-item>
          <el-descriptions-item label="拒绝原因" :span="2" v-if="orderDetail.rejectReason">
            <el-alert type="error" :title="orderDetail.rejectReason" :closable="false" />
          </el-descriptions-item>
        </el-descriptions>
        
        <!-- 操作按钮 -->
        <div class="action-buttons" style="margin-top: 30px;">
          <el-button 
            v-if="orderDetail.status === 4" 
            type="success" 
            @click="handleApprove"
          >
            审核订单
          </el-button>
          <el-button 
            v-if="orderDetail.status === 1" 
            type="primary" 
            @click="handleSettlement"
          >
            提交结算
          </el-button>
          <el-button 
            v-if="[1, 4].includes(orderDetail.status)" 
            type="warning" 
            @click="handleCancel"
          >
            取消订单
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { User, House, Warning, Document } from "@element-plus/icons-vue";
import { getMedicalOrderDetail, updateOrderStatus, type MedicalOrder } from "../../api/medical-order";

const router = useRouter();
const route = useRoute();
const loading = ref(false);

// 订单详情数据
const orderDetail = reactive<MedicalOrder>({
  id: undefined,
  orderNo: "",
  patientName: "",
  patientIdCard: "",
  orderType: "",
  hospitalName: "",
  hospitalLevel: "",
  departmentName: "",
  doctorName: "",
  diagnosis: "",
  visitTime: "",
  dischargeTime: "",
  stayDays: 0,
  totalAmount: 0,
  drugAmount: 0,
  treatmentAmount: 0,
  serviceAmount: 0,
  otherAmount: 0,
  categoryAAmount: 0,
  categoryBAmount: 0,
  categoryCAmount: 0,
  deductible: 0,
  reimbursementRatio: 0,
  reimbursableAmount: 0,
  actualReimbursement: 0,
  selfPayAmount: 0,
  settlementNo: "",
  settlementTime: "",
  approvalResult: "",
  approvalRemark: "",
  rejectReason: "",
  status: 1,
  createTime: "",
  updateTime: "",
  remark: ""
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

// 格式化身份证号
const formatIdCard = (idCard?: string) => {
  if (!idCard) return "";
  if (idCard.length === 18) {
    return idCard.replace(/(\d{6})(\d{8})(\d{4})/, "$1********$3");
  }
  return idCard;
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

// 获取订单详情
const fetchOrderDetail = async (id: string) => {
  loading.value = true;
  try {
    const data = await getMedicalOrderDetail(Number(id));
    console.log('处理后的详情数据:', data);
    
    // 直接使用API处理后的数据
    Object.assign(orderDetail, data);
  } catch (error: any) {
    console.error('获取订单详情失败:', error);
    ElMessage.error(error?.message || "获取订单详情失败");
    router.back();
  } finally {
    loading.value = false;
  }
};

// 编辑
const handleEdit = () => {
  router.push(`/medical-order/edit/${route.params.id}`);
};

// 返回
const handleBack = () => {
  router.back();
};

// 审核订单
const handleApprove = async () => {
  try {
    const { action, value } = await ElMessageBox.prompt(
      '请输入审核意见',
      '审核订单',
      {
        confirmButtonText: '通过',
        cancelButtonText: '拒绝',
        distinguishCancelAndClose: true,
        inputType: 'textarea',
        inputPlaceholder: '请输入审核意见',
      }
    );
    
    const newStatus = action === 'confirm' ? '2' : '5';
    await updateOrderStatus(orderDetail.id!, newStatus);
    
    ElMessage.success(action === 'confirm' ? '审核通过' : '审核拒绝');
    fetchOrderDetail(route.params.id as string);
  } catch (error: any) {
    if (error !== 'cancel' && error !== 'close') {
      ElMessage.error(error?.message || '审核失败');
    }
  }
};

// 提交结算
const handleSettlement = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要提交结算申请吗？',
      '提交结算',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    await updateOrderStatus(orderDetail.id!, '2');
    ElMessage.success('结算申请提交成功');
    fetchOrderDetail(route.params.id as string);
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error?.message || '提交失败');
    }
  }
};

// 取消订单
const handleCancel = async () => {
  try {
    const { value } = await ElMessageBox.prompt(
      '请输入取消原因',
      '取消订单',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入取消原因',
        inputValidator: (value) => {
          if (!value || value.trim() === '') {
            return '取消原因不能为空';
          }
          return true;
        }
      }
    );
    
    await updateOrderStatus(orderDetail.id!, '0');
    ElMessage.success('订单已取消');
    fetchOrderDetail(route.params.id as string);
  } catch (error: any) {
    if (error !== 'cancel') {
      ElMessage.error(error?.message || '取消失败');
    }
  }
};

onMounted(() => {
  const id = route.params.id as string;
  if (!id) {
    ElMessage.error("订单ID无效");
    router.back();
    return;
  }
  
  fetchOrderDetail(id);
});
</script>

<style scoped>
.medical-order-detail {
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

.detail-content {
  padding: 20px 0;
}

.action-buttons {
  text-align: center;
}

.amount {
  color: #e6a23c;
  font-weight: bold;
}

.amount-large {
  color: #e6a23c;
  font-weight: bold;
  font-size: 18px;
}

.reimbursement-large {
  color: #67c23a;
  font-weight: bold;
  font-size: 18px;
}

.self-pay {
  color: #f56c6c;
  font-weight: bold;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: var(--el-text-color-primary);
}

:deep(.el-descriptions__content) {
  color: var(--el-text-color-regular);
}

:deep(.el-tag) {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

:deep(.el-descriptions__title) {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 16px;
}
</style> 