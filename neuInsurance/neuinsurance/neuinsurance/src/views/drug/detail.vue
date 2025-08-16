<template>
  <div class="drug-detail">
    <div class="page-header">
      <el-page-header @back="goBack" title="药品管理">
        <template #content>
          <span class="page-title">药品详情</span>
        </template>
      </el-page-header>
    </div>
    
    <el-skeleton v-if="loading" animated>
      <template #template>
        <div class="loading-container">
          <el-skeleton-item variant="h1" style="width: 200px; margin-bottom: 20px" />
          <el-skeleton-item variant="rect" style="width: 100%; height: 300px" />
        </div>
      </template>
    </el-skeleton>
    
    <div v-else-if="drugData" class="detail-content">
      <el-descriptions
        title="药品基本信息"
        :column="2"
        border
        class="detail-descriptions"
      >
        <el-descriptions-item label="药品编码">
          {{ drugData.drugCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="药品名称">
          {{ drugData.drugName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="商品名">
          {{ drugData.tradeName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="药品类型">
          <el-tag :type="getDrugTypeTag(drugData.drugType)">
            {{ drugData.drugType || '-' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="自付比例">
          {{ drugData.selfPayRatio ? (drugData.selfPayRatio * 100).toFixed(2) + '%' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="规格">
          {{ drugData.specification || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="单位">
          {{ drugData.unit || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="价格">
          ¥{{ drugData.price ? drugData.price.toFixed(2) : '0.00' }}
        </el-descriptions-item>
        <el-descriptions-item label="生产厂家">
          {{ drugData.manufacturer || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="drugData.status === 1 ? 'success' : 'danger'">
            {{ drugData.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDate(drugData.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ formatDate(drugData.updateTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ drugData.createBy || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="更新人">
          {{ drugData.updateBy || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" span="2">
          {{ drugData.remark || '-' }}
        </el-descriptions-item>
      </el-descriptions>
      
      <div class="action-buttons">
        <el-button type="primary" :icon="Edit" @click="handleEdit">
          编辑药品
        </el-button>
        <el-button :icon="Back" @click="goBack">
          返回列表
        </el-button>
      </div>
    </div>
    
    <div v-else class="no-data">
      <el-empty description="未找到药品信息" />
      <div class="action-buttons">
        <el-button :icon="Back" @click="goBack">
          返回列表
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import { Edit, Back } from "@element-plus/icons-vue";
import { getDrugDetail } from "../../api/drug";
import type { Drug } from "../../api/drug";

const router = useRouter();
const route = useRoute();

const drugData = ref<Drug | null>(null);
const loading = ref(false);
const drugId = route.params.id as string;

console.log('Drug ID from route:', drugId);
console.log('Route params:', route.params);

const fetchDrugData = async () => {
  if (!drugId) {
    ElMessage.error('药品ID不存在');
    return;
  }

  loading.value = true;
  try {
    console.log('Fetching drug data for ID:', drugId);
    const response = await getDrugDetail(parseInt(drugId));
    console.log('API Response:', response);
    
    // 处理响应数据格式
    let data;
    if (response?.data) {
      data = response.data;
    } else {
      data = response;
    }
    
    if (!data) {
      throw new Error('No data received from API');
    }

    console.log('Raw data:', data);

    // 处理字段映射，兼容下划线和驼峰格式
    drugData.value = {
      id: data.id,
      drugCode: data.drugCode || data.drug_code || '',
      drugName: data.drugName || data.drug_name || '',
      tradeName: data.tradeName || data.trade_name || '',
      drugType: data.drugType || data.drug_type || '',
      selfPayRatio: data.selfPayRatio || data.self_pay_ratio || 0,
      specification: data.specification || '',
      unit: data.unit || '',
      price: data.price || 0,
      manufacturer: data.manufacturer || '',
      status: data.status !== undefined ? data.status : 1,
      createTime: data.createTime || data.create_time || '',
      updateTime: data.updateTime || data.update_time || '',
      createBy: data.createBy || data.create_by || '',
      updateBy: data.updateBy || data.update_by || '',
      remark: data.remark || ''
    };
    
    console.log('Mapped drug data:', drugData.value);
  } catch (error) {
    console.error('Error fetching drug data:', error);
    ElMessage.error('获取药品详情失败: ' + (error instanceof Error ? error.message : '未知错误'));
  } finally {
    loading.value = false;
  }
};

const formatDate = (date: string | null): string => {
  if (!date) return '-';
  try {
    return new Date(date).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  } catch (error) {
    return date;
  }
};

const getDrugTypeTag = (type: string) => {
  switch (type) {
    case '甲类':
      return 'success';
    case '乙类':
      return 'warning';
    case '丙类':
      return 'info';
    default:
      return 'info';
  }
};

const handleEdit = () => {
  if (drugData.value && drugData.value.id) {
    router.push({
      name: 'DrugEdit',
      params: { id: drugData.value.id.toString() }
    });
  } else {
    ElMessage.warning('无法编辑：药品ID不存在');
  }
};

const goBack = () => {
  router.push("/drug/list");
};

onMounted(() => {
  console.log('Component mounted, fetching drug data');
  fetchDrugData();
});
</script>

<style scoped>
.drug-detail {
  padding: 16px;
}

.page-header {
  margin-bottom: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.detail-content {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.detail-descriptions {
  margin-bottom: 24px;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  color: #606266;
  width: 120px;
}

:deep(.el-descriptions__content) {
  color: #303133;
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.loading-container {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.no-data {
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.no-data .action-buttons {
  border-top: none;
  margin-top: 20px;
  padding-top: 0;
}

/* 移动端响应式 */
@media (max-width: 768px) {
  .drug-detail {
    padding: 8px;
  }
  
  .detail-content {
    padding: 16px;
  }
  
  :deep(.el-descriptions) {
    --el-descriptions-table-border: 1px solid #ebeef5;
  }
  
  :deep(.el-descriptions__label) {
    width: 100px;
    font-size: 14px;
  }
  
  :deep(.el-descriptions__content) {
    font-size: 14px;
  }
  
  .action-buttons {
    flex-direction: column;
    gap: 8px;
  }
  
  .action-buttons .el-button {
    width: 100%;
  }
}
</style> 