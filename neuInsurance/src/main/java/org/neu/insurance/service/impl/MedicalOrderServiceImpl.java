package org.neu.insurance.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.SettlementResult;
import org.neu.insurance.entity.MedicalOrder;
import org.neu.insurance.mapper.MedicalOrderMapper;
import org.neu.insurance.service.MedicalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 医疗订单管理服务实现类（整合结算功能）
 */
@Service
public class MedicalOrderServiceImpl implements MedicalOrderService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalOrderServiceImpl.class);

    @Autowired
    private MedicalOrderMapper medicalOrderMapper;

    @Override
    public PageResponse<MedicalOrder> getMedicalOrderList(PageRequest pageRequest, String orderNo, Long patientId, String status, String keyword) {
        // 计算偏移量
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
        
        // 查询总数
        int total = medicalOrderMapper.countMedicalOrders(orderNo, patientId, status, keyword);
        
        // 查询数据
        List<MedicalOrder> orders = medicalOrderMapper.getMedicalOrderList(offset, pageRequest.getPageSize(), orderNo, patientId, status, keyword);
        
        // 构建分页响应
        PageResponse<MedicalOrder> response = new PageResponse<>();
        response.setRecords(orders);
        response.setTotal((long) total);
        response.setPageNum(pageRequest.getPageNum());
        response.setPageSize(pageRequest.getPageSize());
        response.setTotalPages((int) Math.ceil((double) total / pageRequest.getPageSize()));
        
        return response;
    }

    @Override
    public MedicalOrder getMedicalOrderById(Long id) {
        return medicalOrderMapper.getMedicalOrderById(id);
    }

    @Override
    public MedicalOrder getMedicalOrderByOrderNo(String orderNo) {
        return medicalOrderMapper.getMedicalOrderByOrderNo(orderNo);
    }

    @Override
    public List<MedicalOrder> getMedicalOrdersByPatientId(Long patientId) {
        return medicalOrderMapper.getMedicalOrdersByPatientId(patientId);
    }

    @Override
    @Transactional
    public boolean addMedicalOrder(MedicalOrder medicalOrder) {
        // 生成订单号
        if (medicalOrder.getOrderNo() == null || medicalOrder.getOrderNo().isEmpty()) {
            medicalOrder.setOrderNo(generateOrderNo());
        }
        
        // 检查订单号是否已存在
        if (medicalOrderMapper.getMedicalOrderByOrderNo(medicalOrder.getOrderNo()) != null) {
            return false;
        }
        
        // 自动生成患者ID（如果为空）
        if (medicalOrder.getPatientId() == null) {
            medicalOrder.setPatientId(System.currentTimeMillis());
        }
        
        // 设置创建时间
        if (medicalOrder.getCreateTime() == null) {
            medicalOrder.setCreateTime(LocalDateTime.now());
        }
        
        // 设置默认状态
        if (medicalOrder.getStatus() == null) {
            medicalOrder.setStatus(MedicalOrder.STATUS_PENDING);
        }
        
        return medicalOrderMapper.addMedicalOrder(medicalOrder) > 0;
    }

    @Override
    @Transactional
    public boolean updateMedicalOrder(MedicalOrder medicalOrder) {
        // 检查订单号是否被其他订单使用
        MedicalOrder existingOrder = medicalOrderMapper.getMedicalOrderByOrderNo(medicalOrder.getOrderNo());
        if (existingOrder != null && !existingOrder.getId().equals(medicalOrder.getId())) {
            return false;
        }
        
        // 设置更新时间
        medicalOrder.setUpdateTime(LocalDateTime.now());
        
        return medicalOrderMapper.updateMedicalOrder(medicalOrder) > 0;
    }

    @Override
    @Transactional
    public boolean deleteMedicalOrder(Long id) {
        return medicalOrderMapper.deleteMedicalOrder(id) > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteMedicalOrder(List<Long> ids) {
        return medicalOrderMapper.batchDeleteMedicalOrder(ids) > 0;
    }

    @Override
    public List<MedicalOrder> getMedicalOrdersByStatus(String status) {
        // 将字符串状态转换为整数状态
        Integer statusInt = convertStatusToInteger(status);
        if (statusInt == null) {
            return medicalOrderMapper.getMedicalOrdersByStatus(status); // 如果转换失败，直接传递原字符串
        }
        return medicalOrderMapper.getMedicalOrdersByStatus(statusInt.toString());
    }

    @Override
    public List<MedicalOrder> getMedicalOrdersByDepartment(String department) {
        return medicalOrderMapper.getMedicalOrdersByDepartment(department);
    }

    @Override
    public List<MedicalOrder> getMedicalOrdersByDoctor(Long doctorId) {
        return medicalOrderMapper.getMedicalOrdersByDoctor(doctorId);
    }

    @Override
    public List<MedicalOrder> getMedicalOrdersByDateRange(String startDate, String endDate) {
        return medicalOrderMapper.getMedicalOrdersByDateRange(startDate, endDate);
    }

    @Override
    public List<MedicalOrder> getMedicalOrdersByAmountRange(Double minAmount, Double maxAmount) {
        return medicalOrderMapper.getMedicalOrdersByAmountRange(minAmount, maxAmount);
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long id, String status) {
        // 将字符串状态转换为整数状态
        Integer statusInt = convertStatusToInteger(status);
        if (statusInt == null) {
            return false;
        }
        
        MedicalOrder order = new MedicalOrder();
        order.setId(id);
        order.setStatus(statusInt);
        order.setUpdateTime(LocalDateTime.now());
        
        return medicalOrderMapper.updateMedicalOrder(order) > 0;
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long id, String reason) {
        MedicalOrder order = new MedicalOrder();
        order.setId(id);
        order.setStatus(MedicalOrder.STATUS_CANCELLED); // 取消状态
        order.setRemark("取消原因：" + reason); // 使用remark字段存储取消原因
        order.setUpdateTime(LocalDateTime.now());
        
        return medicalOrderMapper.updateMedicalOrder(order) > 0;
    }

    @Override
    @Transactional
    public boolean completeOrder(Long id) {
        MedicalOrder order = new MedicalOrder();
        order.setId(id);
        order.setStatus(MedicalOrder.STATUS_PAID); // 已完成状态
        order.setUpdateTime(LocalDateTime.now());
        
        return medicalOrderMapper.updateMedicalOrder(order) > 0;
    }

    @Override
    public List<String> getAllOrderStatuses() {
        return medicalOrderMapper.getAllOrderStatuses();
    }

    @Override
    public List<String> getAllDepartments() {
        return medicalOrderMapper.getAllDepartments();
    }

    @Override
    public boolean isOrderNoExists(String orderNo) {
        return medicalOrderMapper.getMedicalOrderByOrderNo(orderNo) != null;
    }

    @Override
    public String generateOrderNo() {
        // 生成订单号：年月日时分秒+4位随机数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        String randomNum = String.format("%04d", random.nextInt(10000));
        
        return "MO" + timestamp + randomNum;
    }

    @Override
    public Double calculateOrderAmount(Long orderId) {
        return medicalOrderMapper.calculateOrderAmount(orderId);
    }

    @Override
    public Object getOrderStatistics() {
        return medicalOrderMapper.getOrderStatistics();
    }

    @Override
    public PageResponse<MedicalOrder> getPatientOrderHistory(Long patientId, PageRequest pageRequest) {
        // 计算偏移量
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
        
        // 查询总数
        int total = medicalOrderMapper.countMedicalOrdersByPatientId(patientId);
        
        // 查询数据
        List<MedicalOrder> orders = medicalOrderMapper.getPatientOrderHistory(patientId, offset, pageRequest.getPageSize());
        
        // 构建分页响应
        PageResponse<MedicalOrder> response = new PageResponse<>();
        response.setRecords(orders);
        response.setTotal((long) total);
        response.setPageNum(pageRequest.getPageNum());
        response.setPageSize(pageRequest.getPageSize());
        response.setTotalPages((int) Math.ceil((double) total / pageRequest.getPageSize()));
        
        return response;
    }

    // ==================== 结算相关方法实现 ====================

    @Override
    public SettlementResult calculateSettlement(Long orderId) {
        logger.info("计算报销金额，订单ID：{}", orderId);
        
        try {
            MedicalOrder order = getMedicalOrderById(orderId);
            if (order == null) {
                logger.warn("订单不存在，订单ID：{}", orderId);
                return null;
            }
            
            // 创建结算结果
            SettlementResult result = new SettlementResult();
            result.setOrderId(order.getId());
            result.setOrderNo(order.getOrderNo());
            result.setPatientId(order.getPatientId());
            result.setPatientName(order.getPatientName());
            result.setHospitalId(order.getHospitalId());
            result.setHospitalName(order.getHospitalName());
            
            // 设置费用信息
            result.setTotalAmount(order.getTotalAmount());
            result.setDrugAmount(order.getDrugAmount());
            result.setTreatmentAmount(order.getTreatmentAmount());
            result.setServiceAmount(order.getServiceAmount());
            result.setOtherAmount(order.getOtherAmount());
            
            // 计算分类费用（这里需要根据实际业务逻辑计算）
            calculateCategoryAmounts(order, result);
            
            // 计算报销金额
            calculateReimbursement(order, result);
            
            // 设置结算信息
            result.setStatus(order.getStatus());
            result.setSettlementTime(order.getSettlementTime());
            result.setApprovalResult(order.getApprovalResult());
            result.setApprovalRemark(order.getApprovalRemark());
            
            logger.info("报销金额计算完成，订单ID：{}，总费用：{}，可报销金额：{}", 
                orderId, result.getTotalAmount(), result.getReimbursableAmount());
            
            return result;
            
        } catch (Exception e) {
            logger.error("计算报销金额失败，订单ID：{}", orderId, e);
            throw new RuntimeException("计算报销金额失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean submitSettlement(Long orderId) {
        logger.info("提交结算申请，订单ID：{}", orderId);
        
        try {
            MedicalOrder order = getMedicalOrderById(orderId);
            if (order == null) {
                logger.warn("订单不存在，订单ID：{}", orderId);
                return false;
            }
            
            if (!order.canSettle()) {
                logger.warn("订单状态不允许结算，订单ID：{}，当前状态：{}", orderId, order.getStatus());
                return false;
            }
            
            // 生成结算单号
            String settlementNo = generateSettlementNo();
            
            // 更新订单状态为待审批
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(orderId);
            updateOrder.setStatus(MedicalOrder.STATUS_PENDING_APPROVAL);
            updateOrder.setSettlementNo(settlementNo);
            updateOrder.setSettlementTime(LocalDateTime.now());
            updateOrder.setUpdateTime(LocalDateTime.now());
            
            boolean success = medicalOrderMapper.updateMedicalOrder(updateOrder) > 0;
            
            if (success) {
                logger.info("结算申请提交成功，订单ID：{}，结算单号：{}", orderId, settlementNo);
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("提交结算申请失败，订单ID：{}", orderId, e);
            throw new RuntimeException("提交结算申请失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean approveSettlement(Long orderId, String approvalResult, String approvalRemark) {
        logger.info("审批结算，订单ID：{}，审批结果：{}", orderId, approvalResult);
        
        try {
            MedicalOrder order = getMedicalOrderById(orderId);
            if (order == null) {
                logger.warn("订单不存在，订单ID：{}", orderId);
                return false;
            }
            
            if (!order.canApprove()) {
                logger.warn("订单状态不允许审批，订单ID：{}，当前状态：{}", orderId, order.getStatus());
                return false;
            }
            
            // 更新订单状态为已结算
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(orderId);
            updateOrder.setStatus(MedicalOrder.STATUS_SETTLED);
            updateOrder.setApprovalResult(approvalResult);
            updateOrder.setApprovalRemark(approvalRemark);
            updateOrder.setUpdateTime(LocalDateTime.now());
            
            boolean success = medicalOrderMapper.updateMedicalOrder(updateOrder) > 0;
            
            if (success) {
                logger.info("结算审批成功，订单ID：{}，审批结果：{}", orderId, approvalResult);
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("审批结算失败，订单ID：{}", orderId, e);
            throw new RuntimeException("审批结算失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean rejectSettlement(Long orderId, String rejectReason) {
        logger.info("拒绝结算，订单ID：{}，拒绝原因：{}", orderId, rejectReason);
        
        try {
            MedicalOrder order = getMedicalOrderById(orderId);
            if (order == null) {
                logger.warn("订单不存在，订单ID：{}", orderId);
                return false;
            }
            
            if (!order.canApprove()) {
                logger.warn("订单状态不允许拒绝，订单ID：{}，当前状态：{}", orderId, order.getStatus());
                return false;
            }
            
            // 更新订单状态为已拒绝
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(orderId);
            updateOrder.setStatus(MedicalOrder.STATUS_REJECTED);
            updateOrder.setRejectReason(rejectReason);
            updateOrder.setUpdateTime(LocalDateTime.now());
            
            boolean success = medicalOrderMapper.updateMedicalOrder(updateOrder) > 0;
            
            if (success) {
                logger.info("结算拒绝成功，订单ID：{}，拒绝原因：{}", orderId, rejectReason);
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("拒绝结算失败，订单ID：{}", orderId, e);
            throw new RuntimeException("拒绝结算失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean completeSettlement(Long orderId) {
        logger.info("完成结算，订单ID：{}", orderId);
        
        try {
            MedicalOrder order = getMedicalOrderById(orderId);
            if (order == null) {
                logger.warn("订单不存在，订单ID：{}", orderId);
                return false;
            }
            
            if (!order.canPay()) {
                logger.warn("订单状态不允许完成结算，订单ID：{}，当前状态：{}", orderId, order.getStatus());
                return false;
            }
            
            // 更新订单状态为已支付
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(orderId);
            updateOrder.setStatus(MedicalOrder.STATUS_PAID);
            updateOrder.setUpdateTime(LocalDateTime.now());
            
            boolean success = medicalOrderMapper.updateMedicalOrder(updateOrder) > 0;
            
            if (success) {
                logger.info("结算完成成功，订单ID：{}", orderId);
            }
            
            return success;
            
        } catch (Exception e) {
            logger.error("完成结算失败，订单ID：{}", orderId, e);
            throw new RuntimeException("完成结算失败：" + e.getMessage());
        }
    }

    @Override
    public String generateSettlementNo() {
        // 生成结算单号：年月日时分秒+4位随机数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        Random random = new Random();
        String randomNum = String.format("%04d", random.nextInt(10000));
        
        return "ST" + timestamp + randomNum;
    }

    @Override
    public boolean isSettlementNoExists(String settlementNo) {
        return medicalOrderMapper.checkSettlementNoExists(settlementNo) > 0;
    }

    @Override
    public Object getSettlementStatistics(String startDate, String endDate, Long hospitalId) {
        logger.info("获取结算统计信息，开始日期：{}，结束日期：{}，医院ID：{}", startDate, endDate, hospitalId);
        
        try {
            // 这里需要实现具体的统计逻辑
            // 暂时返回空对象，实际实现时需要查询数据库
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalOrders", 0);
            statistics.put("totalAmount", BigDecimal.ZERO);
            statistics.put("totalReimbursement", BigDecimal.ZERO);
            statistics.put("totalSelfPay", BigDecimal.ZERO);
            
            return statistics;
            
        } catch (Exception e) {
            logger.error("获取结算统计信息失败", e);
            throw new RuntimeException("获取结算统计信息失败：" + e.getMessage());
        }
    }

    @Override
    public List<MedicalOrder> getOrdersBySettlementStatus(String status) {
        // 将结算状态转换为订单状态
        Integer orderStatus = convertSettlementStatusToOrderStatus(status);
        if (orderStatus == null) {
            return new ArrayList<>();
        }
        return getMedicalOrdersByStatus(orderStatus.toString());
    }

    @Override
    public List<MedicalOrder> getOrdersBySettlementDateRange(String startDate, String endDate) {
        return medicalOrderMapper.getOrdersBySettlementDateRange(startDate, endDate);
    }

    @Override
    public List<MedicalOrder> getOrdersByReimbursementAmountRange(Double minAmount, Double maxAmount) {
        return medicalOrderMapper.getOrdersByReimbursementAmountRange(minAmount, maxAmount);
    }

    @Override
    public List<String> getAllSettlementStatuses() {
        return Arrays.asList("待结算", "已结算", "已支付", "待审批", "已拒绝", "已取消");
    }

    @Override
    public PageResponse<MedicalOrder> getPatientSettlementHistory(Long patientId, PageRequest pageRequest) {
        // 复用患者订单历史方法，但只返回已结算的订单
        PageResponse<MedicalOrder> response = getPatientOrderHistory(patientId, pageRequest);
        
        // 过滤出已结算的订单
        List<MedicalOrder> settledOrders = response.getRecords().stream()
                .filter(order -> order.getStatus() >= MedicalOrder.STATUS_SETTLED)
                .collect(Collectors.toList());
        
        response.setRecords(settledOrders);
        return response;
    }

    @Override
    public List<SettlementResult> batchCalculateSettlement(List<Long> orderIds) {
        logger.info("批量计算报销金额，订单ID列表：{}", orderIds);
        
        try {
            List<SettlementResult> results = new ArrayList<>();
            for (Long orderId : orderIds) {
                SettlementResult result = calculateSettlement(orderId);
                if (result != null) {
                    results.add(result);
                }
            }
            
            logger.info("批量计算报销金额完成，成功计算：{}个订单", results.size());
            return results;
            
        } catch (Exception e) {
            logger.error("批量计算报销金额失败", e);
            throw new RuntimeException("批量计算报销金额失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean batchSubmitSettlement(List<Long> orderIds) {
        logger.info("批量提交结算，订单ID列表：{}", orderIds);
        
        try {
            boolean allSuccess = true;
            for (Long orderId : orderIds) {
                if (!submitSettlement(orderId)) {
                    allSuccess = false;
                    logger.warn("批量提交结算失败，订单ID：{}", orderId);
                }
            }
            
            logger.info("批量提交结算完成，成功提交：{}个订单", 
                orderIds.stream().filter(id -> submitSettlement(id)).count());
            return allSuccess;
            
        } catch (Exception e) {
            logger.error("批量提交结算失败", e);
            throw new RuntimeException("批量提交结算失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean batchApproveSettlement(List<Long> orderIds, String approvalResult, String approvalRemark) {
        logger.info("批量审批结算，订单ID列表：{}，审批结果：{}", orderIds, approvalResult);
        
        try {
            boolean allSuccess = true;
            for (Long orderId : orderIds) {
                if (!approveSettlement(orderId, approvalResult, approvalRemark)) {
                    allSuccess = false;
                    logger.warn("批量审批结算失败，订单ID：{}", orderId);
                }
            }
            
            logger.info("批量审批结算完成，成功审批：{}个订单", 
                orderIds.stream().filter(id -> approveSettlement(id, approvalResult, approvalRemark)).count());
            return allSuccess;
            
        } catch (Exception e) {
            logger.error("批量审批结算失败", e);
            throw new RuntimeException("批量审批结算失败：" + e.getMessage());
        }
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 将字符串状态转换为整数状态
     */
    private Integer convertStatusToInteger(String status) {
        if (status == null || status.trim().isEmpty()) {
            return null;
        }
        
        switch (status.toLowerCase()) {
            case "待结算":
            case "pending":
            case "1":
                return MedicalOrder.STATUS_PENDING;
            case "已结算":
            case "settled":
            case "2":
                return MedicalOrder.STATUS_SETTLED;
            case "已支付":
            case "paid":
            case "completed":
            case "已完成":
            case "3":
                return MedicalOrder.STATUS_PAID;
            case "已取消":
            case "cancelled":
            case "canceled":
            case "0":
                return MedicalOrder.STATUS_CANCELLED;
            case "待审批":
            case "pending_approval":
            case "4":
                return MedicalOrder.STATUS_PENDING_APPROVAL;
            case "已拒绝":
            case "rejected":
            case "5":
                return MedicalOrder.STATUS_REJECTED;
            default:
                return null;
        }
    }

    /**
     * 将结算状态转换为订单状态
     */
    private Integer convertSettlementStatusToOrderStatus(String settlementStatus) {
        if (settlementStatus == null || settlementStatus.trim().isEmpty()) {
            return null;
        }
        
        switch (settlementStatus.toLowerCase()) {
            case "待结算":
                return MedicalOrder.STATUS_PENDING;
            case "已结算":
                return MedicalOrder.STATUS_SETTLED;
            case "已支付":
                return MedicalOrder.STATUS_PAID;
            case "待审批":
                return MedicalOrder.STATUS_PENDING_APPROVAL;
            case "已拒绝":
                return MedicalOrder.STATUS_REJECTED;
            case "已取消":
                return MedicalOrder.STATUS_CANCELLED;
            default:
                return null;
        }
    }

    /**
     * 计算分类费用
     */
    private void calculateCategoryAmounts(MedicalOrder order, SettlementResult result) {
        // 这里需要根据实际业务逻辑计算甲类、乙类、丙类药品费用
        // 暂时设置为0，实际实现时需要根据订单明细计算
        result.setCategoryAAmount(BigDecimal.ZERO);
        result.setCategoryBAmount(BigDecimal.ZERO);
        result.setCategoryCAmount(BigDecimal.ZERO);
    }

    /**
     * 计算报销金额
     */
    private void calculateReimbursement(MedicalOrder order, SettlementResult result) {
        // 设置起付线（这里需要根据患者类型、医院等级等计算）
        result.setDeductible(new BigDecimal("1000"));
        
        // 设置报销比例（这里需要根据患者类型、医院等级等计算）
        result.setReimbursementRatio(new BigDecimal("0.8"));
        
        // 计算可报销金额
        BigDecimal totalAmount = result.getTotalAmount() != null ? result.getTotalAmount() : BigDecimal.ZERO;
        BigDecimal deductible = result.getDeductible();
        BigDecimal ratio = result.getReimbursementRatio();
        
        BigDecimal reimbursableAmount = BigDecimal.ZERO;
        if (totalAmount.compareTo(deductible) > 0) {
            reimbursableAmount = totalAmount.subtract(deductible).multiply(ratio);
        }
        
        result.setReimbursableAmount(reimbursableAmount);
        result.setActualReimbursement(reimbursableAmount);
        
        // 计算自付金额
        BigDecimal selfPayAmount = totalAmount.subtract(reimbursableAmount);
        result.setSelfPayAmount(selfPayAmount);
    }

    public void exportSettlementReport(String reportType, String department,
                                       String status, Date startDate, Date endDate) throws IOException {
        // Create workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("结算报表");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"订单号", "患者姓名", "结算金额", "结算状态", "结算时间", "科室"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Add data rows
        int rowNum = 1;
        List<MedicalOrder> orders = medicalOrderMapper.getOrdersByFilter(reportType, department, status, startDate, endDate);
        for (MedicalOrder order : orders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.getOrderNo());
            row.createCell(1).setCellValue(order.getPatientName());
            row.createCell(2).setCellValue((RichTextString) order.getTotalAmount());
            row.createCell(3).setCellValue(order.getStatus());
            row.createCell(4).setCellValue(order.getSettlementTime());
            row.createCell(5).setCellValue(order.getDepartmentName());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Set response headers
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        String fileName = "结算报表_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // Write workbook to response
        workbook.write(response.getOutputStream());
        workbook.close();
    }
} 