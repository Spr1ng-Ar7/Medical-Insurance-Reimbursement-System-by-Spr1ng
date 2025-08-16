<template>
  <div class="medical-order-add">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>新增医疗订单</span>
          <el-button @click="handleBack">返回</el-button>
        </div>
      </template>
      
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
        class="order-form"
        @submit.prevent="handleSubmit"
      >
        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单号" prop="orderNo">
              <el-input
                v-model="formData.orderNo"
                placeholder="系统自动生成"
                readonly
              >
                <template #append>
                  <el-button @click="generateOrderNo" :loading="generatingOrderNo">
                    生成订单号
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单类型" prop="orderType">
              <el-select v-model="formData.orderType" placeholder="请选择订单类型" style="width: 100%">
                <el-option label="门诊" value="门诊" />
                <el-option label="住院" value="住院" />
                <el-option label="急诊" value="急诊" />
                <el-option label="体检" value="体检" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 患者信息 -->
        <el-divider content-position="left">患者信息</el-divider>
        
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
                @blur="onIdCardChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 医疗信息 -->
        <el-divider content-position="left">医疗信息</el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="医院名称" prop="hospitalName">
              <el-input
                v-model="formData.hospitalName"
                placeholder="请输入医院名称"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医院等级" prop="hospitalLevel">
              <el-select v-model="formData.hospitalLevel" placeholder="请选择医院等级" style="width: 100%">
                <el-option label="三级甲等" value="三级甲等" />
                <el-option label="三级乙等" value="三级乙等" />
                <el-option label="二级甲等" value="二级甲等" />
                <el-option label="二级乙等" value="二级乙等" />
                <el-option label="一级甲等" value="一级甲等" />
                <el-option label="一级乙等" value="一级乙等" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="科室" prop="departmentName">
              <el-select v-model="formData.departmentName" placeholder="请选择科室" style="width: 100%">
                <el-option label="内科" value="内科" />
                <el-option label="外科" value="外科" />
                <el-option label="儿科" value="儿科" />
                <el-option label="妇产科" value="妇产科" />
                <el-option label="急诊科" value="急诊科" />
                <el-option label="骨科" value="骨科" />
                <el-option label="眼科" value="眼科" />
                <el-option label="耳鼻喉科" value="耳鼻喉科" />
                <el-option label="皮肤科" value="皮肤科" />
                <el-option label="神经科" value="神经科" />
                <el-option label="心血管科" value="心血管科" />
                <el-option label="呼吸科" value="呼吸科" />
                <el-option label="消化科" value="消化科" />
                <el-option label="泌尿科" value="泌尿科" />
                <el-option label="肿瘤科" value="肿瘤科" />
                <el-option label="康复科" value="康复科" />
                <el-option label="中医科" value="中医科" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主治医生" prop="doctorName">
              <el-input
                v-model="formData.doctorName"
                placeholder="请输入主治医生姓名"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="就诊时间" prop="visitTime">
              <el-date-picker
                v-model="formData.visitTime"
                type="datetime"
                placeholder="请选择就诊时间"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.orderType === '住院'">
            <el-form-item label="出院时间">
              <el-date-picker
                v-model="formData.dischargeTime"
                type="datetime"
                placeholder="请选择出院时间"
                format="YYYY-MM-DD HH:mm:ss"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.orderType === '住院'">
            <el-form-item label="住院天数">
              <el-input-number
                v-model="formData.stayDays"
                :min="1"
                placeholder="住院天数"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="诊断结果" prop="diagnosis">
          <el-input
            v-model="formData.diagnosis"
            type="textarea"
            :rows="3"
            placeholder="请输入详细的诊断结果"
          />
        </el-form-item>
        
        <!-- 费用信息 -->
        <el-divider content-position="left">费用信息</el-divider>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="总费用" prop="totalAmount">
              <el-input-number
                v-model="formData.totalAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="总费用"
                style="width: 100%"
                @change="calculateTotalAmount"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="药品费用">
              <el-input-number
                v-model="formData.drugAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="药品费用"
                style="width: 100%"
                @change="calculateTotalAmount"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="诊疗费用">
              <el-input-number
                v-model="formData.treatmentAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="诊疗费用"
                style="width: 100%"
                @change="calculateTotalAmount"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="服务设施费用">
              <el-input-number
                v-model="formData.serviceAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="服务设施费用"
                style="width: 100%"
                @change="calculateTotalAmount"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="其他费用">
              <el-input-number
                v-model="formData.otherAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="其他费用"
                style="width: 100%"
                @change="calculateTotalAmount"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 药品分类费用 -->
        <el-divider content-position="left">药品分类费用</el-divider>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="甲类药品费用">
              <el-input-number
                v-model="formData.categoryAAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="甲类药品费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="乙类药品费用">
              <el-input-number
                v-model="formData.categoryBAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="乙类药品费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="丙类药品费用">
              <el-input-number
                v-model="formData.categoryCAmount"
                :precision="2"
                :step="0.01"
                :min="0"
                placeholder="丙类药品费用"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="2"
            placeholder="备注信息（可选）"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
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
import { ref, reactive, computed } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { Check } from "@element-plus/icons-vue";
import { addMedicalOrder, generateOrderNo as generateOrderNoAPI, type MedicalOrder } from "../../api/medical-order";

const router = useRouter();
const formRef = ref();
const submitLoading = ref(false);
const generatingOrderNo = ref(false);

// 表单数据
const formData = reactive<Partial<MedicalOrder>>({
  orderNo: "",
  orderType: "",
  patientName: "",
  patientIdCard: "",
  hospitalName: "",
  hospitalLevel: "",
  departmentName: "",
  doctorName: "",
  visitTime: "",
  dischargeTime: "",
  stayDays: 1,
  diagnosis: "",
  totalAmount: 0,
  drugAmount: 0,
  treatmentAmount: 0,
  serviceAmount: 0,
  otherAmount: 0,
  categoryAAmount: 0,
  categoryBAmount: 0,
  categoryCAmount: 0,
  status: 4, // 默认待审核
  remark: ""
});

// 表单验证规则
const rules = {
  orderNo: [
    { required: true, message: "请生成订单号", trigger: "blur" }
  ],
  orderType: [
    { required: true, message: "请选择订单类型", trigger: "change" }
  ],
  patientName: [
    { required: true, message: "请输入患者姓名", trigger: "blur" },
    { min: 2, max: 50, message: "姓名长度在 2 到 50 个字符", trigger: "blur" }
  ],
  patientIdCard: [
    { required: true, message: "请输入身份证号", trigger: "blur" },
    { 
      pattern: /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/, 
      message: "请输入正确的身份证号", 
      trigger: "blur" 
    }
  ],
  hospitalName: [
    { required: true, message: "请输入医院名称", trigger: "blur" }
  ],
  departmentName: [
    { required: true, message: "请选择科室", trigger: "change" }
  ],
  doctorName: [
    { required: true, message: "请输入主治医生姓名", trigger: "blur" }
  ],
  visitTime: [
    { required: true, message: "请选择就诊时间", trigger: "change" }
  ],
  diagnosis: [
    { required: true, message: "请输入诊断结果", trigger: "blur" },
    { min: 5, max: 1000, message: "诊断结果长度在 5 到 1000 个字符", trigger: "blur" }
  ],
  totalAmount: [
    { required: true, message: "请输入总费用", trigger: "blur" },
    { type: 'number' as const, min: 0.01, message: "总费用必须大于0", trigger: "blur" }
  ]
};

// 身份证号变化处理
const onIdCardChange = () => {
  // 可以添加身份证号验证逻辑，比如自动获取患者信息
  if (formData.patientIdCard && formData.patientIdCard.length === 18) {
    console.log('身份证号已输入:', formData.patientIdCard);
    // TODO: 可以调用API获取患者信息
  }
};

// 计算总费用
const calculateTotalAmount = () => {
  const drugAmount = formData.drugAmount || 0;
  const treatmentAmount = formData.treatmentAmount || 0;
  const serviceAmount = formData.serviceAmount || 0;
  const otherAmount = formData.otherAmount || 0;
  
  // 只有当各项费用都有值时才自动计算总费用
  if (drugAmount > 0 || treatmentAmount > 0 || serviceAmount > 0 || otherAmount > 0) {
    formData.totalAmount = drugAmount + treatmentAmount + serviceAmount + otherAmount;
  }
};

// 生成订单号
const generateOrderNo = async () => {
  generatingOrderNo.value = true;
  try {
    // 生成格式：MO + 年月日 + 时分秒 + 随机数
    const now = new Date();
    const date = now.toISOString().slice(0, 10).replace(/-/g, '');
    const time = now.toTimeString().slice(0, 8).replace(/:/g, '');
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    
    formData.orderNo = `MO${date}${time}${random}`;
    ElMessage.success("订单号生成成功");
  } catch (error: any) {
    console.error('生成订单号失败:', error);
    ElMessage.error(error?.message || "生成订单号失败");
  } finally {
    generatingOrderNo.value = false;
  }
};

// 提交表单
const handleSubmit = () => {
  formRef.value?.validate(async (valid: boolean) => {
    if (!valid) return;
    
    submitLoading.value = true;
    try {
      console.log('提交订单数据:', formData);
      
      await addMedicalOrder(formData);
      ElMessage.success("新增订单成功");
      router.push("/medical-order/list");
    } catch (error: any) {
      console.error('新增订单失败:', error);
      ElMessage.error(error?.message || "新增订单失败");
    } finally {
      submitLoading.value = false;
    }
  });
};

// 取消操作
const handleCancel = () => {
  router.back();
};

// 返回
const handleBack = () => {
  router.back();
};
</script>

<style scoped>
.medical-order-add {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.order-form {
  max-width: 1000px;
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

:deep(.el-divider__text) {
  font-weight: 600;
  color: var(--el-text-color-primary);
}
</style> 