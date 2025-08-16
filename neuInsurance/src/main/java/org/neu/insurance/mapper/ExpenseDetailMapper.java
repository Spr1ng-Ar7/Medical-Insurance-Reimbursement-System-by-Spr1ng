package org.neu.insurance.mapper;

import org.neu.insurance.entity.ExpenseDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 费用明细数据访问层接口
 */
@Mapper
public interface ExpenseDetailMapper {
    
    /**
     * 根据ID查询费用明细
     */
    ExpenseDetail selectById(@Param("id") Long id);
    
    /**
     * 根据患者ID查询费用明细列表
     */
    List<ExpenseDetail> selectByPatientId(@Param("patientId") Long patientId);
    
    /**
     * 根据订单ID查询费用明细列表
     */
    List<ExpenseDetail> selectByOrderId(@Param("orderId") Long orderId);
    
    /**
     * 分页查询费用明细列表
     */
    List<ExpenseDetail> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询费用明细总数
     */
    long count();
    
    /**
     * 根据项目类型查询费用明细列表
     */
    List<ExpenseDetail> selectByItemType(@Param("itemType") String itemType);
    
    /**
     * 插入费用明细
     */
    void insert(ExpenseDetail expenseDetail);
    
    /**
     * 更新费用明细信息
     */
    void update(ExpenseDetail expenseDetail);
    
    /**
     * 根据ID删除费用明细
     */
    void deleteById(@Param("id") Long id);
    
    /**
     * 批量删除费用明细
     */
    void batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 更新费用明细状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 统计费用总额
     */
    BigDecimal calculateTotalAmount(@Param("patientId") Long patientId, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    /**
     * 统计报销总额
     */
    BigDecimal calculateReimbursementAmount(@Param("patientId") Long patientId, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * 统计自付总额
     */
    BigDecimal calculateSelfPayAmount(@Param("patientId") Long patientId, 
                                     @Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    /**
     * 根据项目类型统计费用
     */
    List<ExpenseDetail> selectExpenseStatsByItemType(@Param("patientId") Long patientId, 
                                                    @Param("startDate") LocalDateTime startDate, 
                                                    @Param("endDate") LocalDateTime endDate);
} 