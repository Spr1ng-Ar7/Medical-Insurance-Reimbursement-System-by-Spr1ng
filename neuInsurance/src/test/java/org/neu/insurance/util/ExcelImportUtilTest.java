package org.neu.insurance.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.neu.insurance.entity.Drug;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * Excel导入工具类测试
 */
@Slf4j
@SpringBootTest
public class ExcelImportUtilTest {
    
    @Resource
    private ExcelImportUtil excelImportUtil;
    
    /**
     * 测试从文件路径导入Excel数据
     */
    @Test
    public void testImportDrugDataFromFile() {
        // 测试文件路径（需要创建实际的Excel文件）
        String filePath = "C:/test/drug_data.xlsx";
        
        try {
            log.info("开始测试Excel文件导入: {}", filePath);
            
            List<Drug> drugList = excelImportUtil.importDrugData(filePath);
            
            log.info("导入测试完成，共读取到 {} 条药品数据", drugList.size());
            
            // 打印前5条数据
            for (int i = 0; i < Math.min(drugList.size(), 5); i++) {
                Drug drug = drugList.get(i);
                log.info("药品 {}: 编码={}, 名称={}, 类型={}, 价格={}", 
                    i + 1, drug.getDrugCode(), drug.getDrugName(), 
                    drug.getDrugType(), drug.getPrice());
            }
            
        } catch (Exception e) {
            log.error("测试失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 测试批量导入功能
     */
    @Test
    public void testBatchImport() {
        String filePath = "C:/test/drug_data.xlsx";
        int batchSize = 10;
        
        try {
            log.info("开始测试批量导入: 文件={}, 批处理大小={}", filePath, batchSize);
            
            int totalCount = excelImportUtil.importAndSaveDrugData(filePath, batchSize);
            
            log.info("批量导入测试完成，共处理 {} 条数据", totalCount);
            
        } catch (Exception e) {
            log.error("批量导入测试失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 打印Excel文件格式说明
     */
    @Test
    public void printExcelFormat() {
        log.info("Excel文件格式说明：");
        log.info("第1列: 药品编码 (必填) - 例如: D001, D002");
        log.info("第2列: 药品名称 (必填) - 例如: 阿莫西林胶囊");
        log.info("第3列: 药品类型 - 例如: 甲类, 乙类, 丙类");
        log.info("第4列: 规格 - 例如: 0.25g*24粒");
        log.info("第5列: 单位 - 例如: 盒, 瓶, 支");
        log.info("第6列: 价格 - 例如: 15.80");
        log.info("第7列: 自付比例 - 例如: 0.1 (表示10%)");
        log.info("第8列: 生产厂家 - 例如: XX制药有限公司");
        log.info("第9列: 备注 - 可选填写");
        log.info("注意：第1行为表头，从第2行开始填写数据");
    }
} 