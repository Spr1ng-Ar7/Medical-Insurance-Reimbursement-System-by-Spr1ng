package org.neu.insurance.controller;

import org.neu.insurance.entity.Menu;
import org.neu.insurance.service.MenuService;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/menus")
@Tag(name = "菜单管理", description = "系统菜单权限管理相关接口")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    /**
     * 根据ID查询菜单
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询菜单", description = "根据菜单ID获取菜单详细信息")
    public Result<Menu> getMenuById(
            @Parameter(description = "菜单ID", required = true)
            @PathVariable Long id) {
        try {
            Menu menu = menuService.getMenuById(id);
            return Result.success(menu);
        } catch (Exception e) {
            log.error("查询菜单失败", e);
            return Result.error("查询菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据菜单编码查询菜单
     */
    @GetMapping("/code/{menuCode}")
    @Operation(summary = "根据菜单编码查询菜单", description = "根据菜单编码获取菜单信息")
    public Result<Menu> getMenuByCode(
            @Parameter(description = "菜单编码", required = true)
            @PathVariable String menuCode) {
        try {
            Menu menu = menuService.getMenuByCode(menuCode);
            return Result.success(menu);
        } catch (Exception e) {
            log.error("查询菜单失败", e);
            return Result.error("查询菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询菜单列表
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询菜单列表", description = "分页获取菜单列表")
    public Result<PageResponse<Menu>> getMenuList(@RequestBody PageRequest pageRequest) {
        try {
            PageResponse<Menu> response = menuService.getMenuList(pageRequest);
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询菜单列表失败", e);
            return Result.error("查询菜单列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取菜单树结构
     */
    @GetMapping("/tree")
    @Operation(summary = "获取菜单树结构", description = "获取完整的菜单树结构")
    public Result<List<Menu>> getMenuTree() {
        try {
            List<Menu> menuTree = menuService.getMenuTree();
            return Result.success(menuTree);
        } catch (Exception e) {
            log.error("获取菜单树结构失败", e);
            return Result.error("获取菜单树失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据父菜单ID查询子菜单列表
     */
    @GetMapping("/parent/{parentId}")
    @Operation(summary = "根据父菜单ID查询子菜单", description = "根据父菜单ID获取子菜单列表")
    public Result<List<Menu>> getMenusByParentId(
            @Parameter(description = "父菜单ID", required = true)
            @PathVariable Long parentId) {
        try {
            List<Menu> menus = menuService.getMenusByParentId(parentId);
            return Result.success(menus);
        } catch (Exception e) {
            log.error("查询子菜单列表失败", e);
            return Result.error("查询子菜单列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据用户ID获取用户权限菜单
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "获取用户权限菜单", description = "根据用户ID获取用户权限菜单")
    public Result<List<Menu>> getUserMenus(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long userId) {
        try {
            List<Menu> menus = menuService.getUserMenus(userId);
            return Result.success(menus);
        } catch (Exception e) {
            log.error("获取用户菜单失败", e);
            return Result.error("获取用户菜单失败: " + e.getMessage());
        }
    }

    /**
     * 根据角色ID获取角色权限菜单
     */
    @GetMapping("/role/{roleId}")
    @Operation(summary = "获取角色权限菜单", description = "根据角色ID获取角色权限菜单")
    public Result<List<Menu>> getRoleMenus(
            @Parameter(description = "角色ID", required = true)
            @PathVariable Long roleId) {
        try {
            List<Menu> menus = menuService.getRoleMenus(roleId);
            return Result.success(menus);
        } catch (Exception e) {
            log.error("获取角色菜单失败", e);
            return Result.error("获取角色菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建菜单
     */
    @PostMapping
    @Operation(summary = "创建菜单", description = "创建新的菜单记录")
    public Result<Void> createMenu(@RequestBody Menu menu) {
        try {
            // 检查菜单编码是否已存在
            if (menuService.checkMenuCodeExists(menu.getMenuCode())) {
                return Result.error("菜单编码已存在");
            }
            // 检查菜单路径是否已存在
            if (menu.getMenuPath() != null && menuService.checkMenuPathExists(menu.getMenuPath())) {
                return Result.error("菜单路径已存在");
            }
            menuService.createMenu(menu);
            return Result.success("菜单创建成功", null);
        } catch (Exception e) {
            log.error("创建菜单失败", e);
            return Result.error("创建菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新菜单信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新菜单信息", description = "更新指定菜单的信息")
    public Result<Void> updateMenu(
            @Parameter(description = "菜单ID", required = true)
            @PathVariable Long id,
            @RequestBody Menu menu) {
        try {
            menu.setId(id);
            menuService.updateMenu(menu);
            return Result.success("菜单信息更新成功", null);
        } catch (Exception e) {
            log.error("更新菜单信息失败", e);
            return Result.error("更新菜单信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单", description = "根据ID删除菜单")
    public Result<Void> deleteMenu(
            @Parameter(description = "菜单ID", required = true)
            @PathVariable Long id) {
        try {
            menuService.deleteMenu(id);
            return Result.success("菜单删除成功", null);
        } catch (Exception e) {
            log.error("删除菜单失败", e);
            return Result.error("删除菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除菜单
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除菜单", description = "批量删除多个菜单")
    public Result<Void> batchDeleteMenus(@RequestBody List<Long> ids) {
        try {
            menuService.batchDeleteMenus(ids);
            return Result.success("批量删除菜单成功", null);
        } catch (Exception e) {
            log.error("批量删除菜单失败", e);
            return Result.error("批量删除菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用菜单
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改菜单状态", description = "启用或禁用菜单")
    public Result<Void> changeMenuStatus(
            @Parameter(description = "菜单ID", required = true)
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
            if (status == null) {
                return Result.error("状态参数不能为空");
            }
            menuService.changeMenuStatus(id, status);
            return Result.success("菜单状态更新成功", null);
        } catch (Exception e) {
            log.error("更改菜单状态失败", e);
            return Result.error("更改菜单状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 模糊搜索菜单名称
     */
    @GetMapping("/search")
    @Operation(summary = "搜索菜单", description = "根据关键词模糊搜索菜单名称")
    public Result<List<Menu>> searchMenus(
            @Parameter(description = "搜索关键词", required = true)
            @RequestParam String keyword) {
        try {
            List<Menu> menus = menuService.searchMenusByName(keyword);
            return Result.success(menus);
        } catch (Exception e) {
            log.error("搜索菜单失败", e);
            return Result.error("搜索菜单失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据菜单类型查询菜单列表
     */
    @GetMapping("/type/{menuType}")
    @Operation(summary = "根据类型查询菜单", description = "根据菜单类型获取菜单列表")
    public Result<List<Menu>> getMenusByType(
            @Parameter(description = "菜单类型", required = true)
            @PathVariable String menuType) {
        try {
            List<Menu> menus = menuService.getMenusByType(menuType);
            return Result.success(menus);
        } catch (Exception e) {
            log.error("查询菜单列表失败", e);
            return Result.error("查询菜单列表失败: " + e.getMessage());
        }
    }
} 