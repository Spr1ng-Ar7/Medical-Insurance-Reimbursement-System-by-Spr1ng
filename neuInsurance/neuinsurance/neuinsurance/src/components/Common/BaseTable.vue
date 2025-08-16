<template>
  <div class="base-table">
    <div v-if="showToolbar" class="table-toolbar">
      <div class="toolbar-left">
        <slot name="toolbar-left">
          <ReButton 
            v-if="showAdd" 
            type="primary" 
            iconName="ep:plus" 
            buttonText="新增" 
            @click="handleAdd" 
          />
          <ReButton 
            v-if="showDelete" 
            type="danger" 
            iconName="ep:delete" 
            buttonText="删除" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete" 
          />
        </slot>
      </div>
      <div class="toolbar-right">
        <slot name="toolbar-right">
          <ReButton 
            v-if="showRefresh" 
            iconName="ep:refresh" 
            buttonText="刷新" 
            @click="handleRefresh" 
          />
          <ReButton 
            v-if="showExport" 
            type="success" 
            iconName="ep:download" 
            buttonText="导出" 
            @click="handleExport" 
          />
        </slot>
      </div>
    </div>

    <el-table
      ref="tableRef"
      v-bind="$attrs"
      :id="tableId"
      :data="data"
      :loading="loading"
      :height="height"
      :max-height="maxHeight"
      :stripe="stripe"
      :border="border"
      :size="size"
      :fit="fit"
      :show-header="showHeader"
      :highlight-current-row="highlightCurrentRow"
      :current-row-key="currentRowKey"
      :row-class-name="rowClassName"
      :row-style="rowStyle"
      :cell-class-name="cellClassName"
      :cell-style="cellStyle"
      :header-row-class-name="headerRowClassName"
      :header-row-style="headerRowStyle"
      :header-cell-class-name="headerCellClassName"
      :header-cell-style="headerCellStyle"
      :row-key="rowKey"
      :empty-text="emptyText"
      :default-expand-all="defaultExpandAll"
      :expand-row-keys="expandRowKeys"
      :default-sort="defaultSort"
      :tooltip-effect="tooltipEffect"
      :show-summary="showSummary"
      :sum-text="sumText"
      :summary-method="summaryMethod"
      :span-method="spanMethod"
      :select-on-indeterminate="selectOnIndeterminate"
      :indent="indent"
      :lazy="lazy"
      :load="load"
      :tree-props="treeProps"
      :table-layout="tableLayout"
      :scrollbar-always-on="scrollbarAlwaysOn"
      :flexible="flexible"
      @select="handleSelect"
      @select-all="handleSelectAll"
      @selection-change="handleSelectionChange"
      @cell-mouse-enter="handleCellMouseEnter"
      @cell-mouse-leave="handleCellMouseLeave"
      @cell-click="handleCellClick"
      @cell-dblclick="handleCellDblclick"
      @row-click="handleRowClick"
      @row-contextmenu="handleRowContextmenu"
      @row-dblclick="handleRowDblclick"
      @header-click="handleHeaderClick"
      @header-contextmenu="handleHeaderContextmenu"
      @sort-change="handleSortChange"
      @filter-change="handleFilterChange"
      @header-dragend="handleHeaderDragend"
      @expand-change="handleExpandChange"
    >
      <slot />
    </el-table>

    <div v-if="showPagination" class="table-pagination">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="pageSizes"
        :small="paginationSmall"
        :background="paginationBackground"
        :layout="paginationLayout"
        :total="total"
        :hide-on-single-page="hideOnSinglePage"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        @update:current-page="(val) => emit('update:currentPage', val)"
        @update:page-size="(val) => emit('update:pageSize', val)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from "vue";
import { ReButton } from "../index";
import type { TableColumnCtx } from "element-plus";

interface Props {
  data: any[];
  loading?: boolean;
  height?: string | number;
  maxHeight?: string | number;
  stripe?: boolean;
  border?: boolean;
  size?: "large" | "default" | "small";
  fit?: boolean;
  showHeader?: boolean;
  highlightCurrentRow?: boolean;
  currentRowKey?: string | number;
  rowClassName?: string | ((param: any) => string);
  rowStyle?: any;
  cellClassName?: string | ((param: any) => string);
  cellStyle?: any;
  headerRowClassName?: string | ((param: any) => string);
  headerRowStyle?: any;
  headerCellClassName?: string | ((param: any) => string);
  headerCellStyle?: any;
  rowKey?: string | ((row: any) => string);
  emptyText?: string;
  defaultExpandAll?: boolean;
  expandRowKeys?: any[];
  defaultSort?: any;
  tooltipEffect?: "dark" | "light";
  showSummary?: boolean;
  sumText?: string;
  summaryMethod?: (param: any) => any[];
  spanMethod?: (data: { row: any; rowIndex: number; column: TableColumnCtx<any>; columnIndex: number; }) => number[] | { rowspan: number; colspan: number; };
  selectOnIndeterminate?: boolean;
  indent?: number;
  lazy?: boolean;
  load?: (row: any, treeNode: any, resolve: (data: any[]) => void) => void;
  treeProps?: object;
  tableLayout?: "fixed" | "auto";
  scrollbarAlwaysOn?: boolean;
  flexible?: boolean;
  
  // 新增 tableId 属性
  tableId?: string;
  
  // 自定义属性
  showToolbar?: boolean;
  showAdd?: boolean;
  showDelete?: boolean;
  showRefresh?: boolean;
  showExport?: boolean;
  showPagination?: boolean;
  currentPage?: number;
  pageSize?: number;
  pageSizes?: number[];
  paginationSmall?: boolean;
  paginationBackground?: boolean;
  paginationLayout?: string;
  total?: number;
  hideOnSinglePage?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  data: () => [],
  loading: false,
  stripe: false,
  border: true,
  size: "default",
  fit: true,
  showHeader: true,
  highlightCurrentRow: false,
  emptyText: "暂无数据",
  defaultExpandAll: false,
  tooltipEffect: "dark",
  showSummary: false,
  sumText: "合计",
  selectOnIndeterminate: true,
  indent: 16,
  lazy: false,
  tableLayout: "fixed",
  scrollbarAlwaysOn: false,
  flexible: false,
  // 自定义属性默认值
  showToolbar: true,
  showAdd: true,
  showDelete: true,
  showRefresh: true,
  showExport: false,
  showPagination: true,
  currentPage: 1,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
  paginationSmall: false,
  paginationBackground: true,
  paginationLayout: "total, sizes, prev, pager, next, jumper",
  total: 0,
  hideOnSinglePage: false,
  // 若父组件未显式传入 tableId，自动生成唯一 id，避免插件获取空值
  tableId: (() => `table-${Date.now().toString(36)}-${Math.random().toString(36).slice(2, 8)}`)()
});

const emit = defineEmits<{
  add: [];
  batchDelete: [rows: any[]];
  refresh: [];
  export: [];
  select: [selection: any[], row: any];
  selectAll: [selection: any[]];
  selectionChange: [selection: any[]];
  cellMouseEnter: [row: any, column: any, cell: any, event: Event];
  cellMouseLeave: [row: any, column: any, cell: any, event: Event];
  cellClick: [row: any, column: any, cell: any, event: Event];
  cellDblclick: [row: any, column: any, cell: any, event: Event];
  rowClick: [row: any, column: any, event: Event];
  rowContextmenu: [row: any, column: any, event: Event];
  rowDblclick: [row: any, column: any, event: Event];
  headerClick: [column: any, event: Event];
  headerContextmenu: [column: any, event: Event];
  sortChange: [param: any];
  filterChange: [filters: any];
  headerDragend: [newWidth: number, oldWidth: number, column: any, event: Event];
  expandChange: [row: any, expandedRows: any[]];
  sizeChange: [size: number];
  currentChange: [page: number];
  "update:currentPage": [page: number];
  "update:pageSize": [pageSize: number];
}>();

const tableRef = ref();
const selectedRows = ref<any[]>([]);

// 工具栏事件处理
const handleAdd = () => {
  emit("add");
};

const handleBatchDelete = () => {
  emit("batchDelete", selectedRows.value);
};

const handleRefresh = () => {
  emit("refresh");
};

const handleExport = () => {
  emit("export");
};

// 表格事件处理
const handleSelect = (selection: any[], row: any) => {
  emit("select", selection, row);
};

const handleSelectAll = (selection: any[]) => {
  emit("selectAll", selection);
};

const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection;
  emit("selectionChange", selection);
};

const handleCellMouseEnter = (row: any, column: any, cell: any, event: Event) => {
  emit("cellMouseEnter", row, column, cell, event);
};

const handleCellMouseLeave = (row: any, column: any, cell: any, event: Event) => {
  emit("cellMouseLeave", row, column, cell, event);
};

const handleCellClick = (row: any, column: any, cell: any, event: Event) => {
  emit("cellClick", row, column, cell, event);
};

const handleCellDblclick = (row: any, column: any, cell: any, event: Event) => {
  emit("cellDblclick", row, column, cell, event);
};

const handleRowClick = (row: any, column: any, event: Event) => {
  emit("rowClick", row, column, event);
};

const handleRowContextmenu = (row: any, column: any, event: Event) => {
  emit("rowContextmenu", row, column, event);
};

const handleRowDblclick = (row: any, column: any, event: Event) => {
  emit("rowDblclick", row, column, event);
};

const handleHeaderClick = (column: any, event: Event) => {
  emit("headerClick", column, event);
};

const handleHeaderContextmenu = (column: any, event: Event) => {
  emit("headerContextmenu", column, event);
};

const handleSortChange = (param: any) => {
  emit("sortChange", param);
};

const handleFilterChange = (filters: any) => {
  emit("filterChange", filters);
};

const handleHeaderDragend = (newWidth: number, oldWidth: number, column: any, event: Event) => {
  emit("headerDragend", newWidth, oldWidth, column, event);
};

const handleExpandChange = (row: any, expandedRows: any[]) => {
  emit("expandChange", row, expandedRows);
};

// 分页事件处理
const handleSizeChange = (size: number) => {
  emit("sizeChange", size);
};

const handleCurrentChange = (page: number) => {
  emit("currentChange", page);
};

// 暴露方法
// 增加 tableId 便于外部通过 id 获取表格 DOM
defineExpose({
  tableRef,
  selectedRows,
  tableId: props.tableId,
  clearSelection: () => tableRef.value?.clearSelection(),
  toggleRowSelection: (row: any, selected?: boolean) => tableRef.value?.toggleRowSelection(row, selected),
  toggleAllSelection: () => tableRef.value?.toggleAllSelection(),
  setCurrentRow: (row?: any) => tableRef.value?.setCurrentRow(row),
  clearSort: () => tableRef.value?.clearSort(),
  clearFilter: (columnKeys?: string[]) => tableRef.value?.clearFilter(columnKeys),
  doLayout: () => tableRef.value?.doLayout(),
  sort: (prop: string, order: string) => tableRef.value?.sort(prop, order)
});
</script>

<style scoped>
.base-table {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.table-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color-page);
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  gap: 8px;
}

.table-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 16px 20px;
  border-top: 1px solid var(--el-border-color-light);
  background: var(--el-bg-color-page);
}

:deep(.el-table) {
  --el-table-border-color: var(--el-border-color-lighter);
  --el-table-header-bg-color: var(--el-fill-color-light);
}

:deep(.el-table th) {
  background: var(--el-fill-color-light);
  color: var(--el-text-color-primary);
  font-weight: 600;
}

:deep(.el-table td) {
  color: var(--el-text-color-regular);
}

:deep(.el-table--border) {
  border: 1px solid var(--el-border-color-lighter);
}
</style>