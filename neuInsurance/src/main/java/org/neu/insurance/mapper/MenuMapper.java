package org.neu.insurance.mapper;

import org.neu.insurance.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 菜单数据访问层接口
 */
@Mapper
public interface MenuMapper {
    
    /**
     * 根据ID查询菜单
     */
    Menu selectById(@Param("id") Long id);
    
    /**
     * 根据菜单编码查询菜单
     */
    Menu selectByMenuCode(@Param("menuCode") String menuCode);
    
    /**
     * 分页查询菜单列表
     */
    List<Menu> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询菜单总数
     */
    long count();
    
    /**
     * 查询所有菜单
     */
    List<Menu> selectAll();
    
    /**
     * 根据父菜单ID查询子菜单列表
     */
    List<Menu> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据用户ID获取用户权限菜单
     */
    List<Menu> selectUserMenus(@Param("userId") Long userId);
    
    /**
     * 根据角色ID获取角色权限菜单
     */
    List<Menu> selectRoleMenus(@Param("roleId") Long roleId);
    
    /**
     * 插入菜单
     */
    void insert(Menu menu);
    
    /**
     * 更新菜单信息
     */
    void update(Menu menu);
    
    /**
     * 根据ID删除菜单
     */
    void deleteById(@Param("id") Long id);
    
    /**
     * 批量删除菜单
     */
    void batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 更新菜单状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 检查菜单编码是否存在
     */
    boolean existsByMenuCode(@Param("menuCode") String menuCode);
    
    /**
     * 检查菜单路径是否存在
     */
    boolean existsByMenuPath(@Param("menuPath") String menuPath);
    
    /**
     * 模糊搜索菜单名称
     */
    List<Menu> searchByName(@Param("keyword") String keyword);
    
    /**
     * 根据菜单类型查询菜单列表
     */
    List<Menu> selectByMenuType(@Param("menuType") String menuType);
} 