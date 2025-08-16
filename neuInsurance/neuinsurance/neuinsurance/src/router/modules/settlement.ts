export default {
  path: "/settlement",
  redirect: "/settlement/list",
  meta: {
    icon: "ep:money",
    title: "结算管理",
    rank: 4,
    roles: ["SUPER_ADMIN","FINANCE_ADMIN"]
  },
  children: [
    {
      path: "/settlement/list",
      name: "SettlementList",
      component: () => import("@/views/settlement/list.vue"),
      meta: {
        title: "结算列表",
        showParent: true
      }
    },
    {
      path: "/settlement/detail/:id",
      name: "SettlementDetail",
      component: () => import("@/views/settlement/detail.vue"),
      meta: {
        title: "结算详情",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/settlement/process",
      name: "SettlementProcess",
      component: () => import("@/views/settlement/process.vue"),
      meta: {
        title: "结算处理",
        showParent: true
      }
    },
    {
      path: "/settlement/stats",
      name: "SettlementStats",
      component: () => import("@/views/settlement/stats.vue"),
      meta: {
        title: "结算统计",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/settlement/report",
      name: "SettlementReport",
      component: () => import("@/views/settlement/report.vue"),
      meta: {
        title: "结算报表",
        showParent: true
      }
    }
  ]
} as RouteConfigsTable; 