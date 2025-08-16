package org.neu.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.LoginResponseDTO;
import org.neu.insurance.entity.Admin;
import org.neu.insurance.service.AdminService;
import org.neu.insurance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统一用户管理控制器
 * 整合：管理员、医生、护士、财务等所有用户类型管理
 */
@RestController
@Tag(name = "统一用户管理控制器", description = "统一用户管理控制器")
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "管理员、医生、护士等用户登录接口")
    public Result<LoginResponseDTO> login(
            @Parameter(description = "用户名", required = true) @RequestParam String username, 
            @Parameter(description = "密码", required = true) @RequestParam String password) {
        try {
            Admin admin = adminService.login(username, password);
            if (admin != null) {
                // 生成JWT token
                String token = JwtUtil.generateToken(username);
                LoginResponseDTO loginResponse = new LoginResponseDTO(admin, token);
                return Result.success("登录成功", loginResponse);
            } else {
                return Result.error("用户名或密码错误");
            }
        } catch (Exception e) {
            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询用户列表", description = "分页获取所有用户信息，支持按用户类型、部门、关键词筛选")
    public Result<PageResponse<Admin>> getAdminList(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "用户类型", required = false) @RequestParam(required = false) String userType,
            @Parameter(description = "部门", required = false) @RequestParam(required = false) String department,
            @Parameter(description = "关键词", required = false) @RequestParam(required = false) String keyword) {
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(pageNum);
            pageRequest.setPageSize(pageSize);
            
            PageResponse<Admin> response = adminService.getAdminList(pageRequest, userType, department, keyword);
            return Result.success("查询成功", response);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询用户", description = "根据用户ID获取用户详细信息")
    public Result<Admin> getAdminById(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        try {
            Admin admin = adminService.getAdminById(id);
            if (admin != null) {
                return Result.success("查询成功",admin);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @Operation(summary = "新增用户", description = "创建新的用户账户")
    public Result<String> addAdmin(@RequestBody Admin admin) {
        try {
            if (adminService.addAdmin(admin)) {
                return Result.success("新增成功");
            } else {
                return Result.error("用户名已存在");
            }
        } catch (Exception e) {
            return Result.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户信息", description = "更新用户的基本信息")
    public Result<String> updateAdmin(@RequestBody Admin admin) {
        try {
            if (adminService.updateAdmin(admin)) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户账户")
    public Result<String> deleteAdmin(
            @Parameter(description = "用户ID", required = true) @PathVariable Long id) {
        try {
            if (adminService.deleteAdmin(id)) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除用户", description = "批量删除多个用户账户")
    public Result<String> batchDeleteAdmin(@RequestBody List<Long> ids) {
        try {
            if (adminService.batchDeleteAdmin(ids)) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    public Result<String> changePassword(
            @Parameter(description = "用户ID", required = true) @RequestParam Long id, 
            @Parameter(description = "旧密码", required = true) @RequestParam String oldPassword, 
            @Parameter(description = "新密码", required = true) @RequestParam String newPassword) {
        try {
            if (adminService.changePassword(id, oldPassword, newPassword)) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("旧密码错误");
            }
        } catch (Exception e) {
            return Result.error("密码修改失败: " + e.getMessage());
        }
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    public Result<String> resetPassword(@RequestParam Long id, @RequestParam String newPassword) {
        try {
            if (adminService.resetPassword(id, newPassword)) {
                return Result.success("密码重置成功");
            } else {
                return Result.error("密码重置失败");
            }
        } catch (Exception e) {
            return Result.error("密码重置失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用用户
     */
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        try {
            if (adminService.updateStatus(id, status)) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据用户类型查询用户列表
     */
    @GetMapping("/byType/{userType}")
    public Result<List<Admin>> getAdminsByType(@PathVariable String userType) {
        try {
            List<Admin> admins = adminService.getAdminsByType(userType);
            return Result.success("查询成功",admins);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据部门查询用户列表
     */
    @GetMapping("/byDepartment/{department}")
    public Result<List<Admin>> getAdminsByDepartment(@PathVariable String department) {
        try {
            List<Admin> admins = adminService.getAdminsByDepartment(department);
            return Result.success("查询成功",admins);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据角色查询用户列表
     */
    @GetMapping("/byRole/{role}")
    public Result<List<Admin>> getAdminsByRole(@PathVariable String role) {
        try {
            List<Admin> admins = adminService.getAdminsByRole(role);
            return Result.success("查询成功",admins);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 锁定/解锁用户
     */
    @PostMapping("/lock")
    public Result<String> lockUser(@RequestParam Long id, @RequestParam boolean locked) {
        try {
            if (adminService.lockUser(id, locked)) {
                return Result.success(locked ? "锁定成功" : "解锁成功");
            } else {
                return Result.error(locked ? "锁定失败" : "解锁失败");
            }
        } catch (Exception e) {
            return Result.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有用户类型
     */
    @GetMapping("/userTypes")
    public Result<List<String>> getAllUserTypes() {
        try {
            List<String> userTypes = adminService.getAllUserTypes();
            return Result.success("查询成功",userTypes);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有部门
     */
    @GetMapping("/departments")
    public Result<List<String>> getAllDepartments() {
        try {
            List<String> departments = adminService.getAllDepartments();
            return Result.success("查询成功",departments);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有角色
     */
    @GetMapping("/roles")
    public Result<List<String>> getAllRoles() {
        try {
            List<String> roles = adminService.getAllRoles();
            return Result.success("查询成功",roles);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/checkUsername")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        try {
            boolean exists = adminService.isUsernameExists(username);
            return Result.success("检查完成",exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/checkEmail")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean exists = adminService.isEmailExists(email);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/checkPhone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        try {
            boolean exists = adminService.isPhoneExists(phone);
            return Result.success("检查完成",exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }
} 