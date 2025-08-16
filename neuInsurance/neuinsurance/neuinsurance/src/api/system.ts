import { http } from "@/utils/http";

// 角色管理
export interface Role {
  id?: number;
  name: string;
  code: string;
  description?: string;
  status: boolean;
  createTime?: string;
  updateTime?: string;
}

export interface RoleQueryParams {
  page: number;
  size: number;
  name?: string;
  code?: string;
  status?: boolean;
}

export interface RoleListResult {
  records: Role[];
  total: number;
  size: number;
  current: number;
}

// 权限管理
export interface Permission {
  id?: number;
  name: string;
  code: string;
  type: "MENU" | "BUTTON" | "API";
  parentId?: number;
  path?: string;
  component?: string;
  icon?: string;
  sort: number;
  status: boolean;
  createTime?: string;
  updateTime?: string;
}

export interface PermissionQueryParams {
  page: number;
  size: number;
  name?: string;
  code?: string;
  type?: string;
  parentId?: number;
}

export interface PermissionListResult {
  records: Permission[];
  total: number;
  size: number;
  current: number;
}

// 菜单管理
export interface Menu {
  id?: number;
  name: string;
  path: string;
  component: string;
  icon?: string;
  parentId?: number;
  sort: number;
  status: boolean;
  createTime?: string;
  updateTime?: string;
  children?: Menu[];
}

export interface MenuQueryParams {
  page: number;
  size: number;
  name?: string;
  path?: string;
  parentId?: number;
}

export interface MenuListResult {
  records: Menu[];
  total: number;
  size: number;
  current: number;
}

// 管理员管理
export interface Admin {
  id?: number;
  username: string;
  realName: string;
  email?: string;
  phone?: string;
  avatar?: string;
  status: boolean;
  roleIds: number[];
  createTime?: string;
  updateTime?: string;
}

export interface AdminQueryParams {
  page: number;
  size: number;
  username?: string;
  realName?: string;
  status?: boolean;
}

export interface AdminListResult {
  records: Admin[];
  total: number;
  size: number;
  current: number;
}

// 角色相关API
export const getRoleList = (params: RoleQueryParams) => {
  return http.request<RoleListResult>("get", "/role/list", { params });
};

export const getRoleDetail = (id: number) => {
  return http.request<Role>("get", `/role/${id}`);
};

export const createRole = (data: Role) => {
  return http.request("post", "/role", { data });
};

export const updateRole = (id: number, data: Role) => {
  return http.request("put", `/role/${id}`, { data });
};

export const deleteRole = (id: number) => {
  return http.request("delete", `/role/${id}`);
};

// 权限相关API
export const getPermissionList = (params: PermissionQueryParams) => {
  return http.request<PermissionListResult>("get", "/permission/list", { params });
};

export const getPermissionTree = () => {
  return http.request<Permission[]>("get", "/permission/tree");
};

export const createPermission = (data: Permission) => {
  return http.request("post", "/permission", { data });
};

export const updatePermission = (id: number, data: Permission) => {
  return http.request("put", `/permission/${id}`, { data });
};

export const deletePermission = (id: number) => {
  return http.request("delete", `/permission/${id}`);
};

// 菜单相关API
export const getUserRoleMenus = () => {
  return http.request<Menu[]>("get", "/api/menus/user/roles/menus");
};

export const getUserMenus = () => {
  return http.request<Menu[]>("get", "/api/menus/current-user/menus");
};

export const getMenuList = (params: MenuQueryParams) => {
  return http.request<MenuListResult>("get", "/api/menus/list", { params });
};

export const getMenuTree = () => {
  return http.request<Menu[]>("get", "/api/menus/tree");
};

export const createMenu = (data: Menu) => {
  return http.request("post", "/api/menus", { data });
};

export const updateMenu = (id: number, data: Menu) => {
  return http.request("put", `/api/menus/${id}`, { data });
};

export const deleteMenu = (id: number) => {
  return http.request("delete", `/api/menus/${id}`);
};

// 管理员相关API
export const getAdminList = (params: AdminQueryParams) => {
  return http.request<AdminListResult>("get", "/admin/list", { params });
};

export const getAdminDetail = (id: number) => {
  return http.request<Admin>("get", `/admin/${id}`);
};

export const createAdmin = (data: Admin) => {
  return http.request("post", "/admin", { data });
};

export const updateAdmin = (id: number, data: Admin) => {
  return http.request("put", `/admin/${id}`, { data });
};

export const deleteAdmin = (id: number) => {
  return http.request("delete", `/admin/${id}`);
}; 