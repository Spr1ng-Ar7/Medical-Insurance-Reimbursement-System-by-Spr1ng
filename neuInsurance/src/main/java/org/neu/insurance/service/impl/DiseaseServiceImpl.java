package org.neu.insurance.service.impl;

import org.neu.insurance.entity.Disease;
import org.neu.insurance.service.DiseaseService;
import org.neu.insurance.mapper.DiseaseMapper;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 疾病管理服务实现类
 */
@Slf4j
@Service
public class DiseaseServiceImpl implements DiseaseService {
    
    @Autowired
    private DiseaseMapper diseaseMapper;
    
    @Override
    public Disease getDiseaseById(Long id) {
        try {
            log.info("查询疾病信息，ID: {}", id);
            return diseaseMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询疾病失败，ID: {}", id, e);
            throw new RuntimeException("查询疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    public Disease getDiseaseByCode(String diseaseCode) {
        try {
            log.info("根据疾病编码查询疾病，code: {}", diseaseCode);
            return diseaseMapper.selectByDiseaseCode(diseaseCode);
        } catch (Exception e) {
            log.error("根据疾病编码查询疾病失败，code: {}", diseaseCode, e);
            throw new RuntimeException("查询疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    public Disease getDiseaseByIcdCode(String icdCode) {
        try {
            log.info("根据ICD编码查询疾病，icdCode: {}", icdCode);
            return diseaseMapper.selectByIcdCode(icdCode);
        } catch (Exception e) {
            log.error("根据ICD编码查询疾病失败，icdCode: {}", icdCode, e);
            throw new RuntimeException("查询疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    public PageResponse<Disease> getDiseaseList(PageRequest pageRequest) {
        try {
            log.info("分页查询疾病列表，页码: {}, 页大小: {}", pageRequest.getPageNum(), pageRequest.getPageSize());
            int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
            List<Disease> diseases = diseaseMapper.selectPage(offset, pageRequest.getPageSize());
            long total = diseaseMapper.count();
            
            PageResponse<Disease> response = new PageResponse<>();
            response.setRecords(diseases);
            response.setTotal(total);
            response.setCurrent(pageRequest.getPageNum());
            response.setSize(pageRequest.getPageSize());
            return response;
        } catch (Exception e) {
            log.error("分页查询疾病列表失败", e);
            throw new RuntimeException("查询疾病列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Disease> getDiseasesByCategory(String category) {
        try {
            log.info("根据分类查询疾病列表，category: {}", category);
            return diseaseMapper.selectByCategory(category);
        } catch (Exception e) {
            log.error("根据分类查询疾病列表失败，category: {}", category, e);
            throw new RuntimeException("查询疾病列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void createDisease(Disease disease) {
        try {
            log.info("创建疾病，疾病名称: {}", disease.getDiseaseName());
            disease.setCreateTime(LocalDateTime.now());
            diseaseMapper.insert(disease);
            log.info("疾病创建成功，ID: {}", disease.getId());
        } catch (Exception e) {
            log.error("创建疾病失败，疾病名称: {}", disease.getDiseaseName(), e);
            throw new RuntimeException("创建疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateDisease(Disease disease) {
        try {
            log.info("更新疾病信息，ID: {}", disease.getId());
            disease.setUpdateTime(LocalDateTime.now());
            diseaseMapper.update(disease);
            log.info("疾病信息更新成功");
        } catch (Exception e) {
            log.error("更新疾病信息失败，ID: {}", disease.getId(), e);
            throw new RuntimeException("更新疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteDisease(Long id) {
        try {
            log.info("删除疾病，ID: {}", id);
            diseaseMapper.deleteById(id);
            log.info("疾病删除成功");
        } catch (Exception e) {
            log.error("删除疾病失败，ID: {}", id, e);
            throw new RuntimeException("删除疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteDiseases(List<Long> ids) {
        try {
            log.info("批量删除疾病，数量: {}", ids.size());
            diseaseMapper.batchDelete(ids);
            log.info("批量删除疾病成功");
        } catch (Exception e) {
            log.error("批量删除疾病失败", e);
            throw new RuntimeException("批量删除疾病失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void changeDiseaseStatus(Long id, Integer status) {
        try {
            log.info("更改疾病状态，ID: {}, 状态: {}", id, status);
            diseaseMapper.updateStatus(id, status);
            log.info("疾病状态更新成功");
        } catch (Exception e) {
            log.error("更改疾病状态失败，ID: {}", id, e);
            throw new RuntimeException("更改疾病状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkDiseaseCodeExists(String diseaseCode) {
        try {
            log.info("检查疾病编码是否存在，diseaseCode: {}", diseaseCode);
            return diseaseMapper.existsByDiseaseCode(diseaseCode);
        } catch (Exception e) {
            log.error("检查疾病编码失败，diseaseCode: {}", diseaseCode, e);
            throw new RuntimeException("检查疾病编码失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkIcdCodeExists(String icdCode) {
        try {
            log.info("检查ICD编码是否存在，icdCode: {}", icdCode);
            return diseaseMapper.existsByIcdCode(icdCode);
        } catch (Exception e) {
            log.error("检查ICD编码失败，icdCode: {}", icdCode, e);
            throw new RuntimeException("检查ICD编码失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<Disease> searchDiseasesByName(String keyword) {
        try {
            log.info("模糊搜索疾病名称，keyword: {}", keyword);
            return diseaseMapper.searchByName(keyword);
        } catch (Exception e) {
            log.error("模糊搜索疾病名称失败，keyword: {}", keyword, e);
            throw new RuntimeException("搜索疾病失败: " + e.getMessage());
        }
    }
} 