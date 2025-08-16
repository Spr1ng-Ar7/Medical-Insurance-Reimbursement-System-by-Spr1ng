package org.neu.insurance.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.neu.insurance.entity.ExcelDrugData;
import org.neu.insurance.entity.Drug;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入工具类
 * 使用EasyExcel实现Excel文件数据导入
 */
@Slf4j
@Component
public class ExcelImportUtil {
    
    /**
     * 导入Excel文件中的药品数据
     * 
     * @param filePath Excel文件路径
     * @return 导入的药品数据列表
     */
    public List<Drug> importDrugData(String filePath) {
        log.info("开始导入Excel文件: {}", filePath);
        
        List<Drug> drugList = new ArrayList<>();
        
        try {
            // 创建监听器
            DrugDataListener listener = new DrugDataListener(drugList);
            
            // 读取Excel文件
            EasyExcel.read(filePath, ExcelDrugData.class, listener)
                    .sheet()
                    .headRowNumber(1) // 指定第1行为表头
                    .doRead();
                    
            log.info("Excel导入完成，共导入 {} 条药品数据", drugList.size());
            
        } catch (Exception e) {
            log.error("Excel文件导入失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel文件导入失败: " + e.getMessage());
        }
        
        return drugList;
    }
    
    /**
     * 导入Excel文件中的药品数据（使用InputStream）
     * 
     * @param inputStream Excel文件输入流
     * @param fileName 文件名（用于日志）
     * @return 导入的药品数据列表
     */
    public List<Drug> importDrugData(InputStream inputStream, String fileName) {
        log.info("开始导入Excel流文件: {}", fileName);
        
        List<Drug> drugList = new ArrayList<>();
        
        try {
            // 创建监听器
            DrugDataListener listener = new DrugDataListener(drugList);
            
            // 读取Excel文件流
            EasyExcel.read(inputStream, ExcelDrugData.class, listener)
                    .sheet()
                    .headRowNumber(1) // 指定第1行为表头
                    .doRead();
                    
            log.info("Excel流导入完成，共导入 {} 条药品数据", drugList.size());
            
        } catch (Exception e) {
            log.error("Excel流文件导入失败: {}", e.getMessage(), e);
            throw new RuntimeException("Excel流文件导入失败: " + e.getMessage());
        }
        
        return drugList;
    }
    
    /**
     * 批量导入并保存到数据库
     * 
     * @param filePath Excel文件路径
     * @param batchSize 批处理大小
     * @return 导入成功的数据条数
     */
    public int importAndSaveDrugData(String filePath, int batchSize) {
        log.info("开始批量导入Excel文件: {}, 批处理大小: {}", filePath, batchSize);
        
        final int[] totalCount = {0};
        
        try {
            // 创建批量处理监听器
            BatchDrugDataListener listener = new BatchDrugDataListener(batchSize) {
                @Override
                protected void saveBatch(List<Drug> dataList) {
                    // TODO: 这里应该调用Service层保存到数据库
                    log.info("批量保存 {} 条药品数据到数据库", dataList.size());
                    
                    // 模拟数据库保存操作
                    for (Drug drug : dataList) {
                        log.debug("保存药品: {} - {}", drug.getDrugCode(), drug.getDrugName());
                    }
                    
                    totalCount[0] += dataList.size();
                }
            };
            
            // 读取Excel文件
            EasyExcel.read(filePath, ExcelDrugData.class, listener)
                    .sheet()
                    .headRowNumber(1)
                    .doRead();
                    
            log.info("批量导入完成，共处理 {} 条药品数据", totalCount[0]);
            
        } catch (Exception e) {
            log.error("批量导入失败: {}", e.getMessage(), e);
            throw new RuntimeException("批量导入失败: " + e.getMessage());
        }
        
        return totalCount[0];
    }
    
    /**
     * 药品数据读取监听器
     */
    private static class DrugDataListener implements ReadListener<ExcelDrugData> {
        
        private final List<Drug> drugList;
        
        public DrugDataListener(List<Drug> drugList) {
            this.drugList = drugList;
        }
        
        @Override
        public void invoke(ExcelDrugData data, AnalysisContext context) {
            log.debug("解析到一条药品数据: {}", data);
            
            // 数据校验
            if (isValidData(data)) {
                // 转换为Drug实体并添加到列表
                Drug drug = data.toDrug();
                drugList.add(drug);
            } else {
                log.warn("第{}行数据校验失败，跳过: {}", context.readRowHolder().getRowIndex() + 1, data);
            }
        }
        
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            log.info("所有数据解析完成");
        }
        
        /**
         * 数据校验
         */
        private boolean isValidData(ExcelDrugData data) {
            return data.getDrugCode() != null && !data.getDrugCode().trim().isEmpty() &&
                   data.getDrugName() != null && !data.getDrugName().trim().isEmpty();
        }
    }
    
    /**
     * 批量处理数据监听器
     */
    private abstract static class BatchDrugDataListener implements ReadListener<ExcelDrugData> {
        
        private final int batchSize;
        private final List<Drug> cachedDataList;
        
        public BatchDrugDataListener(int batchSize) {
            this.batchSize = batchSize;
            this.cachedDataList = new ArrayList<>();
        }
        
        @Override
        public void invoke(ExcelDrugData data, AnalysisContext context) {
            log.debug("解析到一条药品数据: {}", data);
            
            // 数据校验
            if (isValidData(data)) {
                Drug drug = data.toDrug();
                cachedDataList.add(drug);
                
                // 达到批处理大小时保存
                if (cachedDataList.size() >= batchSize) {
                    saveBatch(new ArrayList<>(cachedDataList));
                    cachedDataList.clear();
                }
            } else {
                log.warn("第{}行数据校验失败，跳过: {}", context.readRowHolder().getRowIndex() + 1, data);
            }
        }
        
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            // 保存剩余数据
            if (!cachedDataList.isEmpty()) {
                saveBatch(new ArrayList<>(cachedDataList));
                cachedDataList.clear();
            }
            log.info("所有数据解析完成");
        }
        
        /**
         * 批量保存数据（抽象方法，由子类实现）
         */
        protected abstract void saveBatch(List<Drug> dataList);
        
        /**
         * 数据校验
         */
        private boolean isValidData(ExcelDrugData data) {
            return data.getDrugCode() != null && !data.getDrugCode().trim().isEmpty() &&
                   data.getDrugName() != null && !data.getDrugName().trim().isEmpty();
        }
    }
} 