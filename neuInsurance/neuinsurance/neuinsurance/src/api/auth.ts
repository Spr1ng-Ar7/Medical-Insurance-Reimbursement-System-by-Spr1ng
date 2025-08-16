import { http } from "@/utils/http";

export interface LoginParams {
  username: string;
  password: string;
}

export interface LoginResult {
  token: string;
  refreshToken: string;
  userInfo: {
    id: number;
    username: string;
    realName: string;
    avatar?: string;
    roles: string[];
    permissions: string[];
  };
}

export interface RefreshTokenParams {
  refreshToken: string;
}

/** 用户登录 */
export const login = (data: LoginParams) => {
  return http.request<LoginResult>("post", "/auth/login", { data });
};

/** 刷新token */
export const refreshToken = (data: RefreshTokenParams) => {
  return http.request<{ token: string }>("post", "/auth/refresh", { data });
};

/** 用户登出 */
export const logout = () => {
  return http.request("post", "/auth/logout");
};

/** 获取用户信息 */
export const getUserInfo = () => {
  return http.request<LoginResult["userInfo"]>("get", "/auth/userInfo");
}; 