package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.MedicalService;

import java.util.List;

/**
 * 统一医疗服务管理服务接口
 * 整合：诊疗项目、检查服务、手术服务、服务设施等
 */
public interface MedicalServiceService {
    
    /**
     * 分页查询医疗服务列表
     */
    PageResponse<MedicalService> getMedicalServiceList(PageRequest pageRequest, String serviceType, String category, String keyword);
    
    /**
     * 根据ID查询医疗服务
     */
    MedicalService getMedicalServiceById(Long id);
    
    /**
     * 根据编码查询医疗服务
     */
    MedicalService getMedicalServiceByCode(String serviceCode);
    
    /**
     * 新增医疗服务
     */
    boolean addMedicalService(MedicalService medicalService);
    
    /**
     * 更新医疗服务
     */
    boolean updateMedicalService(MedicalService medicalService);
    
    /**
     * 删除医疗服务
     */
    boolean deleteMedicalService(Long id);
    
    /**
     * 批量删除医疗服务
     */
    boolean batchDeleteMedicalService(List<Long> ids);
    
    /**
     * 根据服务类型查询医疗服务列表
     */
    List<MedicalService> getMedicalServicesByType(String serviceType);
    
    /**
     * 根据类别查询医疗服务列表
     */
    List<MedicalService> getMedicalServicesByCategory(String category);
    
    /**
     * 根据科室查询医疗服务列表
     */
    List<MedicalService> getMedicalServicesByDepartment(String department);
    
    /**
     * 根据医保分类查询医疗服务列表
     */
    List<MedicalService> getMedicalServicesByInsuranceCategory(String insuranceCategory);
    
    /**
     * 查询可报销的医疗服务
     */
    List<MedicalService> getReimbursableServices();
    
    /**
     * 查询不可报销的医疗服务
     */
    List<MedicalService> getNonReimbursableServices();
    
    /**
     * 启用/禁用医疗服务
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 获取所有服务类型
     */
    List<String> getAllServiceTypes();
    
    /**
     * 获取所有服务类别
     */
    List<String> getAllCategories();
    
    /**
     * 获取所有科室
     */
    List<String> getAllDepartments();
    
    /**
     * 获取所有医保分类
     */
    List<String> getAllInsuranceCategories();
    
    /**
     * 检查服务编码是否存在
     */
    boolean isServiceCodeExists(String serviceCode);
    
    /**
     * 检查国家编码是否存在
     */
    boolean isNationalCodeExists(String nationalCode);
    
    /**
     * 根据价格范围查询医疗服务
     */
    List<MedicalService> getMedicalServicesByPriceRange(Double minPrice, Double maxPrice);
    
    /**
     * 根据报销比例查询医疗服务
     */
    List<MedicalService> getMedicalServicesByReimbursementRatio(Double minRatio, Double maxRatio);
} 