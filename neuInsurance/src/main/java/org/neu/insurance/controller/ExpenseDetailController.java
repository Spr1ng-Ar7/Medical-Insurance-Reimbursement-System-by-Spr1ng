package org.neu.insurance.controller;

import org.neu.insurance.entity.ExpenseDetail;
import org.neu.insurance.service.ExpenseDetailService;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 费用明细管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/expense-details")

@Tag(name = "费用明细管理", description = "费用明细账单管理相关接口")
public class ExpenseDetailController {
    
    @Autowired
    private ExpenseDetailService expenseDetailService;
    
    /**
     * 根据ID查询费用明细
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询费用明细", description = "根据费用明细ID获取详细信息")
    public Result<ExpenseDetail> getExpenseDetailById(
            @Parameter(description = "费用明细ID", required = true)
            @PathVariable Long id) {
        try {
            ExpenseDetail detail = expenseDetailService.getExpenseDetailById(id);
            return Result.success(detail);
        } catch (Exception e) {
            log.error("查询费用明细失败", e);
            return Result.error("查询费用明细失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据患者ID查询费用明细列表
     */
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "根据患者ID查询费用明细", description = "根据患者ID获取费用明细列表")
    public Result<List<ExpenseDetail>> getExpenseDetailsByPatientId(
            @Parameter(description = "患者ID", required = true)
            @PathVariable Long patientId) {
        try {
            List<ExpenseDetail> details = expenseDetailService.getExpenseDetailsByPatientId(patientId);
            return Result.success(details);
        } catch (Exception e) {
            log.error("查询费用明细列表失败", e);
            return Result.error("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据订单ID查询费用明细列表
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "根据订单ID查询费用明细", description = "根据订单ID获取费用明细列表")
    public Result<List<ExpenseDetail>> getExpenseDetailsByOrderId(
            @Parameter(description = "订单ID", required = true)
            @PathVariable Long orderId) {
        try {
            List<ExpenseDetail> details = expenseDetailService.getExpenseDetailsByOrderId(orderId);
            return Result.success(details);
        } catch (Exception e) {
            log.error("查询费用明细列表失败", e);
            return Result.error("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询费用明细列表
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询费用明细列表", description = "分页获取费用明细列表")
    public Result<PageResponse<ExpenseDetail>> getExpenseDetailList(@RequestBody PageRequest pageRequest) {
        try {
            PageResponse<ExpenseDetail> response = expenseDetailService.getExpenseDetailList(pageRequest);
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询费用明细列表失败", e);
            return Result.error("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据项目类型查询费用明细列表
     */
    @GetMapping("/item-type/{itemType}")
    @Operation(summary = "根据项目类型查询费用明细", description = "根据项目类型获取费用明细列表")
    public Result<List<ExpenseDetail>> getExpenseDetailsByItemType(
            @Parameter(description = "项目类型", required = true)
            @PathVariable String itemType) {
        try {
            List<ExpenseDetail> details = expenseDetailService.getExpenseDetailsByItemType(itemType);
            return Result.success(details);
        } catch (Exception e) {
            log.error("查询费用明细列表失败", e);
            return Result.error("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建费用明细
     */
    @PostMapping
    @Operation(summary = "创建费用明细", description = "创建新的费用明细记录")
    public Result<Void> createExpenseDetail(@RequestBody ExpenseDetail expenseDetail) {
        try {
            expenseDetailService.createExpenseDetail(expenseDetail);
            return Result.success("费用明细创建成功", null);
        } catch (Exception e) {
            log.error("创建费用明细失败", e);
            return Result.error("创建费用明细失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新费用明细信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新费用明细信息", description = "更新指定费用明细的信息")
    public Result<Void> updateExpenseDetail(
            @Parameter(description = "费用明细ID", required = true)
            @PathVariable Long id,
            @RequestBody ExpenseDetail expenseDetail) {
        try {
            expenseDetail.setId(id);
            expenseDetailService.updateExpenseDetail(expenseDetail);
            return Result.success("费用明细信息更新成功", null);
        } catch (Exception e) {
            log.error("更新费用明细信息失败", e);
            return Result.error("更新费用明细信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除费用明细
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除费用明细", description = "根据ID删除费用明细")
    public Result<Void> deleteExpenseDetail(
            @Parameter(description = "费用明细ID", required = true)
            @PathVariable Long id) {
        try {
            expenseDetailService.deleteExpenseDetail(id);
            return Result.success("费用明细删除成功", null);
        } catch (Exception e) {
            log.error("删除费用明细失败", e);
            return Result.error("删除费用明细失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除费用明细
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除费用明细", description = "批量删除多个费用明细")
    public Result<Void> batchDeleteExpenseDetails(@RequestBody List<Long> ids) {
        try {
            expenseDetailService.batchDeleteExpenseDetails(ids);
            return Result.success("批量删除费用明细成功", null);
        } catch (Exception e) {
            log.error("批量删除费用明细失败", e);
            return Result.error("批量删除费用明细失败: " + e.getMessage());
        }
    }
    
    /**
     * 更改费用明细状态
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改费用明细状态", description = "更新费用明细状态")
    public Result<Void> changeExpenseDetailStatus(
            @Parameter(description = "费用明细ID", required = true)
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
            if (status == null) {
                return Result.error("状态参数不能为空");
            }
            expenseDetailService.changeExpenseDetailStatus(id, status);
            return Result.success("费用明细状态更新成功", null);
        } catch (Exception e) {
            log.error("更改费用明细状态失败", e);
            return Result.error("更改费用明细状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计费用总额
     */
    @GetMapping("/stats/total-amount")
    @Operation(summary = "统计费用总额", description = "根据条件统计费用总额")
    public Result<BigDecimal> calculateTotalAmount(
            @Parameter(description = "患者ID") @RequestParam(required = false) Long patientId,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endDate) {
        try {
            BigDecimal totalAmount = expenseDetailService.calculateTotalAmount(patientId, startDate, endDate);
            return Result.success(totalAmount);
        } catch (Exception e) {
            log.error("统计费用总额失败", e);
            return Result.error("统计费用总额失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计报销总额
     */
    @GetMapping("/stats/reimbursement-amount")
    @Operation(summary = "统计报销总额", description = "根据条件统计报销总额")
    public Result<BigDecimal> calculateReimbursementAmount(
            @Parameter(description = "患者ID") @RequestParam(required = false) Long patientId,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endDate) {
        try {
            BigDecimal reimbursementAmount = expenseDetailService.calculateReimbursementAmount(patientId, startDate, endDate);
            return Result.success(reimbursementAmount);
        } catch (Exception e) {
            log.error("统计报销总额失败", e);
            return Result.error("统计报销总额失败: " + e.getMessage());
        }
    }
    
    /**
     * 统计自付总额
     */
    @GetMapping("/stats/self-pay-amount")
    @Operation(summary = "统计自付总额", description = "根据条件统计自付总额")
    public Result<BigDecimal> calculateSelfPayAmount(
            @Parameter(description = "患者ID") @RequestParam(required = false) Long patientId,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endDate) {
        try {
            BigDecimal selfPayAmount = expenseDetailService.calculateSelfPayAmount(patientId, startDate, endDate);
            return Result.success(selfPayAmount);
        } catch (Exception e) {
            log.error("统计自付总额失败", e);
            return Result.error("统计自付总额失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据项目类型统计费用
     */
    @GetMapping("/stats/by-item-type")
    @Operation(summary = "根据项目类型统计费用", description = "根据项目类型统计费用分布")
    public Result<List<ExpenseDetail>> getExpenseStatsByItemType(
            @Parameter(description = "患者ID") @RequestParam(required = false) Long patientId,
            @Parameter(description = "开始时间") @RequestParam(required = false) LocalDateTime startDate,
            @Parameter(description = "结束时间") @RequestParam(required = false) LocalDateTime endDate) {
        try {
            List<ExpenseDetail> stats = expenseDetailService.getExpenseStatsByItemType(patientId, startDate, endDate);
            return Result.success(stats);
        } catch (Exception e) {
            log.error("根据项目类型统计费用失败", e);
            return Result.error("统计费用失败: " + e.getMessage());
        }
    }
} 