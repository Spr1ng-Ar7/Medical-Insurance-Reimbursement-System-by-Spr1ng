package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.Drug;

import java.util.List;
import java.util.Map;

/**
 * 药品服务接口
 */
public interface DrugService {
    
    /**
     * 分页查询药品列表
     */
    PageResponse<Drug> getDrugList(String drugName, String drugType, Integer status, PageRequest pageRequest);
    
    /**
     * 根据ID获取药品详情
     */
    Drug getDrugById(Long id);
    
    /**
     * 新增药品
     */
    void addDrug(Drug drug);
    
    /**
     * 更新药品信息
     */
    void updateDrug(Drug drug);
    
    /**
     * 删除药品
     */
    void deleteDrug(Long id);
    
    
    void batchAddDrugs(List<Drug> drugs);
    /**
     * 根据药品编码查询
     */
    Drug getDrugByCode(String drugCode);

    /**
     * 获取所有药品
     */
    List<Drug> getAllDrugs();
    
    /**
     * 获取药品类型统计
     */
    Map<String, Object> getDrugTypeStatistics();
    
    /**
     * 获取药品状态统计
     */
    Map<String, Object> getDrugStatusStatistics();
    
    /**
     * 获取生产厂家统计
     */
    Map<String, Object> getManufacturerStatistics();
    
    /**
     * 获取药品价格区间统计
     */
    Map<String, Object> getDrugPriceStatistics();
    
    /**
     * 获取综合统计信息
     */
    Map<String, Object> getDrugComprehensiveStatistics();
} 