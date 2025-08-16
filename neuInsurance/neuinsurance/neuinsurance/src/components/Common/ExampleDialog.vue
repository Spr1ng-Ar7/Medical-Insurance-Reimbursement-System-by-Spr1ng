<template>
  <div class="example-dialog">
    <ReForm :model="formData" label-width="100px">
      <el-form-item label="标题">
        <el-input v-model="formData.title" placeholder="请输入标题" />
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入描述" />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="formData.type" placeholder="请选择类型">
          <el-option label="类型一" value="type1" />
          <el-option label="类型二" value="type2" />
          <el-option label="类型三" value="type3" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="formData.status" />
      </el-form-item>
    </ReForm>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import { ReForm } from "../index";

const formData = reactive({
  title: "",
  description: "",
  type: "",
  status: false
});
</script>

<style scoped>
.example-dialog {
  padding: 20px;
}
</style> 