package org.neu.insurance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.neu.insurance.dto.Result;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.Patient;
import org.neu.insurance.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 患者管理控制器
 */
@Tag(name = "患者管理", description = "患者信息管理相关接口")
@RestController
@RequestMapping("/api/patient")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    /**
     * 分页查询患者列表
     */
    
    @Operation(summary = "分页查询患者列表", description = "根据条件分页查询患者信息")
    @GetMapping("/list")
    public Result<PageResponse<Patient>> getPatientList(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "患者姓名") @RequestParam(required = false) String name,
            @Parameter(description = "身份证号") @RequestParam(required = false) String idCard,
            @Parameter(description = "医保类型") @RequestParam(required = false) String insuranceType,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageNum(pageNum);
            pageRequest.setPageSize(pageSize);
            
            PageResponse<Patient> response = patientService.getPatientList(pageRequest, name, idCard, insuranceType, keyword);
            return Result.success("查询成功", response);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据ID获取患者详情
     */
    
    @Operation(summary = "根据ID获取患者详情", description = "根据患者ID获取详细信息")
    @GetMapping("/{id}")
    public Result<Patient> getPatientById(@Parameter(description = "患者ID", required = true, example = "1") @PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient != null) {
                return Result.success("查询成功", patient);
            } else {
                return Result.error("患者不存在");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 新增患者
     */
    @Operation(summary = "新增患者", description = "添加新的患者信息")
    @PostMapping
    public Result<String> addPatient(@Parameter(description = "患者信息", required = true) @RequestBody Patient patient) {
        try {
            if (patientService.addPatient(patient)) {
                return Result.success("新增成功");
            } else {
                return Result.error("身份证号或医保卡号已存在");
            }
        } catch (Exception e) {
            return Result.error("新增失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新患者信息
     */
    @Operation(summary = "更新患者信息", description = "更新指定患者的信息")
    @PutMapping
    public Result<String> updatePatient(@Parameter(description = "患者信息", required = true) @RequestBody Patient patient) {
        try {
            if (patientService.updatePatient(patient)) {
                return Result.success("更新成功");
            } else {
                return Result.error("身份证号或医保卡号已被其他患者使用");
            }
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除患者
     */
    
    @Operation(summary = "删除患者", description = "根据ID删除指定患者")
    @DeleteMapping("/{id}")
    public Result<String> deletePatient(@Parameter(description = "患者ID", required = true, example = "1") @PathVariable Long id) {
        try {
            if (patientService.deletePatient(id)) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量删除患者
     */
    
    @Operation(summary = "批量删除患者", description = "批量删除多个患者")
    @DeleteMapping("/batch")
    public Result<String> batchDeletePatient(@Parameter(description = "患者ID列表", required = true) @RequestBody List<Long> ids) {
        try {
            if (patientService.batchDeletePatient(ids)) {
                return Result.success("批量删除成功");
            } else {
                return Result.error("批量删除失败");
            }
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据身份证号查询患者信息
     */
    
    @Operation(summary = "根据身份证号查询患者", description = "根据身份证号获取患者详细信息")
    @GetMapping("/byIdCard/{idCard}")
    public Result<Patient> getPatientByIdCard(@Parameter(description = "身份证号", required = true, example = "110101199001011234") @PathVariable String idCard) {
        try {
            Patient patient = patientService.getPatientByIdCard(idCard);
            if (patient != null) {
                return Result.success("查询成功", patient);
            } else {
                return Result.error("患者不存在");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据医保卡号查询患者信息
     */
    
    @Operation(summary = "根据医保卡号查询患者", description = "根据医保卡号获取患者详细信息")
    @GetMapping("/byInsuranceCard/{insuranceCard}")
    public Result<Patient> getPatientByInsuranceCard(@Parameter(description = "医保卡号", required = true, example = "IC001") @PathVariable String insuranceCard) {
        try {
            Patient patient = patientService.getPatientByInsuranceCard(insuranceCard);
            if (patient != null) {
                return Result.success("查询成功", patient);
            } else {
                return Result.error("患者不存在");
            }
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据姓名查询患者列表
     */
    
    @Operation(summary = "根据姓名查询患者列表", description = "根据姓名模糊查询患者列表")
    @GetMapping("/byName/{name}")
    public Result<List<Patient>> getPatientsByName(@Parameter(description = "患者姓名", required = true, example = "张三") @PathVariable String name) {
        try {
            List<Patient> patients = patientService.getPatientsByName(name);
            return Result.success("查询成功", patients);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据医保类型查询患者列表
     */
    
    @Operation(summary = "根据医保类型查询患者列表", description = "根据医保类型查询患者列表")
    @GetMapping("/byInsuranceType/{insuranceType}")
    public Result<List<Patient>> getPatientsByInsuranceType(@Parameter(description = "医保类型", required = true, example = "城镇职工") @PathVariable String insuranceType) {
        try {
            List<Patient> patients = patientService.getPatientsByInsuranceType(insuranceType);
            return Result.success("查询成功", patients);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据年龄段查询患者列表
     */
    
    @Operation(summary = "根据年龄段查询患者列表", description = "根据年龄范围查询患者列表")
    @GetMapping("/byAgeRange")
    public Result<List<Patient>> getPatientsByAgeRange(
            @Parameter(description = "最小年龄", required = true, example = "18") @RequestParam Integer minAge,
            @Parameter(description = "最大年龄", required = true, example = "65") @RequestParam Integer maxAge) {
        try {
            List<Patient> patients = patientService.getPatientsByAgeRange(minAge, maxAge);
            return Result.success("查询成功", patients);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据性别查询患者列表
     */
    
    @Operation(summary = "根据性别查询患者列表", description = "根据性别查询患者列表")
    @GetMapping("/byGender/{gender}")
    public Result<List<Patient>> getPatientsByGender(@Parameter(description = "性别", required = true, example = "男") @PathVariable String gender) {
        try {
            List<Patient> patients = patientService.getPatientsByGender(gender);
            return Result.success("查询成功", patients);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 根据地区查询患者列表
     */
    
    @Operation(summary = "根据地区查询患者列表", description = "根据地区查询患者列表")
    @GetMapping("/byRegion/{region}")
    public Result<List<Patient>> getPatientsByRegion(@Parameter(description = "地区", required = true, example = "北京市") @PathVariable String region) {
        try {
            List<Patient> patients = patientService.getPatientsByRegion(region);
            return Result.success("查询成功", patients);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 启用/禁用患者
     */
    
    @Operation(summary = "启用/禁用患者", description = "根据ID启用或禁用患者")
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(
            @Parameter(description = "患者ID", required = true, example = "1") @RequestParam Long id,
            @Parameter(description = "状态", required = true, example = "1") @RequestParam Integer status) {
        try {
            if (patientService.updateStatus(id, status)) {
                return Result.success("状态更新成功");
            } else {
                return Result.error("状态更新失败");
            }
        } catch (Exception e) {
            return Result.error("状态更新失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有医保类型
     */
    
    @Operation(summary = "获取所有医保类型", description = "获取系统中所有可用的医保类型")
    @GetMapping("/insuranceTypes")
    public Result<List<String>> getAllInsuranceTypes() {
        try {
            List<String> insuranceTypes = patientService.getAllInsuranceTypes();
            return Result.success("查询成功", insuranceTypes);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有地区
     */
    
    @Operation(summary = "获取所有地区", description = "获取系统中所有可用的地区")
    @GetMapping("/regions")
    public Result<List<String>> getAllRegions() {
        try {
            List<String> regions = patientService.getAllRegions();
            return Result.success("查询成功", regions);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查身份证号是否存在
     */
    
    @Operation(summary = "检查身份证号是否存在", description = "检查指定身份证号是否已被使用")
    @GetMapping("/checkIdCard")
    public Result<Boolean> checkIdCard(@Parameter(description = "身份证号", required = true, example = "110101199001011234") @RequestParam String idCard) {
        try {
            boolean exists = patientService.isIdCardExists(idCard);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查医保卡号是否存在
     */
    
    @Operation(summary = "检查医保卡号是否存在", description = "检查指定医保卡号是否已被使用")
    @GetMapping("/checkInsuranceCard")
    public Result<Boolean> checkInsuranceCard(@Parameter(description = "医保卡号", required = true, example = "IC001") @RequestParam String insuranceCard) {
        try {
            boolean exists = patientService.isInsuranceCardExists(insuranceCard);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 检查手机号是否存在
     */
    
    @Operation(summary = "检查手机号是否存在", description = "检查指定手机号是否已被使用")
    @GetMapping("/checkPhone")
    public Result<Boolean> checkPhone(@Parameter(description = "手机号", required = true, example = "13800138000") @RequestParam String phone) {
        try {
            boolean exists = patientService.isPhoneExists(phone);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error("检查失败: " + e.getMessage());
        }
    }
    
    /**
     * 搜索患者
     */
    
    @Operation(summary = "搜索患者", description = "根据关键词模糊搜索患者信息")
    @GetMapping("/search")
    public Result<List<Patient>> searchPatients(@Parameter(description = "搜索关键词", required = true, example = "张三") @RequestParam String keyword) {
        try {
            List<Patient> patients = patientService.searchPatients(keyword);
            return Result.success("搜索成功", patients);
        } catch (Exception e) {
            return Result.error("搜索失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取患者统计信息
     */
    
    @Operation(summary = "获取患者统计信息", description = "获取患者相关的统计数据")
    @GetMapping("/statistics")
    public Result<Object> getPatientStatistics() {
        try {
            Object statistics = patientService.getPatientStatistics();
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }
} 