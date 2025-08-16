package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.ReimbursementLevel;
import java.util.List;

/**
 * 报销等级服务接口
 */
public interface ReimbursementLevelService {
    
    /**
     * 分页查询报销等级列表
     */
    PageResponse<ReimbursementLevel> getReimbursementLevelList(String levelName, String insuranceType, 
                                                               String hospitalLevel, Integer status, 
                                                               PageRequest pageRequest);
    
    /**
     * 根据ID获取报销等级详情
     */
    ReimbursementLevel getReimbursementLevelById(Long id);
    
    /**
     * 新增报销等级
     */
    void addReimbursementLevel(ReimbursementLevel level);
    
    /**
     * 更新报销等级信息
     */
    void updateReimbursementLevel(Long id, ReimbursementLevel level);
    
    /**
     * 删除报销等级
     */
    void deleteReimbursementLevel(Long id);
    
    /**
     * 批量删除报销等级
     */
    void batchDeleteReimbursementLevel(List<Long> ids);
    
    /**
     * 更新状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 获取有效的报销等级列表
     */
    List<ReimbursementLevel> getEffectiveReimbursementLevels();
    
    /**
     * 根据医保类型和医院等级获取匹配的报销等级
     */
    List<ReimbursementLevel> getMatchingReimbursementLevels(String insuranceType, String hospitalLevel);
    
    /**
     * 复制报销等级配置
     */
    void copyReimbursementLevel(Long id);

    
    /**
     * 验证报销等级配置是否有效
     */
    boolean validateReimbursementLevel(ReimbursementLevel level);
} 