<template>
  <div class="drug-add">
    <div class="page-header">
      <div class="pure-page-header">
        <ReButton iconName="ep:back" buttonText="返回" @click="goBack" link />
        <span class="page-title">新增药品</span>
      </div>
    </div>

    <DrugForm
      v-model="formData"
      :submit-loading="submitLoading"
      submit-text="新增药品"
      @submit="handleSubmit"
      @cancel="goBack"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import DrugForm from "./components/DrugForm.vue";
import { addDrug } from "../../api/drug";
import type { Drug } from "../../api/drug";
import { ReButton } from "../../components";

const router = useRouter();

const submitLoading = ref(false);
const formData = reactive({
  drugCode: "",
  drugName: "",
  tradeName: "",
  drugType: "",
  selfPayRatio: 0,
  specification: "",
  unit: "",
  price: 0,
  manufacturer: "",
  status: 1,
  remark: "",
});

const handleSubmit = async (data: Drug) => {
  submitLoading.value = true;
  try {
    console.log('提交药品数据:', data);
    
    // 数据校验
    if (!data.drugCode || !data.drugName || !data.drugType || !data.manufacturer) {
      ElMessage.error("请填写必填字段");
      return;
    }
    
    // 确保数字字段的格式正确
    const submitData = {
      ...data,
      price: Number(data.price) || 0,
      selfPayRatio: Number(data.selfPayRatio) || 0,
      status: Number(data.status) || 1
    };
    
    console.log('处理后的提交数据:', submitData);
    
    const result = await addDrug(submitData);
    console.log('新增结果:', result);
    
    ElMessage.success("新增药品成功");
    
    // 等待一小段时间确保后端处理完成，然后跳转
    setTimeout(() => {
      goBack();
    }, 500);
    
  } catch (error) {
    console.error('新增药品失败:', error);
    ElMessage.error("新增药品失败: " + (error instanceof Error ? error.message : '未知错误'));
  } finally {
    submitLoading.value = false;
  }
};

const goBack = () => {
  router.push("/drug/list");
};
</script>

<style scoped>
.drug-add {
  padding: 16px;
}

.page-header {
  margin-bottom: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
</style>