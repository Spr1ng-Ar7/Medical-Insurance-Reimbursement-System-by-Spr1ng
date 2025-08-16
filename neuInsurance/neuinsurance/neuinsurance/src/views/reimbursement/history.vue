<template>
  <div class="reimbursement-history">
    <PureTableBar
      :columns="columns"
      :data="historyList"
      :loading="loading"
      :pagination="pagination"
    >
      <template #operation="{ row }">
        <ReButton type="primary" link buttonText="详情" @click="handleDetail(row)" />
        <ReButton type="success" link buttonText="打印" @click="handlePrint(row)" />
        <ReButton type="info" link buttonText="导出" @click="handleExport(row)" />
      </template>
    </PureTableBar>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { PureTableBar, ReButton } from "@/components";
const loading = ref(false);
const historyList = ref([]);
const pagination = ref({ total: 0, pageSize: 10, currentPage: 1 });
const columns = [
  { label: "报销单号", prop: "no" },
  { label: "姓名", prop: "name" },
  { label: "报销金额", prop: "amount" },
  { label: "报销时间", prop: "date" },
  { label: "操作", slot: "operation", fixed: "right", width: 180 }
];
function handleDetail(row) {}
function handlePrint(row) {}
function handleExport(row) {}
</script> 