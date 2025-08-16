package org.neu.insurance.service.impl;

import org.neu.insurance.entity.Menu;
import org.neu.insurance.service.MenuService;
import org.neu.insurance.mapper.MenuMapper;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单管理服务实现类
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Override
    public Menu getMenuById(Long id) {
        try {
            log.info("查询菜单信息，ID: {}", id);
            return menuMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询菜单失败，ID: {}", id, e);
            throw new RuntimeException("查询菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    public Menu getMenuByCode(String menuCode) {
        try {
            log.info("根据菜单编码查询菜单，code: {}", menuCode);
            return menuMapper.selectByMenuCode(menuCode);
        } catch (Exception e) {
            log.error("根据菜单编码查询菜单失败，code: {}", menuCode, e);
            throw new RuntimeException("查询菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    public PageResponse<Menu> getMenuList(PageRequest pageRequest) {
        try {
            log.info("分页查询菜单列表，页码: {}, 页大小: {}", pageRequest.getPageNum(), pageRequest.getPageSize());
            int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
            List<Menu> menus = menuMapper.selectPage(offset, pageRequest.getPageSize());
            long total = menuMapper.count();
            
            PageResponse<Menu> response = new PageResponse<>();
            response.setRecords(menus);
            response.setTotal(total);
            response.setPageNum(pageRequest.getPageNum());
            response.setPageSize(pageRequest.getPageSize());
            return response;
        } catch (Exception e) {
            log.error("分页查询菜单列表失败", e);
            throw new RuntimeException("查询菜单列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Menu> getMenuTree() {
        try {
            log.info("获取菜单树结构");
            List<Menu> allMenus = menuMapper.selectAll();
            return buildMenuTree(allMenus, 0L);
        } catch (Exception e) {
            log.error("获取菜单树结构失败", e);
            throw new RuntimeException("获取菜单树失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Menu> getMenusByParentId(Long parentId) {
        try {
            log.info("根据父菜单ID查询子菜单列表，parentId: {}", parentId);
            return menuMapper.selectByParentId(parentId);
        } catch (Exception e) {
            log.error("根据父菜单ID查询子菜单列表失败，parentId: {}", parentId, e);
            throw new RuntimeException("查询子菜单列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Menu> getUserMenus(Long userId) {
        try {
            log.info("根据用户ID获取用户权限菜单，userId: {}", userId);
            return menuMapper.selectUserMenus(userId);
        } catch (Exception e) {
            log.error("根据用户ID获取用户权限菜单失败，userId: {}", userId, e);
            throw new RuntimeException("获取用户菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Menu> getRoleMenus(Long roleId) {
        try {
            log.info("根据角色ID获取角色权限菜单，roleId: {}", roleId);
            return menuMapper.selectRoleMenus(roleId);
        } catch (Exception e) {
            log.error("根据角色ID获取角色权限菜单失败，roleId: {}", roleId, e);
            throw new RuntimeException("获取角色菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void createMenu(Menu menu) {
        try {
            log.info("创建菜单，菜单名称: {}", menu.getMenuName());
            menu.setCreateTime(LocalDateTime.now());
            menuMapper.insert(menu);
            log.info("菜单创建成功，ID: {}", menu.getId());
        } catch (Exception e) {
            log.error("创建菜单失败，菜单名称: {}", menu.getMenuName(), e);
            throw new RuntimeException("创建菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateMenu(Menu menu) {
        try {
            log.info("更新菜单信息，ID: {}", menu.getId());
            menu.setUpdateTime(LocalDateTime.now());
            menuMapper.update(menu);
            log.info("菜单信息更新成功");
        } catch (Exception e) {
            log.error("更新菜单信息失败，ID: {}", menu.getId(), e);
            throw new RuntimeException("更新菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteMenu(Long id) {
        try {
            log.info("删除菜单，ID: {}", id);
            // 检查是否有子菜单
            List<Menu> children = menuMapper.selectByParentId(id);
            if (!children.isEmpty()) {
                throw new RuntimeException("存在子菜单，无法删除");
            }
            menuMapper.deleteById(id);
            log.info("菜单删除成功");
        } catch (Exception e) {
            log.error("删除菜单失败，ID: {}", id, e);
            throw new RuntimeException("删除菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteMenus(List<Long> ids) {
        try {
            log.info("批量删除菜单，数量: {}", ids.size());
            // 检查是否有子菜单
            for (Long id : ids) {
                List<Menu> children = menuMapper.selectByParentId(id);
                if (!children.isEmpty()) {
                    throw new RuntimeException("菜单ID " + id + " 存在子菜单，无法删除");
                }
            }
            menuMapper.batchDelete(ids);
            log.info("批量删除菜单成功");
        } catch (Exception e) {
            log.error("批量删除菜单失败", e);
            throw new RuntimeException("批量删除菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void changeMenuStatus(Long id, Integer status) {
        try {
            log.info("更改菜单状态，ID: {}, 状态: {}", id, status);
            menuMapper.updateStatus(id, status);
            log.info("菜单状态更新成功");
        } catch (Exception e) {
            log.error("更改菜单状态失败，ID: {}", id, e);
            throw new RuntimeException("更改菜单状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkMenuCodeExists(String menuCode) {
        try {
            log.info("检查菜单编码是否存在，menuCode: {}", menuCode);
            return menuMapper.existsByMenuCode(menuCode);
        } catch (Exception e) {
            log.error("检查菜单编码失败，menuCode: {}", menuCode, e);
            throw new RuntimeException("检查菜单编码失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkMenuPathExists(String menuPath) {
        try {
            log.info("检查菜单路径是否存在，menuPath: {}", menuPath);
            return menuMapper.existsByMenuPath(menuPath);
        } catch (Exception e) {
            log.error("检查菜单路径失败，menuPath: {}", menuPath, e);
            throw new RuntimeException("检查菜单路径失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Menu> searchMenusByName(String keyword) {
        try {
            log.info("模糊搜索菜单名称，keyword: {}", keyword);
            return menuMapper.searchByName(keyword);
        } catch (Exception e) {
            log.error("模糊搜索菜单名称失败，keyword: {}", keyword, e);
            throw new RuntimeException("搜索菜单失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Menu> getMenusByType(String menuType) {
        try {
            log.info("根据菜单类型查询菜单列表，menuType: {}", menuType);
            return menuMapper.selectByMenuType(menuType);
        } catch (Exception e) {
            log.error("根据菜单类型查询菜单列表失败，menuType: {}", menuType, e);
            throw new RuntimeException("查询菜单列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 构建菜单树
     */
    private List<Menu> buildMenuTree(List<Menu> allMenus, Long parentId) {
        return allMenus.stream()
            .filter(menu -> parentId.equals(menu.getParentId()))
            .map(menu -> {
                List<Menu> children = buildMenuTree(allMenus, menu.getId());
                menu.setChildren(children);
                return menu;
            })
            .collect(Collectors.toList());
    }
} 