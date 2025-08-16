<template>
  <div class="drug-stats">
    <div class="page-header">
      <el-page-header @back="goBack" title="药品管理">
        <template #content>
          <span class="page-title">药品统计</span>
        </template>
      </el-page-header>
    </div>
    
    <div class="stats-content">
      <!-- 统计卡片 -->
      <el-row :gutter="16" class="stats-cards">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon type-icon">
                <el-icon><GoodsFilled /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.totalDrugs || 0 }}</div>
                <div class="stat-label">药品总数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon active-icon">
                <el-icon><Check /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.activeDrugs || 0 }}</div>
                <div class="stat-label">启用药品</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon inactive-icon">
                <el-icon><Close /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stats.inactiveDrugs || 0 }}</div>
                <div class="stat-label">禁用药品</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-icon price-icon">
                <el-icon><Money /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">¥{{ stats.avgPrice?.toFixed(2) || '0.00' }}</div>
                <div class="stat-label">平均价格</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 图表区域 -->
      <el-row :gutter="16" class="charts-row">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <span>药品类型分布</span>
            </template>
            <div ref="typeChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <span>价格区间分布</span>
            </template>
            <div ref="priceChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="16" class="charts-row">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <span>生产厂家TOP10</span>
            </template>
            <div ref="manufacturerChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <span>月度新增趋势</span>
            </template>
            <div ref="trendChartRef" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { GoodsFilled, Check, Close, Money } from "@element-plus/icons-vue";
import * as echarts from "echarts";
import {
  getDrugComprehensiveStatistics,
  getDrugTypeStatistics,
  getDrugPriceStatistics,
  getManufacturerStatistics
} from "@/api/drug";

const router = useRouter();

// 图表引用
const typeChartRef = ref<HTMLElement>();
const priceChartRef = ref<HTMLElement>();
const manufacturerChartRef = ref<HTMLElement>();
const trendChartRef = ref<HTMLElement>();

// 图表实例
let typeChart: echarts.ECharts | null = null;
let priceChart: echarts.ECharts | null = null;
let manufacturerChart: echarts.ECharts | null = null;
let trendChart: echarts.ECharts | null = null;

// 统计数据
const stats = ref({
  totalDrugs: 0,
  activeDrugs: 0,
  inactiveDrugs: 0,
  avgPrice: 0
});

// 获取统计数据
const fetchStats = async () => {
  try {
    const data = await getDrugComprehensiveStatistics();
    stats.value = data;
  } catch (error) {
    ElMessage.error("获取统计数据失败");
  }
};

// 初始化药品类型图表
const initTypeChart = async () => {
  if (!typeChartRef.value) return;
  
  try {
    const data = await getDrugTypeStatistics();
    const arr = Array.isArray(data) ? data : [];
    typeChart = echarts.init(typeChartRef.value);
    
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
          name: '药品类型',
          type: 'pie',
          radius: '50%',
          data: arr.map(item => ({
            name: item.type,
            value: item.count
          })),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    
    typeChart.setOption(option);
  } catch (error) {
    console.error("初始化药品类型图表失败:", error);
  }
};

// 初始化价格区间图表
const initPriceChart = async () => {
  if (!priceChartRef.value) return;
  
  try {
    const data = await getDrugPriceStatistics();
    const arr = Array.isArray(data) ? data : [];
    priceChart = echarts.init(priceChartRef.value);
    
    const option = {
      tooltip: {
        trigger: 'axis'
      },
      xAxis: {
        type: 'category',
        data: arr.map(item => item.range)
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '药品数量',
          type: 'bar',
          data: arr.map(item => item.count),
          itemStyle: {
            color: '#409eff'
          }
        }
      ]
    };
    
    priceChart.setOption(option);
  } catch (error) {
    console.error("初始化价格区间图表失败:", error);
  }
};

// 初始化生产厂家图表
const initManufacturerChart = async () => {
  if (!manufacturerChartRef.value) return;
  
  try {
    const data = await getManufacturerStatistics();
    const arr = Array.isArray(data) ? data : [];
    manufacturerChart = echarts.init(manufacturerChartRef.value);
    
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      xAxis: {
        type: 'value'
      },
      yAxis: {
        type: 'category',
        data: arr.map(item => item.manufacturer)
      },
      series: [
        {
          name: '药品数量',
          type: 'bar',
          data: arr.map(item => item.count),
          itemStyle: {
            color: '#67c23a'
          }
        }
      ]
    };
    
    manufacturerChart.setOption(option);
  } catch (error) {
    console.error("初始化生产厂家图表失败:", error);
  }
};

// 初始化趋势图表
const initTrendChart = () => {
  if (!trendChartRef.value) return;
  
  trendChart = echarts.init(trendChartRef.value);
  
  // 模拟数据，实际应该从API获取
  const option = {
    tooltip: {
      trigger: 'axis'
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
        name: '新增药品',
        type: 'line',
        data: [12, 19, 15, 25, 22, 30],
        smooth: true,
        itemStyle: {
          color: '#e6a23c'
        }
      }
    ]
  };
  
  trendChart.setOption(option);
};

// 初始化所有图表
const initCharts = () => {
  initTypeChart();
  initPriceChart();
  initManufacturerChart();
  initTrendChart();
};

// 响应式处理
const handleResize = () => {
  typeChart?.resize();
  priceChart?.resize();
  manufacturerChart?.resize();
  trendChart?.resize();
};

const goBack = () => {
  router.push("/drug/list");
};

onMounted(() => {
  fetchStats();
  initCharts();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  typeChart?.dispose();
  priceChart?.dispose();
  manufacturerChart?.dispose();
  trendChart?.dispose();
});
</script>

<style scoped>
.drug-stats {
  padding: 16px;
}

.page-header {
  margin-bottom: 16px;
  padding: 16px;
  background: var(--el-bg-color, #222);
  border-radius: 8px;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary, #fff);
}

.stats-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.stats-cards {
  margin-bottom: 16px;
}

.stat-card {
  background: var(--el-bg-color, #222);
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light, 0 2px 8px rgba(0,0,0,0.08));
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: #fff;
}

.type-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.active-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.inactive-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.price-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary, #fff);
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-secondary, #909399);
}

.charts-row {
  margin-bottom: 16px;
}

.chart-card {
  background: var(--el-bg-color, #222);
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light, 0 2px 8px rgba(0,0,0,0.08));
}

.chart-container {
  height: 300px;
  width: 100%;
}
</style>