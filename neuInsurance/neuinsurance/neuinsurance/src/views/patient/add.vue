<template>
  <div class="patient-add">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>新增患者</span>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
        class="patient-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="患者姓名" prop="name">
              <el-input v-model="formData.name" placeholder="请输入患者姓名" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="身份证号" prop="idCard">
              <el-input 
                v-model="formData.idCard" 
                placeholder="请输入身份证号" 
                clearable 
                @blur="onIdCardChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号码" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="formData.gender" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" value="男" />
                <el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="formData.birthDate"
                type="date"
                placeholder="请选择出生日期"
                style="width: 100%"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                @change="onBirthDateChange"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄">
              <el-input :value="calculatedAge" readonly placeholder="自动计算" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="保险类型" prop="insuranceType">
              <el-select v-model="formData.insuranceType" placeholder="请选择保险类型" style="width: 100%">
                <el-option 
                  v-for="type in insuranceTypes" 
                  :key="type" 
                  :label="type" 
                  :value="type" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="保险号" prop="insuranceNumber">
              <el-input v-model="formData.insuranceNumber" placeholder="请输入保险号" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="联系地址" prop="address">
          <el-input 
            v-model="formData.address" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入详细地址" 
          />
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input 
            v-model="formData.remark" 
            type="textarea" 
            :rows="2" 
            placeholder="备注信息（可选）" 
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="onSubmit">
            <el-icon><Check /></el-icon>
            保存
          </el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { Check } from "@element-plus/icons-vue";
import { addPatient, getAllInsuranceTypes, type Patient } from "../../api/patient";

const router = useRouter();
const formRef = ref();
const loading = ref(false);
const insuranceTypes = ref<string[]>([]);

// 表单数据
const formData = reactive<Patient>({
  name: "",
  idCard: "",
  phone: "",
  gender: "",
  birthDate: "",
  address: "",
  insuranceType: "",
  insuranceNumber: "",
  remark: ""
});

// 计算年龄
const calculatedAge = computed(() => {
  if (!formData.birthDate) return '';
  
  const birthDate = new Date(formData.birthDate);
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const monthDiff = today.getMonth() - birthDate.getMonth();
  
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
    age--;
  }
  
  return age >= 0 ? `${age} 岁` : '';
});

// 根据身份证号计算年龄
const calculateAgeFromIdCard = (idCard: string) => {
  if (!idCard || idCard.length !== 18) return;
  
  const year = parseInt(idCard.substring(6, 10));
  const month = parseInt(idCard.substring(10, 12));
  const day = parseInt(idCard.substring(12, 14));
  
  if (year < 1900 || year > new Date().getFullYear()) return;
  if (month < 1 || month > 12) return;
  if (day < 1 || day > 31) return;
  
  // 自动设置出生日期
  formData.birthDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  
  // 根据身份证号设置性别
  const genderCode = parseInt(idCard.substring(16, 17));
  formData.gender = genderCode % 2 === 1 ? '男' : '女';
};

// 身份证号变化处理
const onIdCardChange = () => {
  if (formData.idCard && formData.idCard.length === 18) {
    calculateAgeFromIdCard(formData.idCard);
  }
};

// 出生日期变化处理
const onBirthDateChange = () => {
  // 当手动修改出生日期时，重新计算年龄
  // 年龄会自动通过computed属性更新
};

// 表单验证规则
const rules = {
  name: [
    { required: true, message: "请输入患者姓名", trigger: "blur" },
    { min: 2, max: 50, message: "姓名长度在 2 到 50 个字符", trigger: "blur" }
  ],
  idCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    { 
      pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, 
      message: "请输入正确的身份证号", 
      trigger: "blur" 
    }
  ],
  phone: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
  gender: [
    { required: true, message: "请选择性别", trigger: "change" }
  ],
  birthDate: [
    { required: true, message: "请选择出生日期", trigger: "change" }
  ],
  insuranceType: [
    { required: true, message: "请选择保险类型", trigger: "change" }
  ],
  insuranceNumber: [
    { required: true, message: "请输入保险号", trigger: "blur" },
    { min: 1, max: 50, message: "保险号长度在 1 到 50 个字符", trigger: "blur" }
  ],
  address: [
    { required: true, message: "请输入联系地址", trigger: "blur" },
    { min: 5, max: 255, message: "地址长度在 5 到 255 个字符", trigger: "blur" }
  ]
};

// 获取保险类型列表
const fetchInsuranceTypes = async () => {
  try {
    insuranceTypes.value = await getAllInsuranceTypes();
  } catch (error) {
    console.error('获取保险类型失败:', error);
    // 使用默认保险类型
    insuranceTypes.value = ['城镇职工', '城乡居民', '新农合', '医疗救助', '商业保险'];
  }
};

// 提交表单
const onSubmit = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    
    loading.value = true;
    try {
      console.log('提交患者数据:', formData);
      
      await addPatient(formData);
      ElMessage.success("新增患者成功");
      router.push("/patient/list");
    } catch (error: any) {
      console.error('新增患者失败:', error);
      ElMessage.error(error?.message || "新增患者失败");
    } finally {
      loading.value = false;
    }
  });
};

// 取消操作
const handleCancel = () => {
  router.back();
};

onMounted(() => {
  fetchInsuranceTypes();
});
</script>

<style scoped>
.patient-add {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.patient-form {
  max-width: 800px;
  margin: 0 auto;
}

:deep(.el-form-item__label) {
  color: var(--el-text-color-primary);
  font-weight: 500;
}

:deep(.el-input__inner) {
  height: 40px;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-textarea__inner) {
  resize: vertical;
}
</style> 