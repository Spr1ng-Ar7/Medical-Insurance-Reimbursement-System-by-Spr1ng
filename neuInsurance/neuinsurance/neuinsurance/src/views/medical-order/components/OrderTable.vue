<template>
  <div class="order-table">
    <el-table
      v-loading="loading"
      :data="data"
      :height="height"
      stripe
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-if="showSelection"
        type="selection"
        width="55"
        align="center"
      />
      
      <el-table-column prop="orderNo" label="订单号" width="180" />
      <el-table-column prop="patientName" label="患者姓名" width="120" />
      <el-table-column prop="hospitalName" label="医院名称" width="150" />
      <el-table-column prop="department" label="科室" width="120" />
      <el-table-column prop="doctorName" label="医生" width="100" />
      <el-table-column prop="diagnosis" label="诊断" min-width="150" show-overflow-tooltip />
      <el-table-column prop="visitDate" label="就诊日期" width="120" />
      <el-table-column prop="totalAmount" label="总金额" width="100">
        <template #default="{ row }">
          ¥{{ row.totalAmount?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="订单状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="settlementStatus" label="结算状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getSettlementStatusType(row.settlementStatus)">
            {{ row.settlementStatus }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reimbursementAmount" label="报销金额" width="100">
        <template #default="{ row }">
          ¥{{ row.reimbursementAmount?.toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      
      <el-table-column
        v-if="showAction"
        label="操作"
        width="250"
        align="center"
        fixed="right"
      >
        <template #default="{ row }">
          <el-button type="primary" link @click="handleView(row)">
            查看
          </el-button>
          <el-button type="primary" link @click="handleEdit(row)">
            编辑
          </el-button>
          <el-button type="success" link @click="handleSettlement(row)">
            结算
          </el-button>
          <el-button type="danger" link @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div v-if="showPagination" class="pagination-wrapper">
      <el-pagination
        :current-page="pagination.current"
        :page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import type { MedicalOrder } from "@/api/medical-order";

interface Pagination {
  current: number;
  size: number;
  total: number;
}

interface Props {
  data: MedicalOrder[];
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
  selectionChange: [selection: MedicalOrder[]];
  view: [row: MedicalOrder];
  edit: [row: MedicalOrder];
  settlement: [row: MedicalOrder];
  delete: [row: MedicalOrder];
  sizeChange: [size: number];
  currentChange: [current: number];
}>();

// 获取状态类型
const getStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '待审核': 'warning',
    '已审核': 'success',
    '已拒绝': 'danger',
    '已完成': 'info'
  };
  return statusMap[status] || 'info';
};

// 获取结算状态类型
const getSettlementStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    '待结算': 'warning',
    '待审批': 'warning',
    '已审批': 'success',
    '已拒绝': 'danger',
    '已结算': 'info'
  };
  return statusMap[status] || 'info';
};

// 选择变化
const handleSelectionChange = (selection: MedicalOrder[]) => {
  emit("selectionChange", selection);
};

// 查看
const handleView = (row: MedicalOrder) => {
  emit("view", row);
};

// 编辑
const handleEdit = (row: MedicalOrder) => {
  emit("edit", row);
};

// 结算
const handleSettlement = (row: MedicalOrder) => {
  emit("settlement", row);
};

// 删除
const handleDelete = (row: MedicalOrder) => {
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
.order-table {
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