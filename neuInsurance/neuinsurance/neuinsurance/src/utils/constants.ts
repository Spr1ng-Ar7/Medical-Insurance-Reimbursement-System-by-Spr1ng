// 性别选项
export const GENDER_OPTIONS = [
  { label: "男", value: "男" },
  { label: "女", value: "女" }
];

// 保险类型选项
export const INSURANCE_TYPE_OPTIONS = [
  { label: "城镇职工医保", value: "城镇职工医保" },
  { label: "城乡居民医保", value: "城乡居民医保" },
  { label: "新农合", value: "新农合" }
];

// 药品分类选项
export const DRUG_CATEGORY_OPTIONS = [
  { label: "西药", value: "西药" },
  { label: "中药", value: "中药" },
  { label: "生物制品", value: "生物制品" },
  { label: "医疗器械", value: "医疗器械" }
];

// 订单状态选项
export const ORDER_STATUS_OPTIONS = [
  { label: "待审核", value: "PENDING" },
  { label: "已通过", value: "APPROVED" },
  { label: "已拒绝", value: "REJECTED" },
  { label: "已结算", value: "SETTLED" }
];

// 结算状态选项
export const SETTLEMENT_STATUS_OPTIONS = [
  { label: "待处理", value: "PENDING" },
  { label: "处理中", value: "PROCESSING" },
  { label: "已完成", value: "COMPLETED" },
  { label: "处理失败", value: "FAILED" }
];

// 权限类型选项
export const PERMISSION_TYPE_OPTIONS = [
  { label: "菜单", value: "MENU" },
  { label: "按钮", value: "BUTTON" },
  { label: "接口", value: "API" }
];

// 状态选项
export const STATUS_OPTIONS = [
  { label: "启用", value: true },
  { label: "禁用", value: false }
];

// 获取状态标签类型
export const getStatusTagType = (status: boolean) => {
  return status ? "success" : "danger";
};

// 获取订单状态标签类型
export const getOrderStatusTagType = (status: string) => {
  const statusMap: Record<string, string> = {
    PENDING: "warning",
    APPROVED: "success",
    REJECTED: "danger",
    SETTLED: "info"
  };
  return statusMap[status] || "info";
};

// 获取结算状态标签类型
export const getSettlementStatusTagType = (status: string) => {
  const statusMap: Record<string, string> = {
    PENDING: "warning",
    PROCESSING: "primary",
    COMPLETED: "success",
    FAILED: "danger"
  };
  return statusMap[status] || "info";
}; 