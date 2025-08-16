<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from 'vue-router'
import { useMultiTagsStoreHook } from '@/store/modules/multiTags'
import { getSettlementList, createSettlement, updateSettlement, deleteSettlement } from '@/api/settlement'

interface Settlement {
  id?: number
  settlementNumber: string
  patientId: number
  patientName: string
  orderId: number
  orderNumber: string
  totalAmount: number
  reimbursableAmount: number
  actualReimbursement: number
  reimbursementLevel: string
  settlementDate: string
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED'
  remarks?: string
  createTime?: string
  updateTime?: string
}

interface SettlementQueryParams {
  pageNum: number
  pageSize: number
  orderNo?: string
  keyword?: string
  status?: string
  startDate?: string
  endDate?: string
}

const route = useRoute()
const router = useRouter()
const multiTagsStore = useMultiTagsStoreHook()

const loading = ref(false)
const tableData = ref<Settlement[]>([])
const total = ref(0)
const queryParams = reactive<SettlementQueryParams>({
  pageNum: 1,
  pageSize: 10,
  orderNo: '',
  keyword: '',
  status: '',
  startDate: '',
  endDate: ''
})

const getSettlementListData = async () => {
  try {
    loading.value = true
    const response = await getSettlementList(queryParams)
    tableData.value = response.records
    total.value = response.total
  } catch (error) {
    ElMessage.error('获取结算列表失败')
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.page = 1
  getSettlementListData()
}

const handleReset = () => {
  queryParams.settlementNumber = ''
  queryParams.patientName = ''
  queryParams.status = ''
  queryParams.startDate = ''
  queryParams.endDate = ''
  handleQuery()
}

const handleAdd = () => {
  router.push('/settlement/add')
}

const handleEdit = (row: Settlement) => {
  router.push(`/settlement/edit/${row.id}`)
}

const handleDelete = async (row: Settlement) => {
  try {
    await ElMessageBox.confirm('确定要删除该结算记录吗？', '提示', {
      type: 'warning'
    })
    await deleteSettlement(row.id)
    ElMessage.success('删除成功')
    getSettlementListData()
  } catch (error) {
    console.error(error)
  }
}

const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  getSettlementListData()
}

const handleCurrentChange = (page: number) => {
  queryParams.pageNum = page
  getSettlementListData()
}

onMounted(() => {
  getSettlementListData()
})
</script>

<template>
  <div class="app-container">
    <el-card>
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="结算编号">
          <el-input
            v-model="queryParams.orderNo"
            placeholder="请输入订单号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="关键词">
          <el-input
            v-model="queryParams.keyword"
            placeholder="请输入关键词"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb-4">
        <el-col :span="1.5">
          <el-button type="primary" @click="handleAdd">新增结算</el-button>
        </el-col>
      </el-row>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        highlight-current-row
      >
        <el-table-column prop="settlementNumber" label="结算编号" width="180" />
        <el-table-column prop="patientName" label="患者姓名" />
        <el-table-column prop="orderNumber" label="订单编号" />
        <el-table-column prop="totalAmount" label="总金额" />
        <el-table-column prop="reimbursableAmount" label="可报销金额" />
        <el-table-column prop="actualReimbursement" label="实际报销金额" />
        <el-table-column prop="reimbursementLevel" label="报销等级" />
        <el-table-column prop="settlementDate" label="结算日期" />
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : scope.row.status === 'FAILED' ? 'danger' : 'warning'">
              {{ scope.row.status === 'PENDING' ? '待处理' : scope.row.status === 'PROCESSING' ? '处理中' : scope.row.status === 'COMPLETED' ? '已完成' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getSettlementListData"
      />
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.app-container {
  padding: 15px;
}
</style>
