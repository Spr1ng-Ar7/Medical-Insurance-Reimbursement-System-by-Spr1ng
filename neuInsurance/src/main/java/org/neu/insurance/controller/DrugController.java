package org.neu.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.Drug;
import org.neu.insurance.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 药品管理控制器
 */
@Tag(name = "药品管理", description = "药品信息管理相关接口")
@RestController
@RequestMapping("/api/drug")
@CrossOrigin(origins = "*")
@Slf4j
public class DrugController {
    @Autowired
    private DrugService drugService;
    
    /**
     * 测试药品数据接收
     */
    @Operation(summary = "测试药品数据接收", description = "用于调试JSON数据映射问题")
    @PostMapping("/test")
    public Result<Drug> testDrug(@RequestBody Drug drug) {
        log.info("测试接收到的药品数据: {}", drug);
        log.info("详细字段值:");
        log.info("  drugCode: [{}]", drug.getDrugCode());
        log.info("  drugName: [{}]", drug.getDrugName());
        log.info("  drugType: [{}]", drug.getDrugType());
        log.info("  manufacturer: [{}]", drug.getManufacturer());
        log.info("  specification: [{}]", drug.getSpecification());
        log.info("  unit: [{}]", drug.getUnit());
        log.info("  price: [{}]", drug.getPrice());
        log.info("  selfPayRatio: [{}]", drug.getSelfPayRatio());
        log.info("  status: [{}]", drug.getStatus());
        log.info("  remark: [{}]", drug.getRemark());
        
        return Result.success("测试成功", drug);
    }
    
    /**
     * 分页查询药品列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询药品列表", description = "根据条件分页查询药品信息")
    public Result<PageResponse<Drug>> getDrugList(
            @Parameter(description = "药品名称", example = "阿司匹林") @RequestParam(required = false) String drugName,
            @Parameter(description = "药品类型", example = "西药") @RequestParam(required = false) String drugType,
            @Parameter(description = "药品状态", example = "1") @RequestParam(required = false) Integer status,
            PageRequest pageRequest) {
        try {
            log.info("查询药品列表 - drugName: {}, drugType: {}, status: {}, pageRequest: {}", 
                    drugName, drugType, status, pageRequest);
            PageResponse<Drug> response = drugService.getDrugList(drugName, drugType, status, pageRequest);
            log.info("查询药品列表成功，共{}条记录", response.getTotal());
            return Result.success("查询成功", response);
        } catch (Exception e) {
            log.error("查询药品列表失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取药品详情
     */
    
    @Operation(summary = "根据ID获取药品详情", description = "根据药品ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Drug> getDrugById(@Parameter(description = "药品ID", required = true, example = "1") @PathVariable Long id) {
        try {
            log.info("根据ID查询药品: {}", id);
            Drug drug = drugService.getDrugById(id);
            if (drug != null) {
                log.info("查询药品成功: {}", drug.getDrugName());
                return Result.success("查询成功", drug);
            } else {
                log.warn("药品不存在: {}", id);
                return Result.error("药品不存在");
            }
        } catch (Exception e) {
            log.error("查询药品失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增药品
     */
    
    @Operation(summary = "新增药品", description = "添加新的药品信息")
    @PostMapping
    public Result<Void> addDrug(@Parameter(description = "药品信息，示例：{\"drugCode\":\"TEST001\",\"drugName\":\"阿司匹林\",\"drugType\":\"甲类\",\"specification\":\"100mg\",\"unit\":\"片\",\"price\":15.50,\"manufacturer\":\"华北制药\",\"status\":1}", required = true) @RequestBody Drug drug) {
        try {
            log.info("=== 新增药品请求开始 ===");
            log.info("接收到的原始数据: {}", drug);
            
            // 基本数据验证
            if (drug == null) {
                log.error("药品数据为空");
                return Result.error("药品数据不能为空");
            }
            
            // 详细记录每个字段的原始值
            log.info("原始字段值:");
            log.info("  drugCode: [{}] (length: {})", drug.getDrugCode(), drug.getDrugCode() != null ? drug.getDrugCode().length() : "null");
            log.info("  drugName: [{}] (length: {})", drug.getDrugName(), drug.getDrugName() != null ? drug.getDrugName().length() : "null");
            log.info("  drugType: [{}] (length: {})", drug.getDrugType(), drug.getDrugType() != null ? drug.getDrugType().length() : "null");
            log.info("  manufacturer: [{}] (length: {})", drug.getManufacturer(), drug.getManufacturer() != null ? drug.getManufacturer().length() : "null");
            
            // 检查并提供具体的错误信息
            if (drug.getDrugCode() == null) {
                log.error("药品编码为null，请检查JSON字段映射");
                return Result.error("药品编码不能为空，请检查JSON格式。示例：{\"drugCode\":\"TEST001\",\"drugName\":\"阿司匹林\",\"drugType\":\"甲类\",\"manufacturer\":\"华北制药\"}");
            }
            if (drug.getDrugCode().trim().isEmpty()) {
                log.error("药品编码为空字符串: [{}]", drug.getDrugCode());
                return Result.error("药品编码不能为空，请输入药品编码");
            }
            
            if (drug.getDrugName() == null) {
                log.error("药品名称为null，请检查JSON字段映射");
                return Result.error("药品名称不能为空，请检查JSON格式");
            }
            if (drug.getDrugName().trim().isEmpty()) {
                log.error("药品名称为空字符串: [{}]", drug.getDrugName());
                return Result.error("药品名称不能为空，请输入药品名称");
            }
            
            if (drug.getDrugType() == null) {
                log.error("药品类型为null，请检查JSON字段映射");
                return Result.error("药品类型不能为空，请选择：甲类、乙类或丙类");
            }
            if (drug.getDrugType().trim().isEmpty()) {
                log.error("药品类型为空字符串: [{}]", drug.getDrugType());
                return Result.error("药品类型不能为空，请选择：甲类、乙类或丙类");
            }
            
            if (drug.getManufacturer() == null) {
                log.error("生产厂家为null，请检查JSON字段映射");
                return Result.error("生产厂家不能为空，请检查JSON格式");
            }
            if (drug.getManufacturer().trim().isEmpty()) {
                log.error("生产厂家为空字符串: [{}]", drug.getManufacturer());
                return Result.error("生产厂家不能为空，请输入生产厂家名称");
            }
            
            log.info("字段验证通过，准备调用服务层");
            
            drugService.addDrug(drug);
            
            log.info("新增药品成功: {}", drug.getDrugName());
            return Result.success("新增成功", null);
        } catch (IllegalArgumentException e) {
            log.error("新增药品参数错误: {}", e.getMessage());
            return Result.error("参数错误: " + e.getMessage());
        } catch (Exception e) {
            log.error("新增药品失败: {}", e.getMessage(), e);
            return Result.error("新增失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新药品信息
     */
    
    @Operation(summary = "更新药品信息", description = "更新指定药品的信息")
    @PutMapping("/{id}")
    public Result<Void> updateDrug(
            @Parameter(description = "药品ID", required = true, example = "1") @PathVariable Long id,
            @Parameter(description = "药品信息", required = true) @RequestBody Drug drug) {
        try {
            log.info("接收到更新药品请求，ID: {}, 数据: {}", id, drug);
            
            // 基本数据验证
            if (drug == null) {
                log.error("药品信息不能为空");
                return Result.error("药品信息不能为空");
            }
            
            if (id == null) {
                log.error("药品ID不能为空");
                return Result.error("药品ID不能为空");
            }
            
            // 检查药品是否存在
            Drug existingDrug = drugService.getDrugById(id);
            if (existingDrug == null) {
                log.warn("药品不存在: {}", id);
                return Result.error("药品不存在");
            }
            
            // 设置路径中的ID
            drug.setId(id);
            
            log.info("更新药品信息 - ID: {}, 药品编码: {}, 药品名称: {}, 药品类型: {}, 生产厂家: {}", 
                    id, drug.getDrugCode(), drug.getDrugName(), drug.getDrugType(), drug.getManufacturer());
            
            drugService.updateDrug(drug);
            
            log.info("更新药品成功: ID={}, 名称={}", id, drug.getDrugName());
            return Result.success("更新成功", null);
        } catch (IllegalArgumentException e) {
            log.error("更新药品参数错误: {}", e.getMessage());
            return Result.error("参数错误: " + e.getMessage());
        } catch (Exception e) {
            log.error("更新药品失败: {}", e.getMessage(), e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除药品
     */
    
    @Operation(summary = "删除药品", description = "根据ID软删除指定药品")
    @DeleteMapping("/{id}")
    public Result<Void> deleteDrug(@Parameter(description = "药品ID", required = true, example = "1") @PathVariable Long id) {
        try {
            log.info("接收到删除药品请求，ID: {}", id);
            
            if (id == null) {
                log.error("药品ID不能为空");
                return Result.error("药品ID不能为空");
            }
            
            // 获取药品信息用于日志
            Drug existingDrug = drugService.getDrugById(id);
            String drugName = existingDrug != null ? existingDrug.getDrugName() : "未知药品";
            
            log.info("准备删除药品: ID={}, 名称={}", id, drugName);
            
            drugService.deleteDrug(id);
            
            log.info("删除药品成功: ID={}, 名称={}", id, drugName);
            return Result.success("删除成功", null);
        } catch (IllegalArgumentException e) {
            log.error("删除药品参数错误: {}", e.getMessage());
            return Result.error("参数错误: " + e.getMessage());
        } catch (Exception e) {
            log.error("删除药品失败: {}", e.getMessage(), e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量新增药品
     */
    
    @Operation(summary = "批量新增药品", description = "批量添加多个药品信息")
    @PostMapping("/batch")
    public Result<Void> batchAddDrugs(@Parameter(description = "药品列表", required = true) @RequestBody List<Drug> drugs) {
        try {
            if (drugs == null || drugs.isEmpty()) {
                log.error("药品列表不能为空");
                return Result.error("药品列表不能为空");
            }
            log.info("批量新增药品，数量: {}", drugs.size());
            drugService.batchAddDrugs(drugs);
            log.info("批量新增药品成功，数量: {}", drugs.size());
            return Result.success("批量新增成功", null);
        } catch (Exception e) {
            log.error("批量新增药品失败: {}", e.getMessage(), e);
            return Result.error("批量新增失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据药品编码查询药品
     */
    
    @Operation(summary = "根据编码查询药品", description = "根据药品编码获取详细信息")
    @GetMapping("/code/{drugCode}")
    public Result<Drug> getDrugByCode(@Parameter(description = "药品编码", required = true, example = "DRUG001") @PathVariable String drugCode) {
        try {
            log.info("根据编码查询药品: {}", drugCode);
            Drug drug = drugService.getDrugByCode(drugCode);
            if (drug == null) {
                log.warn("药品不存在: {}", drugCode);
                return Result.error("药品不存在");
            }
            log.info("查询药品成功: {}", drug.getDrugName());
            return Result.success("查询成功", drug);
        } catch (Exception e) {
            log.error("查询药品失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取药品类型统计
     */
    
    @Operation(summary = "获取药品类型统计", description = "统计各类型药品的数量和占比")
    @GetMapping("/statistics/type")
    public Result<Object> getDrugTypeStatistics() {
        try {
            log.info("获取药品类型统计");
            Map<String, Object> statistics = drugService.getDrugTypeStatistics();
            log.info("获取药品类型统计成功");
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取药品类型统计失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取药品状态统计
     */
    
    @Operation(summary = "获取药品状态统计", description = "统计各状态药品的数量")
    @GetMapping("/statistics/status")
    public Result<Object> getDrugStatusStatistics() {
        try {
            log.info("获取药品状态统计");
            Map<String, Object> statistics = drugService.getDrugStatusStatistics();
            log.info("获取药品状态统计成功");
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取药品状态统计失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取生产厂家统计
     */
    
    @Operation(summary = "获取生产厂家统计", description = "统计各生产厂家的药品数量")
    @GetMapping("/statistics/manufacturer")
    public Result<Object> getManufacturerStatistics() {
        try {
            log.info("获取生产厂家统计");
            Map<String, Object> statistics = drugService.getManufacturerStatistics();
            log.info("获取生产厂家统计成功");
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取生产厂家统计失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取药品价格统计
     */
    
    @Operation(summary = "获取药品价格统计", description = "统计药品价格区间分布")
    @GetMapping("/statistics/price")
    public Result<Object> getDrugPriceStatistics() {
        try {
            log.info("获取药品价格统计");
            Map<String, Object> statistics = drugService.getDrugPriceStatistics();
            log.info("获取药品价格统计成功");
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取药品价格统计失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取综合统计信息
     */
    
    @Operation(summary = "获取综合统计信息", description = "获取药品管理的综合统计数据")
    @GetMapping("/statistics/comprehensive")
    public Result<Object> getDrugComprehensiveStatistics() {
        try {
            log.info("获取药品综合统计");
            Map<String, Object> statistics = drugService.getDrugComprehensiveStatistics();
            log.info("获取药品综合统计成功");
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取药品综合统计失败: {}", e.getMessage(), e);
            return Result.error("查询失败: " + e.getMessage());
        }
    }
} 