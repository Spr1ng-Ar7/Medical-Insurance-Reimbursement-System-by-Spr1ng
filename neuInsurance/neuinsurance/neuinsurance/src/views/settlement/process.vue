<template>
  <div class="settlement-process">
    <!-- 处理统计 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.pending }}</div>
            <div class="stat-label">待处理</div>
          </div>
          <div class="stat-icon pending">
            <el-icon><Clock /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.processing }}</div>
            <div class="stat-label">处理中</div>
          </div>
          <div class="stat-icon processing">
            <el-icon><Setting /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.completed }}</div>
            <div class="stat-label">已完成</div>
          </div>
          <div class="stat-icon completed">
            <el-icon><SuccessFilled /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-number">{{ statistics.failed }}</div>
            <div class="stat-label">处理失败</div>
          </div>
          <div class="stat-icon failed">
            <el-icon><CircleCloseFilled /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>结算处理工作台</span>
          <div class="header-actions">
            <el-button 
              type="primary" 
              @click="batchDialogVisible = true"
              :disabled="selectedRows.length === 0"
            >
              <el-icon><Operation /></el-icon>
              批量处理
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 搜索条件 -->
      <div class="search-form">
        <el-form :model="searchParams" inline>
          <el-form-item label="患者姓名">
            <el-input
              v-model="searchParams.patientName"
              placeholder="请输入患者姓名"
              clearable
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="订单编号">
            <el-input
              v-model="searchParams.orderNo"
              placeholder="请输入订单编号"
              clearable
              style="width: 180px"
            />
          </el-form-item>
          <el-form-item label="医院等级">
            <el-select v-model="searchParams.hospitalLevel" placeholder="请选择医院等级" clearable style="width: 150px">
              <el-option label="三甲" value="三甲" />
              <el-option label="三乙" value="三乙" />
              <el-option label="二甲" value="二甲" />
              <el-option label="二乙" value="二乙" />
              <el-option label="一甲" value="一甲" />
              <el-option label="一乙" value="一乙" />
            </el-select>
          </el-form-item>
          <el-form-item label="处理状态">
            <el-select v-model="searchParams.status" placeholder="请选择状态" clearable style="width: 150px">
              <el-option label="待审核" value="PENDING_REVIEW" />
              <el-option label="审核通过" value="APPROVED" />
              <el-option label="审核拒绝" value="REJECTED" />
              <el-option label="待支付" value="PENDING_PAYMENT" />
              <el-option label="支付完成" value="PAID" />
              <el-option label="处理失败" value="FAILED" />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="searchParams.priority" placeholder="请选择优先级" clearable style="width: 120px">
              <el-option label="高" value="high" />
              <el-option label="中" value="medium" />
              <el-option label="低" value="low" />
            </el-select>
          </el-form-item>
          <el-form-item label="日期范围">
            <el-date-picker
              v-model="searchParams.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 240px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 快速筛选 -->
      <div class="quick-filters">
        <el-radio-group v-model="quickFilter" @change="handleQuickFilter">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="PENDING_REVIEW">待审核</el-radio-button>
          <el-radio-button label="APPROVED">审核通过</el-radio-button>
          <el-radio-button label="PENDING_PAYMENT">待支付</el-radio-button>
          <el-radio-button label="URGENT">紧急处理</el-radio-button>
        </el-radio-group>
      </div>
      
      <!-- 结算处理列表 -->
      <el-table
        v-loading="loading"
        :data="processList"
        :row-key="'id'"
        stripe
        border
        @selection-change="handleSelectionChange"
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="55" align="center" />
        
        <el-table-column prop="orderNo" label="订单编号" width="150" fixed="left">
          <template #default="{ row }">
            <el-button type="primary" link @click.stop="handleView(row)">
              {{ row.orderNo }}
            </el-button>
          </template>
        </el-table-column>
        
        <el-table-column prop="patientName" label="患者姓名" width="100" />
        
        <el-table-column prop="hospitalLevel" label="医院等级" width="80" />
        
        <el-table-column prop="totalAmount" label="总金额" width="110" align="right">
          <template #default="{ row }">
            <span class="amount-text">¥{{ (row.totalAmount || 0).toFixed(2) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="reimbursementAmount" label="报销金额" width="110" align="right">
          <template #default="{ row }">
            <span class="amount-text reimbursement">¥{{ (row.reimbursementAmount || 0).toFixed(2) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="selfPayAmount" label="自费金额" width="110" align="right">
          <template #default="{ row }">
            <span class="amount-text self-pay">¥{{ (row.selfPayAmount || 0).toFixed(2) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="priority" label="优先级" width="80" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="getPriorityTag(row.priority)" 
              size="small"
              :class="{ 'priority-urgent': row.priority === 'high' }"
            >
              {{ getPriorityText(row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="处理状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)" size="small">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="processingTime" label="处理时间" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.processingTime" class="processing-time">
              {{ getProcessingDuration(row.processingTime) }}
            </span>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>

        <el-table-column prop="processor" label="处理人" width="100" />
        
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link size="small" @click.stop="handleView(row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              
              <el-button 
                v-if="canReview(row.status)"
                type="success" 
                link 
                size="small"
                @click.stop="handleReview(row)"
              >
                <el-icon><Check /></el-icon>
                审核
              </el-button>
              
              <el-button 
                v-if="canProcess(row.status)"
                type="warning" 
                link 
                size="small"
                @click.stop="handleProcess(row)"
              >
                <el-icon><CreditCard /></el-icon>
                支付
              </el-button>
              
              <el-button 
                v-if="canRetry(row.status)"
                type="info" 
                link 
                size="small"
                @click.stop="handleRetry(row)"
              >
                <el-icon><Refresh /></el-icon>
                重试
              </el-button>

              <el-dropdown @command="(command) => handleMoreAction(command, row)" trigger="click">
                <el-button type="primary" link size="small">
                  更多 <el-icon><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="history">
                      <el-icon><Document /></el-icon>
                      处理历史
                    </el-dropdown-item>
                    <el-dropdown-item command="priority" :disabled="!canChangePriority(row.status)">
                      <el-icon><Flag /></el-icon>
                      调整优先级
                    </el-dropdown-item>
                    <el-dropdown-item command="assign" :disabled="!canAssign(row.status)">
                      <el-icon><User /></el-icon>
                      分配处理人
                    </el-dropdown-item>
                    <el-dropdown-item command="cancel" :disabled="!canCancel(row.status)" divided>
                      <el-icon><Close /></el-icon>
                      取消处理
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-wrapper">
        <div class="selection-info">
          <span v-if="selectedRows.length > 0">
            已选择 <strong>{{ selectedRows.length }}</strong> 项，
            总金额：<strong class="amount-text">¥{{ selectedTotalAmount.toFixed(2) }}</strong>
          </span>
        </div>
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
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      title="结算审核"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="currentOrder" class="review-content">
        <!-- 基本信息 -->
        <el-descriptions title="订单基本信息" :column="2" border>
          <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="患者姓名">{{ currentOrder.patientName }}</el-descriptions-item>
          <el-descriptions-item label="医院等级">{{ currentOrder.hospitalLevel }}</el-descriptions-item>
          <el-descriptions-item label="总金额">
            <span class="amount-text">¥{{ (currentOrder.totalAmount || 0).toFixed(2) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="报销金额">
            <span class="amount-text reimbursement">¥{{ (currentOrder.reimbursementAmount || 0).toFixed(2) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="自费金额">
            <span class="amount-text self-pay">¥{{ (currentOrder.selfPayAmount || 0).toFixed(2) }}</span>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 审核表单 -->
        <el-form :model="reviewForm" :rules="reviewRules" ref="reviewFormRef" label-width="120px" style="margin-top: 20px">
          <el-form-item label="审核结果" prop="reviewResult">
            <el-radio-group v-model="reviewForm.reviewResult">
              <el-radio label="APPROVED">
                <el-icon style="color: #67c23a"><CircleCheckFilled /></el-icon>
                审核通过
              </el-radio>
              <el-radio label="REJECTED">
                <el-icon style="color: #f56c6c"><CircleCloseFilled /></el-icon>
                审核拒绝
              </el-radio>
              <el-radio label="RETURNED">
                <el-icon style="color: #e6a23c"><WarningFilled /></el-icon>
                退回修改
              </el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item 
            v-if="reviewForm.reviewResult === 'APPROVED'" 
            label="支付方式" 
            prop="paymentMethod"
          >
            <el-select v-model="reviewForm.paymentMethod" placeholder="请选择支付方式">
              <el-option label="医保卡支付" value="INSURANCE_CARD" />
              <el-option label="银行转账" value="BANK_TRANSFER" />
              <el-option label="现金支付" value="CASH" />
              <el-option label="第三方支付" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>

          <el-form-item 
            v-if="reviewForm.reviewResult === 'APPROVED'" 
            label="预计到账时间"
          >
            <el-date-picker
              v-model="reviewForm.expectedPaymentTime"
              type="datetime"
              placeholder="请选择预计到账时间"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
          
          <el-form-item label="审核意见" prop="reviewComment">
            <el-input
              v-model="reviewForm.reviewComment"
              type="textarea"
              :rows="4"
              placeholder="请输入审核意见"
              :class="{ 'required-field': reviewForm.reviewResult !== 'APPROVED' }"
            />
          </el-form-item>

          <el-form-item v-if="reviewForm.reviewResult === 'REJECTED'" label="拒绝原因" prop="rejectReason">
            <el-checkbox-group v-model="reviewForm.rejectReasons">
              <el-checkbox label="材料不全">材料不全</el-checkbox>
              <el-checkbox label="金额异常">金额异常</el-checkbox>
              <el-checkbox label="政策不符">政策不符</el-checkbox>
              <el-checkbox label="信息错误">信息错误</el-checkbox>
              <el-checkbox label="其他原因">其他原因</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reviewDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirmReview" :loading="reviewLoading">
            确认审核
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 支付处理对话框 -->
    <el-dialog
      v-model="paymentDialogVisible"
      title="支付处理"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-if="currentOrder" class="payment-content">
        <el-alert
          title="请确认支付信息"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom: 20px"
        />

        <el-descriptions :column="2" border>
          <el-descriptions-item label="患者姓名">{{ currentOrder.patientName }}</el-descriptions-item>
          <el-descriptions-item label="订单编号">{{ currentOrder.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="支付金额">
            <span class="amount-text large">¥{{ (currentOrder.reimbursementAmount || 0).toFixed(2) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="支付方式">{{ getPaymentMethodText(currentOrder.paymentMethod) }}</el-descriptions-item>
        </el-descriptions>

        <el-form :model="paymentForm" :rules="paymentRules" ref="paymentFormRef" label-width="120px" style="margin-top: 20px">
          <el-form-item label="交易流水号" prop="transactionId">
            <el-input
              v-model="paymentForm.transactionId"
              placeholder="请输入交易流水号"
              :disabled="paymentForm.autoGenerate"
            />
            <el-checkbox v-model="paymentForm.autoGenerate" style="margin-top: 8px">
              自动生成交易流水号
            </el-checkbox>
          </el-form-item>
          
          <el-form-item label="支付备注">
            <el-input
              v-model="paymentForm.paymentNote"
              type="textarea"
              :rows="3"
              placeholder="请输入支付备注（可选）"
            />
          </el-form-item>

          <el-form-item label="风险检查">
            <el-checkbox-group v-model="paymentForm.riskChecks">
              <el-checkbox label="identity">身份验证</el-checkbox>
              <el-checkbox label="amount">金额核对</el-checkbox>
              <el-checkbox label="duplicate">重复支付检查</el-checkbox>
              <el-checkbox label="fraud">反欺诈检查</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="paymentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirmPayment" :loading="paymentLoading">
            确认支付
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 批量处理对话框 -->
    <el-dialog
      v-model="batchDialogVisible"
      title="批量处理"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="batch-content">
        <el-alert
          :title="`已选择 ${selectedRows.length} 个订单进行批量处理`"
          type="info"
          show-icon
          :closable="false"
          style="margin-bottom: 20px"
        />

        <el-form :model="batchForm" label-width="120px">
          <el-form-item label="处理类型">
            <el-radio-group v-model="batchForm.batchType">
              <el-radio label="REVIEW">批量审核</el-radio>
              <el-radio label="PAYMENT">批量支付</el-radio>
              <el-radio label="PRIORITY">调整优先级</el-radio>
              <el-radio label="ASSIGN">分配处理人</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 批量审核 -->
          <template v-if="batchForm.batchType === 'REVIEW'">
            <el-form-item label="审核结果">
              <el-radio-group v-model="batchForm.reviewResult">
                <el-radio label="APPROVED">审核通过</el-radio>
                <el-radio label="REJECTED">审核拒绝</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="统一意见">
              <el-input
                v-model="batchForm.comment"
                type="textarea"
                :rows="3"
                placeholder="请输入批量处理意见"
              />
            </el-form-item>
          </template>

          <!-- 批量支付 -->
          <template v-if="batchForm.batchType === 'PAYMENT'">
            <el-form-item label="支付方式">
              <el-select v-model="batchForm.paymentMethod" placeholder="请选择支付方式">
                <el-option label="医保卡支付" value="INSURANCE_CARD" />
                <el-option label="银行转账" value="BANK_TRANSFER" />
                <el-option label="现金支付" value="CASH" />
              </el-select>
            </el-form-item>
            <el-form-item label="预计到账">
              <el-date-picker
                v-model="batchForm.expectedPaymentTime"
                type="datetime"
                placeholder="请选择预计到账时间"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DD HH:mm:ss"
              />
            </el-form-item>
          </template>

          <!-- 调整优先级 -->
          <template v-if="batchForm.batchType === 'PRIORITY'">
            <el-form-item label="新优先级">
              <el-radio-group v-model="batchForm.priority">
                <el-radio label="high">高</el-radio>
                <el-radio label="medium">中</el-radio>
                <el-radio label="low">低</el-radio>
              </el-radio-group>
            </el-form-item>
          </template>

          <!-- 分配处理人 -->
          <template v-if="batchForm.batchType === 'ASSIGN'">
            <el-form-item label="处理人">
              <el-select v-model="batchForm.processor" placeholder="请选择处理人">
                <el-option label="张三" value="zhangsan" />
                <el-option label="李四" value="lisi" />
                <el-option label="王五" value="wangwu" />
              </el-select>
            </el-form-item>
          </template>

          <el-form-item label="处理说明">
            <el-input
              v-model="batchForm.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入处理说明（可选）"
            />
          </el-form-item>
        </el-form>
      </div>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="batchDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirmBatch" :loading="batchLoading">
            确认处理
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 处理历史对话框 -->
    <el-dialog
      v-model="historyDialogVisible"
      title="处理历史"
      width="800px"
    >
      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in processingHistory"
          :key="index"
          :timestamp="formatDateTime(item.timestamp)"
          :type="getHistoryType(item.action)"
        >
          <el-card>
            <h4>{{ item.action }}</h4>
            <p>处理人：{{ item.processor }}</p>
            <p v-if="item.comment">备注：{{ item.comment }}</p>
            <p v-if="item.result">结果：{{ item.result }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { 
  Search, Refresh, View, Setting, Clock, SuccessFilled, CircleCloseFilled,
  Operation, Check, CreditCard, ArrowDown, Document, Flag, User, Close,
  CircleCheckFilled, WarningFilled
} from "@element-plus/icons-vue";
import { 
  getSettlementList, 
  approveSettlement, 
  rejectSettlement, 
  simulatePayment
} from "../../api/settlement";

// 类型定义
interface Settlement {
  id: number;
  orderNumber: string;
  patientName: string;
  hospitalLevel?: string;
  totalAmount: number;
  reimbursableAmount: number;
  status: string;
  priority?: string;
  processor?: string;
  processingTime?: string;
  paymentMethod?: string;
  createTime: string;
}

interface SettlementQueryParams {
  pageNum?: number;
  pageSize?: number;
  patientName?: string;
  orderNumber?: string;
  status?: string;
  hospitalLevel?: string;
  priority?: string;
  startDate?: string;
  endDate?: string;
}

const router = useRouter();

// 数据
const loading = ref(false);
const processList = ref([]);
const selectedRows = ref([]);
const currentOrder = ref(null);
const quickFilter = ref("");
const reviewFormRef = ref();
const paymentFormRef = ref();

// 统计数据
const statistics = reactive({
  pending: 0,
  processing: 0,
  completed: 0,
  failed: 0
});

// 搜索参数
const searchParams = reactive({
  pageNum: 1,
  pageSize: 10,
  patientName: "",
  orderNo: "",
  status: undefined,
  hospitalLevel: "",
  priority: "",
  dateRange: []
});

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 审核对话框相关
const reviewDialogVisible = ref(false);
const reviewLoading = ref(false);
const reviewForm = reactive({
  reviewResult: "APPROVED",
  paymentMethod: "",
  expectedPaymentTime: "",
  reviewComment: "",
  rejectReasons: []
});

const reviewRules = {
  reviewResult: [{ required: true, message: "请选择审核结果", trigger: "change" }],
  paymentMethod: [{ required: true, message: "请选择支付方式", trigger: "change" }],
  reviewComment: [
    { 
      required: true, 
      validator: (rule, value, callback) => {
        if (reviewForm.reviewResult !== 'APPROVED' && !value) {
          callback(new Error('审核拒绝或退回时必须填写审核意见'));
        } else {
          callback();
        }
      }, 
      trigger: "blur" 
    }
  ]
};

// 支付对话框相关
const paymentDialogVisible = ref(false);
const paymentLoading = ref(false);
const paymentForm = reactive({
  transactionId: "",
  autoGenerate: true,
  paymentNote: "",
  riskChecks: ["identity", "amount", "duplicate"]
});

const paymentRules = {
  transactionId: [
    { 
      required: true, 
      validator: (rule, value, callback) => {
        if (!paymentForm.autoGenerate && !value) {
          callback(new Error('请输入交易流水号或选择自动生成'));
        } else {
          callback();
        }
      }, 
      trigger: "blur" 
    }
  ]
};

// 批量处理相关
const batchDialogVisible = ref(false);
const batchLoading = ref(false);
const batchForm = reactive({
  batchType: "REVIEW",
  reviewResult: "APPROVED",
  paymentMethod: "",
  expectedPaymentTime: "",
  priority: "medium",
  processor: "",
  comment: "",
  remark: ""
});

// 处理历史
const historyDialogVisible = ref(false);
const processingHistory = ref([]);

// 计算属性
const selectedTotalAmount = computed(() => {
  return selectedRows.value.reduce((total, row) => total + (row.reimbursementAmount || 0), 0);
});

// 获取处理列表
const fetchProcessList = async (params?: SettlementQueryParams) => {
  loading.value = true;
  try {
    const queryParams = {
      pageNum: searchParams.pageNum,
      pageSize: searchParams.pageSize,
      patientName: searchParams.patientName,
      orderNumber: searchParams.orderNo,
      status: searchParams.status,
      hospitalLevel: searchParams.hospitalLevel,
      priority: searchParams.priority,
      startDate: searchParams.dateRange?.[0],
      endDate: searchParams.dateRange?.[1],
      ...params
    };

    const result = await getSettlementList(queryParams);
    if (!result || !result.records) {
      throw new Error('Invalid response format');
    }

    // 转换数据格式并添加模拟的处理字段
    processList.value = result.records.map((item: Settlement) => ({
      id: item.id,
      orderNo: item.orderNumber,
      patientName: item.patientName,
      hospitalLevel: item.hospitalLevel || "二甲",
      totalAmount: item.totalAmount,
      reimbursementAmount: item.reimbursableAmount,
      selfPayAmount: item.totalAmount - item.reimbursableAmount,
      status: item.status,
      priority: item.priority || "medium",
      processor: item.processor || "系统分配",
      processingTime: item.processingTime,
      paymentMethod: item.paymentMethod,
      createTime: item.createTime
    }));

    pagination.total = result.total;
    pagination.current = result.current;
    pagination.size = result.size;

    // 更新统计数据
    updateStatistics();
  } catch (error) {
    console.error('Error fetching process list:', error);
    ElMessage.error("获取处理列表失败：" + (error instanceof Error ? error.message : '未知错误'));
  } finally {
    loading.value = false;
  }
};

// 更新统计数据
const updateStatistics = () => {
  const stats = processList.value.reduce((acc, item) => {
    switch (item.status) {
      case 'PENDING_REVIEW':
        acc.pending++;
        break;
      case 'APPROVED':
      case 'PENDING_PAYMENT':
        acc.processing++;
        break;
      case 'PAID':
        acc.completed++;
        break;
      case 'REJECTED':
      case 'FAILED':
        acc.failed++;
        break;
    }
    return acc;
  }, { pending: 0, processing: 0, completed: 0, failed: 0 });

  Object.assign(statistics, stats);
};

// 搜索
const handleSearch = () => {
  searchParams.pageNum = 1;
  fetchProcessList();
};

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    if (key !== 'pageNum' && key !== 'pageSize') {
      (searchParams as any)[key] = key === 'dateRange' ? [] : "";
    }
  });
  searchParams.pageNum = 1;
  quickFilter.value = "";
  fetchProcessList();
};

// 快速筛选
const handleQuickFilter = (value: string) => {
  if (value === "URGENT") {
    searchParams.priority = "high";
    searchParams.status = "";
  } else {
    searchParams.status = value;
    searchParams.priority = "";
  }
  searchParams.pageNum = 1;
  fetchProcessList();
};

// 刷新
const handleRefresh = () => {
  fetchProcessList();
  ElMessage.success("数据已刷新");
};

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection;
};

// 行点击
const handleRowClick = (row: any) => {
  // 可以在这里添加行点击逻辑
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size;
  searchParams.pageNum = 1;
  fetchProcessList();
};

// 当前页变化
const handleCurrentChange = (current: number) => {
  searchParams.pageNum = current;
  fetchProcessList();
};

// 查看
const handleView = (row: any) => {
  if (!row.id) {
    ElMessage.error("结算ID不存在");
    return;
  }
  router.push(`/settlement/detail/${row.id}`);
};

// 审核
const handleReview = (row: any) => {
  currentOrder.value = row;
  reviewForm.reviewResult = "APPROVED";
  reviewForm.paymentMethod = "";
  reviewForm.expectedPaymentTime = "";
  reviewForm.reviewComment = "";
  reviewForm.rejectReasons = [];
  reviewDialogVisible.value = true;
};

// 确认审核
const handleConfirmReview = async () => {
  if (!reviewFormRef.value) return;
  
  try {
    await reviewFormRef.value.validate();
    reviewLoading.value = true;

    if (reviewForm.reviewResult === "APPROVED") {
      await approveSettlement(currentOrder.value.id, "APPROVED", reviewForm.reviewComment);
      ElMessage.success("审核通过成功");
    } else {
      const rejectReason = reviewForm.rejectReasons.length > 0 
        ? reviewForm.rejectReasons.join(", ") + (reviewForm.reviewComment ? `: ${reviewForm.reviewComment}` : "")
        : reviewForm.reviewComment || "审核不通过";
      await rejectSettlement(currentOrder.value.id, rejectReason);
      ElMessage.success("审核处理完成");
    }

    reviewDialogVisible.value = false;
    fetchProcessList();
  } catch (error) {
    console.error('Review error:', error);
    ElMessage.error("审核处理失败");
  } finally {
    reviewLoading.value = false;
  }
};

// 支付处理
const handleProcess = (row: any) => {
  currentOrder.value = row;
  paymentForm.transactionId = "";
  paymentForm.autoGenerate = true;
  paymentForm.paymentNote = "";
  paymentForm.riskChecks = ["identity", "amount", "duplicate"];
  paymentDialogVisible.value = true;
};

// 确认支付
const handleConfirmPayment = async () => {
  if (!paymentFormRef.value) return;
  
  try {
    await paymentFormRef.value.validate();
    paymentLoading.value = true;

    const transactionId = paymentForm.autoGenerate 
      ? `TXN${Date.now()}${Math.random().toString(36).substr(2, 6).toUpperCase()}`
      : paymentForm.transactionId;

    await simulatePayment(currentOrder.value.id, {
      paymentType: currentOrder.value.paymentMethod || 'BANK_TRANSFER',
      remarks: paymentForm.paymentNote
    });

    ElMessage.success("支付处理成功");
    paymentDialogVisible.value = false;
    fetchProcessList();
  } catch (error) {
    console.error('Payment error:', error);
    ElMessage.error("支付处理失败");
  } finally {
    paymentLoading.value = false;
  }
};

// 重试
const handleRetry = async (row: any) => {
  try {
    await ElMessageBox.confirm("确定要重试处理该订单吗？", "提示", {
      type: "warning"
    });
    
    // 这里模拟重试逻辑
    ElMessage.success("重试成功，订单已重新进入处理队列");
    fetchProcessList();
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("重试失败");
    }
  }
};

// 更多操作
const handleMoreAction = async (command: string, row: any) => {
  switch (command) {
    case 'history':
      await showProcessingHistory(row);
      break;
    case 'priority':
      await changePriority(row);
      break;
    case 'assign':
      await assignProcessor(row);
      break;
    case 'cancel':
      await cancelProcess(row);
      break;
  }
};

// 显示处理历史
const showProcessingHistory = async (row: any) => {
  // 模拟处理历史数据
  processingHistory.value = [
    {
      timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
      action: "创建订单",
      processor: "系统",
      comment: "订单自动创建",
      result: "成功"
    },
    {
      timestamp: new Date(Date.now() - 1 * 60 * 60 * 1000).toISOString(),
      action: "提交审核",
      processor: "张三",
      comment: "材料齐全，提交审核",
      result: "待审核"
    },
    {
      timestamp: new Date().toISOString(),
      action: "审核中",
      processor: "李四",
      comment: "正在审核中...",
      result: "处理中"
    }
  ];
  historyDialogVisible.value = true;
};

// 调整优先级
const changePriority = async (row: any) => {
  try {
    const { value } = await ElMessageBox.prompt("请输入新的优先级 (high/medium/low)", "调整优先级", {
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    });
    
    // 这里调用调整优先级API
    ElMessage.success("优先级调整成功");
    fetchProcessList();
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("优先级调整失败");
    }
  }
};

// 分配处理人
const assignProcessor = async (row: any) => {
  try {
    const { value } = await ElMessageBox.prompt("请选择处理人", "分配处理人", {
      confirmButtonText: "确定",
      cancelButtonText: "取消"
    });
    
    if (value) {
      // 这里调用分配处理人API
      ElMessage.success("处理人分配成功");
      fetchProcessList();
    }
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("处理人分配失败");
    }
  }
};

// 取消处理
const cancelProcess = async (row: any) => {
  try {
    await ElMessageBox.confirm("确定要取消处理该订单吗？", "提示", {
      type: "warning"
    });
    
    // 这里调用取消处理API
    ElMessage.success("取消处理成功");
    fetchProcessList();
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("取消处理失败");
    }
  }
};

// 批量处理确认
const handleConfirmBatch = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要处理的订单");
    return;
  }
  
  batchLoading.value = true;
  try {
    const ids = selectedRows.value.map(row => row.id);
    
    // 根据处理类型调用不同的API
    switch (batchForm.batchType) {
      case 'REVIEW':
        // 批量审核
        for (const id of ids) {
          if (batchForm.reviewResult === 'APPROVED') {
            await approveSettlement(id, "APPROVED", batchForm.comment);
          } else {
            await rejectSettlement(id, batchForm.comment || "批量审核拒绝");
          }
        }
        break;
      case 'PAYMENT':
        // 批量支付
        for (const row of selectedRows.value) {
          await simulatePayment(row.id, {
            paymentType: batchForm.paymentMethod
          });
        }
        break;
      // 其他批量操作...
    }
    
    ElMessage.success("批量处理成功");
    batchDialogVisible.value = false;
    selectedRows.value = [];
    fetchProcessList();
  } catch (error) {
    console.error('Batch process error:', error);
    ElMessage.error("批量处理失败");
  } finally {
    batchLoading.value = false;
  }
};

// 状态检查函数
const canReview = (status: string) => {
  return ['PENDING_REVIEW'].includes(status);
};

const canProcess = (status: string) => {
  return ['APPROVED', 'PENDING_PAYMENT'].includes(status);
};

const canRetry = (status: string) => {
  return ['FAILED'].includes(status);
};

const canChangePriority = (status: string) => {
  return !['PAID', 'CANCELLED'].includes(status);
};

const canAssign = (status: string) => {
  return !['PAID', 'CANCELLED'].includes(status);
};

const canCancel = (status: string) => {
  return !['PAID', 'CANCELLED'].includes(status);
};

// 获取优先级标签类型
const getPriorityTag = (priority: string) => {
  switch (priority) {
    case 'high':
      return 'danger';
    case 'medium':
      return 'warning';
    case 'low':
      return 'info';
    default:
      return 'info';
  }
};

// 获取优先级文本
const getPriorityText = (priority: string) => {
  switch (priority) {
    case 'high':
      return '高';
    case 'medium':
      return '中';
    case 'low':
      return '低';
    default:
      return '中';
  }
};

// 获取状态标签类型
const getStatusTag = (status: string) => {
  switch (status) {
    case 'PENDING_REVIEW':
      return 'warning';
    case 'APPROVED':
      return 'success';
    case 'REJECTED':
      return 'danger';
    case 'PENDING_PAYMENT':
      return 'primary';
    case 'PAID':
      return 'success';
    case 'FAILED':
      return 'danger';
    default:
      return 'info';
  }
};

// 获取状态文本
const getStatusText = (status: string) => {
  switch (status) {
    case 'PENDING_REVIEW':
      return '待审核';
    case 'APPROVED':
      return '审核通过';
    case 'REJECTED':
      return '审核拒绝';
    case 'PENDING_PAYMENT':
      return '待支付';
    case 'PAID':
      return '支付完成';
    case 'FAILED':
      return '处理失败';
    default:
      return '未知状态';
  }
};

// 获取支付方式文本
const getPaymentMethodText = (method: string) => {
  switch (method) {
    case 'INSURANCE_CARD':
      return '医保卡支付';
    case 'BANK_TRANSFER':
      return '银行转账';
    case 'CASH':
      return '现金支付';
    case 'THIRD_PARTY':
      return '第三方支付';
    default:
      return '未指定';
  }
};

// 获取处理时长
const getProcessingDuration = (startTime: string) => {
  if (!startTime) return '';
  const start = new Date(startTime);
  const now = new Date();
  const diffMs = now.getTime() - start.getTime();
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
  const diffMinutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
  
  if (diffHours > 0) {
    return `${diffHours}h${diffMinutes}m`;
  } else {
    return `${diffMinutes}m`;
  }
};

// 获取历史类型
const getHistoryType = (action: string) => {
  switch (action) {
    case '创建订单':
      return 'primary';
    case '提交审核':
      return 'warning';
    case '审核通过':
      return 'success';
    case '审核拒绝':
      return 'danger';
    case '支付完成':
      return 'success';
    default:
      return 'info';
  }
};

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
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

// 初始化
onMounted(() => {
  fetchProcessList();
});
</script>

<style scoped>
.settlement-process {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-card :deep(.el-card__body) {
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-content {
  text-align: left;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
}

.stat-icon.pending {
  background: linear-gradient(135deg, #e6a23c, #f7c23e);
}

.stat-icon.processing {
  background: linear-gradient(135deg, #409eff, #66b1ff);
}

.stat-icon.completed {
  background: linear-gradient(135deg, #67c23a, #85ce61);
}

.stat-icon.failed {
  background: linear-gradient(135deg, #f56c6c, #f89898);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.search-form {
  margin-bottom: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 6px;
}

.quick-filters {
  margin-bottom: 16px;
  display: flex;
  justify-content: center;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: center;
}

.pagination-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.selection-info {
  font-size: 14px;
  color: #666;
}

.amount-text {
  font-weight: 600;
  color: #409eff;
}

.amount-text.reimbursement {
  color: #67c23a;
}

.amount-text.self-pay {
  color: #e6a23c;
}

.amount-text.large {
  font-size: 16px;
}

.processing-time {
  font-size: 12px;
  color: #909399;
  background: #f0f0f0;
  padding: 2px 6px;
  border-radius: 4px;
}

.text-gray {
  color: #c0c4cc;
}

.priority-urgent {
  animation: blink 1s infinite;
}

@keyframes blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0.5; }
}

.review-content,
.payment-content,
.batch-content {
  padding: 8px 0;
}

.required-field {
  border-color: #f56c6c !important;
}

:deep(.el-table) {
  margin-bottom: 0;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
}

:deep(.el-timeline-item__timestamp) {
  font-size: 12px;
}

:deep(.el-card .el-card__body) {
  padding: 12px;
}

:deep(.el-alert) {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .stats-cards :deep(.el-col) {
    margin-bottom: 16px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 4px;
  }
  
  .pagination-wrapper {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
