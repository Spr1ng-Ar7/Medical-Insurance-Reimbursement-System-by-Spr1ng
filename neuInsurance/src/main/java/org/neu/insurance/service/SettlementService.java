package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.SettlementResult;
import org.neu.insurance.entity.Settlement;
import java.util.List;

/**
 * 医保结算服务接口
 */
public interface SettlementService {
    
    /**
     * 计算报销金额
     */
    SettlementResult calculateSettlement(Long patientId, Long hospitalId, List<Long> orderIds);
    
    /**
     * 提交结算
     */
    String submitSettlement(Settlement settlement);
    
    /**
     * 分页查询结算记录
     */
    PageResponse<Settlement> getSettlementList(String settlementNo, Long patientId, Long hospitalId, 
                                               Integer status, PageRequest pageRequest);
    
    /**
     * 根据ID获取结算详情
     */
    Settlement getSettlementById(Long id);
    
    /**
     * 结算统计分析
     */
    Object getSettlementStatistics(String startDate, String endDate, Long hospitalId);
} 