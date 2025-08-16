<template>
  <div class="layout">
    <aside class="sidebar">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
      >
        <menu-item
          v-for="menu in filteredMenus"
          :key="menu.id"
          :menu="menu"
        />
      </el-menu>
    </aside>
    <main class="main">
      <header class="header">
        <div class="header-left">
          <el-icon class="collapse-icon" @click="toggleCollapse">
            <component :is="isCollapse ? 'Expand' : 'Fold'" />
          </el-icon>
          <el-breadcrumb>
            <el-breadcrumb-item
              v-for="item in breadcrumb"
              :key="item.path"
              :to="item.path"
            >
              {{ item.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ username }}
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      <div class="content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'
import { Menu } from '@/api/system'
import { ROLE_MENUS, ROLE_MAP } from '@/config/roleMenus'
import { getUserMenus } from '@/api/system'
import MenuItem from './components/MenuItem.vue'

const route = useRoute()
const router = useRouter()

const isCollapse = ref(false)
const menus = ref<Menu[]>([])
const username = ref('')
const filteredMenus = ref<Menu[]>([])

// 获取用户菜单
const getMenus = async () => {
  try {
    const response = await getUserMenus()
    menus.value = response.data
    
    // Get user's role from localStorage
    const user = localStorage.getItem('user')
    if (user) {
      const userData = JSON.parse(user)
      const role = userData.role
      const roleType = ROLE_MAP[role] || 'ADMIN'
      
      // Filter menus based on role
      const roleMenus = ROLE_MENUS[roleType]
      
      // Recursive function to filter menus
      const filterMenus = (menus: Menu[]): Menu[] => {
        return menus.filter(menu => {
          const hasMenu = roleMenus.includes(menu.name)
          const hasChildren = menu.children?.some(child => roleMenus.includes(child.name))
          
          // If menu has children, filter them recursively
          if (menu.children && Array.isArray(menu.children)) {
            const filteredChildren = filterMenus(menu.children)
            if (filteredChildren.length > 0) {
              menu.children = filteredChildren
              return true
            }
          }
          
          return hasMenu || hasChildren
        })
      }
      
      filteredMenus.value = filterMenus(menus.value)
    } else {
      filteredMenus.value = menus.value
    }
  } catch (error) {
    console.error('Failed to get menus:', error)
    ElMessage.error('获取菜单失败，请重试')
  }
}

// 获取用户名
const getUsername = () => {
  const user = localStorage.getItem('user')
  if (user) {
    username.value = JSON.parse(user).username
  }
}

// 初始化
getMenus()
getUsername()

// 监听路由变化
watch(
  () => route.path,
  () => {
    getMenus()
  }
)

const activeMenu = computed(() => {
  const { path } = route
  return path
})

const breadcrumb = computed(() => {
  const matched = route.matched.filter(record => record.meta && record.meta.title)
  return matched
})

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const logout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    router.push('/login')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('退出登录失败')
    }
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 200px;
  min-height: 100vh;
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.sidebar .el-menu {
  border-right: none;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  height: 60px;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #eee;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-icon {
  font-size: 20px;
  margin-right: 15px;
  cursor: pointer;
}

.content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
