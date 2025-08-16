package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.MedicalService;
import java.util.List;

@Mapper
public interface MedicalServiceMapper {
    
    /**
     * 根据条件分页查询医疗服务列表
     */
    List<MedicalService> selectByCondition(@Param("serviceType") String serviceType, 
                                          @Param("category") String category, 
                                          @Param("keyword") String keyword,
                                          @Param("offset") Integer offset,
                                          @Param("pageSize") Integer pageSize);
    
    /**
     * 根据条件统计医疗服务总数
     */
    int countByCondition(@Param("serviceType") String serviceType, 
                        @Param("category") String category, 
                        @Param("keyword") String keyword);
    
    /**
     * 根据ID查询医疗服务
     */
    MedicalService selectById(Long id);
    
    /**
     * 根据服务编码查询医疗服务
     */
    MedicalService selectByServiceCode(String serviceCode);
    
    /**
     * 根据国家编码查询医疗服务
     */
    MedicalService selectByNationalCode(String nationalCode);
    
    /**
     * 新增医疗服务
     */
    int insert(MedicalService medicalService);
    
    /**
     * 更新医疗服务
     */
    int update(MedicalService medicalService);
    
    /**
     * 根据ID删除医疗服务
     */
    int deleteById(Long id);
    
    /**
     * 批量删除医疗服务
     */
    int batchDelete(List<Long> ids);
    
    /**
     * 根据服务类型查询医疗服务列表
     */
    List<MedicalService> selectByServiceType(String serviceType);
    
    /**
     * 根据类别查询医疗服务列表
     */
    List<MedicalService> selectByCategory(String category);
    
    /**
     * 根据科室查询医疗服务列表
     */
    List<MedicalService> selectByDepartment(String department);
    
    /**
     * 根据医保分类查询医疗服务列表
     */
    List<MedicalService> selectByInsuranceCategory(String insuranceCategory);
    
    /**
     * 根据是否可报销查询医疗服务列表
     */
    List<MedicalService> selectByReimbursable(Integer reimbursable);
    
    /**
     * 根据价格范围查询医疗服务列表
     */
    List<MedicalService> selectByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * 根据报销比例范围查询医疗服务列表
     */
    List<MedicalService> selectByReimbursementRatio(@Param("minRatio") Double minRatio, @Param("maxRatio") Double maxRatio);
    
    /**
     * 获取所有服务类型
     */
    List<String> selectAllServiceTypes();
    
    /**
     * 获取所有服务类别
     */
    List<String> selectAllCategories();
    
    /**
     * 获取所有科室
     */
    List<String> selectAllDepartments();
    
    /**
     * 获取所有医保分类
     */
    List<String> selectAllInsuranceCategories();
    
    /**
     * 检查服务编码是否存在
     */
    int countByServiceCode(String serviceCode);
    
    /**
     * 检查国家编码是否存在
     */
    int countByNationalCode(String nationalCode);
} 