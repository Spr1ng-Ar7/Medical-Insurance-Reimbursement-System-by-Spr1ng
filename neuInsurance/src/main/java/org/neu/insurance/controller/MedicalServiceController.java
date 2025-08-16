package org.neu.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.MedicalService;
import org.neu.insurance.service.MedicalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统一医疗服务管理控制器
 * 整合：诊疗项目、检查服务、手术服务、服务设施等
 */
@RestController
@RequestMapping("/api/medicalService")
@Tag(name = "MedicalServiceController", description = "统一医疗服务管理控制器")
@CrossOrigin(origins = "*")
public class MedicalServiceController {

    @Autowired
    private MedicalServiceService medicalServiceService;

    /**
     * 分页查询医疗服务列表
     */
    
    @Operation(summary = "分页查询医疗服务列表", description = "根据条件分页查询医疗服务信息")
    @GetMapping("/list")
    public Result<PageResponse<MedicalService>> getMedicalServiceList(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "服务类型") @RequestParam(required = false) String serviceType,
            @Parameter(description = "服务类别") @RequestParam(required = false) String category,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(pageNum);
            pageRequest.setPageSize(pageSize);
            
            PageResponse<MedicalService> response = medicalServiceService.getMedicalServiceList(pageRequest, serviceType, category, keyword);
            return Result.success("查询成功", response);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询医疗服务
     */
    
    @Operation(summary = "根据ID查询医疗服务", description = "根据医疗服务ID获取详细信息")
    @GetMapping("/{id}")
    public Result<MedicalService> getMedicalServiceById(@Parameter(description = "医疗服务ID", required = true, example = "1") @PathVariable Long id) {
        try {
            MedicalService service = medicalServiceService.getMedicalServiceById(id);
            if (service != null) {
                return Result.success("查询成功", service);
            } else {
                return Result.error("医疗服务不存在");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据编码查询医疗服务
     */
    
    @Operation(summary = "根据编码查询医疗服务", description = "根据服务编码获取详细信息")
    @GetMapping("/byCode/{serviceCode}")
    public Result<MedicalService> getMedicalServiceByCode(@Parameter(description = "服务编码", required = true, example = "MS001") @PathVariable String serviceCode) {
        try {
            MedicalService service = medicalServiceService.getMedicalServiceByCode(serviceCode);
            if (service != null) {
                return Result.success("查询成功", service);
            } else {
                return Result.error("医疗服务不存在");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增医疗服务
     */
    
    @Operation(summary = "新增医疗服务", description = "创建新的医疗服务")
    @PostMapping("/add")
    public Result<String> addMedicalService(@Parameter(description = "医疗服务信息", required = true) @RequestBody MedicalService medicalService) {
        try {
            if (medicalServiceService.addMedicalService(medicalService)) {
                return Result.success("新增成功", null);
            } else {
                return Result.error("服务编码已存在");
            }
        } catch (Exception e) {
            return Result.error("新增失败: " + e.getMessage());
        }
    }

    /**
     * 更新医疗服务
     */
    
    @Operation(summary = "更新医疗服务", description = "更新指定医疗服务的信息")
    @PutMapping("/update")
    public Result<String> updateMedicalService(@Parameter(description = "医疗服务信息", required = true) @RequestBody MedicalService medicalService) {
        try {
            if (medicalServiceService.updateMedicalService(medicalService)) {
                return Result.success("更新成功", null);
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除医疗服务
     */
    
    @Operation(summary = "删除医疗服务", description = "根据ID删除指定医疗服务")
    @DeleteMapping("/{id}")
    public Result<String> deleteMedicalService(@Parameter(description = "医疗服务ID", required = true, example = "1") @PathVariable Long id) {
        try {
            if (medicalServiceService.deleteMedicalService(id)) {
                return Result.success("删除成功", null);
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除医疗服务
     */
    
    @Operation(summary = "批量删除医疗服务", description = "批量删除多个医疗服务")
    @DeleteMapping("/batch")
    public Result<String> batchDeleteMedicalService(@Parameter(description = "医疗服务ID列表", required = true) @RequestBody List<Long> ids) {
        try {
            if (medicalServiceService.batchDeleteMedicalService(ids)) {
                return Result.success("批量删除成功", null);
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据服务类型查询医疗服务列表
     */
    
    @Operation(summary = "根据服务类型查询医疗服务列表", description = "根据服务类型查询医疗服务")
    @GetMapping("/byType/{serviceType}")
    public Result<List<MedicalService>> getMedicalServicesByType(@Parameter(description = "服务类型", required = true, example = "诊疗项目") @PathVariable String serviceType) {
        try {
            List<MedicalService> services = medicalServiceService.getMedicalServicesByType(serviceType);
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据类别查询医疗服务列表
     */
    
    @Operation(summary = "根据类别查询医疗服务列表", description = "根据服务类别查询医疗服务")
    @GetMapping("/byCategory/{category}")
    public Result<List<MedicalService>> getMedicalServicesByCategory(@Parameter(description = "服务类别", required = true, example = "内科") @PathVariable String category) {
        try {
            List<MedicalService> services = medicalServiceService.getMedicalServicesByCategory(category);
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据科室查询医疗服务列表
     */
    
    @Operation(summary = "根据科室查询医疗服务列表", description = "根据科室查询医疗服务")
    @GetMapping("/byDepartment/{department}")
    public Result<List<MedicalService>> getMedicalServicesByDepartment(@Parameter(description = "科室名称", required = true, example = "内科") @PathVariable String department) {
        try {
            List<MedicalService> services = medicalServiceService.getMedicalServicesByDepartment(department);
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据医保分类查询医疗服务列表
     */
    
    @Operation(summary = "根据医保分类查询医疗服务列表", description = "根据医保分类查询医疗服务")
    @GetMapping("/byInsuranceCategory/{insuranceCategory}")
    public Result<List<MedicalService>> getMedicalServicesByInsuranceCategory(@Parameter(description = "医保分类", required = true, example = "甲类") @PathVariable String insuranceCategory) {
        try {
            List<MedicalService> services = medicalServiceService.getMedicalServicesByInsuranceCategory(insuranceCategory);
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询可报销的医疗服务
     */
    
    @Operation(summary = "查询可报销的医疗服务", description = "查询所有可报销的医疗服务")
    @GetMapping("/reimbursable")
    public Result<List<MedicalService>> getReimbursableServices() {
        try {
            List<MedicalService> services = medicalServiceService.getReimbursableServices();
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询不可报销的医疗服务
     */
    
    @Operation(summary = "查询不可报销的医疗服务", description = "查询所有不可报销的医疗服务")
    @GetMapping("/nonReimbursable")
    public Result<List<MedicalService>> getNonReimbursableServices() {
        try {
            List<MedicalService> services = medicalServiceService.getNonReimbursableServices();
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用医疗服务
     */
    
    @Operation(summary = "启用/禁用医疗服务", description = "启用或禁用指定医疗服务")
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(
            @Parameter(description = "医疗服务ID", required = true, example = "1") @RequestParam Long id,
            @Parameter(description = "状态", required = true, example = "1") @RequestParam Integer status) {
        try {
            if (medicalServiceService.updateStatus(id, status)) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有服务类型
     */
    
    @Operation(summary = "获取所有服务类型", description = "获取系统中所有可用的服务类型")
    @GetMapping("/serviceTypes")
    public Result<List<String>> getAllServiceTypes() {
        try {
            List<String> serviceTypes = medicalServiceService.getAllServiceTypes();
            return Result.success("查询成功", serviceTypes);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有服务类别
     */
    
    @Operation(summary = "获取所有服务类别", description = "获取系统中所有可用的服务类别")
    @GetMapping("/categories")
    public Result<List<String>> getAllCategories() {
        try {
            List<String> categories = medicalServiceService.getAllCategories();
            return Result.success("查询成功", categories);
        } catch (Exception e) {
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
            List<String> departments = medicalServiceService.getAllDepartments();
            return Result.success("查询成功", departments);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有医保分类
     */
    
    @Operation(summary = "获取所有医保分类", description = "获取系统中所有可用的医保分类")
    @GetMapping("/insuranceCategories")
    public Result<List<String>> getAllInsuranceCategories() {
        try {
            List<String> insuranceCategories = medicalServiceService.getAllInsuranceCategories();
            return Result.success("查询成功", insuranceCategories);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 检查服务编码是否存在
     */
    
    @Operation(summary = "检查服务编码是否存在", description = "检查指定服务编码是否已被使用")
    @GetMapping("/checkServiceCode")
    public Result<Boolean> checkServiceCode(@Parameter(description = "服务编码", required = true, example = "MS001") @RequestParam String serviceCode) {
        try {
            boolean exists = medicalServiceService.isServiceCodeExists(serviceCode);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 检查国家编码是否存在
     */
    
    @Operation(summary = "检查国家编码是否存在", description = "检查指定国家编码是否已被使用")
    @GetMapping("/checkNationalCode")
    public Result<Boolean> checkNationalCode(@Parameter(description = "国家编码", required = true, example = "NC001") @RequestParam String nationalCode) {
        try {
            boolean exists = medicalServiceService.isNationalCodeExists(nationalCode);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }

    /**
     * 根据价格范围查询医疗服务
     */
    
    @Operation(summary = "根据价格范围查询医疗服务", description = "根据价格范围查询医疗服务")
    @GetMapping("/byPriceRange")
    public Result<List<MedicalService>> getMedicalServicesByPriceRange(
            @Parameter(description = "最小价格", required = true, example = "10.0") @RequestParam Double minPrice,
            @Parameter(description = "最大价格", required = true, example = "100.0") @RequestParam Double maxPrice) {
        try {
            List<MedicalService> services = medicalServiceService.getMedicalServicesByPriceRange(minPrice, maxPrice);
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据报销比例查询医疗服务
     */
    
    @Operation(summary = "根据报销比例查询医疗服务", description = "根据报销比例范围查询医疗服务")
    @GetMapping("/byReimbursementRatio")
    public Result<List<MedicalService>> getMedicalServicesByReimbursementRatio(
            @Parameter(description = "最小报销比例", required = true, example = "0.5") @RequestParam Double minRatio,
            @Parameter(description = "最大报销比例", required = true, example = "0.8") @RequestParam Double maxRatio) {
        try {
            List<MedicalService> services = medicalServiceService.getMedicalServicesByReimbursementRatio(minRatio, maxRatio);
            return Result.success("查询成功", services);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
} 