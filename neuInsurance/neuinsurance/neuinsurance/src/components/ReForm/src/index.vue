<script setup lang="ts">
import { computed, ref } from "vue";
import type { FormProps } from "./type";

defineOptions({
  name: "ReForm"
});

const props = withDefaults(defineProps<FormProps>(), {
  model: undefined,
  rules: undefined,
  labelPosition: "right",
  labelWidth: "auto",
  labelSuffix: "",
  inline: false,
  inlineMessage: false,
  showMessage: true,
  size: "default",
  disabled: false,
  validateOnRuleChange: true,
  hideRequiredAsterisk: false,
  scrollToError: false,
  customClass: "",
  showBorder: true,
  showTitle: false,
  title: ""
});

const emit = defineEmits<{
  validate: [prop: string | string[], isValid: boolean, message: string];
}>();

const formRef = ref();

const formClass = computed(() => {
  return [
    "re-form",
    props.customClass,
    {
      "re-form--border": props.showBorder,
      "re-form--title": props.showTitle
    }
  ];
});

const handleValidate = (prop: string | string[], isValid: boolean, message: string) => {
  emit("validate", prop, isValid, message);
};

// 暴露el-form的方法
defineExpose({
  validate: (callback?: (isValid: boolean, invalidFields?: any) => void) => {
    return formRef.value?.validate(callback);
  },
  validateField: (props?: string | string[], callback?: (isValid: boolean, invalidFields?: any) => void) => {
    return formRef.value?.validateField(props, callback);
  },
  resetFields: (props?: string | string[]) => {
    return formRef.value?.resetFields(props);
  },
  scrollToField: (prop: string) => {
    return formRef.value?.scrollToField(prop);
  },
  clearValidate: (props?: string | string[]) => {
    return formRef.value?.clearValidate(props);
  }
});
</script>

<template>
  <div :class="formClass">
    <div v-if="showTitle && title" class="re-form__title">
      {{ title }}
    </div>
    <el-form
      ref="formRef"
      v-bind="$attrs"
      :class="formClass"
      :model="model"
      :rules="rules"
      :label-position="labelPosition"
      :label-width="labelWidth"
      :label-suffix="labelSuffix"
      :inline="inline"
      :inline-message="inlineMessage"
      :show-message="showMessage"
      :size="size"
      :disabled="disabled"
      :validate-on-rule-change="validateOnRuleChange"
      :hide-required-asterisk="hideRequiredAsterisk"
      :scroll-to-error="scrollToError"
      @validate="handleValidate"
    >
      <slot />
    </el-form>
  </div>
</template>

<style scoped>
.re-form {
  width: 100%;
}

.re-form--border {
  border: 1px solid var(--el-border-color);
  border-radius: 8px;
  padding: 20px;
  background: var(--el-bg-color);
}

.re-form--title {
  margin-bottom: 16px;
}

.re-form__title {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-light);
}
</style> 