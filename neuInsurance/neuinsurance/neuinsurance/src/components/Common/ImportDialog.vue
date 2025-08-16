<template>
  <div class="import-dialog">
    <ReForm :model="formData" label-width="100px">
      <el-form-item label="导入方式">
        <el-radio-group v-model="formData.importType">
          <el-radio label="file">文件导入</el-radio>
          <el-radio label="manual">手动输入</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item v-if="formData.importType === 'file'" label="选择文件">
        <el-upload
          class="upload-demo"
          drag
          action="#"
          :auto-upload="false"
          :on-change="handleFileChange"
          accept=".xlsx,.xls,.csv"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传 xlsx/xls/csv 文件，且不超过 10MB
            </div>
          </template>
        </el-upload>
      </el-form-item>
      
      <el-form-item v-if="formData.importType === 'manual'" label="药品数据">
        <el-input
          v-model="formData.manualData"
          type="textarea"
          :rows="8"
          placeholder="请输入药品数据，格式：药品名称,分类,规格,价格,库存,生产厂家,描述"
        />
      </el-form-item>
      
      <el-form-item label="导入选项">
        <el-checkbox v-model="formData.skipDuplicate">跳过重复数据</el-checkbox>
        <el-checkbox v-model="formData.updateExisting">更新已存在数据</el-checkbox>
      </el-form-item>
      
      <el-form-item label="数据预览" v-if="previewData.length > 0">
        <div class="preview-table">
          <el-table :data="previewData" size="small" max-height="200">
            <el-table-column prop="name" label="药品名称" />
            <el-table-column prop="category" label="分类" />
            <el-table-column prop="specification" label="规格" />
            <el-table-column prop="price" label="价格" />
            <el-table-column prop="stock" label="库存" />
          </el-table>
        </div>
      </el-form-item>
    </ReForm>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { UploadFilled } from "@element-plus/icons-vue";
import { ReForm } from "../index";

const formData = reactive({
  importType: "file",
  manualData: "",
  skipDuplicate: true,
  updateExisting: false
});

const previewData = ref([]);

const handleFileChange = (file: any) => {
  // 模拟文件解析
  previewData.value = [
    { name: "示例药品1", category: "非处方药", specification: "100mg*30片", price: 15.80, stock: 150 },
    { name: "示例药品2", category: "中药", specification: "10g*12袋", price: 18.90, stock: 200 }
  ];
};

watch(() => formData.manualData, (newVal) => {
  if (newVal && formData.importType === "manual") {
    // 模拟手动数据解析
    const lines = newVal.split('\n').filter(line => line.trim());
    previewData.value = lines.slice(0, 5).map(line => {
      const parts = line.split(',');
      return {
        name: parts[0] || "",
        category: parts[1] || "",
        specification: parts[2] || "",
        price: parseFloat(parts[3]) || 0,
        stock: parseInt(parts[4]) || 0
      };
    });
  }
});
</script>

<style scoped>
.import-dialog {
  padding: 20px;
}

.preview-table {
  border: 1px solid var(--el-border-color-light);
  border-radius: 4px;
  padding: 10px;
}

.upload-demo {
  width: 100%;
}
</style> 