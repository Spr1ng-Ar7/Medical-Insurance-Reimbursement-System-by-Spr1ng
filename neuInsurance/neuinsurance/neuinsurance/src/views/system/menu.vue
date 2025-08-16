<template>
  <div class="menu-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <ReButton type="primary" iconName="ep:plus" buttonText="新增菜单" @click="handleAdd" />
        </div>
      </template>

      <!-- 搜索区域 -->
      <ReForm :model="searchForm" inline>
        <el-form-item label="菜单名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入菜单名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-select v-model="searchForm.type" placeholder="请选择菜单类型" clearable>
            <el-option label="目录" value="directory" />
            <el-option label="菜单" value="menu" />
            <el-option label="按钮" value="button" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <ReButton type="primary" iconName="ep:search" buttonText="搜索" @click="handleSearch" />
          <ReButton iconName="ep:refresh" buttonText="重置" @click="handleReset" />
        </el-form-item>
      </ReForm>

      <PureTable
        row-key="id"
        :data="menuList"
        
        :loading="loading"
        :tableToolbar="false"
        :showPagination="false"
      >
        <el-table-column prop="name" label="菜单名称" min-width="120" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <ReTag :type="getTypeTagType(row.type)" :tagText="getTypeLabel(row.type)" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" min-width="120" />
        <el-table-column prop="component" label="组件" min-width="120" />
        <el-table-column prop="icon" label="图标" width="80" />
        <el-table-column prop="permission" label="权限标识" min-width="120" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <ReTag :type="row.status === 1 ? 'success' : 'danger'" :tagText="row.status === 1 ? '启用' : '禁用'" size="small" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <ReButton type="primary" link buttonText="编辑" @click="handleEdit(row)" />
            <ReButton type="success" link buttonText="添加子菜单" @click="handleAddChild(row)" />
            <ReButton type="danger" link buttonText="删除" @click="handleDelete(row)" />
          </template>
        </el-table-column>
      </PureTable>
    </el-card>

    <!-- 菜单表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="上级菜单">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTreeData"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            placeholder="请选择上级菜单"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="directory">目录</el-radio>
            <el-radio label="menu">菜单</el-radio>
            <el-radio label="button">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path" v-if="form.type !== 'button'">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component" v-if="form.type === 'menu'">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="图标" v-if="form.type !== 'button'">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission" v-if="form.type === 'button'">
          <el-input v-model="form.permission" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch
            v-model="form.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <ReButton @click="dialogVisible = false" buttonText="取消" />
        <ReButton type="primary" @click="handleSubmit" :loading="submitLoading" buttonText="确定" />
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import { ReButton, ReTag, ReForm } from "../../components"
import { getMenuList as getMenuListApi, getMenuTree as getMenuTreeApi, createMenu, updateMenu, deleteMenu } from '@/api/system'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

const searchForm = reactive({
  name: '',
  type: ''
})

const form = reactive({
  id: '',
  parentId: null,
  name: '',
  path: '',
  component: '',
  icon: '',
  permission: '',
  type: 'menu',
  sort: 0,
  status: 1
})

const menuList = ref([])
const menuTreeData = ref([])

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ],
  path: [
    { required: true, message: '请输入路由路径', trigger: 'blur' }
  ],
  component: [
    { required: true, message: '请输入组件路径', trigger: 'blur' }
  ],
  permission: [
    { required: true, message: '请输入权限标识', trigger: 'blur' }
  ]
}

// 获取菜单列表
const getMenuList = async () => {
  loading.value = true
  try {
    // 获取菜单列表
    const { data: listResponse } = await getMenuListApi({
      page: 1,
      size: 100,
      name: searchForm.name,
      type: searchForm.type
    })
    menuList.value = listResponse.records || []
    
    // 获取菜单树结构
    const { data: treeResponse } = await getMenuTreeApi()
    menuTreeData.value = [{ 
      id: 0, 
      name: '顶级菜单', 
      children: treeResponse || [] 
    }]
  } catch (error) {
    console.error('获取菜单列表失败:', error)
    ElMessage.error('获取菜单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  getMenuList()
}

// 重置搜索
const handleReset = () => {
  searchForm.name = ''
  searchForm.type = ''
  getMenuList()
}

// 新增菜单
const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  dialogVisible.value = true
  resetForm()
}

// 编辑菜单
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑菜单'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 添加子菜单
const handleAddChild = (row: any) => {
  dialogTitle.value = '新增子菜单'
  dialogVisible.value = true
  resetForm()
  form.parentId = row.id
}

// 删除菜单
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该菜单吗？删除后不可恢复！',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteMenu(row.id)
    ElMessage.success('删除成功')
    await getMenuList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除菜单失败:', error)
      ElMessage.error('删除菜单失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (form.id) {
      await updateMenu(form.id, form)
    } else {
      await createMenu(form)
    }
    
    ElMessage.success(form.id ? '更新成功' : '新增成功')
    dialogVisible.value = false
    await getMenuList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: '',
    parentId: null,
    name: '',
    path: '',
    component: '',
    icon: '',
    permission: '',
    type: 'menu',
    sort: 0,
    status: 1
  })
  formRef.value?.clearValidate()
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 获取类型标签样式
const getTypeTagType = (type: string) => {
  const typeMap: Record<string, 'primary' | 'success' | 'warning' | 'info'> = {
    directory: 'primary',
    menu: 'success',
    button: 'warning'
  }
  return typeMap[type] || 'info'
}

// 获取类型标签文本
const getTypeLabel = (type: string) => {
  const typeMap: Record<string, string> = {
    directory: '目录',
    menu: '菜单',
    button: '按钮'
  }
  return typeMap[type] || type
}

// 页面加载时获取数据
onMounted(() => {
  getMenuList()
})
</script>

<style scoped>
.menu-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-form {
  margin-bottom: 20px;
}
</style>