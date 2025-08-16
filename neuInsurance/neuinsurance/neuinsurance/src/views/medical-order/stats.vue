<template>
  <div class="medical-order-stats">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon size="40" color="#409eff"><Document /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ stats.totalOrders }}</div>
              <div class="stats-label">总订单数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon size="40" color="#67c23a"><Check /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ stats.approvedOrders }}</div>
              <div class="stats-label">已审核订单</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon size="40" color="#e6a23c"><Clock /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-number">{{ stats.pendingOrders }}</div>
              <div class="stats-label">待审核订单</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <el-icon size="40" color="#f56c6c"><Money /></el-icon>
            </div>
            <div class="stats-info">
              <div class="stats-number">¥{{ stats.totalAmount?.toFixed(2) }}</div>
              <div class="stats-label">总金额</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>订单类型分布</span>
            </div>
          </template>
          <div ref="orderTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>科室订单统计</span>
            </div>
          </template>
          <div ref="departmentChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="charts-row">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>订单趋势</span>
            </div>
          </template>
          <div ref="trendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 详细统计表格 -->
    <el-card class="stats-table">
      <template #header>
        <div class="card-header">
          <span>详细统计</span>
        </div>
      </template>
      
      <el-table :data="detailStats" border stripe>
        <el-table-column prop="department" label="科室" />
        <el-table-column prop="totalOrders" label="订单总数" />
        <el-table-column prop="approvedOrders" label="已审核" />
        <el-table-column prop="pendingOrders" label="待审核" />
        <el-table-column prop="rejectedOrders" label="已拒绝" />
        <el-table-column prop="totalAmount" label="总金额">
          <template #default="{ row }">
            ¥{{ row.totalAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="avgAmount" label="平均金额">
          <template #default="{ row }">
            ¥{{ row.avgAmount?.toFixed(2) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from "vue";
import { Document, Check, Clock, Money } from "@element-plus/icons-vue";
import * as echarts from "echarts";

// 统计数据
const stats = reactive({
  totalOrders: 0,
  approvedOrders: 0,
  pendingOrders: 0,
  totalAmount: 0
});

// 详细统计数据
const detailStats = ref([]);

// 图表引用
const orderTypeChart = ref();
const departmentChart = ref();
const trendChart = ref();

// 获取统计数据
const fetchStats = async () => {
  try {
    // 这里调用统计API
    // const result = await getMedicalOrderStats();
    // Object.assign(stats, result);
    
    // 模拟数据
    Object.assign(stats, {
      totalOrders: 1250,
      approvedOrders: 980,
      pendingOrders: 270,
      totalAmount: 1250000.50
    });
    
    detailStats.value = [
      {
        department: "内科",
        totalOrders: 450,
        approvedOrders: 380,
        pendingOrders: 70,
        rejectedOrders: 0,
        totalAmount: 450000.00,
        avgAmount: 1000.00
      },
      {
        department: "外科",
        totalOrders: 320,
        approvedOrders: 280,
        pendingOrders: 40,
        rejectedOrders: 0,
        totalAmount: 320000.00,
        avgAmount: 1000.00
      },
      {
        department: "儿科",
        totalOrders: 280,
        approvedOrders: 220,
        pendingOrders: 60,
        rejectedOrders: 0,
        totalAmount: 280000.00,
        avgAmount: 1000.00
      },
      {
        department: "妇产科",
        totalOrders: 200,
        approvedOrders: 100,
        pendingOrders: 100,
        rejectedOrders: 0,
        totalAmount: 200000.00,
        avgAmount: 1000.00
      }
    ];
  } catch (error) {
    console.error("获取统计数据失败:", error);
  }
};

// 初始化订单类型分布图表
const initOrderTypeChart = () => {
  const chart = echarts.init(orderTypeChart.value);
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '订单类型',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '18',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 800, name: '门诊' },
          { value: 300, name: '住院' },
          { value: 150, name: '急诊' }
        ]
      }
    ]
  };
  
  chart.setOption(option);
};

// 初始化科室统计图表
const initDepartmentChart = () => {
  const chart = echarts.init(departmentChart.value);
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'category',
      data: ['内科', '外科', '儿科', '妇产科']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '订单数量',
        type: 'bar',
        data: [450, 320, 280, 200],
        itemStyle: {
          color: '#409eff'
        }
      }
    ]
  };
  
  chart.setOption(option);
};

// 初始化趋势图表
const initTrendChart = () => {
  const chart = echarts.init(trendChart.value);
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['总订单数', '已审核', '待审核']
    },
    xAxis: {
      type: 'category',
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '总订单数',
        type: 'line',
        data: [120, 132, 101, 134, 90, 230],
        itemStyle: {
          color: '#409eff'
        }
      },
      {
        name: '已审核',
        type: 'line',
        data: [100, 120, 90, 120, 80, 200],
        itemStyle: {
          color: '#67c23a'
        }
      },
      {
        name: '待审核',
        type: 'line',
        data: [20, 12, 11, 14, 10, 30],
        itemStyle: {
          color: '#e6a23c'
        }
      }
    ]
  };
  
  chart.setOption(option);
};

// 初始化所有图表
const initCharts = async () => {
  await nextTick();
  initOrderTypeChart();
  initDepartmentChart();
  initTrendChart();
};

// 初始化
onMounted(async () => {
  await fetchStats();
  await initCharts();
});
</script>

<style scoped>
.medical-order-stats {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  height: 120px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
  margin-right: 20px;
}

.stats-info {
  flex: 1;
}

.stats-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 14px;
  color: #909399;
}

.charts-row {
  margin-bottom: 20px;
}

.chart-container {
  height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stats-table {
  margin-top: 20px;
}
</style> 