<template>
  <div class="settlement-report">
    <!-- 报表筛选条件 -->
    <el-card class="filter-card">
      <template #header>
        <span>报表筛选</span>
      </template>
      <el-form :model="filterForm" inline>
        <el-form-item label="报表类型">
          <el-select v-model="filterForm.reportType" placeholder="请选择报表类型">
            <el-option label="结算汇总报表" value="summary" />
            <el-option label="科室结算报表" value="department" />
            <el-option label="医生结算报表" value="doctor" />
            <el-option label="患者结算报表" value="patient" />
            <el-option label="时间趋势报表" value="trend" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="科室">
          <el-select v-model="filterForm.department" placeholder="请选择科室" clearable>
            <el-option label="内科" value="internal" />
            <el-option label="外科" value="surgery" />
            <el-option label="儿科" value="pediatrics" />
            <el-option label="妇产科" value="obstetrics" />
            <el-option label="急诊科" value="emergency" />
          </el-select>
        </el-form-item>
        <el-form-item label="结算状态">
          <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
            <el-option label="已结算" value="completed" />
            <el-option label="待处理" value="pending" />
            <el-option label="处理中" value="processing" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleGenerateReport">
            <el-icon><Document /></el-icon>
            生成报表
          </el-button>
          <el-button @click="handleExport">
            <el-icon><Download /></el-icon>
            导出Excel
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 报表概览 -->
    <el-row :gutter="20" class="overview-section" v-if="reportData">
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon primary">
              <el-icon><Money /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-value">{{ reportData.totalAmount }}</div>
              <div class="overview-label">总结算金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon success">
              <el-icon><Document /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-value">{{ reportData.totalCount }}</div>
              <div class="overview-label">结算笔数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon warning">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-value">{{ reportData.avgAmount }}</div>
              <div class="overview-label">平均金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card">
          <div class="overview-content">
            <div class="overview-icon info">
              <el-icon><DataAnalysis /></el-icon>
            </div>
            <div class="overview-info">
              <div class="overview-value">{{ reportData.completionRate }}</div>
              <div class="overview-label">完成率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表展示 -->
    <el-row :gutter="20" class="charts-section" v-if="reportData">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>结算金额趋势</span>
          </template>
          <div ref="trendChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>科室结算分布</span>
          </template>
          <div ref="departmentChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细数据表格 -->
    <el-card v-if="reportData">
      <template #header>
        <span>详细数据</span>
      </template>
      <el-table
        :data="reportData.details"
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="date" label="日期" width="120" />
        <el-table-column prop="department" label="科室" width="120" />
        <el-table-column prop="doctor" label="医生" width="120" />
        <el-table-column prop="patientCount" label="患者数" width="100" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="reimbursedAmount" label="报销金额" width="120">
          <template #default="{ row }">
            ¥{{ row.reimbursedAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="selfPayAmount" label="自费金额" width="120">
          <template #default="{ row }">
            ¥{{ row.selfPayAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="reimbursementRate" label="报销比例" width="100">
          <template #default="{ row }">
            {{ (row.reimbursementRate * 100).toFixed(1) }}%
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
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

    <!-- 空状态 -->
    <el-empty v-if="!reportData && !loading" description="请选择筛选条件并生成报表" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, Download, Refresh, Money, TrendCharts, DataAnalysis } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 响应式数据
const loading = ref(false)
const trendChartRef = ref()
const departmentChartRef = ref()

const filterForm = reactive({
  reportType: 'summary',
  dateRange: [],
  department: '',
  status: ''
})

const reportData = ref(null)
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 生成报表
const handleGenerateReport = async () => {
  if (!filterForm.reportType) {
    ElMessage.warning('请选择报表类型')
    return
  }
  
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.append('reportType', filterForm.reportType)
    if (filterForm.department) params.append('department', filterForm.department)
    if (filterForm.status) params.append('status', filterForm.status)
    if (filterForm.dateRange && filterForm.dateRange.length === 2) {
      params.append('startDate', filterForm.dateRange[0])
      params.append('endDate', filterForm.dateRange[1])
    }
    
    // 模拟API调用
    const response = await fetch(`/api/settlement/report?${params}`)
    const data = await response.json()
    reportData.value = data.data
    
    await nextTick()
    initCharts()
  } catch (error) {
    console.error('生成报表失败:', error)
    ElMessage.error('生成报表失败')
  } finally {
    loading.value = false
  }
}

// 导出Excel
const handleExport = async () => {
  if (!reportData.value) {
    ElMessage.warning('请先生成报表')
    return
  }
  
  try {
    const params = new URLSearchParams()
    params.append('reportType', filterForm.reportType)
    if (filterForm.department) params.append('department', filterForm.department)
    if (filterForm.status) params.append('status', filterForm.status)
    if (filterForm.dateRange && filterForm.dateRange.length === 2) {
      params.append('startDate', filterForm.dateRange[0])
      params.append('endDate', filterForm.dateRange[1])
    }
    
    // 模拟导出
    const response = await fetch(`/api/settlement/export?${params}`)
    const blob = await response.blob()
    
    // 创建下载链接
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `结算报表_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 重置筛选
const handleReset = () => {
  filterForm.reportType = 'summary'
  filterForm.dateRange = []
  filterForm.department = ''
  filterForm.status = ''
  reportData.value = null
}

// 分页大小改变
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  handleGenerateReport()
}

// 当前页改变
const handleCurrentChange = (current: number) => {
  pagination.current = current
  handleGenerateReport()
}

// 初始化趋势图表
const initTrendChart = () => {
  if (!trendChartRef.value || !reportData.value) return
  
  const chart = echarts.init(trendChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['结算金额', '报销金额', '自费金额']
    },
    xAxis: {
      type: 'category',
      data: reportData.value.trendData?.dates || []
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [
      {
        name: '结算金额',
        type: 'line',
        data: reportData.value.trendData?.totalAmounts || []
      },
      {
        name: '报销金额',
        type: 'line',
        data: reportData.value.trendData?.reimbursedAmounts || []
      },
      {
        name: '自费金额',
        type: 'line',
        data: reportData.value.trendData?.selfPayAmounts || []
      }
    ]
  }
  chart.setOption(option)
}

// 初始化科室分布图表
const initDepartmentChart = () => {
  if (!departmentChartRef.value || !reportData.value) return
  
  const chart = echarts.init(departmentChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: ¥{c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '科室结算',
        type: 'pie',
        radius: '50%',
        data: reportData.value.departmentData || []
      }
    ]
  }
  chart.setOption(option)
}

// 初始化所有图表
const initCharts = () => {
  initTrendChart()
  initDepartmentChart()
}

// 获取状态标签样式
const getStatusTagType = (status: string) => {
  const statusMap: Record<string, 'success' | 'warning' | 'info'> = {
    completed: 'success',
    pending: 'warning',
    processing: 'info'
  }
  return statusMap[status] || 'info'
}

// 获取状态标签文本
const getStatusLabel = (status: string) => {
  const statusMap: Record<string, string> = {
    completed: '已结算',
    pending: '待处理',
    processing: '处理中'
  }
  return statusMap[status] || status
}

// 页面加载时初始化
onMounted(() => {
  // 设置默认时间范围为最近一个月
  const endDate = new Date()
  const startDate = new Date()
  startDate.setMonth(startDate.getMonth() - 1)
  
  filterForm.dateRange = [
    startDate.toISOString().split('T')[0],
    endDate.toISOString().split('T')[0]
  ]
})
</script>

<style scoped>
.settlement-report {
  padding: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.overview-section {
  margin-bottom: 20px;
}

.overview-card {
  height: 120px;
}

.overview-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.overview-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.overview-icon.primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.overview-icon.success {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.overview-icon.warning {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.overview-icon.info {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.overview-info {
  flex: 1;
}

.overview-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.overview-label {
  font-size: 14px;
  color: #909399;
}

.charts-section {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style> 