<template>
  <div class="order-search">
    <el-form :model="searchForm" inline>
      <el-form-item label="订单号">
        <el-input
          v-model="searchForm.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="患者姓名">
        <el-input
          v-model="searchForm.patientName"
          placeholder="请输入患者姓名"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="订单状态">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择订单状态"
          clearable
        >
          <el-option
            v-for="status in orderStatuses"
            :key="status"
            :label="status"
            :value="status"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="结算状态">
        <el-select
          v-model="searchForm.settlementStatus"
          placeholder="请选择结算状态"
          clearable
        >
          <el-option
            v-for="status in settlementStatuses"
            :key="status"
            :label="status"
            :value="status"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="关键词">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入关键词"
          clearable
          @keyup.enter="handleSearch"
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
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { Search, Refresh } from "@element-plus/icons-vue";
import { getAllOrderStatuses, getAllSettlementStatuses } from "@/api/medical-order";
import type { MedicalOrderQueryParams } from "@/api/medical-order";

interface Props {
  modelValue: MedicalOrderQueryParams;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  "update:modelValue": [value: MedicalOrderQueryParams];
  search: [params: MedicalOrderQueryParams];
  reset: [];
}>();

const searchForm = reactive<MedicalOrderQueryParams>({ ...props.modelValue });
const orderStatuses = ref<string[]>([]);
const settlementStatuses = ref<string[]>([]);

// 搜索
const handleSearch = () => {
  emit("update:modelValue", { ...searchForm });
  emit("search", { ...searchForm });
};

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    if (key !== 'pageNum' && key !== 'pageSize') {
      delete searchForm[key as keyof MedicalOrderQueryParams];
    }
  });
  emit("update:modelValue", { ...searchForm });
  emit("reset");
  emit("search", { ...searchForm });
};

// 获取订单状态
const fetchOrderStatuses = async () => {
  try {
    const result = await getAllOrderStatuses();
    orderStatuses.value = result || [];
  } catch (error) {
    console.error("获取订单状态失败:", error);
  }
};

// 获取结算状态
const fetchSettlementStatuses = async () => {
  try {
    const result = await getAllSettlementStatuses();
    settlementStatuses.value = result || [];
  } catch (error) {
    console.error("获取结算状态失败:", error);
  }
};

onMounted(() => {
  fetchOrderStatuses();
  fetchSettlementStatuses();
});
</script>

<style scoped>
.order-search {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 6px;
  margin-bottom: 16px;
}
</style> 