package org.neu.insurance.service;

import org.neu.insurance.entity.Disease;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import java.util.List;

/**
 * 疾病管理服务接口
 */
public interface DiseaseService {
    
    /**
     * 根据ID查询疾病
     */
    Disease getDiseaseById(Long id);
    
    /**
     * 根据疾病编码查询疾病
     */
    Disease getDiseaseByCode(String diseaseCode);
    
    /**
     * 根据ICD编码查询疾病
     */
    Disease getDiseaseByIcdCode(String icdCode);
    
    /**
     * 分页查询疾病列表
     */
    PageResponse<Disease> getDiseaseList(PageRequest pageRequest);
    
    /**
     * 根据疾病分类查询疾病列表
     */
    List<Disease> getDiseasesByCategory(String category);
    
    /**
     * 创建疾病
     */
    void createDisease(Disease disease);
    
    /**
     * 更新疾病信息
     */
    void updateDisease(Disease disease);
    
    /**
     * 删除疾病
     */
    void deleteDisease(Long id);
    
    /**
     * 批量删除疾病
     */
    void batchDeleteDiseases(List<Long> ids);
    
    /**
     * 启用/禁用疾病
     */
    void changeDiseaseStatus(Long id, Integer status);
    
    /**
     * 检查疾病编码是否存在
     */
    boolean checkDiseaseCodeExists(String diseaseCode);
    
    /**
     * 检查ICD编码是否存在
     */
    boolean checkIcdCodeExists(String icdCode);
    
    /**
     * 模糊搜索疾病名称
     */
    List<Disease> searchDiseasesByName(String keyword);
} 