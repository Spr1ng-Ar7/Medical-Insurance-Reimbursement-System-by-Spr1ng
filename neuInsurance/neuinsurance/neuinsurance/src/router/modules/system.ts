export default {
  path: "/system",
  redirect: "/system/admin",
  meta: {
    icon: "ep:setting",
    title: "系统管理",
    rank: 5,
    roles: ["SUPER_ADMIN"]
  },
  children: [
    {
      path: "/system/admin",
      name: "SystemAdmin",
      component: () => import("@/views/system/admin.vue"),
      meta: {
        title: "管理员管理",
        showParent: true
      }
    },
    {
      path: "/system/role",
      name: "SystemRole",
      component: () => import("@/views/system/role.vue"),
      meta: {
        title: "角色管理",
        showParent: true
      }
    },
    {
      path: "/system/permission",
      name: "SystemPermission",
      component: () => import("@/views/system/permission.vue"),
      meta: {
        title: "权限管理",
        showParent: true
      }
    },
    {
      path: "/system/menu",
      name: "SystemMenu",
      component: () => import("@/views/system/menu.vue"),
      meta: {
        title: "菜单管理",
        showParent: true,
        hidden: true
      }
    }
  ]
} as RouteConfigsTable; 