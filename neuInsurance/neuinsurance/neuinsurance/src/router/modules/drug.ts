export default {
  path: "/drug",
  redirect: "/drug/list",
  meta: {
    icon: "ep:medicine-box",
    title: "药品管理",
    rank: 2,
    hidden: false,
    roles: ["SUPER_ADMIN"]
  },
  children: [
    {
      path: "/drug/list",
      name: "DrugList",
      component: () => import("@/views/drug/list.vue"),
      meta: {
        title: "药品列表",
        showParent: true
      }
    },
    {
      path: "/drug/add",
      name: "DrugAdd",
      component: () => import("@/views/drug/add.vue"),
      meta: {
        title: "新增药品",
        showParent: true
      }
    },
    {
      path: "/drug/edit/:id",
      name: "DrugEdit",
      component: () => import("@/views/drug/edit.vue"),
      meta: {
        title: "编辑药品",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/drug/detail/:id",
      name: "DrugDetail",
      component: () => import("@/views/drug/detail.vue"),
      meta: {
        title: "药品详情",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/drug/stats",
      name: "DrugStats",
      component: () => import("@/views/drug/stats.vue"),
      meta: {
        title: "药品统计",
        showParent: true
      }
    }
  ]
} as RouteConfigsTable; 