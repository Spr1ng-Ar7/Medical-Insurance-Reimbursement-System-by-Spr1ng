package org.neu.insurance.controller;

import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.ReimbursementLevel;
import org.neu.insurance.service.ReimbursementLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

/**
 * 报销等级配置控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/reimbursement-level")
@Tag(name = "报销等级管理", description = "报销等级配置管理相关接口")
@CrossOrigin(origins = "*")
public class ReimbursementLevelController {
    
    @Autowired
    private ReimbursementLevelService reimbursementLevelService;
    
    /**
     * 分页查询报销等级列表
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询报销等级列表", description = "分页获取报销等级配置列表")
    public Result<PageResponse<ReimbursementLevel>> getReimbursementLevelList(
            @Parameter(description = "等级名称", required = false)
            @RequestParam(required = false) String levelName,
            @Parameter(description = "医保类型", required = false)
            @RequestParam(required = false) String insuranceType,
            @Parameter(description = "医院等级", required = false)
            @RequestParam(required = false) String hospitalLevel,
            @Parameter(description = "状态", required = false)
            @RequestParam(required = false) Integer status,
            @RequestBody PageRequest pageRequest) {
        try {
            log.info("分页查询报销等级列表，参数：levelName={}, insuranceType={}, hospitalLevel={}, status={}", 
                    levelName, insuranceType, hospitalLevel, status);
            
            PageResponse<ReimbursementLevel> response = reimbursementLevelService.getReimbursementLevelList(
                    levelName, insuranceType, hospitalLevel, status, pageRequest);
            
            return Result.success("查询成功", response);
        } catch (Exception e) {
            log.error("分页查询报销等级列表失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取报销等级详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取报销等级详情", description = "根据报销等级ID获取详细信息")
    public Result<ReimbursementLevel> getReimbursementLevelById(
            @Parameter(description = "报销等级ID", required = true)
            @PathVariable Long id) {
        try {
            log.info("根据ID获取报销等级详情，ID：{}", id);
            ReimbursementLevel level = reimbursementLevelService.getReimbursementLevelById(id);
            return Result.success("查询成功", level);
        } catch (Exception e) {
            log.error("获取报销等级详情失败，ID：{}", id, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增报销等级
     */
    @PostMapping
    @Operation(summary = "新增报销等级", description = "创建新的报销等级配置")
    public Result<Void> addReimbursementLevel(@RequestBody ReimbursementLevel level) {
        try {
            log.info("新增报销等级，等级名称：{}", level.getLevelName());
            
            // 验证配置
            if (!reimbursementLevelService.validateReimbursementLevel(level)) {
                return Result.error("报销等级配置验证失败");
            }
            
            reimbursementLevelService.addReimbursementLevel(level);
            return Result.success("新增成功", null);
        } catch (Exception e) {
            log.error("新增报销等级失败", e);
            return Result.error("新增失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新报销等级信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新报销等级信息", description = "更新指定报销等级的配置信息")
    public Result<Void> updateReimbursementLevel(
            @Parameter(description = "报销等级ID", required = true)
            @PathVariable Long id, 
            @RequestBody ReimbursementLevel level) {
        try {
            log.info("更新报销等级信息，ID：{}", id);
            
            // 验证配置
            if (!reimbursementLevelService.validateReimbursementLevel(level)) {
                return Result.error("报销等级配置验证失败");
            }
            
            reimbursementLevelService.updateReimbursementLevel(id, level);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            log.error("更新报销等级信息失败，ID：{}", id, e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除报销等级
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除报销等级", description = "根据ID删除报销等级配置")
    public Result<Void> deleteReimbursementLevel(
            @Parameter(description = "报销等级ID", required = true)
            @PathVariable Long id) {
        try {
            log.info("删除报销等级，ID：{}", id);
            reimbursementLevelService.deleteReimbursementLevel(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            log.error("删除报销等级失败，ID：{}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除报销等级
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除报销等级", description = "批量删除多个报销等级配置")
    public Result<Void> batchDeleteReimbursementLevel(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除报销等级，IDs：{}", ids);
            reimbursementLevelService.batchDeleteReimbursementLevel(ids);
            return Result.success("批量删除成功", null);
        } catch (Exception e) {
            log.error("批量删除报销等级失败，IDs：{}", ids, e);
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用报销等级
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改报销等级状态", description = "启用或禁用报销等级配置")
    public Result<Void> updateStatus(
            @Parameter(description = "报销等级ID", required = true)
            @PathVariable Long id, 
            @RequestBody Map<String, Integer> request) {
        try {
            log.info("更改报销等级状态，ID：{}", id);
            
            Integer status = request.get("status");
            if (status == null) {
                return Result.error("状态参数不能为空");
            }
            
            reimbursementLevelService.updateStatus(id, status);
            return Result.success("状态更新成功", null);
        } catch (Exception e) {
            log.error("更改报销等级状态失败，ID：{}", id, e);
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取有效的报销等级列表（用于下拉选择）
     */
    @GetMapping("/effective")
    @Operation(summary = "获取有效的报销等级列表", description = "获取所有有效的报销等级配置，用于下拉选择")
    public Result<List<ReimbursementLevel>> getEffectiveReimbursementLevels() {
        try {
            log.info("获取有效的报销等级列表");
            List<ReimbursementLevel> levels = reimbursementLevelService.getEffectiveReimbursementLevels();
            return Result.success("查询成功", levels);
        } catch (Exception e) {
            log.error("获取有效的报销等级列表失败", e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据医保类型和医院等级获取匹配的报销等级
     */
    @GetMapping("/match")
    @Operation(summary = "匹配报销等级", description = "根据医保类型和医院等级获取匹配的报销等级配置")
    public Result<List<ReimbursementLevel>> getMatchingReimbursementLevels(
            @Parameter(description = "医保类型", required = true)
            @RequestParam String insuranceType,
            @Parameter(description = "医院等级", required = true)
            @RequestParam String hospitalLevel) {
        try {
            log.info("匹配报销等级，医保类型：{}，医院等级：{}", insuranceType, hospitalLevel);
            List<ReimbursementLevel> levels = reimbursementLevelService.getMatchingReimbursementLevels(
                    insuranceType, hospitalLevel);
            return Result.success("查询成功", levels);
        } catch (Exception e) {
            log.error("匹配报销等级失败，医保类型：{}，医院等级：{}", insuranceType, hospitalLevel, e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 复制报销等级配置
     */
    @PostMapping("/{id}/copy")
    @Operation(summary = "复制报销等级配置", description = "复制指定报销等级的配置信息")
    public Result<Void> copyReimbursementLevel(
            @Parameter(description = "报销等级ID", required = true)
            @PathVariable Long id) {
        try {
            log.info("复制报销等级配置，ID：{}", id);
            reimbursementLevelService.copyReimbursementLevel(id);
            return Result.success("复制成功", null);
        } catch (Exception e) {
            log.error("复制报销等级配置失败，ID：{}", id, e);
            return Result.error("复制失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证报销等级配置
     */
    @PostMapping("/validate")
    @Operation(summary = "验证报销等级配置", description = "验证报销等级配置的有效性")
    public Result<Boolean> validateReimbursementLevel(@RequestBody ReimbursementLevel level) {
        try {
            log.info("验证报销等级配置");
            boolean isValid = reimbursementLevelService.validateReimbursementLevel(level);
            return Result.success("验证完成", isValid);
        } catch (Exception e) {
            log.error("验证报销等级配置失败", e);
            return Result.error("验证失败: " + e.getMessage());
        }
    }

} 