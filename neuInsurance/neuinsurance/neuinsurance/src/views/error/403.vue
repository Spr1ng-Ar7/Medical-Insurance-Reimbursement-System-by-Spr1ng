<script setup lang="ts">
import { useRouter } from "vue-router";
import noAccess from "@/assets/status/403.svg?component";

defineOptions({
  name: "403"
});

const router = useRouter();
</script>

<template>
  <div class="flex justify-center items-center h-[640px] bg-gray-50">
    <div class="flex flex-col items-center">
      <div class="mb-8">
        <noAccess />
      </div>
      <div class="text-center">
        <h1 class="text-6xl font-bold text-gray-800 mb-4">403</h1>
        <h2 class="text-2xl text-gray-600 mb-4">抱歉，您没有访问该页面的权限</h2>
        <p class="text-gray-500 mb-8">
          您当前的角色没有访问该页面的权限。请联系管理员获取相应权限。
        </p>
        <el-button
          type="primary"
          size="large"
          @click="router.push('/')"
          class="mb-4"
        >
          返回首页
        </el-button>
        <el-button
          type="info"
          size="large"
          @click="router.back()"
        >
          返回上一页
        </el-button>
      </div>
    </div>
  </div>
</template>
