<template>
  <div class="patient-table">
    <BaseTable table-id="patient-table"
      :data="data"
      :loading="loading"
      :height="height"
      :show-selection="showSelection"
      :show-pagination="false"
      @selection-change="handleSelectionChange"
    >
      <el-table-column v-if="showSelection" type="selection" width="55" align="center" />
      <el-table-column prop="name" label="患者姓名" width="120" />
      <el-table-column prop="idCard" label="身份证号" width="180" />
      <el-table-column prop="phone" label="手机号码" width="130" />
      <el-table-column prop="gender" label="性别" width="80" />
      <el-table-column prop="age" label="年龄" width="80" />
      <el-table-column prop="address" label="地址" min-width="200">
        <template #default="{ row }">
          <ReText :line-clamp="1" :tippy-props="{ content: row.address }">
            {{ row.address }}
          </ReText>
        </template>
      </el-table-column>
      <el-table-column prop="insuranceType" label="保险类型" width="120" />
      <el-table-column prop="insuranceNumber" label="保险号" width="150" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column v-if="showAction" label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <ReButton type="primary" link buttonText="查看" @click="handleView(row)" />
          <ReButton type="primary" link buttonText="编辑" @click="handleEdit(row)" />
          <ReButton type="danger" link buttonText="删除" @click="handleDelete(row)" />
        </template>
      </el-table-column>
    </BaseTable>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { Patient } from "@/api/patient";

interface Pagination {
  current: number;
  size: number;
  total: number;
}

interface Props {
  data: Patient[];
  loading?: boolean;
  height?: string | number;
  showSelection?: boolean;
  showAction?: boolean;
  showPagination?: boolean;
  pagination: Pagination;
}

const props = withDefaults(defineProps<Props>(), {
  data: () => [],
  loading: false,
  height: "auto",
  showSelection: true,
  showAction: true,
  showPagination: true
});

const emit = defineEmits<{
  selectionChange: [selection: Patient[]];
  view: [row: Patient];
  edit: [row: Patient];
  delete: [row: Patient];
  sizeChange: [size: number];
  currentChange: [current: number];
}>();

// 选择变化
const handleSelectionChange = (selection: Patient[]) => {
  emit("selectionChange", selection);
};

// 查看
const handleView = (row: Patient) => {
  emit("view", row);
};

// 编辑
const handleEdit = (row: Patient) => {
  emit("edit", row);
};

// 删除
const handleDelete = (row: Patient) => {
  emit("delete", row);
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  emit("sizeChange", size);
};

// 当前页变化
const handleCurrentChange = (current: number) => {
  emit("currentChange", current);
};
</script>

<style scoped>
.patient-table {
  background: #fff;
  border-radius: 8px;
  padding: 16px;
}

.pagination-wrapper {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style> 