// 分页参数
export interface PageParams {
  page: number;
  size: number;
}

// 分页结果
export interface PageResult<T> {
  records: T[];
  total: number;
  size: number;
  current: number;
}

// API响应结果
export interface ApiResponse<T = any> {
  code: number;
  message: string;
  data: T;
  success: boolean;
}

// 表格列配置
export interface TableColumn {
  prop: string;
  label: string;
  width?: string | number;
  minWidth?: string | number;
  align?: "left" | "center" | "right";
  fixed?: boolean | "left" | "right";
  sortable?: boolean;
  formatter?: (row: any, column: any, cellValue: any, index: number) => string;
}

// 表单验证规则
export interface FormRule {
  required?: boolean;
  message?: string;
  trigger?: "blur" | "change" | "submit";
  min?: number;
  max?: number;
  pattern?: RegExp;
  validator?: (rule: any, value: any, callback: any) => void;
}

// 路由配置
export interface RouteConfig {
  path: string;
  name: string;
  component: any;
  meta: {
    title: string;
    icon?: string;
    rank?: number;
    showParent?: boolean;
    hidden?: boolean;
    keepAlive?: boolean;
  };
  children?: RouteConfig[];
}

// 菜单项
export interface MenuItem {
  id: number;
  name: string;
  path: string;
  component: string;
  icon?: string;
  parentId?: number;
  sort: number;
  status: boolean;
  children?: MenuItem[];
}

// 权限项
export interface PermissionItem {
  id: number;
  name: string;
  code: string;
  type: "MENU" | "BUTTON" | "API";
  parentId?: number;
  path?: string;
  component?: string;
  icon?: string;
  sort: number;
  status: boolean;
  children?: PermissionItem[];
}

// 用户信息
export interface UserInfo {
  id: number;
  username: string;
  realName: string;
  avatar?: string;
  email?: string;
  phone?: string;
  roles: string[];
  permissions: string[];
}

// 登录参数
export interface LoginParams {
  username: string;
  password: string;
  captcha?: string;
  captchaId?: string;
}

// 登录结果
export interface LoginResult {
  token: string;
  refreshToken: string;
  userInfo: UserInfo;
}

// 文件上传结果
export interface UploadResult {
  url: string;
  filename: string;
  size: number;
  type: string;
}

// 统计图表数据
export interface ChartData {
  name: string;
  value: number;
  [key: string]: any;
}

// 树形数据
export interface TreeNode {
  id: number | string;
  label: string;
  children?: TreeNode[];
  [key: string]: any;
}

// 选择器选项
export interface SelectOption {
  label: string;
  value: any;
  disabled?: boolean;
  children?: SelectOption[];
}

// 表格操作按钮
export interface TableAction {
  label: string;
  type?: "primary" | "success" | "warning" | "danger" | "info";
  icon?: string;
  disabled?: boolean | ((row: any) => boolean);
  onClick: (row: any, index: number) => void;
}

// 搜索表单配置
export interface SearchFormConfig {
  label: string;
  prop: string;
  type: "input" | "select" | "date" | "daterange" | "datetime" | "datetimerange";
  placeholder?: string;
  options?: SelectOption[];
  props?: Record<string, any>;
}

// 表单字段配置
export interface FormFieldConfig {
  label: string;
  prop: string;
  type: "input" | "select" | "textarea" | "date" | "datetime" | "switch" | "radio" | "checkbox" | "upload";
  placeholder?: string;
  options?: SelectOption[];
  rules?: FormRule[];
  props?: Record<string, any>;
} 