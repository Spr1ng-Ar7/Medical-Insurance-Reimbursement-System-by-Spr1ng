export default {
  path: "/facility",
  redirect: "/facility/list",
  meta: {
    icon: "ep:office-building",
    title: "医疗设施",
    rank: 7,
    roles: ["SUPER_ADMIN"]
  },
  children: [
    {
      path: "/facility/list",
      name: "FacilityList",
      component: () => import("@/views/facility/list.vue"),
      meta: {
        title: "医疗设施管理",
        showParent: true
      }
    }
  ]
} as RouteConfigsTable;
