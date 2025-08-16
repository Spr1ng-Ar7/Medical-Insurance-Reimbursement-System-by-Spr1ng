<template>
  <div class="drug-form">
    <ReForm
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      showBorder
      showTitle
      title="药品信息"
    >
      <div class="row-flex">
        <ReCol :value="12">
          <el-form-item label="药品编码" prop="drugCode">
            <el-input
              v-model="formData.drugCode"
              placeholder="请输入药品编码"
              clearable
            />
          </el-form-item>
        </ReCol>
        <ReCol :value="12">
          <el-form-item label="药品名称" prop="drugName">
            <el-input
              v-model="formData.drugName"
              placeholder="请输入药品名称"
              clearable
            />
          </el-form-item>
        </ReCol>
      </div>
      <div class="row-flex">
        <ReCol :value="12">
          <el-form-item label="商品名" prop="tradeName">
            <el-input
              v-model="formData.tradeName"
              placeholder="请输入商品名"
              clearable
            />
          </el-form-item>
        </ReCol>
        <ReCol :value="12">
          <el-form-item label="药品类型" prop="drugType">
            <el-select v-model="formData.drugType" placeholder="请选择药品类型">
              <el-option label="甲类" value="甲类" />
              <el-option label="乙类" value="乙类" />
              <el-option label="丙类" value="丙类" />
            </el-select>
          </el-form-item>
        </ReCol>
      </div>
      <div class="row-flex">
        <ReCol :value="12">
          <el-form-item label="自付比例" prop="selfPayRatio">
            <el-input-number
              v-model="formData.selfPayRatio"
              :min="0"
              :max="1"
              :precision="4"
              :step="0.1"
              placeholder="请输入自付比例"
              style="width: 100%"
            />
          </el-form-item>
        </ReCol>
        <ReCol :value="12">
          <el-form-item label="规格" prop="specification">
            <el-input
              v-model="formData.specification"
              placeholder="请输入规格"
              clearable
            />
          </el-form-item>
        </ReCol>
      </div>
      <div class="row-flex">
        <ReCol :value="12">
          <el-form-item label="单位" prop="unit">
            <el-input
              v-model="formData.unit"
              placeholder="请输入单位"
              clearable
            />
          </el-form-item>
        </ReCol>
        <ReCol :value="12">
          <el-form-item label="价格" prop="price">
            <el-input-number
              v-model="formData.price"
              :min="0"
              :precision="2"
              placeholder="请输入价格"
              style="width: 100%"
            />
          </el-form-item>
        </ReCol>
      </div>
      <div class="row-flex">
        <ReCol :value="12">
          <el-form-item label="生产厂家" prop="manufacturer">
            <el-input
              v-model="formData.manufacturer"
              placeholder="请输入生产厂家"
              clearable
            />
          </el-form-item>
        </ReCol>
        <ReCol :value="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="formData.status" placeholder="请选择状态">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
        </ReCol>
      </div>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>
      <el-form-item>
        <ReButton 
          type="primary" 
          :loading="submitLoading" 
          iconName="ep:check" 
          :buttonText="submitText" 
          @click="handleSubmit" 
        />
        <ReButton buttonText="取消" @click="handleCancel" />
      </el-form-item>
    </ReForm>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { ReForm, ReButton, ReCol } from "../../../components";
import type { Drug } from "../../../api/drug";

interface Props {
  modelValue: Drug;
  submitLoading?: boolean;
  submitText?: string;
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => ({
    id: 0,
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
    createTime: "",
    updateTime: "",
    createBy: "",
    updateBy: ""
  }),
  submitLoading: false,
  submitText: "保存"
});

const emit = defineEmits<{
  "update:modelValue": [value: Drug];
  submit: [data: Drug];
  cancel: [];
}>();

const formRef = ref();
const formData = reactive<Drug>({ ...props.modelValue });

// 表单验证规则
const rules = {
  drugCode: [
    { required: true, message: "请输入药品编码", trigger: "blur" }
  ],
  drugName: [
    { required: true, message: "请输入药品名称", trigger: "blur" }
  ],
  drugType: [
    { required: true, message: "请选择药品类型", trigger: "change" }
  ],
  specification: [
    { required: true, message: "请输入规格", trigger: "blur" }
  ],
  unit: [
    { required: true, message: "请输入单位", trigger: "blur" }
  ],
  price: [
    { required: true, message: "请输入价格", trigger: "blur" }
  ],
  manufacturer: [
    { required: true, message: "请输入生产厂家", trigger: "blur" }
  ]
};

// 提交表单
const handleSubmit = () => {
  emit("submit", formData);
};

// 取消
const handleCancel = () => {
  emit("cancel");
};
</script>

<style scoped>
.drug-form {
  background: var(--el-bg-color, #fff);
  border-radius: 8px;
  padding: 24px;
}
.row-flex {
  display: flex;
  gap: 20px;
  margin-bottom: 0.5em;
}
.el-form-item {
  background: transparent;
  border: none;
  margin-bottom: 18px;
}
.el-form-item__label {
  color: var(--el-text-color-primary, #fff);
  font-weight: 500;
  font-size: 15px;
}
.el-input, .el-select, .el-input-number {
  background: var(--el-bg-color, #222);
  color: var(--el-text-color-primary, #fff);
  border-radius: 6px;
  border: 1px solid var(--el-border-color-light, #444);
}
.el-input__inner, .el-select__input, .el-input-number__input {
  background: transparent;
  color: var(--el-text-color-primary, #fff);
}
.el-input:focus, .el-select:focus, .el-input-number:focus {
  border-color: var(--el-color-primary, #409eff);
}
.el-form-item.is-required .el-form-item__label {
  color: var(--el-color-danger, #f56c6c);
}
.el-form-item__content {
  color: var(--el-text-color-primary, #fff);
}
.el-select-dropdown {
  background: var(--el-bg-color, #222);
  color: var(--el-text-color-primary, #fff);
}
.el-select-dropdown__item {
  color: var(--el-text-color-primary, #fff);
}
</style>