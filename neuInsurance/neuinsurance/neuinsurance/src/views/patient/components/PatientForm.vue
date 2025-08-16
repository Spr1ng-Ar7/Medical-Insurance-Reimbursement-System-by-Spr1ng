<template>
  <div class="patient-form">
    <BaseForm
      :model="formData"
      :validate-on-rule-change="false"
      :rules="rules"
      :submit-loading="submitLoading"
      :submit-text="submitText"
      @submit="handleSubmit"
      @cancel="handleCancel"
    >
      <template #default>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="name">
              <el-input
                v-model="formData.name"
                placeholder="请输入患者姓名"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input
                v-model="formData.idCard"
                placeholder="请输入身份证号"
                clearable
                @blur="handleIdCardBlur"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input
                v-model="formData.phone"
                placeholder="请输入手机号码"
                clearable
                @blur="handlePhoneBlur"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="formData.gender" placeholder="请选择性别">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input-number
                v-model="formData.age"
                :min="0"
                :max="150"
                placeholder="请输入年龄"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保险类型" prop="insuranceType">
              <el-select
                v-model="formData.insuranceType"
                placeholder="请选择保险类型"
              >
                <el-option
                  v-for="type in insuranceTypes"
                  :key="type"
                  :label="type"
                  :value="type"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="保险号" prop="insuranceNumber">
              <el-input
                v-model="formData.insuranceNumber"
                placeholder="请输入保险号"
                clearable
                @blur="handleInsuranceCardBlur"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="地址" prop="address">
          <el-input
            v-model="formData.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
      </template>
    </BaseForm>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from "vue";
import { ElMessage } from "element-plus";
import BaseForm from "@/components/Common/BaseForm.vue";
import { getAllInsuranceTypes, checkIdCard, checkPhone, checkInsuranceCard } from "@/api/patient";
import type { Patient } from "@/api/patient";

interface Props {
  modelValue: Patient;
  submitLoading?: boolean;
  submitText?: string;
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => ({
    name: "",
    idCard: "",
    phone: "",
    gender: "",
    age: 0,
    address: "",
    insuranceType: "",
    insuranceNumber: ""
  }),
  submitLoading: false,
  submitText: "保存"
});

const emit = defineEmits<{
  "update:modelValue": [value: Patient];
  submit: [data: Patient];
  cancel: [];
}>();

const formData = reactive<Patient>({ ...props.modelValue });

// 同步外部 v-model 更新
watch(
  () => props.modelValue,
  newVal => {
    if (newVal) Object.assign(formData, newVal);
  },
  { deep: true }
);
const insuranceTypes = ref<string[]>([]);

// 表单验证规则
const rules = {
  name: [
    { required: true, message: "请输入患者姓名", trigger: "blur" }
  ],
  idCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    { pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, message: "请输入正确的身份证号", trigger: "blur" }
  ],
  phone: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
  gender: [
    { required: true, message: "请选择性别", trigger: "change" }
  ],
  age: [
    { required: true, message: "请输入年龄", trigger: "blur" }
  ],
  insuranceType: [
    { required: true, message: "请选择保险类型", trigger: "change" }
  ],
  insuranceNumber: [
    { required: true, message: "请输入保险号", trigger: "blur" }
  ],
  address: [
    { required: true, message: "请输入地址", trigger: "blur" }
  ]
};

// 提交表单
const handleSubmit = (data: Patient) => {
  emit("submit", data);
};

// 取消
const handleCancel = () => {
  emit("cancel");
};

// 身份证号失焦验证
const handleIdCardBlur = async () => {
  if (formData.idCard && formData.idCard.length === 18) {
    try {
      const exists = await checkIdCard(formData.idCard);
      if (exists) {
        ElMessage.warning("该身份证号已存在");
      }
    } catch (error) {
      console.error("验证身份证号失败:", error);
    }
  }
};

// 手机号失焦验证
const handlePhoneBlur = async () => {
  if (formData.phone && formData.phone.length === 11) {
    try {
      const exists = await checkPhone(formData.phone);
      if (exists) {
        ElMessage.warning("该手机号已存在");
      }
    } catch (error) {
      console.error("验证手机号失败:", error);
    }
  }
};

// 保险号失焦验证
const handleInsuranceCardBlur = async () => {
  if (formData.insuranceNumber) {
    try {
      const exists = await checkInsuranceCard(formData.insuranceNumber);
      if (exists) {
        ElMessage.warning("该保险号已存在");
      }
    } catch (error) {
      console.error("验证保险号失败:", error);
    }
  }
};

// 获取保险类型
const fetchInsuranceTypes = async () => {
  try {
    console.log('开始获取保险类型...');
    const result = await getAllInsuranceTypes();
    console.log('获取到的保险类型:', result);
    
    // API已经处理好了数据格式，直接使用
    insuranceTypes.value = result || ['城镇职工', '城乡居民', '新农合', '医疗救助', '商业保险'];
    console.log('最终保险类型列表:', insuranceTypes.value);
  } catch (error) {
    console.error("获取保险类型失败:", error);
    // 设置默认保险类型
    insuranceTypes.value = ['城镇职工', '城乡居民', '新农合', '医疗救助', '商业保险'];
  }
};

onMounted(() => {
  fetchInsuranceTypes();
});
</script>

<style scoped>
.patient-form {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
}
</style> 