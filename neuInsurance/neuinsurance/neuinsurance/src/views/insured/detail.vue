<template>
  <div class="insured-detail">
    <ReTag type="primary" tagText="参保人员详情" />
    <el-descriptions :column="2" border>
      <el-descriptions-item label="姓名">{{ detail.name }}</el-descriptions-item>
      <el-descriptions-item label="性别">{{ detail.gender }}</el-descriptions-item>
      <el-descriptions-item label="身份证号">{{ detail.idCard }}</el-descriptions-item>
      <el-descriptions-item label="家庭住址">{{ detail.address }}</el-descriptions-item>
    </el-descriptions>
    <h3>历史报销记录</h3>
    <PureTableBar :columns="columns" :data="reimburseList" />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { PureTableBar, ReTag } from "@/components";
const detail = ref({});
const reimburseList = ref([]);
const columns = [
  { label: "报销单号", prop: "no" },
  { label: "报销时间", prop: "date" },
  { label: "报销金额", prop: "amount" }
];
</script> 