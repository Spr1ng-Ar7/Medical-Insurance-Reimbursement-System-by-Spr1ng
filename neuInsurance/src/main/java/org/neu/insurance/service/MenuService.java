package org.neu.insurance.service;

import org.neu.insurance.entity.Menu;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import java.util.List;

/**
 * 菜单管理服务接口
 */
public interface MenuService {
    
    /**
     * 根据ID查询菜单
     */
    Menu getMenuById(Long id);
    
    /**
     * 根据菜单编码查询菜单
     */
    Menu getMenuByCode(String menuCode);
    
    /**
     * 分页查询菜单列表
     */
    PageResponse<Menu> getMenuList(PageRequest pageRequest);

    /**
     * 根据角色获取菜单
     * @param role 角色标识
     * @return 菜单列表
     */
    List<Menu> getRoleMenus(Long roleId);

    /**
     * 获取菜单树结构
     */
    List<Menu> getMenuTree();
    
    /**
     * 根据父菜单ID查询子菜单列表
     */
    List<Menu> getMenusByParentId(Long parentId);
    
    /**
     * 根据用户ID获取用户权限菜单
     */
    List<Menu> getUserMenus(Long userId);

    
    /**
     * 创建菜单
     */
    void createMenu(Menu menu);
    
    /**
     * 更新菜单信息
     */
    void updateMenu(Menu menu);
    
    /**
     * 删除菜单
     */
    void deleteMenu(Long id);
    
    /**
     * 批量删除菜单
     */
    void batchDeleteMenus(List<Long> ids);
    
    /**
     * 启用/禁用菜单
     */
    void changeMenuStatus(Long id, Integer status);
    
    /**
     * 检查菜单编码是否存在
     */
    boolean checkMenuCodeExists(String menuCode);
    
    /**
     * 检查菜单路径是否存在
     */
    boolean checkMenuPathExists(String menuPath);
    
    /**
     * 模糊搜索菜单名称
     */
    List<Menu> searchMenusByName(String keyword);
    
    /**
     * 根据菜单类型查询菜单列表
     */
    List<Menu> getMenusByType(String menuType);
}