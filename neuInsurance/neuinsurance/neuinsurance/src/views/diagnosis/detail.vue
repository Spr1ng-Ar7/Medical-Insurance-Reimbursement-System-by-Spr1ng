<template>
  <div class="diagnosis-detail">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="诊断名称">{{ detail.name }}</el-descriptions-item>
      <el-descriptions-item label="ICD编码">{{ detail.icd }}</el-descriptions-item>
      <el-descriptions-item label="疾病类型">{{ detail.type }}</el-descriptions-item>
      <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
const detail = ref({});
</script> 