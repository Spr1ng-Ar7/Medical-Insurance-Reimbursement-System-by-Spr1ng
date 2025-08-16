<template>
  <div class="base-form">
    <ReForm
      ref="formRef"
      :model="model"
      :rules="rules"
      :label-width="labelWidth"
      :label-position="labelPosition"
      :label-suffix="labelSuffix"
      :inline="inline"
      :inline-message="inlineMessage"
      :show-message="showMessage"
      :size="size"
      :disabled="disabled"
      :validate-on-rule-change="validateOnRuleChange"
      :hide-required-asterisk="hideRequiredAsterisk"
      :scroll-to-error="scrollToError"
      showBorder
      :showTitle="showTitle"
      :title="title"
      @validate="handleValidate"
    >
      <slot />
      
      <ReForm v-if="showActions">
        <ReButton 
          type="primary" 
          :loading="submitLoading" 
          iconName="ep:check" 
          :buttonText="submitText" 
          @click="handleSubmit" 
        />
        <ReButton 
          v-if="showCancel"
          buttonText="取消" 
          @click="handleCancel" 
        />
        <ReButton 
          v-if="showReset"
          buttonText="重置" 
          @click="handleReset" 
        />
      </ReForm>
    </ReForm>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { ReForm, ReButton } from "../index";

interface Props {
  model: any;
  rules?: any;
  labelWidth?: string | number;
  labelPosition?: "left" | "right" | "top";
  labelSuffix?: string;
  inline?: boolean;
  inlineMessage?: boolean;
  showMessage?: boolean;
  size?: "large" | "default" | "small";
  disabled?: boolean;
  validateOnRuleChange?: boolean;
  hideRequiredAsterisk?: boolean;
  scrollToError?: boolean;
  
  // 自定义属性
  showTitle?: boolean;
  title?: string;
  showActions?: boolean;
  submitLoading?: boolean;
  submitText?: string;
  showCancel?: boolean;
  showReset?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  model: () => ({}),
  rules: undefined,
  labelWidth: "auto",
  labelPosition: "right",
  labelSuffix: "",
  inline: false,
  inlineMessage: false,
  showMessage: true,
  size: "default",
  disabled: false,
  validateOnRuleChange: true,
  hideRequiredAsterisk: false,
  scrollToError: false,
  
  // 自定义属性默认值
  showTitle: false,
  title: "",
  showActions: true,
  submitLoading: false,
  submitText: "提交",
  showCancel: true,
  showReset: false
});

const emit = defineEmits<{
  submit: [data: any];
  cancel: [];
  reset: [];
  validate: [prop: string | string[], isValid: boolean, message: string];
}>();

const formRef = ref();

// 表单验证
const handleValidate = (prop: string | string[], isValid: boolean, message: string) => {
  emit("validate", prop, isValid, message);
};

// 提交表单
const handleSubmit = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    emit("submit", props.model);
  });
};

// 取消
const handleCancel = () => {
  emit("cancel");
};

// 重置表单
const handleReset = () => {
  formRef.value.resetFields();
  emit("reset");
};

// 暴露方法
defineExpose({
  formRef,
  validate: (callback?: (valid: boolean) => void) => formRef.value?.validate(callback),
  validateField: (props?: string | string[], callback?: (valid: boolean) => void) => formRef.value?.validateField(props, callback),
  resetFields: (props?: string | string[]) => formRef.value?.resetFields(props),
  scrollToField: (prop: string) => formRef.value?.scrollToField(prop),
  clearValidate: (props?: string | string[]) => formRef.value?.clearValidate(props)
});
</script>

<style scoped>
.base-form {
  width: 100%;
}
</style> 