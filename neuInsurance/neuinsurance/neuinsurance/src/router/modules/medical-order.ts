export default {
  path: "/medical-order",
  redirect: "/medical-order/list",
  meta: {
    icon: "ep:document",
    title: "医疗订单",
    rank: 3,
    roles: ["SUPER_ADMIN","DOCTOR_ADMIN","FINANCE_ADMIN"]
  },
  children: [
    {
      path: "/medical-order/list",
      name: "MedicalOrderList",
      component: () => import("@/views/medical-order/list.vue"),
      meta: {
        title: "订单列表",
        showParent: true
      }
    },
    {
      path: "/medical-order/add",
      name: "MedicalOrderAdd",
      component: () => import("@/views/medical-order/add.vue"),
      meta: {
        title: "新增订单",
        showParent: true
      }
    },
    {
      path: "/medical-order/edit/:id",
      name: "MedicalOrderEdit",
      component: () => import("@/views/medical-order/edit.vue"),
      meta: {
        title: "编辑订单",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/medical-order/detail/:id",
      name: "MedicalOrderDetail",
      component: () => import("@/views/medical-order/detail.vue"),
      meta: {
        title: "订单详情",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/medical-order/approve",
      name: "MedicalOrderApprove",
      component: () => import("@/views/medical-order/approve.vue"),
      meta: {
        title: "订单审核",
        showParent: true
      }
    },
    {
      path: "/medical-order/stats",
      name: "MedicalOrderStats",
      component: () => import("@/views/medical-order/stats.vue"),
      meta: {
        title: "订单统计",
        showParent: true
      }
    }
  ]
} as RouteConfigsTable; 