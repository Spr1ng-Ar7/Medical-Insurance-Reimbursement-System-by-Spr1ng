package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.MedicalOrder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 医疗订单数据访问接口（整合结算功能）
 */
@Mapper
public interface MedicalOrderMapper {
    
    /**
     * 分页查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrderList(@Param("offset") int offset, 
                                          @Param("pageSize") int pageSize,
                                          @Param("orderNo") String orderNo,
                                          @Param("patientId") Long patientId,
                                          @Param("status") String status,
                                          @Param("keyword") String keyword);
    
    /**
     * 统计医疗订单总数
     */
    int countMedicalOrders(@Param("orderNo") String orderNo,
                          @Param("patientId") Long patientId,
                          @Param("status") String status,
                          @Param("keyword") String keyword);
    
    /**
     * 根据ID查询医疗订单
     */
    MedicalOrder getMedicalOrderById(@Param("id") Long id);
    
    /**
     * 根据订单号查询医疗订单
     */
    MedicalOrder getMedicalOrderByOrderNo(@Param("orderNo") String orderNo);
    
    /**
     * 根据结算单号查询医疗订单
     */
    MedicalOrder getMedicalOrderBySettlementNo(@Param("settlementNo") String settlementNo);
    
    /**
     * 根据患者ID查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByPatientId(@Param("patientId") Long patientId);
    
    /**
     * 新增医疗订单
     */
    int addMedicalOrder(MedicalOrder medicalOrder);
    
    /**
     * 更新医疗订单
     */
    int updateMedicalOrder(MedicalOrder medicalOrder);
    
    /**
     * 删除医疗订单
     */
    int deleteMedicalOrder(@Param("id") Long id);
    
    /**
     * 批量删除医疗订单
     */
    int batchDeleteMedicalOrder(@Param("ids") List<Long> ids);
    
    /**
     * 根据状态查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByStatus(@Param("status") String status);
    
    /**
     * 根据科室查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByDepartment(@Param("department") String department);
    
    /**
     * 根据医生查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByDoctor(@Param("doctorId") Long doctorId);
    
    /**
     * 根据日期范围查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByDateRange(@Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);
    
    /**
     * 根据金额范围查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByAmountRange(@Param("minAmount") Double minAmount,
                                                    @Param("maxAmount") Double maxAmount);
    
    /**
     * 根据结算状态查询订单列表
     */
    List<MedicalOrder> getOrdersBySettlementStatus(@Param("status") String status);
    
    /**
     * 根据结算日期范围查询订单列表
     */
    List<MedicalOrder> getOrdersBySettlementDateRange(@Param("startDate") String startDate,
                                                     @Param("endDate") String endDate);
    
    /**
     * 根据报销金额范围查询订单列表
     */
    List<MedicalOrder> getOrdersByReimbursementAmountRange(@Param("minAmount") Double minAmount,
                                                          @Param("maxAmount") Double maxAmount);
    
    /**
     * 获取所有订单状态
     */
    List<String> getAllOrderStatuses();
    
    /**
     * 获取所有科室
     */
    List<String> getAllDepartments();
    
    /**
     * 获取所有结算状态
     */
    List<String> getAllSettlementStatuses();
    
    /**
     * 计算订单总金额
     */
    Double calculateOrderAmount(@Param("orderId") Long orderId);
    
    /**
     * 获取订单统计信息
     */
    Map<String, Object> getOrderStatistics();
    
    /**
     * 获取结算统计信息
     */
    Map<String, Object> getSettlementStatistics(@Param("startDate") String startDate,
                                               @Param("endDate") String endDate,
                                               @Param("hospitalId") Long hospitalId);
    
    /**
     * 统计患者订单总数
     */
    int countMedicalOrdersByPatientId(@Param("patientId") Long patientId);
    
    /**
     * 获取患者订单历史
     */
    List<MedicalOrder> getPatientOrderHistory(@Param("patientId") Long patientId,
                                             @Param("offset") int offset,
                                             @Param("pageSize") int pageSize);
    
    /**
     * 获取患者结算历史
     */
    List<MedicalOrder> getPatientSettlementHistory(@Param("patientId") Long patientId,
                                                  @Param("offset") int offset,
                                                  @Param("pageSize") int pageSize);
    
    /**
     * 检查订单号是否存在
     */
    int checkOrderNoExists(@Param("orderNo") String orderNo);
    
    /**
     * 检查结算单号是否存在
     */
    int checkSettlementNoExists(@Param("settlementNo") String settlementNo);
    
    /**
     * 批量更新订单状态
     */
    int batchUpdateOrderStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
    
    /**
     * 批量更新结算状态
     */
    int batchUpdateSettlementStatus(@Param("ids") List<Long> ids, 
                                   @Param("status") Integer status,
                                   @Param("approvalResult") String approvalResult,
                                   @Param("approvalRemark") String approvalRemark);

    List<MedicalOrder> getOrdersByFilter(String reportType, String department, String status, Date startDate, Date endDate);
} 