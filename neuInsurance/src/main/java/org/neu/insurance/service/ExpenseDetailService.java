package org.neu.insurance.service;

import org.neu.insurance.entity.ExpenseDetail;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用明细管理服务接口
 */
public interface ExpenseDetailService {
    
    /**
     * 根据ID查询费用明细
     */
    ExpenseDetail getExpenseDetailById(Long id);
    
    /**
     * 根据患者ID查询费用明细列表
     */
    List<ExpenseDetail> getExpenseDetailsByPatientId(Long patientId);
    
    /**
     * 根据订单ID查询费用明细列表
     */
    List<ExpenseDetail> getExpenseDetailsByOrderId(Long orderId);
    
    /**
     * 分页查询费用明细列表
     */
    PageResponse<ExpenseDetail> getExpenseDetailList(PageRequest pageRequest);
    
    /**
     * 根据项目类型查询费用明细列表
     */
    List<ExpenseDetail> getExpenseDetailsByItemType(String itemType);
    
    /**
     * 创建费用明细
     */
    void createExpenseDetail(ExpenseDetail expenseDetail);
    
    /**
     * 更新费用明细信息
     */
    void updateExpenseDetail(ExpenseDetail expenseDetail);
    
    /**
     * 删除费用明细
     */
    void deleteExpenseDetail(Long id);
    
    /**
     * 批量删除费用明细
     */
    void batchDeleteExpenseDetails(List<Long> ids);
    
    /**
     * 更改费用明细状态
     */
    void changeExpenseDetailStatus(Long id, Integer status);
    
    /**
     * 根据条件统计费用总额
     */
    BigDecimal calculateTotalAmount(Long patientId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 根据条件统计报销总额
     */
    BigDecimal calculateReimbursementAmount(Long patientId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 根据条件统计自付总额
     */
    BigDecimal calculateSelfPayAmount(Long patientId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 根据项目类型统计费用
     */
    List<ExpenseDetail> getExpenseStatsByItemType(Long patientId, LocalDateTime startDate, LocalDateTime endDate);
} 