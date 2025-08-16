<template>
  <div class="system-role">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <ReButton type="primary" iconName="ep:plus" buttonText="新增角色" @click="handleAdd" />
        </div>
      </template>
      <!-- 搜索条件 -->
      <div class="search-form">
        <ReForm :model="searchParams" inline>
          <el-form-item label="角色名称">
            <el-input
              v-model="searchParams.roleName"
              placeholder="请输入角色名称"
              clearable
            />
          </el-form-item>
          <el-form-item label="角色编码">
            <el-input
              v-model="searchParams.roleCode"
              placeholder="请输入角色编码"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchParams.status" placeholder="请选择状态" clearable>
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <ReButton type="primary" iconName="ep:search" buttonText="搜索" @click="handleSearch" />
            <ReButton iconName="ep:refresh" buttonText="重置" @click="handleReset" />
          </el-form-item>
        </ReForm>
      </div>
      <!-- 角色列表 -->
      <PureTable row-key="id" :data="roleList" :columns="columns" :headerButtons="headerButtons" :loading="loading" :pagination="pagination" :tableToolbar="true" :search="true" :searchInfo="searchParams" @search="handleSearch" @reset="handleReset" @page-size-change="handleSizeChange" @page-current-change="handleCurrentChange" >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="description" label="角色描述" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <ReTag :type="row.status === 1 ? 'success' : 'danger'" :tagText="row.status === 1 ? '启用' : '禁用'" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template #default="{ row }">
            <ReButton type="primary" link buttonText="编辑" @click="handleEdit(row)" />
            <ReButton type="success" link buttonText="权限" @click="handlePermission(row)" />
            <ReButton :type="row.status === 1 ? 'danger' : 'success'" link :buttonText="row.status === 1 ? '禁用' : '启用'" @click="handleToggleStatus(row)" />
            <ReButton v-if="row.id !== 1" type="danger" link buttonText="删除" @click="handleDelete(row)" />
          </template>
        </el-table-column>
      </PureTable>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Search, Refresh, Edit, Setting, Switch, Delete } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import { ReButton, ReTag, ReForm } from "../../components";
import { PureTable } from "@pureadmin/table";

// 列表列配置
const columns = ref([
  { prop: "id", label: "ID", width: 80 },
  { prop: "roleName", label: "角色名称", minWidth: 150 },
  { prop: "roleCode", label: "角色编码", minWidth: 150 },
  { prop: "description", label: "角色描述" },
  { prop: "status", label: "状态", width: 80, slot: "status" },
  { prop: "createTime", label: "创建时间", width: 160 },
  { prop: "operation", label: "操作", fixed: "right", width: 250, slot: "operation", align: "center" }
]);

// 头部工具按钮
const headerButtons = ref([
  { type: "primary", label: "新增角色", icon: "ep:plus", click: handleAdd },
  { type: "success", label: "刷新", icon: "ep:refresh", click: handleRefresh }
]);

// 数据
const loading = ref(false);

// 分页信息
const pagination = reactive({ current: 1, size: 10, total: 0 });
const roleList = ref([]);
const currentRole = ref(null);

// 对话框相关
const dialogVisible = ref(false);
const dialogTitle = ref("");
const isEdit = ref(false);
const submitLoading = ref(false);
const formRef = ref<FormInstance>();

// 权限对话框相关
const permissionDialogVisible = ref(false);
const permissionLoading = ref(false);
const permissionTree = ref([]);
const permissionTreeRef = ref();
const treeProps = {
  children: 'children',
  label: 'name'
};

// 搜索参数
const searchParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: "",
  roleCode: "",
  status: undefined
});



// 表单数据
const formData = reactive({
  id: "",
  roleName: "",
  roleCode: "",
  description: "",
  status: 1
});

// 表单验证规则
const rules: FormRules = {
  roleName: [
    { required: true, message: "请输入角色名称", trigger: "blur" }
  ],
  roleCode: [
    { required: true, message: "请输入角色编码", trigger: "blur" },
    { pattern: /^[A-Z_]+$/, message: "角色编码只能包含大写字母和下划线", trigger: "blur" }
  ]
};

// 获取角色列表
const fetchRoleList = async (params: any) => {
  loading.value = true;
  try {
    // 这里调用获取列表API
    // const result = await getRoleList(params);
    // roleList.value = result.records || [];
    // pagination.total = result.total || 0;
    // pagination.current = result.current || 1;
    // pagination.size = result.size || 10;
    
    // 模拟数据
    roleList.value = [
      {
        id: 1,
        roleName: "超级管理员",
        roleCode: "SUPER_ADMIN",
        description: "系统超级管理员，拥有所有权限",
        status: 1,
        createTime: "2024-01-01 00:00:00"
      },
      {
        id: 2,
        roleName: "医生",
        roleCode: "DOCTOR",
        description: "医生角色，可以管理患者和订单",
        status: 1,
        createTime: "2024-01-02 00:00:00"
      },
      {
        id: 3,
        roleName: "护士",
        roleCode: "NURSE",
        description: "护士角色，可以查看患者信息",
        status: 1,
        createTime: "2024-01-03 00:00:00"
      }
    ];
    pagination.total = 3;
  } catch (error) {
    ElMessage.error("获取角色列表失败");
  } finally {
    loading.value = false;
  }
};

// 获取权限树
const fetchPermissionTree = async () => {
  try {
    // 这里调用获取权限树API
    // const result = await getPermissionTree();
    // permissionTree.value = result || [];
    
    // 模拟数据
    permissionTree.value = [
      {
        id: 1,
        name: "系统管理",
        children: [
          {
            id: 11,
            name: "用户管理",
            children: [
              { id: 111, name: "用户查询" },
              { id: 112, name: "用户新增" },
              { id: 113, name: "用户编辑" },
              { id: 114, name: "用户删除" }
            ]
          },
          {
            id: 12,
            name: "角色管理",
            children: [
              { id: 121, name: "角色查询" },
              { id: 122, name: "角色新增" },
              { id: 123, name: "角色编辑" },
              { id: 124, name: "角色删除" }
            ]
          }
        ]
      },
      {
        id: 2,
        name: "业务管理",
        children: [
          {
            id: 21,
            name: "患者管理",
            children: [
              { id: 211, name: "患者查询" },
              { id: 212, name: "患者新增" },
              { id: 213, name: "患者编辑" },
              { id: 214, name: "患者删除" }
            ]
          },
          {
            id: 22,
            name: "订单管理",
            children: [
              { id: 221, name: "订单查询" },
              { id: 222, name: "订单新增" },
              { id: 223, name: "订单编辑" },
              { id: 224, name: "订单删除" }
            ]
          }
        ]
      }
    ];
  } catch (error) {
    ElMessage.error("获取权限树失败");
  }
};

// 搜索
const handleSearch = () => {
  searchParams.pageNum = 1;
  fetchRoleList(searchParams);
};

// 刷新
function handleRefresh() {
  fetchRoleList(searchParams);
}

// 重置
const handleReset = () => {
  Object.keys(searchParams).forEach(key => {
    if (key !== 'pageNum' && key !== 'pageSize') {
      delete searchParams[key as keyof typeof searchParams];
    }
  });
  searchParams.pageNum = 1;
  fetchRoleList(searchParams);
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size;
  searchParams.pageNum = 1;
  fetchRoleList(searchParams);
};

// 当前页变化
const handleCurrentChange = (current: number) => {
  searchParams.pageNum = current;
  fetchRoleList(searchParams);
};

// 新增
function handleAdd() {
  dialogTitle.value = "新增角色";
  isEdit.value = false;
  Object.assign(formData, {
    id: "",
    roleName: "",
    roleCode: "",
    description: "",
    status: 1
  });
  dialogVisible.value = true;
};

// 编辑
const handleEdit = (row: any) => {
  dialogTitle.value = "编辑角色";
  isEdit.value = true;
  Object.assign(formData, row);
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  
  try {
    await formRef.value.validate();
    submitLoading.value = true;
    
    // 这里调用新增或更新API
    // if (isEdit.value) {
    //   await updateRole(formData);
    // } else {
    //   await createRole(formData);
    // }
    
    ElMessage.success(isEdit.value ? "更新成功" : "新增成功");
    dialogVisible.value = false;
    fetchRoleList(searchParams);
  } catch (error) {
    console.error("表单验证失败:", error);
  } finally {
    submitLoading.value = false;
  }
};

// 权限分配
const handlePermission = async (row: any) => {
  currentRole.value = row;
  permissionDialogVisible.value = true;
  
  // 获取该角色的权限
  try {
    // const permissions = await getRolePermissions(row.id);
    // 设置选中的权限
    // permissionTree.value.setCheckedKeys(permissions);
  } catch (error) {
    ElMessage.error("获取角色权限失败");
  }
};

// 保存权限
const handleSavePermission = async () => {
  if (!currentRole.value || !permissionTreeRef.value) return;
  
  permissionLoading.value = true;
  try {
    const checkedKeys = permissionTreeRef.value.getCheckedKeys();
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys();
    const allKeys = [...checkedKeys, ...halfCheckedKeys];
    
    // 这里调用保存权限API
    // await saveRolePermissions(currentRole.value.id, allKeys);
    
    ElMessage.success("权限保存成功");
    permissionDialogVisible.value = false;
  } catch (error) {
    ElMessage.error("权限保存失败");
  } finally {
    permissionLoading.value = false;
  }
};

// 切换状态
const handleToggleStatus = async (row: any) => {
  try {
    const action = row.status === 1 ? "禁用" : "启用";
    await ElMessageBox.confirm(`确定要${action}该角色吗？`, "提示", {
      type: "warning"
    });
    
    // 这里调用更新状态API
    // await updateRoleStatus(row.id, row.status === 1 ? 0 : 1);
    
    ElMessage.success(`${action}成功`);
    fetchRoleList(searchParams);
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("操作失败");
    }
  }
};

// 删除
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm("确定要删除该角色吗？删除后不可恢复！", "提示", {
      type: "warning"
    });
    
    // 这里调用删除API
    // await deleteRole(row.id);
    
    ElMessage.success("删除成功");
    fetchRoleList(searchParams);
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除失败");
    }
  }
};

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
};

// 初始化
onMounted(() => {
  fetchRoleList(searchParams);
  fetchPermissionTree();
});
</script>

<style scoped>
.system-role {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 6px;
}

:deep(.el-table) {
  margin-bottom: 16px;
}

:deep(.el-pagination) {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}

:deep(.el-tree) {
  max-height: 400px;
  overflow-y: auto;
}
</style>