<template>
  <div class="admin-list">
    <PureTable
      row-key="id"
      :data="adminList" :columns="columns" :headerButtons="headerButtons" :tableToolbar="true" :search="true" :searchInfo="searchParams"
      :loading="loading"
      :pagination="pagination"
      :initial-search-form="searchParams"
      :show-selection="true"
      @search="handleSearch"
      @reset="handleReset"
      @selection-change="handleSelectionChange"
      @page-size-change="handleSizeChange"
      @page-current-change="handleCurrentChange"
    >
      <!-- 搜索区域 -->
      <template #search="{ searchForm }">
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input
            v-model="searchForm.realName"
            placeholder="请输入姓名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="手机号码">
          <el-input
            v-model="searchForm.phone"
            placeholder="请输入手机号码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </template>

      <!-- 工具栏左侧 -->
      <template #toolbar-left>
        <ReButton type="primary" @click="openAddDialog" buttonText="新增管理员" />
        <ReButton 
          type="danger" 
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          <el-icon><Delete /></el-icon>
          批量删除
        </ReButton>
      </template>

      <!-- 工具栏右侧 -->
      <template #toolbar-right>
        <ReButton @click="handleRefresh" buttonText="刷新" />
      </template>

      <!-- 表格列 -->
      <el-table-column prop="username" label="用户名" min-width="120" />
      <el-table-column prop="realName" label="姓名" min-width="120" />
      <el-table-column prop="phone" label="手机号码" min-width="130" />
      <el-table-column prop="email" label="邮箱" min-width="180" />
      <el-table-column prop="roleName" label="角色" min-width="120" />
      <el-table-column prop="departmentName" label="部门" min-width="120" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" min-width="160" />

      <!-- 操作列 -->
      <template #action="{ row }">
        <ReButton type="primary" link @click="handleView(row)" buttonText="查看" />
        <ReButton type="success" link @click="handleEdit(row)" buttonText="编辑" />
        <ReButton type="warning" link @click="handleResetPassword(row)" buttonText="重置密码" />
        <ReButton type="danger" link @click="handleDelete(row)" buttonText="删除" />
      </template>
    <template #toolbar>
        <ReButton type="primary" buttonText="新增管理员" iconName="ep:plus" @click="openAddDialog" />
        <ReButton type="danger" buttonText="批量删除" iconName="ep:delete" :disabled="selectedRows.length === 0" @click="handleBatchDelete" />
        <ReButton type="success" buttonText="刷新" iconName="ep:refresh" @click="handleRefresh" />
      </template>

      <template #status="{ row }">
        <el-tag :type="row.status === 1 ? 'success' : 'danger'">
          {{ row.status === 1 ? '启用' : '禁用' }}
        </el-tag>
      </template>

      <template #operation="{ row }">
        <ReButton type="primary" link buttonText="查看" @click="handleView(row)" />
        <ReButton type="success" link buttonText="编辑" @click="handleEdit(row)" />
        <ReButton type="warning" link buttonText="重置密码" @click="handleResetPassword(row)" />
        <ReButton type="danger"  link buttonText="删除" @click="handleDelete(row)" />
      </template>
    </PureTable>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <BaseForm
        v-model="formData"
        :rules="rules"
        submit-text="保存"
        cancel-text="取消"
        :submit-loading="submitLoading"
        @submit="handleSubmit"
        @cancel="handleDialogClose"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="formData.username"
                placeholder="请输入用户名"
                clearable
                :disabled="dialogTitle === '编辑管理员'"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input
                v-model="formData.realName"
                placeholder="请输入姓名"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input
                v-model="formData.phone"
                placeholder="请输入手机号码"
                clearable
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="formData.email"
                placeholder="请输入邮箱"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="角色" prop="roleId">
              <el-select v-model="formData.roleId" placeholder="请选择角色">
                <el-option label="超级管理员" :value="1" />
                <el-option label="系统管理员" :value="2" />
                <el-option label="财务人员" :value="3" />
                <el-option label="医生" :value="4" />
                <el-option label="护士" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="部门" prop="departmentId">
              <el-select v-model="formData.departmentId" placeholder="请选择部门">
                <el-option label="信息科" :value="1" />
                <el-option label="财务科" :value="2" />
                <el-option label="内科" :value="3" />
                <el-option label="外科" :value="4" />
                <el-option label="护理部" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item v-if="dialogTitle === '新增管理员'" label="密码" prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            show-password
            clearable
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="formData.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </BaseForm>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Delete, Refresh, View, Edit, Key } from "@element-plus/icons-vue";
import { PureTable } from "@pureadmin/table";
import { ReButton, BaseForm } from "../../components";

// 列表列配置
const columns = ref([
  { type: "selection", width: 55, align: "center", hide: false },
  { label: "用户名", prop: "username", minWidth: 120, hide: false },
  { label: "姓名", prop: "realName", minWidth: 120, hide: false },
  { label: "手机号码", prop: "phone", minWidth: 130, hide: false },
  { label: "邮箱", prop: "email", minWidth: 180, hide: false },
  { label: "角色", prop: "roleName", minWidth: 120, hide: false },
  { label: "部门", prop: "departmentName", minWidth: 120, hide: false },
  { label: "状态", prop: "status", width: 80, slot: "status", hide: false },
  { label: "创建时间", prop: "createTime", minWidth: 160, hide: false },
  { label: "操作", fixed: "right", width: 240, slot: "operation", hide: false }
]);

// 头部工具按钮
const headerButtons = ref([
  { type: "primary", label: "新增管理员", icon: "ep:plus", click: openAddDialog },
  { type: "success", label: "刷新", icon: "ep:refresh", click: handleRefresh }
]);

// 数据
const loading = ref(false);
const submitLoading = ref(false);
const adminList = ref<any[]>([]);
const selectedRows = ref<any[]>([]);

// 对话框相关
const dialogVisible = ref(false);
const dialogTitle = ref("");
const formData = ref<any>({
  username: "",
  realName: "",
  phone: "",
  email: "",
  roleId: undefined,
  departmentId: undefined,
  password: "",
  status: 1
});

// 搜索参数
const searchParams = reactive({
  page: 1,
  size: 10,
  username: "",
  realName: "",
  phone: "",
  status: undefined
});

// 分页信息
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 表单验证规则
const rules = {
  username: [
    { required: true, message: "请输入用户名", trigger: "blur" },
    { min: 3, max: 20, message: "用户名长度在 3 到 20 个字符", trigger: "blur" }
  ],
  realName: [
    { required: true, message: "请输入姓名", trigger: "blur" }
  ],
  phone: [
    { required: true, message: "请输入手机号码", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号码", trigger: "blur" }
  ],
  email: [
    { required: true, message: "请输入邮箱", trigger: "blur" },
    { type: "email", message: "请输入正确的邮箱地址", trigger: "blur" }
  ],
  roleId: [
    { required: true, message: "请选择角色", trigger: "change" }
  ],
  departmentId: [
    { required: true, message: "请选择部门", trigger: "change" }
  ],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, max: 20, message: "密码长度在 6 到 20 个字符", trigger: "blur" }
  ]
};

// 获取管理员列表
const fetchAdminList = async (params: any) => {
  loading.value = true;
  try {
    // 模拟API调用
    adminList.value = [
      {
        id: 1,
        username: "admin",
        realName: "系统管理员",
        phone: "13800138000",
        email: "admin@example.com",
        roleName: "超级管理员",
        departmentName: "信息科",
        status: 1,
        createTime: "2024-01-15 10:30:00"
      },
      {
        id: 2,
        username: "doctor001",
        realName: "张医生",
        phone: "13800138001",
        email: "doctor001@example.com",
        roleName: "医生",
        departmentName: "内科",
        status: 1,
        createTime: "2024-01-15 11:20:00"
      }
    ];
    pagination.total = 2;
  } catch (error) {
    ElMessage.error("获取管理员列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = (params: any) => {
  Object.assign(searchParams, params);
  searchParams.page = 1;
  fetchAdminList(searchParams);
};

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    if (key !== 'page' && key !== 'size') {
      delete searchParams[key as keyof typeof searchParams];
    }
  });
  searchParams.page = 1;
  fetchAdminList(searchParams);
};

// 刷新
function handleRefresh() {
  fetchAdminList(searchParams);
};

// 选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection;
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  searchParams.size = size;
  searchParams.page = 1;
  fetchAdminList(searchParams);
};

// 当前页变化
const handleCurrentChange = (current: number) => {
  searchParams.page = current;
  fetchAdminList(searchParams);
};

// 新增管理员对话框
function openAddDialog() {
  dialogTitle.value = "新增管理员";
  formData.value = {
    username: "",
    realName: "",
    phone: "",
    email: "",
    roleId: undefined,
    departmentId: undefined,
    password: "",
    status: 1
  };
  dialogVisible.value = true;
};

// 编辑管理员对话框
const handleEdit = (row: any) => {
  dialogTitle.value = "编辑管理员";
  formData.value = { ...row };
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async (data: any) => {
  submitLoading.value = true;
  try {
    // 这里调用新增或更新API
    ElMessage.success(dialogTitle.value === "新增管理员" ? "新增成功" : "更新成功");
    dialogVisible.value = false;
    fetchAdminList(searchParams);
  } catch (error) {
    ElMessage.error(dialogTitle.value === "新增管理员" ? "新增失败" : "更新失败");
  } finally {
    submitLoading.value = false;
  }
};

// 查看管理员
const handleView = (row: any) => {
  ElMessage.info("查看功能开发中");
};

// 重置密码
const handleResetPassword = async (row: any) => {
  try {
    await ElMessageBox.confirm("确定要重置该管理员的密码吗？", "提示", {
      type: "warning"
    });
    // 调用重置密码API
    ElMessage.success("密码重置成功");
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("密码重置失败");
    }
  }
};

// 删除管理员
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm("确定要删除该管理员吗？", "提示", {
      type: "warning"
    });
    // 调用删除API
    ElMessage.success("删除成功");
    fetchAdminList(searchParams);
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除失败");
    }
  }
};

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个管理员吗？`,
      "提示",
      { type: "warning" }
    );
    // 调用批量删除API
    ElMessage.success("批量删除成功");
    fetchAdminList(searchParams);
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("批量删除失败");
    }
  }
};

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false;
};

// 初始化
onMounted(() => {
  fetchAdminList(searchParams);
});
</script>

<style scoped>
.admin-list {
  padding: 16px;
}
</style>