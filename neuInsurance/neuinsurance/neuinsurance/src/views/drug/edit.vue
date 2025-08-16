<template>
  <div class="drug-edit">
    <div class="page-header">
      <el-page-header @back="goBack" title="药品管理">
        <template #content>
          <span class="page-title">编辑药品</span>
        </template>
      </el-page-header>
    </div>
    
    <DrugForm
      v-if="drugData"
      v-model="formData"
      :submit-loading="submitLoading"
      submit-text="更新药品"
      @submit="handleSubmit"
      @cancel="goBack"
    />
    
    <div v-else class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import DrugForm from "./components/DrugForm.vue";
import { getDrugDetail, updateDrug } from "@/api/drug";
import type { Drug } from "@/api/drug";

const router = useRouter();
const route = useRoute();

const submitLoading = ref(false);
const drugData = ref<Drug | null>(null);
const formData = reactive<Drug>({
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
  remark: ""
});

const drugId = route.params.id as string;

const fetchDrugData = async () => {
  try {
    const data = await getDrugDetail(parseInt(drugId));
    drugData.value = {
       id: data.id,
       drugCode: data.drug_code || data.drugCode,
       drugName: data.drug_name || data.drugName,
       tradeName: data.trade_name || data.tradeName,
       drugType: data.drug_type || data.drugType,
       selfPayRatio: data.self_pay_ratio ?? data.selfPayRatio,
       specification: data.specification,
       manufacturer: data.manufacturer,
       unit: data.unit,
       price: data.price,
       status: data.status,
       remark: data.remark,
       createTime: data.create_time || data.createTime,
       updateTime: data.update_time || data.updateTime,
     };
     Object.assign(formData, drugData.value);
  } catch (error) {
    ElMessage.error("获取药品信息失败");
    goBack();
  }
};

const handleSubmit = async (data: Drug) => {
  submitLoading.value = true;
  try {
    const updateData = { ...data, id: parseInt(drugId) };
    await updateDrug(updateData);
    ElMessage.success("更新药品成功");
    goBack();
  } catch (error) {
    console.error('Update error:', error);
    ElMessage.error("更新药品失败");
  } finally {
    submitLoading.value = false;
  }
};

const goBack = () => {
  router.push("/drug/list");
};

onMounted(() => {
  fetchDrugData();
});
</script>

<style scoped>
.drug-edit {
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

.loading-container {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}
</style> 