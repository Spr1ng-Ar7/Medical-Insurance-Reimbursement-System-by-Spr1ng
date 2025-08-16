package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.SettlementResult;
import org.neu.insurance.entity.MedicalOrder;

import java.util.List;

/**
 * 医疗订单管理服务接口（整合结算功能）
 */
public interface MedicalOrderService {

    
    /**
     * 分页查询医疗订单列表
     */
    PageResponse<MedicalOrder> getMedicalOrderList(PageRequest pageRequest, String orderNo, Long patientId, String status, String keyword);
    
    /**
     * 根据ID查询医疗订单
     */
    MedicalOrder getMedicalOrderById(Long id);
    
    /**
     * 根据订单号查询医疗订单
     */
    MedicalOrder getMedicalOrderByOrderNo(String orderNo);
    
    /**
     * 根据患者ID查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByPatientId(Long patientId);
    
    /**
     * 新增医疗订单
     */
    boolean addMedicalOrder(MedicalOrder medicalOrder);
    
    /**
     * 更新医疗订单
     */
    boolean updateMedicalOrder(MedicalOrder medicalOrder);
    
    /**
     * 删除医疗订单
     */
    boolean deleteMedicalOrder(Long id);
    
    /**
     * 批量删除医疗订单
     */
    boolean batchDeleteMedicalOrder(List<Long> ids);
    
    /**
     * 根据状态查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByStatus(String status);
    
    /**
     * 根据科室查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByDepartment(String department);
    
    /**
     * 根据医生查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByDoctor(Long doctorId);
    
    /**
     * 根据日期范围查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByDateRange(String startDate, String endDate);
    
    /**
     * 根据金额范围查询医疗订单列表
     */
    List<MedicalOrder> getMedicalOrdersByAmountRange(Double minAmount, Double maxAmount);
    
    /**
     * 更新订单状态
     */
    boolean updateOrderStatus(Long id, String status);
    
    /**
     * 取消订单
     */
    boolean cancelOrder(Long id, String reason);
    
    /**
     * 完成订单
     */
    boolean completeOrder(Long id);
    
    /**
     * 获取所有订单状态
     */
    List<String> getAllOrderStatuses();
    
    /**
     * 获取所有科室
     */
    List<String> getAllDepartments();
    
    /**
     * 检查订单号是否存在
     */
    boolean isOrderNoExists(String orderNo);
    
    /**
     * 生成订单号
     */
    String generateOrderNo();
    
    /**
     * 计算订单总金额
     */
    Double calculateOrderAmount(Long orderId);
    
    /**
     * 获取订单统计信息
     */
    Object getOrderStatistics();
    
    /**
     * 获取患者订单历史
     */
    PageResponse<MedicalOrder> getPatientOrderHistory(Long patientId, PageRequest pageRequest);
    
    // ==================== 结算相关方法 ====================
    
    /**
     * 计算报销金额
     */
    SettlementResult calculateSettlement(Long orderId);
    
    /**
     * 提交结算申请
     */
    boolean submitSettlement(Long orderId);
    
    /**
     * 审批结算
     */
    boolean approveSettlement(Long orderId, String approvalResult, String approvalRemark);
    
    /**
     * 拒绝结算
     */
    boolean rejectSettlement(Long orderId, String rejectReason);
    
    /**
     * 完成结算
     */
    boolean completeSettlement(Long orderId);
    
    /**
     * 生成结算单号
     */
    String generateSettlementNo();
    
    /**
     * 检查结算单号是否存在
     */
    boolean isSettlementNoExists(String settlementNo);
    
    /**
     * 获取结算统计信息
     */
    Object getSettlementStatistics(String startDate, String endDate, Long hospitalId);
    
    /**
     * 根据结算状态查询订单列表
     */
    List<MedicalOrder> getOrdersBySettlementStatus(String status);
    
    /**
     * 根据结算日期范围查询订单列表
     */
    List<MedicalOrder> getOrdersBySettlementDateRange(String startDate, String endDate);
    
    /**
     * 根据报销金额范围查询订单列表
     */
    List<MedicalOrder> getOrdersByReimbursementAmountRange(Double minAmount, Double maxAmount);
    
    /**
     * 获取所有结算状态
     */
    List<String> getAllSettlementStatuses();
    
    /**
     * 获取患者结算历史
     */
    PageResponse<MedicalOrder> getPatientSettlementHistory(Long patientId, PageRequest pageRequest);
    
    /**
     * 批量计算报销金额
     */
    List<SettlementResult> batchCalculateSettlement(List<Long> orderIds);
    
    /**
     * 批量提交结算
     */
    boolean batchSubmitSettlement(List<Long> orderIds);
    
    /**
     * 批量审批结算
     */
    boolean batchApproveSettlement(List<Long> orderIds, String approvalResult, String approvalRemark);
} 