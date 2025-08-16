<template>
  <div class="patient-search">
    <el-form :model="searchForm" inline>
      <el-form-item label="患者姓名">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入患者姓名"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="身份证号">
        <el-input
          v-model="searchForm.idCard"
          placeholder="请输入身份证号"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input
          v-model="searchForm.phone"
          placeholder="请输入手机号码"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="保险类型">
        <el-select
          v-model="searchForm.insuranceType"
          placeholder="请选择保险类型"
          clearable
        >
          <el-option
            v-for="type in insuranceTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
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
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { Search, Refresh } from "@element-plus/icons-vue";
import { getAllInsuranceTypes } from "@/api/patient";
import type { PatientQueryParams } from "@/api/patient";

interface Props {
  modelValue: PatientQueryParams;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  "update:modelValue": [value: PatientQueryParams];
  search: [params: PatientQueryParams];
  reset: [];
}>();

const searchForm = reactive<PatientQueryParams>({ ...props.modelValue });
const insuranceTypes = ref<string[]>([]);

// 搜索
const handleSearch = () => {
  emit("update:modelValue", { ...searchForm });
  emit("search", { ...searchForm });
};

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    if (key !== 'page' && key !== 'size') {
      delete searchForm[key as keyof PatientQueryParams];
    }
  });
  emit("update:modelValue", { ...searchForm });
  emit("reset");
  emit("search", { ...searchForm });
};

// 获取保险类型
const fetchInsuranceTypes = async () => {
  try {
    const result = await getAllInsuranceTypes();
    insuranceTypes.value = result || [];
  } catch (error) {
    console.error("获取保险类型失败:", error);
  }
};

onMounted(() => {
  fetchInsuranceTypes();
});
</script>

<style scoped>
.patient-search {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 6px;
  margin-bottom: 16px;
}
</style> 