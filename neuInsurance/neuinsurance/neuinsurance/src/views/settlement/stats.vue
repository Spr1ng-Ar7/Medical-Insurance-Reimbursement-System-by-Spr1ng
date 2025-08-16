<template>
  <div class="settlement-stats">
    <!-- 实时数据概览 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stats-card primary" @click="handleCardClick('amount')">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon><Money /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ formatCurrency(statsData.totalAmount) }}</div>
              <div class="stats-label">总结算金额</div>
              <div class="stats-trend" :class="{ 'trend-up': statsData.amountTrend > 0, 'trend-down': statsData.amountTrend < 0 }">
                <el-icon v-if="statsData.amountTrend > 0"><ArrowUp /></el-icon>
                <el-icon v-if="statsData.amountTrend < 0"><ArrowDown /></el-icon>
                {{ Math.abs(statsData.amountTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card success" @click="handleCardClick('count')">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statsData.totalCount.toLocaleString() }}</div>
              <div class="stats-label">结算笔数</div>
              <div class="stats-trend" :class="{ 'trend-up': statsData.countTrend > 0, 'trend-down': statsData.countTrend < 0 }">
                <el-icon v-if="statsData.countTrend > 0"><ArrowUp /></el-icon>
                <el-icon v-if="statsData.countTrend < 0"><ArrowDown /></el-icon>
                {{ Math.abs(statsData.countTrend) }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card warning" @click="handleCardClick('pending')">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statsData.pendingCount }}</div>
              <div class="stats-label">待处理</div>
              <div class="stats-sublabel">平均处理时间: {{ statsData.avgProcessingTime }}小时</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card info" @click="handleCardClick('rate')">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statsData.completionRate }}%</div>
              <div class="stats-label">完成率</div>
              <div class="stats-sublabel">今日处理: {{ statsData.todayProcessed }}笔</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速统计面板 -->
    <el-row :gutter="20" class="quick-stats">
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>医院等级分布</span>
              <el-button type="text" @click="viewHospitalDetails">查看详情</el-button>
            </div>
          </template>
          <div class="hospital-stats">
            <div v-for="item in statsData.hospitalStats" :key="item.level" class="hospital-item">
              <div class="hospital-info">
                <span class="hospital-level">{{ item.level }}</span>
                <span class="hospital-count">{{ item.count }}笔</span>
              </div>
              <div class="hospital-bar">
                <div class="hospital-bar-fill" :style="{ width: item.percentage + '%' }"></div>
              </div>
              <span class="hospital-amount">{{ formatCurrency(item.amount) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>支付方式统计</span>
              <el-button type="text" @click="viewPaymentDetails">查看详情</el-button>
            </div>
          </template>
          <div ref="paymentChartRef" class="mini-chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>异常处理统计</span>
              <el-button type="text" @click="viewExceptionDetails">查看详情</el-button>
            </div>
          </template>
          <div class="exception-stats">
            <div class="exception-item">
              <el-icon class="exception-icon error"><WarningFilled /></el-icon>
              <div class="exception-info">
                <div class="exception-count">{{ statsData.exceptionStats.failed }}</div>
                <div class="exception-label">处理失败</div>
              </div>
            </div>
            <div class="exception-item">
              <el-icon class="exception-icon warning"><Clock /></el-icon>
              <div class="exception-info">
                <div class="exception-count">{{ statsData.exceptionStats.timeout }}</div>
                <div class="exception-label">处理超时</div>
              </div>
            </div>
            <div class="exception-item">
              <el-icon class="exception-icon info"><RefreshRight /></el-icon>
              <div class="exception-info">
                <div class="exception-count">{{ statsData.exceptionStats.retry }}</div>
                <div class="exception-label">重试处理</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 高级筛选面板 -->
    <el-card class="filter-section">
      <template #header>
        <div class="filter-header">
          <span>高级筛选</span>
          <div class="filter-actions">
            <el-button size="small" @click="handleSaveFilter">保存筛选</el-button>
            <el-button size="small" @click="handleLoadFilter">加载筛选</el-button>
            <el-button size="small" @click="handleExportData">导出数据</el-button>
          </div>
        </div>
      </template>
      <el-form :model="filterForm" inline>
        <el-form-item label="时间维度">
          <el-select v-model="filterForm.timeDimension" @change="handleTimeDimensionChange">
            <el-option label="今日" value="today" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
            <el-option label="本季度" value="quarter" />
            <el-option label="本年" value="year" />
            <el-option label="自定义" value="custom" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="filterForm.timeDimension === 'custom'" label="自定义时间">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="YYYY-MM-DD HH:mm"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="结算状态">
          <el-select v-model="filterForm.status" multiple placeholder="选择状态" clearable>
            <el-option label="待审核" value="PENDING_REVIEW" />
            <el-option label="审核通过" value="APPROVED" />
            <el-option label="审核拒绝" value="REJECTED" />
            <el-option label="待支付" value="PENDING_PAYMENT" />
            <el-option label="支付完成" value="PAID" />
            <el-option label="处理失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item label="医院等级">
          <el-select v-model="filterForm.hospitalLevel" multiple placeholder="选择医院等级" clearable>
            <el-option label="三甲" value="三甲" />
            <el-option label="三乙" value="三乙" />
            <el-option label="二甲" value="二甲" />
            <el-option label="二乙" value="二乙" />
            <el-option label="一甲" value="一甲" />
            <el-option label="一乙" value="一乙" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额范围">
          <el-input-number v-model="filterForm.minAmount" placeholder="最小金额" style="width: 120px" />
          <span style="margin: 0 8px">-</span>
          <el-input-number v-model="filterForm.maxAmount" placeholder="最大金额" style="width: 120px" />
        </el-form-item>
        <el-form-item label="支付方式">
          <el-select v-model="filterForm.paymentMethod" multiple placeholder="选择支付方式" clearable>
            <el-option label="医保卡支付" value="INSURANCE_CARD" />
            <el-option label="银行转账" value="BANK_TRANSFER" />
            <el-option label="现金支付" value="CASH" />
            <el-option label="第三方支付" value="THIRD_PARTY" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter" :loading="loading">
            <el-icon><Search /></el-icon>
            查询统计
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
          <el-button @click="handleAutoRefresh" :type="autoRefresh ? 'success' : 'info'">
            <el-icon><Timer /></el-icon>
            {{ autoRefresh ? '停止' : '开启' }}自动刷新
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-section">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="chart-header">
              <span>结算趋势分析</span>
              <el-radio-group v-model="trendChartType" size="small" @change="updateTrendChart">
                <el-radio-button label="amount">金额</el-radio-button>
                <el-radio-button label="count">数量</el-radio-button>
                <el-radio-button label="rate">完成率</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="monthlyChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="chart-header">
              <span>分类统计分析</span>
              <el-select v-model="categoryChartType" size="small" @change="updateCategoryChart" style="width: 120px">
                <el-option label="医院等级" value="hospital" />
                <el-option label="支付方式" value="payment" />
                <el-option label="处理状态" value="status" />
                <el-option label="时间分布" value="time" />
              </el-select>
            </div>
          </template>
          <div ref="typeChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时监控面板 -->
    <el-card class="monitor-section">
      <template #header>
        <div class="monitor-header">
          <span>实时处理监控</span>
          <div class="monitor-actions">
            <el-switch
              v-model="realTimeMonitor"
              active-text="实时监控"
              @change="toggleRealTimeMonitor"
            />
            <el-button size="small" type="primary" @click="handleManualRefresh">
              <el-icon><Refresh /></el-icon>
              手动刷新
            </el-button>
          </div>
        </div>
      </template>
      <div class="monitor-content">
        <div class="monitor-metrics">
          <div class="metric-item">
            <div class="metric-label">当前处理中</div>
            <div class="metric-value processing">{{ monitorData.processing }}</div>
          </div>
          <div class="metric-item">
            <div class="metric-label">今日已完成</div>
            <div class="metric-value completed">{{ monitorData.completed }}</div>
          </div>
          <div class="metric-item">
            <div class="metric-label">平均处理时长</div>
            <div class="metric-value time">{{ monitorData.avgProcessTime }}分钟</div>
          </div>
          <div class="metric-item">
            <div class="metric-label">系统负载</div>
            <div class="metric-value load" :class="{ 'high-load': monitorData.systemLoad > 80 }">
              {{ monitorData.systemLoad }}%
            </div>
          </div>
        </div>
        <div ref="monitorChartRef" class="monitor-chart"></div>
      </div>
    </el-card>

    <!-- 详细数据表格 -->
    <el-card>
      <template #header>
        <div class="table-header">
          <span>结算明细数据</span>
          <div class="table-actions">
            <el-button size="small" @click="handleBatchExport">批量导出</el-button>
            <el-button size="small" @click="handleTableSettings">表格设置</el-button>
            <el-button size="small" @click="handleDataAnalysis">数据分析</el-button>
          </div>
        </div>
      </template>
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        row-key="id"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="结算编号" width="120" sortable="custom" />
        <el-table-column prop="patientName" label="患者姓名" width="120" />
        <el-table-column prop="hospitalLevel" label="医院等级" width="100">
          <template #default="{ row }">
            <el-tag :type="getHospitalLevelTag(row.hospitalLevel)" size="small">
              {{ row.hospitalLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="总金额" width="120" sortable="custom" align="right">
          <template #default="{ row }">
            <span class="amount-text">{{ formatCurrency(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reimbursableAmount" label="报销金额" width="120" sortable="custom" align="right">
          <template #default="{ row }">
            <span class="amount-text reimbursement">{{ formatCurrency(row.reimbursableAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="selfPayAmount" label="自费金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount-text self-pay">{{ formatCurrency(row.totalAmount - row.reimbursableAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="reimbursementRatio" label="报销比例" width="100" align="center">
          <template #default="{ row }">
            <el-progress 
              :percentage="Math.round(row.reimbursableAmount / row.totalAmount * 100)" 
              :stroke-width="8"
              :show-text="false"
              :color="getProgressColor(row.reimbursableAmount / row.totalAmount * 100)"
            />
            <span class="ratio-text">{{ Math.round(row.reimbursableAmount / row.totalAmount * 100) }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentMethod" label="支付方式" width="120">
          <template #default="{ row }">
            <span v-if="row.paymentMethod">{{ getPaymentMethodText(row.paymentMethod) }}</span>
            <span v-else class="text-gray">未指定</span>
          </template>
        </el-table-column>
        <el-table-column prop="processingTime" label="处理时长" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.processingTime" class="processing-time">
              {{ getProcessingDuration(row.processingTime) }}
            </span>
            <span v-else class="text-gray">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleDetail(row)">
              详情
            </el-button>
            <el-button type="success" link size="small" @click="handleAnalyze(row)">
              分析
            </el-button>
            <el-dropdown @command="(command) => handleRowAction(command, row)">
              <el-button type="primary" link size="small">
                更多<el-icon><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="export">导出</el-dropdown-item>
                  <el-dropdown-item command="compare">对比</el-dropdown-item>
                  <el-dropdown-item command="history">历史</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <div class="pagination-info">
          <span>共 {{ pagination.total }} 条记录</span>
          <span v-if="selectedRows.length > 0">
            已选择 {{ selectedRows.length }} 条，总金额 {{ formatCurrency(selectedTotalAmount) }}
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

    <!-- 数据对比对话框 -->
    <el-dialog v-model="compareDialogVisible" title="数据对比分析" width="900px">
      <div class="compare-content">
        <el-form :model="compareForm" inline>
          <el-form-item label="对比维度">
            <el-select v-model="compareForm.dimension">
              <el-option label="时间对比" value="time" />
              <el-option label="医院等级对比" value="hospital" />
              <el-option label="支付方式对比" value="payment" />
              <el-option label="状态对比" value="status" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="generateCompare">生成对比</el-button>
          </el-form-item>
        </el-form>
        <div ref="compareChartRef" class="compare-chart"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, computed, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Money, Document, Clock, DataAnalysis, Search, Refresh, Timer, 
  ArrowUp, ArrowDown, WarningFilled, RefreshRight, ArrowDown as ArrowDownIcon 
} from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getSettlementList, getSettlementStatistics } from '@/api/settlement'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const autoRefresh = ref(false)
const realTimeMonitor = ref(false)
const monthlyChartRef = ref()
const typeChartRef = ref()
const paymentChartRef = ref()
const monitorChartRef = ref()
const compareChartRef = ref()
const trendChartType = ref('amount')
const categoryChartType = ref('hospital')
const compareDialogVisible = ref(false)

// 定时器
let refreshTimer: NodeJS.Timeout | null = null
let monitorTimer: NodeJS.Timeout | null = null

const filterForm = reactive({
  timeDimension: 'month',
  dateRange: [],
  status: [],
  hospitalLevel: [],
  minAmount: null,
  maxAmount: null,
  paymentMethod: []
})

const compareForm = reactive({
  dimension: 'time'
})

const statsData = reactive({
  totalAmount: 0,
  totalCount: 0,
  pendingCount: 0,
  completionRate: 0,
  amountTrend: 0,
  countTrend: 0,
  avgProcessingTime: 0,
  todayProcessed: 0,
  hospitalStats: [],
  exceptionStats: {
    failed: 0,
    timeout: 0,
    retry: 0
  }
})

const monitorData = reactive({
  processing: 0,
  completed: 0,
  avgProcessTime: 0,
  systemLoad: 0
})

const tableData = ref([])
const selectedRows = ref([])
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 计算属性
const selectedTotalAmount = computed(() => {
  return selectedRows.value.reduce((total, row) => total + (row.totalAmount || 0), 0)
})

// 格式化货币
const formatCurrency = (amount: number) => {
  if (amount === null || amount === undefined) return '¥0.00'
  return `¥${amount.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
}

// 获取统计数据
const getStatsData = async () => {
  try {
    const params = buildQueryParams()
    const response = await getSettlementStatistics(params)
    
    if (response) {
      Object.assign(statsData, {
        totalAmount: response.totalAmount || 0,
        totalCount: response.totalCount || 0,
        pendingCount: response.pendingCount || 0,
        completionRate: response.completionRate || 0,
        amountTrend: response.amountTrend || 0,
        countTrend: response.countTrend || 0,
        avgProcessingTime: response.avgProcessingTime || 0,
        todayProcessed: response.todayProcessed || 0,
        hospitalStats: response.hospitalStats || [],
        exceptionStats: response.exceptionStats || { failed: 0, timeout: 0, retry: 0 }
      })
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

// 构建查询参数
const buildQueryParams = () => {
  const params: any = {
    pageNum: pagination.current,
    pageSize: pagination.size
  }

  if (filterForm.timeDimension !== 'custom') {
    const now = new Date()
    switch (filterForm.timeDimension) {
      case 'today':
        params.startDate = now.toISOString().split('T')[0]
        params.endDate = now.toISOString().split('T')[0]
        break
      case 'week':
        const weekStart = new Date(now.setDate(now.getDate() - now.getDay()))
        params.startDate = weekStart.toISOString().split('T')[0]
        params.endDate = new Date().toISOString().split('T')[0]
        break
      case 'month':
        params.startDate = new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0]
        params.endDate = new Date().toISOString().split('T')[0]
        break
    }
  } else if (filterForm.dateRange && filterForm.dateRange.length === 2) {
    params.startDate = filterForm.dateRange[0]
    params.endDate = filterForm.dateRange[1]
  }

  if (filterForm.status.length > 0) params.status = filterForm.status.join(',')
  if (filterForm.hospitalLevel.length > 0) params.hospitalLevel = filterForm.hospitalLevel.join(',')
  if (filterForm.minAmount !== null) params.minAmount = filterForm.minAmount
  if (filterForm.maxAmount !== null) params.maxAmount = filterForm.maxAmount
  if (filterForm.paymentMethod.length > 0) params.paymentMethod = filterForm.paymentMethod.join(',')

  return params
}

// 获取表格数据
const getTableData = async () => {
  loading.value = true
  try {
    const params = buildQueryParams()
    const response = await getSettlementList(params)
    
    if (response && response.records) {
      tableData.value = response.records.map((item: any) => ({
        ...item,
        selfPayAmount: item.totalAmount - item.reimbursableAmount,
        reimbursementRatio: item.reimbursableAmount / item.totalAmount
      }))
      pagination.total = response.total || 0
      pagination.current = response.current || 1
      pagination.size = response.size || 20
    }
  } catch (error) {
    console.error('获取表格数据失败:', error)
    ElMessage.error('获取表格数据失败')
  } finally {
    loading.value = false
  }
}

// 初始化图表
const initCharts = async () => {
  await nextTick()
  initTrendChart()
  initCategoryChart()
  initPaymentChart()
  initMonitorChart()
}

// 初始化趋势图表
const initTrendChart = () => {
  if (!monthlyChartRef.value) return
  
  const chart = echarts.init(monthlyChartRef.value)
  updateTrendChart()
}

// 更新趋势图表
const updateTrendChart = () => {
  if (!monthlyChartRef.value) return
  
  const chart = echarts.getInstanceByDom(monthlyChartRef.value) || echarts.init(monthlyChartRef.value)
  
  // 模拟数据
  const dates = ['1月', '2月', '3月', '4月', '5月', '6月']
  let series: any[] = []
  
  switch (trendChartType.value) {
    case 'amount':
      series = [
        { name: '总金额', type: 'line', data: [12000, 15000, 18000, 22000, 25000, 28000] },
        { name: '报销金额', type: 'line', data: [8000, 10000, 12000, 15000, 18000, 20000] }
      ]
      break
    case 'count':
      series = [
        { name: '总笔数', type: 'bar', data: [120, 150, 180, 220, 250, 280] },
        { name: '已完成', type: 'bar', data: [100, 130, 160, 200, 230, 260] }
      ]
      break
    case 'rate':
      series = [
        { name: '完成率', type: 'line', data: [83, 87, 89, 91, 92, 93] },
        { name: '及时率', type: 'line', data: [78, 82, 85, 88, 90, 91] }
      ]
      break
  }

  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: series.map(s => s.name) },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value' },
    series
  }
  
  chart.setOption(option)
}

// 初始化分类图表
const initCategoryChart = () => {
  if (!typeChartRef.value) return
  updateCategoryChart()
}

// 更新分类图表
const updateCategoryChart = () => {
  if (!typeChartRef.value) return
  
  const chart = echarts.getInstanceByDom(typeChartRef.value) || echarts.init(typeChartRef.value)
  
  let data: any[] = []
  
  switch (categoryChartType.value) {
    case 'hospital':
      data = [
        { value: 335, name: '三甲' },
        { value: 310, name: '三乙' },
        { value: 234, name: '二甲' },
        { value: 135, name: '二乙' }
      ]
      break
    case 'payment':
      data = [
        { value: 400, name: '医保卡' },
        { value: 300, name: '银行转账' },
        { value: 200, name: '现金' },
        { value: 100, name: '第三方' }
      ]
      break
    case 'status':
      data = [
        { value: 500, name: '已完成' },
        { value: 200, name: '处理中' },
        { value: 100, name: '待处理' },
        { value: 50, name: '失败' }
      ]
      break
  }

  const option = {
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '分类统计',
      type: 'pie',
      radius: '50%',
      data,
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
  
  chart.setOption(option)
}

// 初始化支付方式图表
const initPaymentChart = () => {
  if (!paymentChartRef.value) return
  
  const chart = echarts.init(paymentChartRef.value)
  const option = {
    tooltip: { trigger: 'item' },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      data: [
        { value: 400, name: '医保卡' },
        { value: 300, name: '银行转账' },
        { value: 200, name: '现金' },
        { value: 100, name: '第三方' }
      ],
      label: { show: false },
      emphasis: { label: { show: true } }
    }]
  }
  chart.setOption(option)
}

// 初始化监控图表
const initMonitorChart = () => {
  if (!monitorChartRef.value) return
  
  const chart = echarts.init(monitorChartRef.value)
  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: [] },
    yAxis: { type: 'value' },
    series: [
      { name: '处理速度', type: 'line', data: [] },
      { name: '成功率', type: 'line', data: [] }
    ]
  }
  chart.setOption(option)
}

// 处理方法
const handleCardClick = (type: string) => {
  // 根据卡片类型执行相应操作
  switch (type) {
    case 'amount':
      filterForm.timeDimension = 'month'
      break
    case 'count':
      // 跳转到详细统计
      break
    case 'pending':
      filterForm.status = ['PENDING_REVIEW', 'PENDING_PAYMENT']
      break
    case 'rate':
      // 显示完成率详情
      break
  }
  handleFilter()
}

const handleTimeDimensionChange = () => {
  if (filterForm.timeDimension !== 'custom') {
    filterForm.dateRange = []
  }
  handleFilter()
}

const handleFilter = async () => {
  pagination.current = 1
  await getStatsData()
  await getTableData()
  await initCharts()
}

const handleReset = () => {
  Object.assign(filterForm, {
    timeDimension: 'month',
    dateRange: [],
    status: [],
    hospitalLevel: [],
    minAmount: null,
    maxAmount: null,
    paymentMethod: []
  })
  handleFilter()
}

const handleAutoRefresh = () => {
  autoRefresh.value = !autoRefresh.value
  if (autoRefresh.value) {
    refreshTimer = setInterval(() => {
      getStatsData()
      getTableData()
    }, 30000) // 30秒刷新一次
    ElMessage.success('已开启自动刷新')
  } else {
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
    ElMessage.info('已停止自动刷新')
  }
}

const toggleRealTimeMonitor = (enabled: boolean) => {
  if (enabled) {
    monitorTimer = setInterval(() => {
      // 更新监控数据
      Object.assign(monitorData, {
        processing: Math.floor(Math.random() * 50),
        completed: Math.floor(Math.random() * 200) + 100,
        avgProcessTime: Math.floor(Math.random() * 60) + 30,
        systemLoad: Math.floor(Math.random() * 30) + 50
      })
    }, 5000)
    ElMessage.success('实时监控已开启')
  } else {
    if (monitorTimer) {
      clearInterval(monitorTimer)
      monitorTimer = null
    }
    ElMessage.info('实时监控已关闭')
  }
}

const handleManualRefresh = async () => {
  await getStatsData()
  await getTableData()
  ElMessage.success('数据已刷新')
}

// 表格操作
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

const handleSortChange = ({ prop, order }: any) => {
  // 实现排序逻辑
  getTableData()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  getTableData()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getTableData()
}

const handleDetail = (row: any) => {
  router.push(`/settlement/detail/${row.id}`)
}

const handleAnalyze = (row: any) => {
  // 分析单条记录
  ElMessage.info('分析功能开发中...')
}

const handleRowAction = (command: string, row: any) => {
  switch (command) {
    case 'export':
      // 导出单条记录
      break
    case 'compare':
      // 对比分析
      compareDialogVisible.value = true
      break
    case 'history':
      // 查看历史
      break
  }
}

// 工具方法
const getHospitalLevelTag = (level: string) => {
  const tagMap: Record<string, string> = {
    '三甲': 'danger',
    '三乙': 'warning',
    '二甲': 'primary',
    '二乙': 'success',
    '一甲': 'info',
    '一乙': 'info'
  }
  return tagMap[level] || 'info'
}

const getStatusTagType = (status: string) => {
  const statusMap: Record<string, string> = {
    'PAID': 'success',
    'APPROVED': 'primary',
    'PENDING_REVIEW': 'warning',
    'PENDING_PAYMENT': 'warning',
    'REJECTED': 'danger',
    'FAILED': 'danger'
  }
  return statusMap[status] || 'info'
}

const getStatusLabel = (status: string) => {
  const statusMap: Record<string, string> = {
    'PAID': '已支付',
    'APPROVED': '已审核',
    'PENDING_REVIEW': '待审核',
    'PENDING_PAYMENT': '待支付',
    'REJECTED': '已拒绝',
    'FAILED': '失败'
  }
  return statusMap[status] || status
}

const getPaymentMethodText = (method: string) => {
  const methodMap: Record<string, string> = {
    'INSURANCE_CARD': '医保卡',
    'BANK_TRANSFER': '银行转账',
    'CASH': '现金',
    'THIRD_PARTY': '第三方'
  }
  return methodMap[method] || method
}

const getProgressColor = (percentage: number) => {
  if (percentage < 50) return '#f56c6c'
  if (percentage < 80) return '#e6a23c'
  return '#67c23a'
}

const getProcessingDuration = (startTime: string) => {
  if (!startTime) return ''
  const start = new Date(startTime)
  const now = new Date()
  const diffMs = now.getTime() - start.getTime()
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60))
  const diffMinutes = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60))
  
  if (diffHours > 0) {
    return `${diffHours}h${diffMinutes}m`
  } else {
    return `${diffMinutes}m`
  }
}

const formatDateTime = (dateTime: string) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 导出和分析功能
const handleSaveFilter = () => {
  const filterData = JSON.stringify(filterForm)
  localStorage.setItem('settlement-stats-filter', filterData)
  ElMessage.success('筛选条件已保存')
}

const handleLoadFilter = () => {
  const filterData = localStorage.getItem('settlement-stats-filter')
  if (filterData) {
    Object.assign(filterForm, JSON.parse(filterData))
    ElMessage.success('筛选条件已加载')
    handleFilter()
  } else {
    ElMessage.warning('未找到保存的筛选条件')
  }
}

const handleExportData = () => {
  ElMessage.info('导出功能开发中...')
}

const handleBatchExport = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要导出的数据')
    return
  }
  ElMessage.info('批量导出功能开发中...')
}

const handleTableSettings = () => {
  ElMessage.info('表格设置功能开发中...')
}

const handleDataAnalysis = () => {
  ElMessage.info('数据分析功能开发中...')
}

const viewHospitalDetails = () => {
  ElMessage.info('医院等级详情功能开发中...')
}

const viewPaymentDetails = () => {
  ElMessage.info('支付方式详情功能开发中...')
}

const viewExceptionDetails = () => {
  ElMessage.info('异常处理详情功能开发中...')
}

const generateCompare = () => {
  ElMessage.info('对比分析功能开发中...')
}

// 页面加载时初始化
onMounted(async () => {
  await getStatsData()
  await getTableData()
  await initCharts()
})

// 页面卸载时清理定时器
onBeforeUnmount(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
  }
  if (monitorTimer) {
    clearInterval(monitorTimer)
  }
})
</script>

<style scoped>
.settlement-stats {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  height: 120px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.stats-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.stats-card.primary:hover {
  border-color: #409eff;
}

.stats-card.success:hover {
  border-color: #67c23a;
}

.stats-card.warning:hover {
  border-color: #e6a23c;
}

.stats-card.info:hover {
  border-color: #909399;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 20px;
}

.stats-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}

.stats-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.stats-sublabel {
  font-size: 12px;
  color: #c0c4cc;
}

.stats-trend {
  display: flex;
  align-items: center;
  font-size: 12px;
  font-weight: 600;
}

.stats-trend.trend-up {
  color: #67c23a;
}

.stats-trend.trend-down {
  color: #f56c6c;
}

.quick-stats {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hospital-stats {
  padding: 16px 0;
}

.hospital-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.hospital-info {
  width: 100px;
  display: flex;
  justify-content: space-between;
  margin-right: 12px;
}

.hospital-level {
  font-weight: 600;
}

.hospital-count {
  font-size: 12px;
  color: #909399;
}

.hospital-bar {
  flex: 1;
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  margin-right: 12px;
}

.hospital-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.hospital-amount {
  width: 80px;
  text-align: right;
  font-size: 12px;
  font-weight: 600;
  color: #409eff;
}

.mini-chart {
  height: 200px;
}

.exception-stats {
  padding: 16px 0;
  display: flex;
  justify-content: space-around;
}

.exception-item {
  text-align: center;
}

.exception-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.exception-icon.error {
  color: #f56c6c;
}

.exception-icon.warning {
  color: #e6a23c;
}

.exception-icon.info {
  color: #409eff;
}

.exception-count {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 4px;
}

.exception-label {
  font-size: 12px;
  color: #909399;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.filter-actions {
  display: flex;
  gap: 8px;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  height: 350px;
}

.monitor-section {
  margin-bottom: 20px;
}

.monitor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.monitor-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.monitor-content {
  display: flex;
  gap: 20px;
}

.monitor-metrics {
  width: 300px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.metric-item {
  text-align: center;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
}

.metric-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.metric-value {
  font-size: 24px;
  font-weight: bold;
}

.metric-value.processing {
  color: #409eff;
}

.metric-value.completed {
  color: #67c23a;
}

.metric-value.time {
  color: #e6a23c;
}

.metric-value.load {
  color: #909399;
}

.metric-value.high-load {
  color: #f56c6c;
}

.monitor-chart {
  flex: 1;
  height: 200px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-actions {
  display: flex;
  gap: 8px;
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

.ratio-text {
  font-size: 12px;
  color: #909399;
  margin-left: 8px;
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

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}

.pagination-info {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #666;
}

.compare-content {
  padding: 16px 0;
}

.compare-chart {
  height: 400px;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .stats-cards :deep(.el-col) {
    margin-bottom: 16px;
  }
  
  .quick-stats :deep(.el-col) {
    margin-bottom: 16px;
  }
  
  .charts-section :deep(.el-col) {
    margin-bottom: 16px;
  }
  
  .monitor-content {
    flex-direction: column;
  }
  
  .monitor-metrics {
    width: 100%;
  }
  
  .pagination-container {
    flex-direction: column;
    gap: 16px;
  }
}
</style> 