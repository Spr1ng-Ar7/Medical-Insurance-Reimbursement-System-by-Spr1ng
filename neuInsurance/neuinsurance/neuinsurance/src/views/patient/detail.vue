<template>
  <div class="patient-detail">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>患者详情</span>
          <div class="header-actions">
            <el-button type="primary" @click="handleEdit">编辑</el-button>
            <el-button @click="handleBack">返回</el-button>
          </div>
        </div>
      </template>
      
      <div v-loading="loading" class="detail-content">
        <el-descriptions :column="2" border size="large">
          <el-descriptions-item label="患者姓名">
            <el-tag type="primary" size="large">{{ patientInfo.name || '-' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="身份证号">
            {{ formatIdCard(patientInfo.idCard) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="手机号码">
            {{ formatPhone(patientInfo.phone) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="性别">
            <el-tag :type="patientInfo.gender === '男' ? 'primary' : 'success'">
              <el-icon style="margin-right: 4px;">
                <Male v-if="patientInfo.gender === '男'" />
                <Female v-else />
              </el-icon>
              {{ patientInfo.gender || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="出生日期">
            {{ patientInfo.birthDate || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="年龄">
            {{ calculatedAge || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="保险类型">
            <el-tag :type="getInsuranceTypeColor(patientInfo.insuranceType)">
              {{ patientInfo.insuranceType || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="保险号">
            {{ patientInfo.insuranceNumber || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="patientInfo.status === 1 ? 'success' : 'danger'">
              {{ patientInfo.status === 1 ? '正常' : '注销' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDateTime(patientInfo.createTime) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDateTime(patientInfo.updateTime) || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="联系地址" :span="2">
            {{ patientInfo.address || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="2" v-if="patientInfo.remark">
            {{ patientInfo.remark }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import { Male, Female } from "@element-plus/icons-vue";
import { getPatientDetail, type Patient } from "../../api/patient";

const router = useRouter();
const route = useRoute();
const loading = ref(false);

const patientInfo = reactive<Patient>({
  id: undefined,
  name: "",
  idCard: "",
  phone: "",
  gender: "",
  birthDate: "",
  address: "",
  insuranceType: "",
  insuranceNumber: "",
  status: 1,
  createTime: "",
  updateTime: "",
  remark: ""
});

// 计算年龄
const calculatedAge = computed(() => {
  if (!patientInfo.birthDate) return '';
  
  const birthDate = new Date(patientInfo.birthDate);
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();
  
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
    age--;
  }
  
  return age >= 0 ? `${age} 岁` : '';
});

// 格式化身份证号
const formatIdCard = (idCard: string) => {
  if (!idCard) return "";
  if (idCard.length === 18) {
    return idCard.replace(/(\d{6})(\d{8})(\d{4})/, "$1********$3");
  }
  return idCard;
};

// 格式化手机号
const formatPhone = (phone: string) => {
  if (!phone) return "";
  if (phone.length === 11) {
    return phone.replace(/(\d{3})(\d{4})(\d{4})/, "$1****$3");
  }
  return phone;
};

// 获取保险类型颜色
const getInsuranceTypeColor = (type: string) => {
  const colorMap: Record<string, "primary" | "success" | "warning" | "info"> = {
    "城镇职工": "primary",
    "城乡居民": "success", 
    "新农合": "warning",
    "医疗救助": "info",
    "商业保险": "info"
  };
  return colorMap[type] || "info";
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

// 获取患者详情
const fetchPatientDetail = async (id: number) => {
  loading.value = true;
  try {
    const result = await getPatientDetail(id);
    console.log('患者详情数据:', result);
    
    Object.assign(patientInfo, result);
  } catch (error: any) {
    console.error('获取患者详情失败:', error);
    ElMessage.error(error?.message || "获取患者详情失败");
    router.back();
  } finally {
    loading.value = false;
  }
};

const handleEdit = () => {
  router.push(`/patient/edit/${route.params.id}`);
};

const handleBack = () => {
  router.back();
};

onMounted(() => {
  const id = Number(route.params.id);
  if (!id) {
    ElMessage.error("患者ID无效");
    router.back();
    return;
  }
  
  fetchPatientDetail(id);
});
</script>

<style scoped>
.patient-detail {
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
</style> 