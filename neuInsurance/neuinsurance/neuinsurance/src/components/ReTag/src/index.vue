<script setup lang="ts">
import { computed } from "vue";
import { IconifyIconOffline } from "../../ReIcon";
import type { TagProps } from "./type";

defineOptions({
  name: "ReTag"
});

const props = withDefaults(defineProps<TagProps>(), {
  type: undefined,
  effect: "light",
  round: false,
  closable: false,
  disableTransitions: false,
  hit: false,
  color: "",
  size: "default",
  showIcon: false,
  customClass: ""
});

const emit = defineEmits<{
  close: [event: MouseEvent];
  click: [event: MouseEvent];
}>();

const tagClass = computed(() => {
  return [
    "re-tag",
    props.customClass,
    {
      "re-tag--with-icon": props.showIcon && props.iconName
    }
  ];
});

const handleClose = (event: MouseEvent) => {
  emit("close", event);
};

const handleClick = (event: MouseEvent) => {
  emit("click", event);
};
</script>

<template>
  <el-tag
    v-bind="$attrs"
    :class="tagClass"
    :type="type"
    :effect="effect"
    :round="round"
    :closable="closable"
    :disable-transitions="disableTransitions"
    :hit="hit"
    :color="color"
    :size="size"
    @close="handleClose"
    @click="handleClick"
  >
    <IconifyIconOffline
      v-if="showIcon && iconName"
      :icon="iconName"
      class="re-tag__icon"
    />
    <span v-if="tagText" class="re-tag__text">
      {{ tagText }}
    </span>
    <slot />
  </el-tag>
</template>

<style scoped>
.re-tag {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  transition: all 0.3s ease;
}

.re-tag--with-icon {
  padding-left: 8px;
}

.re-tag__icon {
  font-size: 14px;
}

.re-tag__text {
  font-weight: 500;
}
</style> 