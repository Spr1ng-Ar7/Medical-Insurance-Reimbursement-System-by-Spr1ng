<template>
  <div class="drug-table">
    <BaseTable table-id="drug-table"
      :data="data"
      :loading="loading"
      :height="height"
      :show-selection="showSelection"
      :show-pagination="false"
      @selection-change="handleSelectionChange"
    >
      <el-table-column v-if="showSelection" type="selection" width="55" align="center" />
      <el-table-column prop="drugName" label="药品名称" width="150" />
      <el-table-column prop="drugCode" label="药品编码" width="120" />
      <el-table-column prop="drugType" label="药品类型" width="100" />
      <el-table-column prop="specification" label="规格" width="120" />
      <el-table-column prop="manufacturer" label="生产厂家" min-width="150">
        <template #default="{ row }">
          <ReText :line-clamp="1" :tippy-props="{ content: row.manufacturer }">
            {{ row.manufacturer }}
          </ReText>
        </template>
      </el-table-column>
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">
          ¥{{ row.price?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <ReTag :type="row.status === 1 ? 'success' : 'danger'" :tagText="row.status === 1 ? '启用' : '禁用'" size="small" />
        </template>
      </el-table-column>
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
import type { Drug } from "@/api/drug";

interface Pagination {
  current: number;
  size: number;
  total: number;
}

interface Props {
  data: Drug[];
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
  selectionChange: [selection: Drug[]];
  view: [row: Drug];
  edit: [row: Drug];
  delete: [row: Drug];
  sizeChange: [size: number];
  currentChange: [current: number];
}>();

// 选择变化
const handleSelectionChange = (selection: Drug[]) => {
  emit("selectionChange", selection);
};

// 查看
const handleView = (row: Drug) => {
  emit("view", row);
};

// 编辑
const handleEdit = (row: Drug) => {
  emit("edit", row);
};

// 删除
const handleDelete = (row: Drug) => {
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
.drug-table {
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