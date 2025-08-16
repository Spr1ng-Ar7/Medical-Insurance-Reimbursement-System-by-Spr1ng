export default {
  path: "/reimbursement",
  redirect: "/reimbursement/list",
  meta: {
    icon: "ep:wallet",
    title: "报销等级",
    rank: 6,
    roles: ["SUPER_ADMIN","FINANCE_ADMIN"]
  },
  children: [
    {
      path: "/reimbursement/list",
      name: "ReimbursementList",
      component: () => import("@/views/reimbursement/list.vue"),
      meta: {
        title: "报销等级管理",
        showParent: true
      }
    }
  ]
} as RouteConfigsTable;
