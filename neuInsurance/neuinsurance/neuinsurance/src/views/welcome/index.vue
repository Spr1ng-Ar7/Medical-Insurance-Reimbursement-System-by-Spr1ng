

<template>
  <div class="welcome-wrapper">
    <section class="hero">
      <img src="/logo.svg" alt="NeuInsurance Logo" class="logo" />
      <h1 class="title">欢迎使用 NeuInsurance 医保管理系统</h1>
      <p class="subtitle">高效 · 便捷 · 安全 的医保业务平台</p>
    </section>

    <el-row :gutter="20" class="quick-links">
      <el-col v-for="item in shortcuts" :key="item.path" :xs="24" :sm="12" :md="8">
        <el-card shadow="hover" class="link-card" @click="go(item.path)">
          <template #header>
            <div class="card-header">
              <el-icon :size="28"><component :is="item.icon" /></el-icon>
              <span class="card-title">{{ item.title }}</span>
            </div>
          </template>
          <p class="card-desc">{{ item.desc }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { HomeFilled, Operation, Goods, Setting } from "@element-plus/icons-vue";

const router = useRouter();

interface Shortcut {
  title: string;
  desc: string;
  path: string;
  icon: any;
}

const shortcuts: Shortcut[] = [
  { title: "参保人员管理", desc: "录入、查询参保人员信息", path: "/insured/list", icon: HomeFilled },
  { title: "医疗设施管理", desc: "维护医疗设备与科室", path: "/facility/list", icon: Operation },
  { title: "药品目录", desc: "药品录入及价格管理", path: "/drug/list", icon: Goods },
  { title: "系统设置", desc: "角色权限与基础配置", path: "/system/admin", icon: Setting }
];

const go = (path: string) => {
  router.push(path);
};
</script>

<style scoped>
.welcome-wrapper {
  padding: 40px 20px;
}
.hero {
  text-align: center;
  margin-bottom: 40px;
}
.logo {
  height: 80px;
  margin-bottom: 20px;
}
.title {
  font-size: 28px;
  color: var(--el-color-primary);
  margin-bottom: 10px;
}
.subtitle {
  font-size: 16px;
  color: var(--el-text-color-secondary);
}
.quick-links {
  max-width: 1200px;
  margin: 0 auto;
}
.link-card {
  cursor: pointer;
  transition: transform .2s;
}
.link-card:hover {
  transform: translateY(-4px);
}
.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
}
.card-title {
  font-size: 18px;
  font-weight: 600;
}
.card-desc {
  color: var(--el-text-color-secondary);
}
</style>
   

<style scoped>
.welcome-page {
  padding: 20px;
}

.custom-card {
  background: var(--el-bg-color);
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light);
  padding: 24px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.component-demo h3 {
  margin: 30px 0 15px 0;
  color: var(--el-text-color-primary);
  font-size: 18px;
  font-weight: 600;
}

.demo-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid var(--el-border-color-light);
  border-radius: 8px;
  background: var(--el-bg-color-page);
}

.demo-section .re-button {
  margin-right: 10px;
  margin-bottom: 10px;
}

.demo-section .re-tag {
  margin-right: 10px;
  margin-bottom: 10px;
}

.text-demo {
  max-width: 300px;
  margin-bottom: 15px;
}

.text-demo .re-text {
  display: block;
  width: 100%;
}
</style>
