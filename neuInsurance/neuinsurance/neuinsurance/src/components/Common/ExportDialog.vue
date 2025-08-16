<template>
  <div class="export-dialog">
    <ReForm :model="formData" label-width="100px">
      <el-form-item label="导出格式">
        <el-select v-model="formData.format" placeholder="请选择导出格式">
          <el-option label="Excel (.xlsx)" value="xlsx" />
          <el-option label="CSV (.csv)" value="csv" />
          <el-option label="PDF (.pdf)" value="pdf" />
        </el-select>
      </el-form-item>
      <el-form-item label="导出范围">
        <el-radio-group v-model="formData.scope">
          <el-radio label="all">全部数据</el-radio>
          <el-radio label="selected">选中数据</el-radio>
          <el-radio label="current">当前页</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="文件名称">
        <el-input v-model="formData.filename" placeholder="请输入文件名称" />
      </el-form-item>
    </ReForm>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { ReForm } from "../index";

const formData = reactive({
  format: "xlsx",
  scope: "all",
  filename: "患者数据"
});
</script>

<style scoped>
.export-dialog {
  padding: 20px;
}
</style> 