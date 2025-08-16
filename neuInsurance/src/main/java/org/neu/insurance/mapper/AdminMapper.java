package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.Admin;
import java.util.List;

@Mapper
public interface AdminMapper {
    
    /**
     * 根据条件分页查询用户列表
     */
    List<Admin> selectByCondition(@Param("userType") String userType, 
                                 @Param("department") String department, 
                                 @Param("keyword") String keyword,
                                 @Param("offset") Integer offset,
                                 @Param("pageSize") Integer pageSize);
    
    /**
     * 根据条件统计用户总数
     */
    int countByCondition(@Param("userType") String userType, 
                        @Param("department") String department, 
                        @Param("keyword") String keyword);
    
    /**
     * 根据ID查询用户
     */
    Admin selectById(Long id);
    
    /**
     * 根据用户名查询用户
     */
    Admin selectByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     */
    Admin selectByEmail(String email);
    
    /**
     * 根据手机号查询用户
     */
    Admin selectByPhone(String phone);
    
    /**
     * 新增用户
     */
    int insert(Admin admin);
    
    /**
     * 更新用户
     */
    int update(Admin admin);
    
    /**
     * 根据ID删除用户
     */
    int deleteById(Long id);
    
    /**
     * 批量删除用户
     */
    int batchDelete(List<Long> ids);
    
    /**
     * 根据用户类型查询用户列表
     */
    List<Admin> selectByUserType(String userType);
    
    /**
     * 根据部门查询用户列表
     */
    List<Admin> selectByDepartment(String department);
    
    /**
     * 根据角色查询用户列表
     */
    List<Admin> selectByRole(String role);
    
    /**
     * 更新最后登录信息
     */
    int updateLastLoginInfo(@Param("id") Long id, @Param("ip") String ip);
    
    /**
     * 获取所有用户类型
     */
    List<String> selectAllUserTypes();
    
    /**
     * 获取所有部门
     */
    List<String> selectAllDepartments();
    
    /**
     * 获取所有角色
     */
    List<String> selectAllRoles();
    
    /**
     * 检查用户名是否存在
     */
    int countByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    int countByEmail(String email);
    
    /**
     * 检查手机号是否存在
     */
    int countByPhone(String phone);
}
