package org.neu.insurance.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.Drug;
import org.neu.insurance.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.neu.insurance.mapper.DrugMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 药品服务实现类
 */
@Service
@Slf4j
public class DrugServiceImpl implements DrugService {
    
    @Autowired
    private DrugMapper drugMapper;
    
    @Override
    public PageResponse<Drug> getDrugList(String drugName, String drugType, Integer status, PageRequest pageRequest) {
        try {
            validatePageRequest(pageRequest);
            List<Drug> drugs = drugMapper.getDrugList(pageRequest.getOffset(), pageRequest.getPageSize(), null, drugName, drugType, null, null);
            int total = drugMapper.countDrugs(null, drugName, drugType, null, null);
            
            PageResponse<Drug> response = new PageResponse<>();
            response.setRecords(drugs);
            response.setTotal((long) total);
            response.setCurrent(pageRequest.getPageNum());
            response.setSize(pageRequest.getPageSize());
            return response;
        }catch(Exception e){
            throw new RuntimeException("获取药品列表失败", e);
        }
    }
    
    @Override
    public Drug getDrugById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("药品ID不能为空");
        }
        return drugMapper.getDrugById(id);
    }
    
    @Override
    public void addDrug(Drug drug) {
        if (drug == null) {
            throw new IllegalArgumentException("药品信息不能为空");
        }
        
        // 验证必填字段
        if (drug.getDrugCode() == null || drug.getDrugCode().trim().isEmpty()) {
            throw new IllegalArgumentException("药品编码不能为空");
        }
        if (drug.getDrugName() == null || drug.getDrugName().trim().isEmpty()) {
            throw new IllegalArgumentException("药品名称不能为空");
        }
        if (drug.getDrugType() == null || drug.getDrugType().trim().isEmpty()) {
            throw new IllegalArgumentException("药品类型不能为空");
        }
        if (drug.getManufacturer() == null || drug.getManufacturer().trim().isEmpty()) {
            throw new IllegalArgumentException("生产厂家不能为空");
        }
        
        // 检查药品编码是否已存在
        if (drugMapper.isDrugCodeExists(drug.getDrugCode())) {
            throw new RuntimeException("药品编码已存在: " + drug.getDrugCode());
        }
        
        // 设置默认值
        LocalDateTime now = LocalDateTime.now();
        drug.setCreateTime(now);
        drug.setUpdateTime(now);
        
        // 如果状态为空，设置默认值为1（正常）
        if (drug.getStatus() == null) {
            drug.setStatus(1);
        }
        
        // 如果价格为空，设置默认值为0
        if (drug.getPrice() == null) {
            drug.setPrice(BigDecimal.ZERO);
        }
        
        // 如果自付比例为空，设置默认值为0
        if (drug.getSelfPayRatio() == null) {
            drug.setSelfPayRatio(BigDecimal.ZERO);
        }
        
        int result = drugMapper.addDrug(drug);
        if (result <= 0) {
            throw new RuntimeException("新增药品失败");
        }
    }
    
    @Override
    public void updateDrug(Drug drug) {
        if (drug == null) {
            throw new IllegalArgumentException("药品信息不能为空");
        }
        if (drug.getId() == null) {
            throw new IllegalArgumentException("药品ID不能为空");
        }
        
        // 检查药品是否存在
        Drug existingDrug = drugMapper.getDrugById(drug.getId());
        if (existingDrug == null) {
            throw new IllegalArgumentException("药品不存在，ID: " + drug.getId());
        }
        
        // 如果更新了药品编码，检查新编码是否与其他药品冲突
        if (drug.getDrugCode() != null && !drug.getDrugCode().equals(existingDrug.getDrugCode())) {
            if (drugMapper.isDrugCodeExistsExcludeId(drug.getDrugCode(), drug.getId())) {
                throw new RuntimeException("药品编码已存在: " + drug.getDrugCode());
            }
        }
        
        // 验证必填字段（如果提供了值）
        if (drug.getDrugCode() != null && drug.getDrugCode().trim().isEmpty()) {
            throw new IllegalArgumentException("药品编码不能为空");
        }
        if (drug.getDrugName() != null && drug.getDrugName().trim().isEmpty()) {
            throw new IllegalArgumentException("药品名称不能为空");
        }
        if (drug.getDrugType() != null && drug.getDrugType().trim().isEmpty()) {
            throw new IllegalArgumentException("药品类型不能为空");
        }
        if (drug.getManufacturer() != null && drug.getManufacturer().trim().isEmpty()) {
            throw new IllegalArgumentException("生产厂家不能为空");
        }
        
        // 设置更新时间
        drug.setUpdateTime(LocalDateTime.now());
        
        // 如果价格或自付比例为负数，设置为0
        if (drug.getPrice() != null && drug.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            drug.setPrice(BigDecimal.ZERO);
        }
        if (drug.getSelfPayRatio() != null && drug.getSelfPayRatio().compareTo(BigDecimal.ZERO) < 0) {
            drug.setSelfPayRatio(BigDecimal.ZERO);
        }
        
        int result = drugMapper.updateDrug(drug);
        if (result <= 0) {
            throw new RuntimeException("更新药品失败，可能是数据库操作异常");
        }
    }
    
    @Override
    public void deleteDrug(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("药品ID不能为空");
        }
        
        // 直接更新状态为0，不检查是否存在（因为可能已经被软删除了）
        Drug drugToDelete = new Drug();
        drugToDelete.setId(id);
        drugToDelete.setStatus(0);
        drugToDelete.setUpdateTime(LocalDateTime.now());
        
        int result = drugMapper.updateDrug(drugToDelete);
        if (result <= 0) {
            // 如果更新失败，可能是记录不存在，返回成功（幂等性）
            log.warn("删除药品失败，可能是记录不存在，ID: {}", id);
        } else {
            log.info("药品删除成功，ID: {}", id);
        }
    }


    @Override
    public Drug getDrugByCode(String drugCode) {
        if (drugCode == null || drugCode.trim().isEmpty()) {
            throw new IllegalArgumentException("药品编码不能为空");
        }
        return drugMapper.getDrugByCode(drugCode);
    }
    private void validatePageRequest(PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new IllegalArgumentException("分页参数不能为空");
        }
        if (pageRequest.getPageNum() == null || pageRequest.getPageNum() < 1) {
            pageRequest.setPageNum(1);
        }
        if (pageRequest.getPageSize() == null || pageRequest.getPageSize() < 1) {
            pageRequest.setPageSize(10);
        }
    }
    
    @Override
    public List<Drug> getAllDrugs() {
        return drugMapper.getAllDrugs();
    }
    
    @Override
    public void batchAddDrugs(List<Drug> drugs) {
        if (drugs == null || drugs.isEmpty()) {
            throw new IllegalArgumentException("药品列表不能为空");
        }
        
        // 批量验证
        for (Drug drug : drugs) {
            // 验证必填字段
            if (drug.getDrugCode() == null || drug.getDrugCode().trim().isEmpty()) {
                throw new IllegalArgumentException("药品编码不能为空: " + drug.getDrugName());
            }
            if (drug.getDrugName() == null || drug.getDrugName().trim().isEmpty()) {
                throw new IllegalArgumentException("药品名称不能为空");
            }
            if (drug.getDrugType() == null || drug.getDrugType().trim().isEmpty()) {
                throw new IllegalArgumentException("药品类型不能为空: " + drug.getDrugName());
            }
            if (drug.getManufacturer() == null || drug.getManufacturer().trim().isEmpty()) {
                throw new IllegalArgumentException("生产厂家不能为空: " + drug.getDrugName());
            }
            
            // 检查编码重复
            if (drugMapper.isDrugCodeExists(drug.getDrugCode())) {
                throw new RuntimeException("药品编码已存在: " + drug.getDrugCode());
            }
        }
        
        // 批量新增
        for (Drug drug : drugs) {
            // 设置默认值
            LocalDateTime now = LocalDateTime.now();
            drug.setCreateTime(now);
            drug.setUpdateTime(now);
            
            if (drug.getStatus() == null) {
                drug.setStatus(1);
            }
            if (drug.getPrice() == null) {
                drug.setPrice(BigDecimal.ZERO);
            }
            if (drug.getSelfPayRatio() == null) {
                drug.setSelfPayRatio(BigDecimal.ZERO);
            }
            
            int result = drugMapper.addDrug(drug);
            if (result <= 0) {
                throw new RuntimeException("批量新增药品失败: " + drug.getDrugName());
            }
        }
    }
    
    @Override
    public Map<String, Object> getDrugTypeStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            List<Drug> allDrugs = getAllDrugs();
            
            // 按药品类型分组统计
            Map<String, Long> typeCount = allDrugs.stream()
                    .filter(drug -> drug.getDrugType() != null)
                    .collect(Collectors.groupingBy(
                            Drug::getDrugType,
                            Collectors.counting()
                    ));
            
            // 计算各类型占比
            long total = allDrugs.size();
            Map<String, Double> typePercentage = new HashMap<>();
            for (Map.Entry<String, Long> entry : typeCount.entrySet()) {
                double percentage = total > 0 ? (double) entry.getValue() / total * 100 : 0;
                typePercentage.put(entry.getKey(), Math.round(percentage * 100.0) / 100.0);
            }
            
            statistics.put("totalDrugs", total);
            statistics.put("typeCount", typeCount);
            statistics.put("typePercentage", typePercentage);
            statistics.put("typeList", typeCount.keySet());
            
        } catch (Exception e) {
            throw new RuntimeException("获取药品类型统计失败", e);
        }
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getDrugStatusStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            List<Drug> allDrugs = getAllDrugs();
            
            // 按状态分组统计
            Map<Integer, Long> statusCount = allDrugs.stream()
                    .filter(drug -> drug.getStatus() != null)
                    .collect(Collectors.groupingBy(
                            Drug::getStatus,
                            Collectors.counting()
                    ));
            
            // 状态名称映射
            Map<Integer, String> statusNames = new HashMap<>();
            statusNames.put(0, "已删除");
            statusNames.put(1, "正常");
            statusNames.put(2, "停用");
            
            // 转换为状态名称统计
            Map<String, Long> statusNameCount = new HashMap<>();
            for (Map.Entry<Integer, Long> entry : statusCount.entrySet()) {
                String statusName = statusNames.getOrDefault(entry.getKey(), "未知状态");
                statusNameCount.put(statusName, entry.getValue());
            }
            
            statistics.put("totalDrugs", allDrugs.size());
            statistics.put("statusCount", statusNameCount);
            statistics.put("statusList", statusNameCount.keySet());
            
        } catch (Exception e) {
            throw new RuntimeException("获取药品状态统计失败", e);
        }
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getManufacturerStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            List<Drug> allDrugs = getAllDrugs();
            
            // 按生产厂家分组统计
            Map<String, Long> manufacturerCount = allDrugs.stream()
                    .filter(drug -> drug.getManufacturer() != null && !drug.getManufacturer().trim().isEmpty())
                    .collect(Collectors.groupingBy(
                            Drug::getManufacturer,
                            Collectors.counting()
                    ));
            
            // 按数量排序，取前10名
            List<Map.Entry<String, Long>> topManufacturers = manufacturerCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toList());
            
            statistics.put("totalManufacturers", manufacturerCount.size());
            statistics.put("manufacturerCount", manufacturerCount);
            statistics.put("topManufacturers", topManufacturers);
            
        } catch (Exception e) {
            throw new RuntimeException("获取生产厂家统计失败", e);
        }
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getDrugPriceStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            List<Drug> allDrugs = getAllDrugs();
            
            // 过滤有价格的药品
            List<Drug> drugsWithPrice = allDrugs.stream()
                    .filter(drug -> drug.getPrice() != null && drug.getPrice().doubleValue() > 0)
                    .collect(Collectors.toList());
            
            if (drugsWithPrice.isEmpty()) {
                statistics.put("message", "暂无价格数据");
                return statistics;
            }
            
            // 价格区间统计
            Map<String, Long> priceRangeCount = new HashMap<>();
            long lowPrice = 0, mediumPrice = 0, highPrice = 0, veryHighPrice = 0;
            
            for (Drug drug : drugsWithPrice) {
                double price = drug.getPrice().doubleValue();
                if (price <= 10) {
                    lowPrice++;
                } else if (price <= 50) {
                    mediumPrice++;
                } else if (price <= 200) {
                    highPrice++;
                } else {
                    veryHighPrice++;
                }
            }
            
            priceRangeCount.put("10元以下", lowPrice);
            priceRangeCount.put("10-50元", mediumPrice);
            priceRangeCount.put("50-200元", highPrice);
            priceRangeCount.put("200元以上", veryHighPrice);
            
            // 计算平均价格
            double avgPrice = drugsWithPrice.stream()
                    .mapToDouble(drug -> drug.getPrice().doubleValue())
                    .average()
                    .orElse(0.0);
            
            statistics.put("totalDrugsWithPrice", drugsWithPrice.size());
            statistics.put("priceRangeCount", priceRangeCount);
            statistics.put("averagePrice", Math.round(avgPrice * 100.0) / 100.0);
            
        } catch (Exception e) {
            throw new RuntimeException("获取药品价格统计失败", e);
        }
        
        return statistics;
    }
    
    @Override
    public Map<String, Object> getDrugComprehensiveStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // 获取各种统计信息
            Map<String, Object> typeStats = getDrugTypeStatistics();
            Map<String, Object> statusStats = getDrugStatusStatistics();
            Map<String, Object> manufacturerStats = getManufacturerStatistics();
            Map<String, Object> priceStats = getDrugPriceStatistics();
            
            // 合并统计信息
            statistics.put("typeStatistics", typeStats);
            statistics.put("statusStatistics", statusStats);
            statistics.put("manufacturerStatistics", manufacturerStats);
            statistics.put("priceStatistics", priceStats);
            
            // 总体统计
            List<Drug> allDrugs = getAllDrugs();
            statistics.put("totalDrugs", allDrugs.size());
            statistics.put("activeDrugs", allDrugs.stream().filter(drug -> drug.getStatus() != null && drug.getStatus() == 1).count());
            statistics.put("inactiveDrugs", allDrugs.stream().filter(drug -> drug.getStatus() != null && drug.getStatus() == 2).count());
            statistics.put("deletedDrugs", allDrugs.stream().filter(drug -> drug.getStatus() != null && drug.getStatus() == 0).count());
            
        } catch (Exception e) {
            throw new RuntimeException("获取综合统计信息失败", e);
        }
        
        return statistics;
    }
}
