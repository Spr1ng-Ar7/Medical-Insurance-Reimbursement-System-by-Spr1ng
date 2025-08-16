package org.neu.insurance.interceptor;

import org.neu.insurance.entity.Permission;
import org.neu.insurance.service.AdminService;
import org.neu.insurance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    
    @Autowired
    private AdminService adminService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                           Object handler) throws Exception {
        
        // 1. 获取请求路径和方法
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        // 2. 白名单路径，不需要验证权限
        if (isWhitelistPath(requestURI)) {
            return true;
        }
        
        // 3. 获取当前用户信息（从JWT令牌中解析）
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"未提供访问令牌\"}");
            return false;
        }
        
        // 4. 验证令牌
        if (!JwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"访问令牌无效\"}");
            return false;
        }
        
        if (JwtUtil.isTokenExpired(token)) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"访问令牌已过期\"}");
            return false;
        }
        
        // 5. 获取用户信息
        String username = JwtUtil.getUsernameFromToken(token);
        if (username == null || username.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"无法解析用户信息\"}");
            return false;
        }
        
        // 6. 检查权限
        if (!checkPermission(username, requestURI, method)) {
            response.setStatus(403);
            response.getWriter().write("{\"code\":403,\"message\":\"权限不足\"}");
            return false;
        }
        
        return true;
    }
    
    /**
     * 检查是否为白名单路径
     */
    private boolean isWhitelistPath(String requestURI) {
        String[] whitelist = {
            "/api/auth/login",
            "/api/admin/login",  // 添加管理员登录路径
            "/api/auth/logout", 
            "/api/auth/refresh",
            "/error",
            "/favicon.ico",
            "/static/",
            "/public/"
        };
        
        for (String path : whitelist) {
            if (requestURI.startsWith(path)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查用户权限
     */
    private boolean checkPermission(String username, String requestURI, String method) {
        try {
            // 1. 获取用户信息
            org.neu.insurance.entity.Admin admin = adminService.getAdminByUsername(username);
            if (admin == null || admin.getStatus() == 0) {
                return false;
            }
            
            // 2. 获取用户权限列表
            List<String> permissions = adminService.getAdminPermissions(admin.getId())
                    .stream()
                    .map(Permission::getPermissionCode)
                    .collect(java.util.stream.Collectors.toList());
            
            // 3. 检查是否有超级管理员权限
            if (permissions.contains("SUPER_ADMIN")) {
                return true;
            }
            
            // 4. 根据请求路径和方法检查具体权限
            String requiredPermission = getRequiredPermission(requestURI, method);
            if (requiredPermission == null) {
                return true; // 没有明确权限要求，允许访问
            }
            
            return permissions.contains(requiredPermission);
            
        } catch (Exception e) {
            // 记录日志
            System.err.println("权限检查异常: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据请求路径和方法获取所需权限
     */
    private String getRequiredPermission(String requestURI, String method) {
        // 管理员管理权限
        if (requestURI.startsWith("/api/admin")) {
            if (method.equals("GET")) {
                return "ADMIN_VIEW";
            } else if (method.equals("POST")) {
                return "ADMIN_CREATE";
            } else if (method.equals("PUT")) {
                return "ADMIN_UPDATE";
            } else if (method.equals("DELETE")) {
                return "ADMIN_DELETE";
            }
        }
        
        // 角色管理权限
        if (requestURI.startsWith("/api/role")) {
            if (method.equals("GET")) {
                return "ROLE_VIEW";
            } else if (method.equals("POST")) {
                return "ROLE_CREATE";
            } else if (method.equals("PUT")) {
                return "ROLE_UPDATE";
            } else if (method.equals("DELETE")) {
                return "ROLE_DELETE";
            }
        }
        
        // 权限管理权限
        if (requestURI.startsWith("/api/permission")) {
            if (method.equals("GET")) {
                return "PERMISSION_VIEW";
            } else if (method.equals("POST")) {
                return "PERMISSION_CREATE";
            } else if (method.equals("PUT")) {
                return "PERMISSION_UPDATE";
            } else if (method.equals("DELETE")) {
                return "PERMISSION_DELETE";
            }
        }
        
        // 药品管理权限
        if (requestURI.startsWith("/api/drug")) {
            if (method.equals("GET")) {
                return "DRUG_VIEW";
            } else if (method.equals("POST")) {
                return "DRUG_CREATE";
            } else if (method.equals("PUT")) {
                return "DRUG_UPDATE";
            } else if (method.equals("DELETE")) {
                return "DRUG_DELETE";
            }
        }
        
        // 患者管理权限
        if (requestURI.startsWith("/api/patient")) {
            if (method.equals("GET")) {
                return "PATIENT_VIEW";
            } else if (method.equals("POST")) {
                return "PATIENT_CREATE";
            } else if (method.equals("PUT")) {
                return "PATIENT_UPDATE";
            } else if (method.equals("DELETE")) {
                return "PATIENT_DELETE";
            }
        }
        
        // 结算管理权限
        if (requestURI.startsWith("/api/settlement")) {
            if (method.equals("GET")) {
                return "SETTLEMENT_VIEW";
            } else if (method.equals("POST")) {
                return "SETTLEMENT_CREATE";
            } else if (method.equals("PUT")) {
                return "SETTLEMENT_UPDATE";
            } else if (method.equals("DELETE")) {
                return "SETTLEMENT_DELETE";
            }
        }
        
        return null; // 没有明确权限要求
    }
} 