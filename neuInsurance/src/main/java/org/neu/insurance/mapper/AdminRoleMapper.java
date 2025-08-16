package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.Role;
import java.util.List;

/**
 * 管理员角色关联Mapper
 */
@Mapper
public interface AdminRoleMapper {
    
    /**
     * 查询管理员的所有角色
     */
    List<Role> selectAdminRoles(@Param("adminId") Long adminId);
    
    /**
     * 删除管理员的所有角色关联
     */
    void deleteAdminRoles(@Param("adminId") Long adminId);
    
    /**
     * 批量插入管理员角色关联
     */
    void insertAdminRoles(@Param("adminId") Long adminId, @Param("roleIds") List<Long> roleIds);
} 