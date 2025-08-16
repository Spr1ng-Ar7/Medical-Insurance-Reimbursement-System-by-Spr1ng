<template>
  <div class="admission-history">
    <PureTableBar
      :columns="columns"
      :data="admissionList"
      :loading="loading"
      :pagination="pagination"
      @page-size-change="handleSizeChange"
      @page-current-change="handleCurrentChange"
    >
      <template #toolbar>
        <ReButton type="primary" iconName="ep:plus" buttonText="新增登记" @click="handleAdd" />
      </template>
      <template #operation="{ row }">
        <ReButton type="primary" link buttonText="详情" @click="handleDetail(row)" />
        <ReButton type="success" link buttonText="编辑" @click="handleEdit(row)" />
        <ReButton type="danger" link buttonText="删除" @click="handleDelete(row)" />
      </template>
    </PureTableBar>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { PureTableBar, ReButton } from "@/components";

const loading = ref(false);
const admissionList = ref([]);
const pagination = ref({ total: 0, pageSize: 10, currentPage: 1 });

const columns = [
  { label: "登记号", prop: "admissionNo" },
  { label: "姓名", prop: "name" },
  { label: "性别", prop: "gender" },
  { label: "身份证号", prop: "idCard" },
  { label: "入院日期", prop: "admissionDate" },
  { label: "操作", slot: "operation", fixed: "right", width: 180 }
];

function handleAdd() {}
function handleDetail(row) {}
function handleEdit(row) {}
function handleDelete(row) {}
function handleSizeChange() {}
function handleCurrentChange() {}
</script> 