<script setup lang="ts">
import { computed } from "vue";
import { IconifyIconOffline } from "../../ReIcon";
import type { ButtonProps } from "./type";

defineOptions({
  name: "ReButton"
});

const props = withDefaults(defineProps<ButtonProps>(), {
  type: "default",
  size: "default",
  loading: false,
  disabled: false,
  round: false,
  circle: false,
  plain: false,
  autofocus: false,
  nativeType: "button",
  autoInsertSpace: false,
  showBg: false,
  customClass: ""
});

const emit = defineEmits<{
  click: [event: MouseEvent];
}>();

const buttonClass = computed(() => {
  return [
    "re-button",
    props.customClass,
    {
      "re-button--bg": props.showBg,
      "re-button--icon-only": props.iconName && !props.buttonText
    }
  ];
});

const handleClick = (event: MouseEvent) => {
  emit("click", event);
};
</script>

<template>
  <el-button
    v-bind="$attrs"
    :class="buttonClass"
    :type="type"
    :size="size"
    :loading="loading"
    :disabled="disabled"
    :round="round"
    :circle="circle"
    :plain="plain"
    :autofocus="autofocus"
    :native-type="nativeType"
    :auto-insert-space="autoInsertSpace"
    @click="handleClick"
  >
    <IconifyIconOffline
      v-if="iconName"
      :icon="iconName"
      class="re-button__icon"
    />
    <span v-if="buttonText" class="re-button__text">
      {{ buttonText }}
    </span>
    <slot />
  </el-button>
</template>

<style scoped>
.re-button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.re-button--bg {
  background: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary-light-7);
}

.re-button--bg:hover {
  background: var(--el-color-primary-light-8);
  border-color: var(--el-color-primary-light-6);
}

.re-button--icon-only {
  padding: 8px;
}

.re-button__icon {
  font-size: 16px;
}

.re-button__text {
  font-weight: 500;
}
</style> 