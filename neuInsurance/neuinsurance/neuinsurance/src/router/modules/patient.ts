export default {
  path: "/patient",
  redirect: "/patient/list",
  meta: {
    icon: "ep:user",
    title: "患者管理",
    rank: 2,
    roles: ["SUPER_ADMIN","DOCTOR_ADMIN","NURSE_ADMIN"]
  },
  children: [
    {
      path: "/patient/list",
      name: "PatientList",
      component: () => import("@/views/patient/list.vue"),
      meta: {
        title: "患者列表",
        showParent: true
      }
    },
    {
      path: "/patient/add",
      name: "PatientAdd",
      component: () => import("@/views/patient/add.vue"),
      meta: {
        title: "新增患者",
        showParent: true
      }
    },
    {
      path: "/patient/edit/:id",
      name: "PatientEdit",
      component: () => import("@/views/patient/edit.vue"),
      meta: {
        title: "编辑患者",
        showParent: true,
        hidden: true
      }
    },
    {
      path: "/patient/detail/:id",
      name: "PatientDetail",
      component: () => import("@/views/patient/detail.vue"),
      meta: {
        title: "患者详情",
        showParent: true,
        hidden: true
      }
    }
  ]
} as RouteConfigsTable; 