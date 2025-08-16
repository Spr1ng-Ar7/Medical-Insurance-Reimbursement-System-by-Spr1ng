package org.neu.insurance.controller;

import org.neu.insurance.entity.Disease;
import org.neu.insurance.service.DiseaseService;
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
 * 疾病管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/diseases")
@Tag(name = "疾病管理", description = "疾病编码管理相关接口")
public class DiseaseController {
    
    @Autowired
    private DiseaseService diseaseService;
    
    /**
     * 根据ID查询疾病
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据ID查询疾病", description = "根据疾病ID获取疾病详细信息")
    public Result<Disease> getDiseaseById(
            @Parameter(description = "疾病ID", required = true)
            @PathVariable Long id) {
        try {
            Disease disease = diseaseService.getDiseaseById(id);
            return Result.success(disease);
        } catch (Exception e) {
            log.error("查询疾病失败", e);
            return Result.error("查询疾病失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据疾病编码查询疾病
     */
    @GetMapping("/code/{diseaseCode}")
    @Operation(summary = "根据疾病编码查询疾病", description = "根据疾病编码获取疾病信息")
    public Result<Disease> getDiseaseByCode(
            @Parameter(description = "疾病编码", required = true)
            @PathVariable String diseaseCode) {
        try {
            Disease disease = diseaseService.getDiseaseByCode(diseaseCode);
            return Result.success(disease);
        } catch (Exception e) {
            log.error("查询疾病失败", e);
            return Result.error("查询疾病失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ICD编码查询疾病
     */
    @GetMapping("/icd/{icdCode}")
    @Operation(summary = "根据ICD编码查询疾病", description = "根据国际疾病分类编码获取疾病信息")
    public Result<Disease> getDiseaseByIcdCode(
            @Parameter(description = "ICD编码", required = true)
            @PathVariable String icdCode) {
        try {
            Disease disease = diseaseService.getDiseaseByIcdCode(icdCode);
            return Result.success(disease);
        } catch (Exception e) {
            log.error("查询疾病失败", e);
            return Result.error("查询疾病失败: " + e.getMessage());
        }
    }
    
    /**
     * 分页查询疾病列表
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询疾病列表", description = "分页获取疾病列表")
    public Result<PageResponse<Disease>> getDiseaseList(@RequestBody PageRequest pageRequest) {
        try {
            PageResponse<Disease> response = diseaseService.getDiseaseList(pageRequest);
            return Result.success(response);
        } catch (Exception e) {
            log.error("查询疾病列表失败", e);
            return Result.error("查询疾病列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据疾病分类查询疾病列表
     */
    @GetMapping("/category/{category}")
    @Operation(summary = "根据分类查询疾病", description = "根据疾病分类获取疾病列表")
    public Result<List<Disease>> getDiseasesByCategory(
            @Parameter(description = "疾病分类", required = true)
            @PathVariable String category) {
        try {
            List<Disease> diseases = diseaseService.getDiseasesByCategory(category);
            return Result.success(diseases);
        } catch (Exception e) {
            log.error("查询疾病列表失败", e);
            return Result.error("查询疾病列表失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建疾病
     */
    @PostMapping
    @Operation(summary = "创建疾病", description = "创建新的疾病记录")
    public Result<Void> createDisease(@RequestBody Disease disease) {
        try {
            // 检查疾病编码是否已存在
            if (diseaseService.checkDiseaseCodeExists(disease.getDiseaseCode())) {
                return Result.error("疾病编码已存在");
            }
            // 检查ICD编码是否已存在
            if (disease.getIcdCode() != null && diseaseService.checkIcdCodeExists(disease.getIcdCode())) {
                return Result.error("ICD编码已存在");
            }
            diseaseService.createDisease(disease);
            return Result.success("疾病创建成功", null);
        } catch (Exception e) {
            log.error("创建疾病失败", e);
            return Result.error("创建疾病失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新疾病信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新疾病信息", description = "更新指定疾病的信息")
    public Result<Void> updateDisease(
            @Parameter(description = "疾病ID", required = true)
            @PathVariable Long id,
            @RequestBody Disease disease) {
        try {
            disease.setId(id);
            diseaseService.updateDisease(disease);
            return Result.success("疾病信息更新成功", null);
        } catch (Exception e) {
            log.error("更新疾病信息失败", e);
            return Result.error("更新疾病信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除疾病
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除疾病", description = "根据ID删除疾病")
    public Result<Void> deleteDisease(
            @Parameter(description = "疾病ID", required = true)
            @PathVariable Long id) {
        try {
            diseaseService.deleteDisease(id);
            return Result.success("疾病删除成功", null);
        } catch (Exception e) {
            log.error("删除疾病失败", e);
            return Result.error("删除疾病失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除疾病
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除疾病", description = "批量删除多个疾病")
    public Result<Void> batchDeleteDiseases(@RequestBody List<Long> ids) {
        try {
            diseaseService.batchDeleteDiseases(ids);
            return Result.success("批量删除疾病成功", null);
        } catch (Exception e) {
            log.error("批量删除疾病失败", e);
            return Result.error("批量删除疾病失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用疾病
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "更改疾病状态", description = "启用或禁用疾病")
    public Result<Void> changeDiseaseStatus(
            @Parameter(description = "疾病ID", required = true)
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        try {
            Integer status = request.get("status");
            if (status == null) {
                return Result.error("状态参数不能为空");
            }
            diseaseService.changeDiseaseStatus(id, status);
            return Result.success("疾病状态更新成功", null);
        } catch (Exception e) {
            log.error("更改疾病状态失败", e);
            return Result.error("更改疾病状态失败: " + e.getMessage());
        }
    }
    
    /**
     * 模糊搜索疾病名称
     */
    @GetMapping("/search")
    @Operation(summary = "搜索疾病", description = "根据关键词模糊搜索疾病名称")
    public Result<List<Disease>> searchDiseases(
            @Parameter(description = "搜索关键词", required = true)
            @RequestParam String keyword) {
        try {
            List<Disease> diseases = diseaseService.searchDiseasesByName(keyword);
            return Result.success(diseases);
        } catch (Exception e) {
            log.error("搜索疾病失败", e);
            return Result.error("搜索疾病失败: " + e.getMessage());
        }
    }
} 