<template>
  <div class="system-permission">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>权限管理</span>
          <div class="header-actions">
            <ReButton type="primary" @click="handleAdd" buttonText="新增" />
            <ReButton type="success" @click="handleExpandAll" buttonText="展开全部" />
            <ReButton type="warning" @click="handleCollapseAll" buttonText="收起全部" />
          </div>
        </div>
      </template>
      
      <!-- 权限树 -->
      <div class="permission-tree">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          :props="treeProps"
          node-key="id"
          default-expand-all
          :expand-on-click-node="false"
        >
          <template #default="{ data }">
            <div class="permission-node">
              <div class="node-content">
                <el-icon v-if="data.icon" class="node-icon">
                  <component :is="data.icon" />
                </el-icon>
                <span class="node-label">{{ data.name }}</span>
                <span class="node-code" v-if="data.code">({{ data.code }})</span>
              </div>
              <div class="node-actions">
                <ReButton type="primary" link size="small" @click="handleEdit(data)" buttonText="编辑" />
                <ReButton type="success" link size="small" @click="handleAddChild(data)" buttonText="添加子项" />
                <ReButton 
                  v-if="data.id !== 1"
                  type="danger" 
                  link 
                  size="small" 
                  @click="handleDelete(data)"
                >
                  <el-icon><Delete /></el-icon>
                  删除
                </ReButton>
              </div>
            </div>
          </template>
        </el-tree>
      </div>
      
      <!-- 新增/编辑对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="600px"
      >
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="100px"
        >
          <el-form-item label="权限名称" prop="name">
            <el-input
              v-model="formData.name"
              placeholder="请输入权限名称"
            />
          </el-form-item>
          
          <el-form-item label="权限编码" prop="code">
            <el-input
              v-model="formData.code"
              placeholder="请输入权限编码"
              :disabled="isEdit"
            />
          </el-form-item>
          
          <el-form-item label="权限类型" prop="type">
            <el-radio-group v-model="formData.type">
              <el-radio :label="1">菜单</el-radio>
              <el-radio :label="2">按钮</el-radio>
              <el-radio :label="3">接口</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="上级权限" prop="parentId">
            <el-tree-select
              v-model="formData.parentId"
              :data="permissionOptions"
              :props="treeProps"
              placeholder="请选择上级权限"
              check-strictly
              :render-after-expand="false"
            />
          </el-form-item>
          
          <el-form-item label="图标" prop="icon">
            <el-input
              v-model="formData.icon"
              placeholder="请输入图标名称"
            />
          </el-form-item>
          
          <el-form-item label="路径" prop="path">
            <el-input
              v-model="formData.path"
              placeholder="请输入路径"
            />
          </el-form-item>
          
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model="formData.sort"
              :min="0"
              :max="999"
              placeholder="请输入排序"
            />
          </el-form-item>
          
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              :rows="3"
              placeholder="请输入权限描述"
            />
          </el-form-item>
        </el-form>
        
        <template #footer>
          <span class="dialog-footer">
            <ReButton @click="dialogVisible = false" buttonText="取消" />
            <ReButton type="primary" @click="handleSubmit" :loading="submitLoading" buttonText="确定" />
          </span>
        </template>
      </el-dialog>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Plus, Edit, Delete, Expand, Fold } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";
import { ReButton } from "../../components";

// 数据
const permissionTree = ref([]);
const permissionTreeRef = ref();

// 对话框相关
const dialogVisible = ref(false);
const dialogTitle = ref("");
const isEdit = ref(false);
const submitLoading = ref(false);
const formRef = ref<FormInstance>();

// 树配置
const treeProps = {
  children: 'children',
  label: 'name'
};

// 表单数据
const formData = reactive({
  id: "",
  name: "",
  code: "",
  type: 1,
  parentId: null,
  icon: "",
  path: "",
  sort: 0,
  status: 1,
  description: ""
});

// 表单验证规则
const rules: FormRules = {
  name: [
    { required: true, message: "请输入权限名称", trigger: "blur" }
  ],
  code: [
    { required: true, message: "请输入权限编码", trigger: "blur" },
    { pattern: /^[A-Z_]+$/, message: "权限编码只能包含大写字母和下划线", trigger: "blur" }
  ],
  type: [
    { required: true, message: "请选择权限类型", trigger: "change" }
  ]
};

// 权限选项（用于上级权限选择）
const permissionOptions = computed(() => {
  return [{ id: 0, name: '顶级权限', children: permissionTree.value }];
});

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
        code: "SYSTEM_MANAGE",
        type: 1,
        parentId: 0,
        icon: "Setting",
        path: "/system",
        sort: 1,
        status: 1,
        description: "系统管理模块",
        children: [
          {
            id: 11,
            name: "用户管理",
            code: "USER_MANAGE",
            type: 1,
            parentId: 1,
            icon: "User",
            path: "/system/user",
            sort: 1,
            status: 1,
            description: "用户管理",
            children: [
              {
                id: 111,
                name: "用户查询",
                code: "USER_QUERY",
                type: 2,
                parentId: 11,
                icon: "Search",
                path: "",
                sort: 1,
                status: 1,
                description: "查询用户信息"
              },
              {
                id: 112,
                name: "用户新增",
                code: "USER_ADD",
                type: 2,
                parentId: 11,
                icon: "Plus",
                path: "",
                sort: 2,
                status: 1,
                description: "新增用户"
              }
            ]
          },
          {
            id: 12,
            name: "角色管理",
            code: "ROLE_MANAGE",
            type: 1,
            parentId: 1,
            icon: "UserFilled",
            path: "/system/role",
            sort: 2,
            status: 1,
            description: "角色管理",
            children: []
          }
        ]
      },
      {
        id: 2,
        name: "业务管理",
        code: "BUSINESS_MANAGE",
        type: 1,
        parentId: 0,
        icon: "Document",
        path: "/business",
        sort: 2,
        status: 1,
        description: "业务管理模块",
        children: [
          {
            id: 21,
            name: "患者管理",
            code: "PATIENT_MANAGE",
            type: 1,
            parentId: 2,
            icon: "User",
            path: "/patient",
            sort: 1,
            status: 1,
            description: "患者管理",
            children: []
          },
          {
            id: 22,
            name: "订单管理",
            code: "ORDER_MANAGE",
            type: 1,
            parentId: 2,
            icon: "Document",
            path: "/medical-order",
            sort: 2,
            status: 1,
            description: "医疗订单管理",
            children: []
          }
        ]
      }
    ];
  } catch (error) {
    ElMessage.error("获取权限树失败");
  }
};

// 展开全部
const handleExpandAll = () => {
  permissionTreeRef.value?.expandAll();
};

// 折叠全部
const handleCollapseAll = () => {
  permissionTreeRef.value?.collapseAll();
};

// 新增
const handleAdd = () => {
  dialogTitle.value = "新增权限";
  isEdit.value = false;
  Object.assign(formData, {
    id: "",
    name: "",
    code: "",
    type: 1,
    parentId: null,
    icon: "",
    path: "",
    sort: 0,
    status: 1,
    description: ""
  });
  dialogVisible.value = true;
};

// 添加子权限
const handleAddChild = (data: any) => {
  dialogTitle.value = "新增子权限";
  isEdit.value = false;
  Object.assign(formData, {
    id: "",
    name: "",
    code: "",
    type: 1,
    parentId: data.id,
    icon: "",
    path: "",
    sort: 0,
    status: 1,
    description: ""
  });
  dialogVisible.value = true;
};

// 编辑
const handleEdit = (data: any) => {
  dialogTitle.value = "编辑权限";
  isEdit.value = true;
  Object.assign(formData, data);
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
    //   await updatePermission(formData);
    // } else {
    //   await createPermission(formData);
    // }
    
    ElMessage.success(isEdit.value ? "更新成功" : "新增成功");
    dialogVisible.value = false;
    fetchPermissionTree();
  } catch (error) {
    console.error("表单验证失败:", error);
  } finally {
    submitLoading.value = false;
  }
};

// 删除
const handleDelete = async (data: any) => {
  try {
    await ElMessageBox.confirm("确定要删除该权限吗？删除后不可恢复！", "提示", {
      type: "warning"
    });
    
    // 这里调用删除API
    // await deletePermission(data.id);
    
    ElMessage.success("删除成功");
    fetchPermissionTree();
  } catch (error) {
    if (error !== "cancel") {
      ElMessage.error("删除失败");
    }
  }
};

// 初始化
onMounted(() => {
  fetchPermissionTree();
});
</script>

<style scoped>
.system-permission {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.permission-tree {
  margin-top: 20px;
}

.permission-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 4px 0;
}

.node-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.node-icon {
  color: #409eff;
}

.node-label {
  font-weight: 500;
}

.node-code {
  color: #909399;
  font-size: 12px;
}

.node-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.permission-node:hover .node-actions {
  opacity: 1;
}

:deep(.el-tree-node__content) {
  height: auto;
  padding: 8px 0;
}

:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}
</style>