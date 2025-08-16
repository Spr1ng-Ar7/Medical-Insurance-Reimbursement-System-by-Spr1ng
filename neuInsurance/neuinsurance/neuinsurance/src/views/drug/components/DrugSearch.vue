<template>
  <div class="drug-search">
    <el-form :model="searchForm" inline>
      <el-form-item label="药品名称">
        <el-input
          v-model="searchForm.drugName"
          placeholder="请输入药品名称"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="药品类型">
        <el-select
          v-model="searchForm.drugType"
          placeholder="请选择药品类型"
          clearable
        >
          <el-option label="甲类" value="甲类" />
          <el-option label="乙类" value="乙类" />
          <el-option label="丙类" value="丙类" />
        </el-select>
      </el-form-item>
      <el-form-item label="药品状态">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择状态"
          clearable
        >
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
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
import { reactive } from "vue";
import { Search, Refresh } from "@element-plus/icons-vue";
import type { DrugQueryParams } from "@/api/drug";

interface Props {
  modelValue: DrugQueryParams;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  "update:modelValue": [value: DrugQueryParams];
  search: [params: DrugQueryParams];
  reset: [];
}>();

const searchForm = reactive<DrugQueryParams>({ ...props.modelValue });

// 搜索
const handleSearch = () => {
  emit("update:modelValue", { ...searchForm });
  emit("search", { ...searchForm });
};

// 重置
const handleReset = () => {
  Object.keys(searchForm).forEach(key => {
    if (key !== 'pageNum' && key !== 'pageSize') {
      delete searchForm[key as keyof DrugQueryParams];
    }
  });
  emit("update:modelValue", { ...searchForm });
  emit("reset");
  emit("search", { ...searchForm });
};
</script>

<style scoped>
.drug-search {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 6px;
  margin-bottom: 16px;
}
</style> 