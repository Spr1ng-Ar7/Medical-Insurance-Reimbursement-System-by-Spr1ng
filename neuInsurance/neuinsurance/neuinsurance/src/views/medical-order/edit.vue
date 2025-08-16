<template>
  <div class="medical-order-edit">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>编辑医疗订单</span>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
        @submit.prevent="handleSubmit"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="patientName">
              <el-input
                v-model="formData.patientName"
                placeholder="请输入患者姓名"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="患者身份证号" prop="patientIdCard">
              <el-input
                v-model="formData.patientIdCard"
                placeholder="请输入身份证号"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单类型" prop="orderType">
              <el-select v-model="formData.orderType" placeholder="请选择订单类型">
                <el-option label="门诊" value="门诊" />
                <el-option label="住院" value="住院" />
                <el-option label="急诊" value="急诊" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="科室" prop="department">
              <el-select v-model="formData.department" placeholder="请选择科室">
                <el-option label="内科" value="内科" />
                <el-option label="外科" value="外科" />
                <el-option label="儿科" value="儿科" />
                <el-option label="妇产科" value="妇产科" />
                <el-option label="急诊科" value="急诊科" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主治医生" prop="doctorName">
              <el-input
                v-model="formData.doctorName"
                placeholder="请输入主治医生姓名"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单状态" prop="status">
              <el-select v-model="formData.status" placeholder="请选择状态">
                <el-option label="待审核" :value="0" />
                <el-option label="已审核" :value="1" />
                <el-option label="已结算" :value="2" />
                <el-option label="已取消" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input
            v-model="formData.diagnosis"
            type="textarea"
            :rows="3"
            placeholder="请输入诊断结果"
          />
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            保存
          </el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import { getMedicalOrderDetail, updateMedicalOrder } from "@/api/medical-order";
import type { FormInstance, FormRules } from "element-plus";

const router = useRouter();
const route = useRoute();
const formRef = ref<FormInstance>();
const submitLoading = ref(false);

// 表单数据
const formData = reactive({
  id: "",
  patientName: "",
  patientIdCard: "",
  orderType: "",
  department: "",
  doctorName: "",
  diagnosis: "",
  status: 0,
  remark: ""
});

// 表单验证规则
const rules: FormRules = {
  patientName: [
    { required: true, message: "请输入患者姓名", trigger: "blur" }
  ],
  patientIdCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: "请输入正确的身份证号", trigger: "blur" }
  ],
  orderType: [
    { required: true, message: "请选择订单类型", trigger: "change" }
  ],
  department: [
    { required: true, message: "请选择科室", trigger: "change" }
  ],
  doctorName: [
    { required: true, message: "请输入主治医生姓名", trigger: "blur" }
  ],
  diagnosis: [
    { required: true, message: "请输入诊断结果", trigger: "blur" }
  ]
};

// 获取订单详情
const fetchOrderDetail = async (id: string) => {
  try {
    // 这里调用获取详情API
    const result = await getMedicalOrderDetail(Number(id));
    Object.assign(formData, result as any);
    return;
    
    // 模拟数据
    Object.assign(formData, {
      id,
      patientName: "张三",
      patientIdCard: "110101199001011234",
      orderType: "门诊",
      department: "内科",
      doctorName: "李医生",
      diagnosis: "感冒",
      status: 1,
      remark: "患者感冒症状明显"
    });
  } catch (error) {
    ElMessage.error("获取订单详情失败");
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitLoading.value = true;
    
    // 调用更新API
    await updateMedicalOrder(formData as any);
    
    ElMessage.success("更新成功");
    router.push("/medical-order/list");
  } catch (error) {
    console.error("表单验证失败:", error);
  } finally {
    submitLoading.value = false;
  }
};

// 取消
const handleCancel = () => {
  router.back();
};

// 初始化
onMounted(() => {
  const id = route.params.id as string;
  if (id) {
    formData.id = id;
    fetchOrderDetail(id);
  }
});
</script>

<style scoped>
.medical-order-edit {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 