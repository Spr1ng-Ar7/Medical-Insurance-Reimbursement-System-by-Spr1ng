import { useUserStore } from '@/store/modules/user';
import { ROLE_MAP, ROLE_MENUS } from '@/config/roleMenus';

/**
 * 获取当前用户的角色类型
 * @returns 角色类型
 */
const getUserRoleType = (): string => {
  const userStore = useUserStore();
  const roles = userStore.roles;
  if (!roles || roles.length === 0) return 'GUEST';
  const role = roles[0];
  return ROLE_MAP[role] || 'GUEST';
};

/**
 * 检查路由是否需要权限
 * @param route 路由对象
 * @returns 是否需要权限
 */
const isRouteProtected = (route: any): boolean => {
  // 如果路由没有meta或者meta为空，不需要权限
  if (!route.meta || Object.keys(route.meta).length === 0) return false;
  
  // 如果路由有roles属性，需要权限检查
  return route.meta.roles !== undefined;
};

/**
 * 检查用户是否有访问某个路由的权限
 * @param route 路由对象
 * @returns 是否有权限
 */
export const hasRoutePermission = (route: any): boolean => {
  const roleType = getUserRoleType();
  
  // 如果路由不需要权限，直接允许访问
  if (!isRouteProtected(route)) return true;
  
  // 如果路由有roles属性，检查是否包含用户的角色
  if (route.meta.roles) {
    return route.meta.roles.includes(roleType);
  }
  
  // 如果路由有meta.title，检查是否在角色允许的菜单中
  if (route.meta.title) {
    return ROLE_MENUS[roleType]?.includes(route.meta.title) ?? false;
  }
  
  // 默认允许访问
  return true;
};

/**
 * 获取路由的权限提示信息
 * @param route 路由对象
 * @returns 权限提示信息
 */
export const getPermissionMessage = (route: any): string => {
  const roleType = getUserRoleType();
  
  if (route.meta.roles) {
    return `您没有访问此页面的权限。需要以下角色：${route.meta.roles.join('、')}`;
  }
  
  if (route.meta.title) {
    return `您没有访问"${route.meta.title}"的权限。`;
  }
  
  return '您没有访问此页面的权限。';
};

/**
 * 创建权限守卫
 */
export const createPermissionGuard = () => {
  return async (to: any, from: any, next: any) => {
    try {
      // 如果路由不需要权限，直接通过
      if (!isRouteProtected(to)) {
        next();
        return;
      }

      // 检查是否有权限
      if (!hasRoutePermission(to)) {
        // 保存原始路由
        const redirect = to.fullPath;
        
        // 跳转到403页面
        next({
          path: '/error/403',
          replace: true,
          query: {
            redirect: redirect,
            message: getPermissionMessage(to)
          }
        });
        return;
      }

      // 有权限，继续导航
      next();
    } catch (error) {
      console.error('权限检查失败:', error);
      next({
        path: '/error/403',
        replace: true,
        query: {
          redirect: to.fullPath,
          message: '权限检查失败，请重试。'
        }
      });
    }
  };
};
