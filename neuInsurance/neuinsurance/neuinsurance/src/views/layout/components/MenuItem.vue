<template>
  <el-menu-item v-if="!menu.children || menu.children.length === 0" :index="menu.path">
    <el-icon>
      <component :is="menu.icon" />
    </el-icon>
    <template #title>{{ menu.name }}</template>
  </el-menu-item>
  <el-sub-menu v-else :index="menu.path">
    <template #title>
      <el-icon>
        <component :is="menu.icon" />
      </el-icon>
      <span>{{ menu.name }}</span>
    </template>
    <menu-item
      v-for="child in menu.children"
      :key="child.id"
      :menu="child"
    />
  </el-sub-menu>
</template>

<script setup lang="ts">
import { Menu } from '@/api/system'

interface Props {
  menu: Menu
}

const props = defineProps<Props>()
</script>
