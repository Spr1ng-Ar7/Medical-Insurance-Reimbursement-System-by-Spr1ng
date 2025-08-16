package org.neu.insurance.controller;

import lombok.extern.slf4j.Slf4j;
import org.neu.insurance.dto.ApiResponse;
import org.neu.insurance.entity.Drug;
import org.neu.insurance.util.ExcelImportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Excel导入测试控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class ExcelImportController {
    
    @Autowired
    private ExcelImportUtil excelImportUtil;
    
    /**
     * 通过文件上传导入Excel数据
     */
    @PostMapping("/upload")
    public ApiResponse<String> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error("请选择要上传的文件");
        }
        
        try {
            log.info("开始处理上传的Excel文件: {}", file.getOriginalFilename());
            
            // 使用流方式导入
            List<Drug> drugList = excelImportUtil.importDrugData(
                file.getInputStream(), 
                file.getOriginalFilename()
            );
            
            // TODO: 这里可以调用Service保存到数据库
            log.info("成功导入 {} 条药品数据", drugList.size());
            
            // 打印前几条数据作为示例
            for (int i = 0; i < Math.min(drugList.size(), 5); i++) {
                Drug drug = drugList.get(i);
                log.info("药品 {}: {} - {} - {}", 
                    i + 1, drug.getDrugCode(), drug.getDrugName(), drug.getPrice());
            }
            
            return ApiResponse.success("文件上传成功，共导入 " + drugList.size() + " 条数据");
            
        } catch (IOException e) {
            log.error("文件处理失败: {}", e.getMessage(), e);
            return ApiResponse.error("文件处理失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("数据导入失败: {}", e.getMessage(), e);
            return ApiResponse.error("数据导入失败: " + e.getMessage());
        }
    }
    
    /**
     * 通过指定文件路径导入Excel数据
     */
    @PostMapping("/import")
    public ApiResponse<String> importFromPath(@RequestParam String filePath) {
        try {
            log.info("开始从指定路径导入Excel文件: {}", filePath);
            
            // 检查文件是否存在
            File file = new File(filePath);
            if (!file.exists()) {
                return ApiResponse.error("文件不存在: " + filePath);
            }
            
            // 导入数据
            List<Drug> drugList = excelImportUtil.importDrugData(filePath);
            
            // TODO: 这里可以调用Service保存到数据库
            log.info("成功导入 {} 条药品数据", drugList.size());
            
            return ApiResponse.success("数据导入成功，共导入 " + drugList.size() + " 条数据");
            
        } catch (Exception e) {
            log.error("数据导入失败: {}", e.getMessage(), e);
            return ApiResponse.error("数据导入失败: " + e.getMessage());
        }
    }
    
    /**
     * 批量导入Excel数据（适用于大文件）
     */
    @PostMapping("/batch-import")
    public ApiResponse<String> batchImport(
            @RequestParam String filePath,
            @RequestParam(defaultValue = "1000") int batchSize) {
        try {
            log.info("开始批量导入Excel文件: {}, 批处理大小: {}", filePath, batchSize);
            
            // 检查文件是否存在
            File file = new File(filePath);
            if (!file.exists()) {
                return ApiResponse.error("文件不存在: " + filePath);
            }
            
            // 批量导入数据
            int totalCount = excelImportUtil.importAndSaveDrugData(filePath, batchSize);
            
            return ApiResponse.success("批量导入成功，共处理 " + totalCount + " 条数据");
            
        } catch (Exception e) {
            log.error("批量导入失败: {}", e.getMessage(), e);
            return ApiResponse.error("批量导入失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试方法 - 创建示例Excel文件
     */
    @GetMapping("/create-sample")
    public ApiResponse<String> createSampleExcel() {
        try {
            String samplePath = createSampleExcelFile();
            return ApiResponse.success("示例Excel文件已创建: " + samplePath);
        } catch (Exception e) {
            log.error("创建示例文件失败: {}", e.getMessage(), e);
            return ApiResponse.error("创建示例文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 创建示例Excel文件
     */
    private String createSampleExcelFile() {
        // TODO: 可以使用EasyExcel创建示例文件
        String message = "示例Excel文件格式说明：\n" +
                "列1: 药品编码 (如: D001)\n" +
                "列2: 药品名称 (如: 阿莫西林胶囊)\n" +
                "列3: 药品类型 (甲类/乙类/丙类)\n" +
                "列4: 规格 (如: 0.25g*24粒)\n" +
                "列5: 单位 (如: 盒)\n" +
                "列6: 价格 (如: 15.80)\n" +
                "列7: 自付比例 (如: 0.1)\n" +
                "列8: 生产厂家 (如: XX制药有限公司)\n" +
                "列9: 备注";
        
        log.info("Excel模板格式:\n{}", message);
        return "请根据日志中的格式创建Excel文件";
    }
} 