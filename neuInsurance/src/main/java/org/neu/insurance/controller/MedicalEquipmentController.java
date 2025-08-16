package org.neu.insurance.controller;

import org.neu.insurance.entity.MedicalEquipment;
import org.neu.insurance.service.MedicalEquipmentService;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;

/**
 * 医疗器械管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/medical-equipments")
@Tag(name = "医疗器械管理", description = "医疗器械设备管理相关接口")
public class MedicalEquipmentController {
    
    @Autowired
    private MedicalEquipmentService medicalEquipmentService;
    
    /**
     * 根据ID查询医疗器械
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询医疗器械", description = "根据医疗器械ID获取器械详细信息")
    public Result<MedicalEquipment> getMedicalEquipmentById(
            @Parameter(description = "医疗器械ID", required = true)
            @PathVariable Long id) {
        try {
            MedicalEquipment equipment = medicalEquipmentService.getMedicalEquipmentById(id);
            return Result.success(equipment);
        } catch (Exception e) {
            log.error("查询医疗器械失败", e);
            return Result.error("查询医疗器械失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据器械编码查询医疗器械
     */
    @GetMapping("/code/{equipmentCode}")
    @Operation(summary = "根据器械编码查询医疗器械", description = "根据器械编码获取医疗器械信息")
    public Result<MedicalEquipment> getMedicalEquipmentByCode(
            @Parameter(description = "器械编码", required = true)
            @PathVariable String equipmentCode) {
        try {
            MedicalEquipment equipment = medicalEquipmentService.getMedicalEquipmentByCode(equipmentCode);
            return Result.success(equipment);
        } catch (Exception e) {
            log.error("查询医疗器械失败", e);
            return Result.error("查询医疗器械失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询医疗器械列表
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询医疗器械列表", description = "分页获取医疗器械列表")
    public Result<PageResponse<MedicalEquipment>> getMedicalEquipmentList(@RequestBody PageRequest pageRequest) {
        try {
            PageResponse<MedicalEquipment> response = medicalEquipmentService.getMedicalEquipmentList(pageRequest);
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询医疗器械列表失败", e);
            return Result.error("查询医疗器械列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据器械类型查询医疗器械列表
     */
    @GetMapping("/type/{equipmentType}")
    @Operation(summary = "根据类型查询医疗器械", description = "根据器械类型获取医疗器械列表")
    public Result<List<MedicalEquipment>> getMedicalEquipmentsByType(
            @Parameter(description = "器械类型", required = true)
            @PathVariable String equipmentType) {
        try {
            List<MedicalEquipment> equipments = medicalEquipmentService.getMedicalEquipmentsByType(equipmentType);
            return Result.success(equipments);
        } catch (Exception e) {
            log.error("查询医疗器械列表失败", e);
            return Result.error("查询医疗器械列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建医疗器械
     */
    @PostMapping
    @Operation(summary = "创建医疗器械", description = "创建新的医疗器械记录")
    public Result<Void> createMedicalEquipment(@RequestBody MedicalEquipment medicalEquipment) {
        try {
            // 检查器械编码是否已存在
            if (medicalEquipmentService.checkEquipmentCodeExists(medicalEquipment.getEquipmentCode())) {
                return Result.error("器械编码已存在");
            }
            medicalEquipmentService.createMedicalEquipment(medicalEquipment);
            return Result.success("医疗器械创建成功", null);
        } catch (Exception e) {
            log.error("创建医疗器械失败", e);
            return Result.error("创建医疗器械失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新医疗器械信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新医疗器械信息", description = "更新指定医疗器械的信息")
    public Result<Void> updateMedicalEquipment(
            @Parameter(description = "医疗器械ID", required = true)
            @PathVariable Long id,
            @RequestBody MedicalEquipment medicalEquipment) {
        try {
            medicalEquipment.setId(id);
            medicalEquipmentService.updateMedicalEquipment(medicalEquipment);
            return Result.success("医疗器械信息更新成功", null);
        } catch (Exception e) {
            log.error("更新医疗器械信息失败", e);
            return Result.error("更新医疗器械信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除医疗器械
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除医疗器械", description = "根据ID删除医疗器械")
    public Result<Void> deleteMedicalEquipment(
            @Parameter(description = "医疗器械ID", required = true)
            @PathVariable Long id) {
        try {
            medicalEquipmentService.deleteMedicalEquipment(id);
            return Result.success("医疗器械删除成功", null);
        } catch (Exception e) {
            log.error("删除医疗器械失败", e);
            return Result.error("删除医疗器械失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除医疗器械
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除医疗器械", description = "批量删除多个医疗器械")
    public Result<Void> batchDeleteMedicalEquipments(@RequestBody List<Long> ids) {
        try {
            medicalEquipmentService.batchDeleteMedicalEquipments(ids);
            return Result.success("批量删除医疗器械成功", null);
        } catch (Exception e) {
            log.error("批量删除医疗器械失败", e);
            return Result.error("批量删除医疗器械失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用医疗器械
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改医疗器械状态", description = "启用或禁用医疗器械")
    public Result<Void> changeMedicalEquipmentStatus(
            @Parameter(description = "医疗器械ID", required = true)
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
            if (status == null) {
                return Result.error("状态参数不能为空");
            }
            medicalEquipmentService.changeMedicalEquipmentStatus(id, status);
            return Result.success("医疗器械状态更新成功", null);
        } catch (Exception e) {
            log.error("更改医疗器械状态失败", e);
            return Result.error("更改医疗器械状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 模糊搜索医疗器械名称
     */
    @GetMapping("/search")
    @Operation(summary = "搜索医疗器械", description = "根据关键词模糊搜索医疗器械名称")
    public Result<List<MedicalEquipment>> searchMedicalEquipments(
            @Parameter(description = "搜索关键词", required = true)
            @RequestParam String keyword) {
        try {
            List<MedicalEquipment> equipments = medicalEquipmentService.searchMedicalEquipmentsByName(keyword);
            return Result.success(equipments);
        } catch (Exception e) {
            log.error("搜索医疗器械失败", e);
            return Result.error("搜索医疗器械失败: " + e.getMessage());
        }
    }
} 