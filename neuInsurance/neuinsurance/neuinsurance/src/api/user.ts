import { http } from "@/utils/http";

export type UserResult = {
  success: boolean;
  data: {
    /** 用户信息 */
    user: {
      /** 用户名 */
      username: string;
      /** 角色 */
      role: string;
      /** 头像 */
      avatar: string;
      /** 昵称 */
      nickname: string;
    };
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

export type RefreshTokenResult = {
  success: boolean;
  data: {
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

/** 登录 */
export const getLogin = (data?: object) => {
  // 后端 AdminController.login 映射为 POST /api/admin/login，用户名和密码通过 @RequestParam 接收
  // 因此发送方式改用 `params`，Axios 会自动序列化为查询字符串：username=xxx&password=yyy
  return http.request<UserResult>("post", "/api/admin/login", {
    params: data
  });
};

/** 刷新`token` */
export const refreshTokenApi = (data?: object) => {
  return http.request<RefreshTokenResult>("post", "/refresh-token", { data });
};
