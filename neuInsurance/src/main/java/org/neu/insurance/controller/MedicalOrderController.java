package org.neu.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.SettlementResult;
import org.neu.insurance.entity.MedicalOrder;
import org.neu.insurance.service.MedicalOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 医疗订单管理控制器（整合结算功能）
 */
@Tag(name = "医疗订单管理", description = "医疗订单和结算管理相关接口")
@RestController
@RequestMapping("/api/medical-order")
@CrossOrigin(origins = "*")
public class MedicalOrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(MedicalOrderController.class);
    
    @Autowired
    private MedicalOrderService medicalOrderService;
    
    /**
     * 分页查询医疗订单列表
     */
    
    @Operation(summary = "分页查询医疗订单列表", description = "根据条件分页查询医疗订单信息")
    @GetMapping("/list")
    public Result<PageResponse<MedicalOrder>> getMedicalOrderList(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "订单号") @RequestParam(required = false) String orderNo,
            @Parameter(description = "患者ID") @RequestParam(required = false) Long patientId,
            @Parameter(description = "订单状态") @RequestParam(required = false) String status,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(pageNum);
            pageRequest.setPageSize(pageSize);
            
            PageResponse<MedicalOrder> response = medicalOrderService.getMedicalOrderList(pageRequest, orderNo, patientId, status, keyword);
            return Result.success("查询成功", response);
        } catch (Exception e) {
            logger.error("查询医疗订单列表失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询医疗订单
     */
    
    @Operation(summary = "根据ID查询医疗订单", description = "根据订单ID获取详细信息")
    @GetMapping("/{id}")
    public Result<MedicalOrder> getMedicalOrderById(@Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long id) {
        try {
            MedicalOrder order = medicalOrderService.getMedicalOrderById(id);
            if (order != null) {
                return Result.success("查询成功", order);
            } else {
                return Result.error("订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询医疗订单失败，ID：{}", id, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据订单号查询医疗订单
     */
    
    @Operation(summary = "根据订单号查询医疗订单", description = "根据订单号获取详细信息")
    @GetMapping("/byOrderNo/{orderNo}")
    public Result<MedicalOrder> getMedicalOrderByOrderNo(@Parameter(description = "订单号", required = true, example = "MO202312011200001") @PathVariable String orderNo) {
        try {
            MedicalOrder order = medicalOrderService.getMedicalOrderByOrderNo(orderNo);
            if (order != null) {
                return Result.success("查询成功", order);
            } else {
                return Result.error("订单不存在");
            }
        } catch (Exception e) {
            logger.error("查询医疗订单失败，订单号：{}", orderNo, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据患者ID查询医疗订单列表
     */
    
    @Operation(summary = "根据患者ID查询医疗订单列表", description = "获取指定患者的所有订单")
    @GetMapping("/byPatient/{patientId}")
    public Result<List<MedicalOrder>> getMedicalOrdersByPatientId(@Parameter(description = "患者ID", required = true, example = "1") @PathVariable Long patientId) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getMedicalOrdersByPatientId(patientId);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("查询患者订单列表失败，患者ID：{}", patientId, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增医疗订单
     */
    
    @Operation(summary = "新增医疗订单", description = "创建新的医疗订单")
    @PostMapping("/add")
    public Result<String> addMedicalOrder(@Parameter(description = "订单信息", required = true) @RequestBody MedicalOrder medicalOrder) {
        try {
            // 添加调试日志
            logger.info("接收到新增订单请求，数据：{}", medicalOrder);
            logger.info("关键字段检查 - 患者姓名：{}，订单类型：{}，科室：{}，医生：{}，诊断：{}", 
                medicalOrder.getPatientName(), 
                medicalOrder.getOrderType(), 
                medicalOrder.getDepartmentName(), 
                medicalOrder.getDoctorName(), 
                medicalOrder.getDiagnosis());
            
            // 数据验证
            if (medicalOrder.getPatientName() == null || medicalOrder.getPatientName().trim().isEmpty()) {
                logger.error("患者姓名为空");
                return Result.error("患者姓名不能为空");
            }
            
            if (medicalOrder.getOrderType() == null || medicalOrder.getOrderType().trim().isEmpty()) {
                logger.error("订单类型为空");
                return Result.error("订单类型不能为空");
            }
            
            if (medicalOrder.getDepartmentName() == null || medicalOrder.getDepartmentName().trim().isEmpty()) {
                logger.error("科室名称为空");
                return Result.error("科室名称不能为空");
            }
            
            if (medicalOrder.getDoctorName() == null || medicalOrder.getDoctorName().trim().isEmpty()) {
                logger.error("医生姓名为空");
                return Result.error("医生姓名不能为空");
            }
            
            if (medicalOrder.getDiagnosis() == null || medicalOrder.getDiagnosis().trim().isEmpty()) {
                logger.error("诊断信息为空");
                return Result.error("诊断信息不能为空");
            }
            
            // 设置默认值
            if (medicalOrder.getStatus() == null) {
                medicalOrder.setStatus(MedicalOrder.STATUS_PENDING_APPROVAL); // 默认待审核
            }
            
            // 确保字段不为null
            if (medicalOrder.getTotalAmount() == null) {
                medicalOrder.setTotalAmount(BigDecimal.ZERO);
            }
            
            logger.info("验证通过，准备保存订单数据");
            
            if (medicalOrderService.addMedicalOrder(medicalOrder)) {
                logger.info("订单保存成功，订单号：{}", medicalOrder.getOrderNo());
                return Result.success("新增成功");
            } else {
                logger.error("订单保存失败，可能订单号已存在");
                return Result.error("订单号已存在");
            }
        } catch (Exception e) {
            logger.error("新增医疗订单失败", e);
            return Result.error("新增失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新医疗订单
     */
    
    @Operation(summary = "更新医疗订单", description = "更新指定订单的信息")
    @PutMapping("/update")
    public Result<String> updateMedicalOrder(@Parameter(description = "订单信息", required = true) @RequestBody MedicalOrder medicalOrder) {
        try {
            if (medicalOrderService.updateMedicalOrder(medicalOrder)) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            logger.error("更新医疗订单失败", e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除医疗订单
     */
    
    @Operation(summary = "删除医疗订单", description = "根据ID删除指定订单")
    @DeleteMapping("/{id}")
    public Result<String> deleteMedicalOrder(@Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long id) {
        try {
            if (medicalOrderService.deleteMedicalOrder(id)) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            logger.error("删除医疗订单失败，ID：{}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除医疗订单
     */
    
    @Operation(summary = "批量删除医疗订单", description = "批量删除多个订单")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteMedicalOrder(@Parameter(description = "订单ID列表", required = true) @RequestBody List<Long> ids) {
        try {
            if (medicalOrderService.batchDeleteMedicalOrder(ids)) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            logger.error("批量删除医疗订单失败", e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新订单状态
     */
    
    @Operation(summary = "更新订单状态", description = "更新指定订单的状态")
    @PostMapping("/updateStatus")
    public Result<String> updateOrderStatus(
            @Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long id,
            @Parameter(description = "新状态", required = true, example = "已结算") @RequestParam String status) {
        try {
            if (medicalOrderService.updateOrderStatus(id, status)) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            logger.error("更新订单状态失败，ID：{}", id, e);
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 取消订单
     */
    
    @Operation(summary = "取消订单", description = "取消指定订单并记录取消原因")
    @PostMapping("/cancel")
    public Result<String> cancelOrder(
            @Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long id,
            @Parameter(description = "取消原因", required = true, example = "患者要求取消") @RequestParam String reason) {
        try {
            if (medicalOrderService.cancelOrder(id, reason)) {
                return Result.success("订单取消成功");
            } else {
                return Result.error("订单取消失败");
            }
        } catch (Exception e) {
            logger.error("取消订单失败，ID：{}", id, e);
            return Result.error("订单取消失败: " + e.getMessage());
        }
    }

    /**
     * 完成订单
     */
    
    @Operation(summary = "完成订单", description = "完成指定订单")
    @PostMapping("/complete")
    public Result<String> completeOrder(@Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long id) {
        try {
            if (medicalOrderService.completeOrder(id)) {
                return Result.success("订单完成成功");
            } else {
                return Result.error("订单完成失败");
            }
        } catch (Exception e) {
            logger.error("完成订单失败，ID：{}", id, e);
            return Result.error("订单完成失败: " + e.getMessage());
        }
    }

    /**
     * 根据状态查询医疗订单列表
     */
    
    @Operation(summary = "根据状态查询医疗订单列表", description = "根据订单状态查询订单")
    @GetMapping("/byStatus/{status}")
    public Result<List<MedicalOrder>> getMedicalOrdersByStatus(@Parameter(description = "订单状态", required = true, example = "待结算") @PathVariable String status) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getMedicalOrdersByStatus(status);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据状态查询订单失败，状态：{}", status, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据科室查询医疗订单列表
     */
    
    @Operation(summary = "根据科室查询医疗订单列表", description = "根据科室查询订单")
    @GetMapping("/byDepartment/{department}")
    public Result<List<MedicalOrder>> getMedicalOrdersByDepartment(@Parameter(description = "科室名称", required = true, example = "内科") @PathVariable String department) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getMedicalOrdersByDepartment(department);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据科室查询订单失败，科室：{}", department, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据医生查询医疗订单列表
     */
    
    @Operation(summary = "根据医生查询医疗订单列表", description = "根据医生查询订单")
    @GetMapping("/byDoctor/{doctorId}")
    public Result<List<MedicalOrder>> getMedicalOrdersByDoctor(@Parameter(description = "医生ID", required = true, example = "1") @PathVariable Long doctorId) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getMedicalOrdersByDoctor(doctorId);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据医生查询订单失败，医生ID：{}", doctorId, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据日期范围查询医疗订单列表
     */
    
    @Operation(summary = "根据日期范围查询医疗订单列表", description = "根据就诊日期范围查询订单")
    @GetMapping("/byDateRange")
    public Result<List<MedicalOrder>> getMedicalOrdersByDateRange(
            @Parameter(description = "开始日期", required = true, example = "2023-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", required = true, example = "2023-12-31") @RequestParam String endDate) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getMedicalOrdersByDateRange(startDate, endDate);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据日期范围查询订单失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据金额范围查询医疗订单列表
     */
    
    @Operation(summary = "根据金额范围查询医疗订单列表", description = "根据总费用范围查询订单")
    @GetMapping("/byAmountRange")
    public Result<List<MedicalOrder>> getMedicalOrdersByAmountRange(
            @Parameter(description = "最小金额", required = true, example = "100.0") @RequestParam Double minAmount,
            @Parameter(description = "最大金额", required = true, example = "1000.0") @RequestParam Double maxAmount) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getMedicalOrdersByAmountRange(minAmount, maxAmount);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据金额范围查询订单失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有订单状态
     */
    
    @Operation(summary = "获取所有订单状态", description = "获取系统中所有可用的订单状态")
    @GetMapping("/orderStatuses")
    public Result<List<String>> getAllOrderStatuses() {
        try {
            List<String> statuses = medicalOrderService.getAllOrderStatuses();
            return Result.success("查询成功", statuses);
        } catch (Exception e) {
            logger.error("获取订单状态失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有科室
     */
    
    @Operation(summary = "获取所有科室", description = "获取系统中所有可用的科室")
    @GetMapping("/departments")
    public Result<List<String>> getAllDepartments() {
        try {
            List<String> departments = medicalOrderService.getAllDepartments();
            return Result.success("查询成功", departments);
        } catch (Exception e) {
            logger.error("获取科室列表失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 检查订单号是否存在
     */
    
    @Operation(summary = "检查订单号是否存在", description = "检查指定订单号是否已被使用")
    @GetMapping("/checkOrderNo")
    public Result<Boolean> checkOrderNo(@Parameter(description = "订单号", required = true, example = "MO202312011200001") @RequestParam String orderNo) {
        try {
            boolean exists = medicalOrderService.isOrderNoExists(orderNo);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            logger.error("检查订单号失败", e);
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 生成订单号
     */
    
    @Operation(summary = "生成订单号", description = "生成新的唯一订单号")
    @GetMapping("/generateOrderNo")
    public Result<String> generateOrderNo() {
        try {
            String orderNo = medicalOrderService.generateOrderNo();
            return Result.success("生成成功", orderNo);
        } catch (Exception e) {
            logger.error("生成订单号失败", e);
            return Result.error("生成失败: " + e.getMessage());
        }
    }

    /**
     * 计算订单金额
     */
    
    @Operation(summary = "计算订单金额", description = "计算指定订单的总金额")
    @GetMapping("/calculateAmount/{orderId}")
    public Result<Double> calculateOrderAmount(@Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long orderId) {
        try {
            Double amount = medicalOrderService.calculateOrderAmount(orderId);
            return Result.success("计算成功", amount);
        } catch (Exception e) {
            logger.error("计算订单金额失败，ID：{}", orderId, e);
            return Result.error("计算失败: " + e.getMessage());
        }
    }

    /**
     * 获取订单统计信息
     */
    
    @Operation(summary = "获取订单统计信息", description = "获取订单相关的统计数据")
    @GetMapping("/statistics")
    public Result<Object> getOrderStatistics() {
        try {
            Object statistics = medicalOrderService.getOrderStatistics();
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            logger.error("获取订单统计信息失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取患者订单历史
     */
    
    @Operation(summary = "获取患者订单历史", description = "分页获取指定患者的订单历史")
    @GetMapping("/patientHistory/{patientId}")
    public Result<PageResponse<MedicalOrder>> getPatientOrderHistory(
            @Parameter(description = "患者ID", required = true, example = "1") @PathVariable Long patientId,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(pageNum);
            pageRequest.setPageSize(pageSize);
            
            PageResponse<MedicalOrder> response = medicalOrderService.getPatientOrderHistory(patientId, pageRequest);
            return Result.success("查询成功", response);
        } catch (Exception e) {
            logger.error("获取患者订单历史失败，患者ID：{}", patientId, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 结算相关API ====================

    /**
     * 计算报销金额
     */
    
    @Operation(summary = "计算报销金额", description = "根据订单计算报销金额")
    @PostMapping("/calculateSettlement/{orderId}")
    public Result<SettlementResult> calculateSettlement(@Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long orderId) {
        try {
            SettlementResult result = medicalOrderService.calculateSettlement(orderId);
            if (result != null) {
                return Result.success("计算成功", result);
            } else {
                return Result.error("订单不存在");
            }
        } catch (Exception e) {
            logger.error("计算报销金额失败，订单ID：{}", orderId, e);
            return Result.error("计算失败: " + e.getMessage());
        }
    }

    /**
     * 提交结算申请
     */
    
    @Operation(summary = "提交结算申请", description = "提交订单的结算申请")
    @PostMapping("/submitSettlement")
    public Result<String> submitSettlement(@Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long orderId) {
        try {
            if (medicalOrderService.submitSettlement(orderId)) {
                return Result.success("结算申请提交成功");
            } else {
                return Result.error("结算申请提交失败");
            }
        } catch (Exception e) {
            logger.error("提交结算申请失败，订单ID：{}", orderId, e);
            return Result.error("提交失败: " + e.getMessage());
        }
    }

    /**
     * 审批结算
     */
    
    @Operation(summary = "审批结算", description = "审批订单的结算申请")
    @PostMapping("/approveSettlement")
    public Result<String> approveSettlement(
            @Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long orderId,
            @Parameter(description = "审批结果", required = true, example = "通过") @RequestParam String approvalResult,
            @Parameter(description = "审批备注") @RequestParam(required = false) String approvalRemark) {
        try {
            if (medicalOrderService.approveSettlement(orderId, approvalResult, approvalRemark)) {
                return Result.success("审批成功");
            } else {
                return Result.error("审批失败");
            }
        } catch (Exception e) {
            logger.error("审批结算失败，订单ID：{}", orderId, e);
            return Result.error("审批失败: " + e.getMessage());
        }
    }

    /**
     * 拒绝结算
     */
    
    @Operation(summary = "拒绝结算", description = "拒绝订单的结算申请")
    @PostMapping("/rejectSettlement")
    public Result<String> rejectSettlement(
            @Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long orderId,
            @Parameter(description = "拒绝原因", required = true, example = "资料不完整") @RequestParam String rejectReason) {
        try {
            if (medicalOrderService.rejectSettlement(orderId, rejectReason)) {
                return Result.success("拒绝成功");
            } else {
                return Result.error("拒绝失败");
            }
        } catch (Exception e) {
            logger.error("拒绝结算失败，订单ID：{}", orderId, e);
            return Result.error("拒绝失败: " + e.getMessage());
        }
    }

    /**
     * 完成结算
     */
    
    @Operation(summary = "完成结算", description = "完成订单的结算流程")
    @PostMapping("/completeSettlement")
    public Result<String> completeSettlement(@Parameter(description = "订单ID", required = true, example = "1") @RequestParam Long orderId) {
        try {
            if (medicalOrderService.completeSettlement(orderId)) {
                return Result.success("结算完成成功");
            } else {
                return Result.error("结算完成失败");
            }
        } catch (Exception e) {
            logger.error("完成结算失败，订单ID：{}", orderId, e);
            return Result.error("结算完成失败: " + e.getMessage());
        }
    }

    /**
     * 生成结算单号
     */
    
    @Operation(summary = "生成结算单号", description = "生成新的唯一结算单号")
    @GetMapping("/generateSettlementNo")
    public Result<String> generateSettlementNo() {
        try {
            String settlementNo = medicalOrderService.generateSettlementNo();
            return Result.success("生成成功", settlementNo);
        } catch (Exception e) {
            logger.error("生成结算单号失败", e);
            return Result.error("生成失败: " + e.getMessage());
        }
    }

    /**
     * 检查结算单号是否存在
     */
    
    @Operation(summary = "检查结算单号是否存在", description = "检查指定结算单号是否已被使用")
    @GetMapping("/checkSettlementNo")
    public Result<Boolean> checkSettlementNo(@Parameter(description = "结算单号", required = true, example = "ST202312011200001") @RequestParam String settlementNo) {
        try {
            boolean exists = medicalOrderService.isSettlementNoExists(settlementNo);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            logger.error("检查结算单号失败", e);
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 获取结算统计信息
     */
    
    @Operation(summary = "获取结算统计信息", description = "获取结算相关的统计数据")
    @GetMapping("/settlementStatistics")
    public Result<Object> getSettlementStatistics(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "医院ID") @RequestParam(required = false) Long hospitalId) {
        try {
            Object statistics = medicalOrderService.getSettlementStatistics(startDate, endDate, hospitalId);
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            logger.error("获取结算统计信息失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据结算状态查询订单列表
     */
    
    @Operation(summary = "根据结算状态查询订单列表", description = "根据结算状态查询订单")
    @GetMapping("/bySettlementStatus/{status}")
    public Result<List<MedicalOrder>> getOrdersBySettlementStatus(@Parameter(description = "结算状态", required = true, example = "待审批") @PathVariable String status) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getOrdersBySettlementStatus(status);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据结算状态查询订单失败，状态：{}", status, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据结算日期范围查询订单列表
     */
    
    @Operation(summary = "根据结算日期范围查询订单列表", description = "根据结算日期范围查询订单")
    @GetMapping("/bySettlementDateRange")
    public Result<List<MedicalOrder>> getOrdersBySettlementDateRange(
            @Parameter(description = "开始日期", required = true, example = "2023-01-01") @RequestParam String startDate,
            @Parameter(description = "结束日期", required = true, example = "2023-12-31") @RequestParam String endDate) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getOrdersBySettlementDateRange(startDate, endDate);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据结算日期范围查询订单失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据报销金额范围查询订单列表
     */
    
    @Operation(summary = "根据报销金额范围查询订单列表", description = "根据报销金额范围查询订单")
    @GetMapping("/byReimbursementAmountRange")
    public Result<List<MedicalOrder>> getOrdersByReimbursementAmountRange(
            @Parameter(description = "最小金额", required = true, example = "100.0") @RequestParam Double minAmount,
            @Parameter(description = "最大金额", required = true, example = "1000.0") @RequestParam Double maxAmount) {
        try {
            List<MedicalOrder> orders = medicalOrderService.getOrdersByReimbursementAmountRange(minAmount, maxAmount);
            return Result.success("查询成功", orders);
        } catch (Exception e) {
            logger.error("根据报销金额范围查询订单失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有结算状态
     */
    
    @Operation(summary = "获取所有结算状态", description = "获取系统中所有可用的结算状态")
    @GetMapping("/settlementStatuses")
    public Result<List<String>> getAllSettlementStatuses() {
        try {
            List<String> statuses = medicalOrderService.getAllSettlementStatuses();
            return Result.success("查询成功", statuses);
        } catch (Exception e) {
            logger.error("获取结算状态失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取患者结算历史
     */
    
    @Operation(summary = "获取患者结算历史", description = "分页获取指定患者的结算历史")
    @GetMapping("/patientSettlementHistory/{patientId}")
    public Result<PageResponse<MedicalOrder>> getPatientSettlementHistory(
            @Parameter(description = "患者ID", required = true, example = "1") @PathVariable Long patientId,
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(pageNum);
            pageRequest.setPageSize(pageSize);
            
            PageResponse<MedicalOrder> response = medicalOrderService.getPatientSettlementHistory(patientId, pageRequest);
            return Result.success("查询成功", response);
        } catch (Exception e) {
            logger.error("获取患者结算历史失败，患者ID：{}", patientId, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 批量计算报销金额
     */
    
    @Operation(summary = "批量计算报销金额", description = "批量计算多个订单的报销金额")
    @PostMapping("/batchCalculateSettlement")
    public Result<List<SettlementResult>> batchCalculateSettlement(@Parameter(description = "订单ID列表", required = true) @RequestBody List<Long> orderIds) {
        try {
            List<SettlementResult> results = medicalOrderService.batchCalculateSettlement(orderIds);
            return Result.success("批量计算成功", results);
        } catch (Exception e) {
            logger.error("批量计算报销金额失败", e);
            return Result.error("批量计算失败: " + e.getMessage());
        }
    }

    /**
     * 批量提交结算
     */
    
    @Operation(summary = "批量提交结算", description = "批量提交多个订单的结算申请")
    @PostMapping("/batchSubmitSettlement")
    public Result<String> batchSubmitSettlement(@Parameter(description = "订单ID列表", required = true) @RequestBody List<Long> orderIds) {
        try {
            if (medicalOrderService.batchSubmitSettlement(orderIds)) {
                return Result.success("批量提交成功");
            } else {
                return Result.error("批量提交失败");
            }
        } catch (Exception e) {
            logger.error("批量提交结算失败", e);
            return Result.error("批量提交失败: " + e.getMessage());
        }
    }

    /**
     * 批量审批结算
     */
    
    @Operation(summary = "批量审批结算", description = "批量审批多个订单的结算申请")
    @PostMapping("/batchApproveSettlement")
    public Result<String> batchApproveSettlement(
            @Parameter(description = "订单ID列表", required = true) @RequestBody List<Long> orderIds,
            @Parameter(description = "审批结果", required = true, example = "通过") @RequestParam String approvalResult,
            @Parameter(description = "审批备注") @RequestParam(required = false) String approvalRemark) {
        try {
            if (medicalOrderService.batchApproveSettlement(orderIds, approvalResult, approvalRemark)) {
                return Result.success("批量审批成功");
            } else {
                return Result.error("批量审批失败");
            }
        } catch (Exception e) {
            logger.error("批量审批结算失败", e);
            return Result.error("批量审批失败: " + e.getMessage());
        }
    }

    // ==================== 结算增强功能 ====================

    /**
     * 模拟支付
     */
    @Operation(summary = "模拟支付", description = "模拟结算支付流程")
    @PostMapping("/settlement/payment/simulate")
    public Result<Object> simulatePayment(@Parameter(description = "支付数据", required = true) @RequestBody java.util.Map<String, Object> paymentData) {
        try {
            Long settlementId = Long.valueOf(paymentData.get("settlementId").toString());
            String paymentType = paymentData.get("paymentType").toString();
            String bankAccount = paymentData.getOrDefault("bankAccount", "").toString();
            String remarks = paymentData.getOrDefault("remarks", "").toString();
            
            // 获取订单信息计算支付金额
            MedicalOrder order = medicalOrderService.getMedicalOrderById(settlementId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 模拟支付处理
            java.util.Map<String, Object> paymentResult = new java.util.HashMap<>();
            paymentResult.put("paymentNo", "PAY" + System.currentTimeMillis());
            paymentResult.put("paymentType", paymentType);
            paymentResult.put("paymentAmount", order.getActualReimbursement());
            paymentResult.put("paymentTime", java.time.LocalDateTime.now());
            paymentResult.put("paymentStatus", "SUCCESS");
            paymentResult.put("bankAccount", bankAccount);
            paymentResult.put("transactionId", "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000));
            paymentResult.put("remarks", remarks);
            
            // 更新订单状态为已支付
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(settlementId);
            updateOrder.setStatus(MedicalOrder.STATUS_PAID);
            updateOrder.setUpdateTime(java.time.LocalDateTime.now());
            medicalOrderService.updateMedicalOrder(updateOrder);
            
            logger.info("模拟支付成功，订单ID：{}，支付金额：{}", settlementId, order.getActualReimbursement());
            
            return Result.success("支付成功", paymentResult);
        } catch (Exception e) {
            logger.error("模拟支付失败", e);
            return Result.error("支付失败: " + e.getMessage());
        }
    }

    /**
     * 获取结算概览统计
     */
    @Operation(summary = "获取结算概览统计", description = "获取结算相关的统计数据")
    @GetMapping("/settlement/overview")
    public Result<Object> getSettlementOverview() {
        try {
            java.util.Map<String, Object> overview = new java.util.HashMap<>();
            
            // 获取今日统计
            String today = java.time.LocalDate.now().toString();
            Object todayStatsObj = medicalOrderService.getSettlementStatistics(today, today, null);
            overview.put("today", todayStatsObj);
            
            // 获取本月统计
            String monthStart = java.time.LocalDate.now().withDayOfMonth(1).toString();
            String monthEnd = java.time.LocalDate.now().toString();
            Object monthStatsObj = medicalOrderService.getSettlementStatistics(monthStart, monthEnd, null);
            overview.put("month", monthStatsObj);
            
            // 获取今年统计
            String yearStart = java.time.LocalDate.now().withDayOfYear(1).toString();
            String yearEnd = java.time.LocalDate.now().toString();
            Object yearStatsObj = medicalOrderService.getSettlementStatistics(yearStart, yearEnd, null);
            overview.put("year", yearStatsObj);
            
            return Result.success("查询成功", overview);
        } catch (Exception e) {
            logger.error("获取结算概览统计失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 重新计算结算金额
     */
    @Operation(summary = "重新计算结算金额", description = "重新计算指定订单的结算金额")
    @PostMapping("/settlement/{orderId}/recalculate")
    public Result<Object> recalculateSettlement(
            @Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long orderId,
            @Parameter(description = "计算参数", required = true) @RequestBody java.util.Map<String, Object> params) {
        try {
            MedicalOrder order = medicalOrderService.getMedicalOrderById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 获取新的计算参数
            BigDecimal deductible = params.containsKey("deductible") ? 
                new BigDecimal(params.get("deductible").toString()) : order.getDeductible();
            BigDecimal reimbursementRatio = params.containsKey("reimbursementRatio") ? 
                new BigDecimal(params.get("reimbursementRatio").toString()) : order.getReimbursementRatio();
            
            // 重新计算
            BigDecimal totalAmount = order.getTotalAmount();
            BigDecimal reimbursableAmount = totalAmount.subtract(deductible);
            if (reimbursableAmount.compareTo(BigDecimal.ZERO) < 0) {
                reimbursableAmount = BigDecimal.ZERO;
            }
            
            BigDecimal actualReimbursement = reimbursableAmount.multiply(reimbursementRatio);
            BigDecimal selfPayAmount = totalAmount.subtract(actualReimbursement);
            
            // 更新订单
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(orderId);
            updateOrder.setDeductible(deductible);
            updateOrder.setReimbursementRatio(reimbursementRatio);
            updateOrder.setReimbursableAmount(reimbursableAmount);
            updateOrder.setActualReimbursement(actualReimbursement);
            updateOrder.setSelfPayAmount(selfPayAmount);
            updateOrder.setUpdateTime(java.time.LocalDateTime.now());
            
            medicalOrderService.updateMedicalOrder(updateOrder);
            
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("totalAmount", totalAmount);
            result.put("deductible", deductible);
            result.put("reimbursementRatio", reimbursementRatio);
            result.put("reimbursableAmount", reimbursableAmount);
            result.put("actualReimbursement", actualReimbursement);
            result.put("selfPayAmount", selfPayAmount);
            
            return Result.success("重新计算成功", result);
        } catch (Exception e) {
            logger.error("重新计算结算金额失败，订单ID：{}", orderId, e);
            return Result.error("计算失败: " + e.getMessage());
        }
    }

    /**
     * 获取结算审计日志
     */
    @Operation(summary = "获取结算审计日志", description = "获取指定结算的操作审计日志")
    @GetMapping("/settlement/{settlementId}/audit-log")
    public Result<List<Object>> getSettlementAuditLog(@Parameter(description = "结算ID", required = true, example = "1") @PathVariable Long settlementId) {
        try {
            // 模拟审计日志数据
            List<Object> auditLogs = new java.util.ArrayList<>();
            
            java.util.Map<String, Object> log1 = new java.util.HashMap<>();
            log1.put("id", 1);
            log1.put("action", "CREATE");
            log1.put("description", "创建结算申请");
            log1.put("operator", "系统");
            log1.put("operateTime", java.time.LocalDateTime.now().minusDays(1));
            log1.put("details", "订单提交结算申请");
            auditLogs.add(log1);
            
            java.util.Map<String, Object> log2 = new java.util.HashMap<>();
            log2.put("id", 2);
            log2.put("action", "CALCULATE");
            log2.put("description", "计算结算金额");
            log2.put("operator", "系统");
            log2.put("operateTime", java.time.LocalDateTime.now().minusHours(12));
            log2.put("details", "自动计算报销金额");
            auditLogs.add(log2);
            
            java.util.Map<String, Object> log3 = new java.util.HashMap<>();
            log3.put("id", 3);
            log3.put("action", "APPROVE");
            log3.put("description", "审批通过");
            log3.put("operator", "管理员");
            log3.put("operateTime", java.time.LocalDateTime.now().minusHours(6));
            log3.put("details", "结算申请审批通过");
            auditLogs.add(log3);
            
            return Result.success("查询成功", auditLogs);
        } catch (Exception e) {
            logger.error("获取结算审计日志失败，结算ID：{}", settlementId, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 导出结算报表
     */
    @Operation(summary = "导出结算报表", description = "导出结算数据报表")
    @GetMapping("/settlement/export")
    public Result<Object> exportSettlementReport(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "医院ID") @RequestParam(required = false) Long hospitalId) {
        try {
            // 模拟导出功能
            String fileName = "settlement_report_" + System.currentTimeMillis() + ".xlsx";
            
            logger.info("导出结算报表，文件名：{}，参数：startDate={}, endDate={}, hospitalId={}", 
                fileName, startDate, endDate, hospitalId);
            
            // 实际项目中这里会生成真实的Excel文件
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("fileName", fileName);
            result.put("downloadUrl", "/api/files/download/" + fileName);
            result.put("fileSize", "256KB");
            result.put("exportTime", java.time.LocalDateTime.now());
            
            return Result.success("导出成功", result);
        } catch (Exception e) {
            logger.error("导出结算报表失败", e);
            return Result.error("导出失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据结算ID获取结算详情", description = "根据结算ID获取结算详情信息")
    @GetMapping("/settlement/{id}")
    public Result<SettlementResult> getSettlementDetailById(@Parameter(description = "结算ID", required = true, example = "1") @PathVariable Long id) {
        try {
            SettlementResult result = medicalOrderService.calculateSettlement(id);
            if (result != null) {
                return Result.success("查询成功", result);
            } else {
                return Result.error("结算单不存在");
            }
        } catch (Exception e) {
            logger.error("查询结算详情失败，ID：{}", id, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除结算记录
     */
    @Operation(summary = "删除结算记录", description = "根据订单ID删除对应的结算记录")
    @DeleteMapping("/settlement/{id}")
    public Result<String> deleteSettlement(@Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long id) {
        try {
            // 获取订单信息
            MedicalOrder order = medicalOrderService.getMedicalOrderById(id);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 检查订单状态是否允许删除结算
            if (order.getStatus() == null || order.getStatus() < MedicalOrder.STATUS_PENDING_APPROVAL) {
                return Result.error("订单状态不允许删除结算记录");
            }
            
            // 清空结算相关字段，将状态重置为待结算
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(id);
            updateOrder.setStatus(MedicalOrder.STATUS_PENDING);
            updateOrder.setSettlementNo(null);
            updateOrder.setSettlementTime(null);
            updateOrder.setReimbursableAmount(null);
            updateOrder.setActualReimbursement(null);
            updateOrder.setSelfPayAmount(null);
            updateOrder.setApprovalResult(null);
            updateOrder.setApprovalRemark(null);
            updateOrder.setRejectReason(null);
            updateOrder.setUpdateTime(java.time.LocalDateTime.now());
            
            if (medicalOrderService.updateMedicalOrder(updateOrder)) {
                return Result.success("结算记录删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            logger.error("删除结算记录失败，ID：{}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 创建结算记录
     */
    @Operation(summary = "创建结算记录", description = "为指定订单创建新的结算记录")
    @PostMapping("/settlement")
    public Result<String> createSettlement(@Parameter(description = "请求体", required = true) @RequestBody java.util.Map<String, Object> request) {
        try {
            Object orderIdObj = request.get("orderId");
            if (orderIdObj == null) {
                return Result.error("订单ID不能为空");
            }
            
            Long orderId = Long.valueOf(orderIdObj.toString());
            
            // 提交结算申请
            if (medicalOrderService.submitSettlement(orderId)) {
                return Result.success("结算记录创建成功");
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            logger.error("创建结算记录失败", e);
            return Result.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新结算记录
     */
    @Operation(summary = "更新结算记录", description = "更新指定订单的结算信息")
    @PutMapping("/settlement/{id}")
    public Result<String> updateSettlement(
            @Parameter(description = "订单ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "结算数据", required = true) @RequestBody java.util.Map<String, Object> request) {
        try {
            // 获取订单信息
            MedicalOrder order = medicalOrderService.getMedicalOrderById(id);
            if (order == null) {
                return Result.error("订单不存在");
            }
            
            // 构建更新对象
            MedicalOrder updateOrder = new MedicalOrder();
            updateOrder.setId(id);
            
            // 从请求中提取字段并更新
            if (request.containsKey("reimbursableAmount")) {
                updateOrder.setReimbursableAmount(new java.math.BigDecimal(request.get("reimbursableAmount").toString()));
            }
            if (request.containsKey("actualReimbursement")) {
                updateOrder.setActualReimbursement(new java.math.BigDecimal(request.get("actualReimbursement").toString()));
            }
            if (request.containsKey("selfPayAmount")) {
                updateOrder.setSelfPayAmount(new java.math.BigDecimal(request.get("selfPayAmount").toString()));
            }
            if (request.containsKey("approvalRemark")) {
                updateOrder.setApprovalRemark(request.get("approvalRemark").toString());
            }
            
            updateOrder.setUpdateTime(java.time.LocalDateTime.now());
            
            if (medicalOrderService.updateMedicalOrder(updateOrder)) {
                return Result.success("结算记录更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            logger.error("更新结算记录失败，ID：{}", id, e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
} 