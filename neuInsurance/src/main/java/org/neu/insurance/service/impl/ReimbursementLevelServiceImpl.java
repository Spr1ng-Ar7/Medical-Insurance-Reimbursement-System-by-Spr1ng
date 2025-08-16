package org.neu.insurance.service.impl;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.ReimbursementLevel;
import org.neu.insurance.mapper.ReimbursementLevelMapper;
import org.neu.insurance.service.ReimbursementLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 报销等级服务实现类
 */
@Slf4j
@Service
public class ReimbursementLevelServiceImpl implements ReimbursementLevelService {
    
    @Autowired
    private ReimbursementLevelMapper reimbursementLevelMapper;
    
    @Override
    public PageResponse<ReimbursementLevel> getReimbursementLevelList(String levelName, String insuranceType, 
                                                                     String hospitalLevel, Integer status, 
                                                                     PageRequest pageRequest) {
        try {
            log.info("分页查询报销等级列表，参数：levelName={}, insuranceType={}, hospitalLevel={}, status={}", 
                    levelName, insuranceType, hospitalLevel, status);
            
            // 查询数据
            List<ReimbursementLevel> list = reimbursementLevelMapper.selectReimbursementLevelList(
                    levelName, insuranceType, hospitalLevel, status);
            
            // 计算分页信息
            int total = list.size();
            int pageSize = pageRequest.getPageSize();
            int pageNum = pageRequest.getPageNum();
            int startIndex = (pageNum - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, total);
            
            List<ReimbursementLevel> pageData = list.subList(startIndex, endIndex);
            
            PageResponse<ReimbursementLevel> response = new PageResponse<>();
            response.setRecords(pageData);
            response.setTotal((long) total);
            response.setPageNum(pageNum);
            response.setPageSize(pageSize);
            
            log.info("分页查询报销等级列表完成，总数：{}", total);
            return response;
            
        } catch (Exception e) {
            log.error("分页查询报销等级列表失败", e);
            throw new RuntimeException("查询报销等级列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public ReimbursementLevel getReimbursementLevelById(Long id) {
        try {
            log.info("根据ID查询报销等级，ID：{}", id);
            ReimbursementLevel level = reimbursementLevelMapper.selectReimbursementLevelById(id);
            if (level == null) {
                log.warn("报销等级不存在，ID：{}", id);
                throw new RuntimeException("报销等级不存在");
            }
            return level;
        } catch (Exception e) {
            log.error("根据ID查询报销等级失败，ID：{}", id, e);
            throw new RuntimeException("查询报销等级失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void addReimbursementLevel(ReimbursementLevel level) {
        try {
            log.info("新增报销等级，等级名称：{}", level.getLevelName());
            
            // 验证等级编码唯一性
            if (reimbursementLevelMapper.checkLevelCodeUnique(level.getLevelCode(), null) > 0) {
                throw new RuntimeException("等级编码已存在");
            }
            
            // 设置创建时间
            level.setCreateTime(LocalDateTime.now());
            level.setUpdateTime(LocalDateTime.now());
            
            // 设置默认状态
            if (level.getStatus() == null) {
                level.setStatus(1);
            }
            
            reimbursementLevelMapper.insertReimbursementLevel(level);
            log.info("新增报销等级成功，ID：{}", level.getId());
            
        } catch (Exception e) {
            log.error("新增报销等级失败，等级名称：{}", level.getLevelName(), e);
            throw new RuntimeException("新增报销等级失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateReimbursementLevel(Long id, ReimbursementLevel level) {
        try {
            log.info("更新报销等级，ID：{}", id);
            
            // 检查是否存在
            ReimbursementLevel existingLevel = reimbursementLevelMapper.selectReimbursementLevelById(id);
            if (existingLevel == null) {
                throw new RuntimeException("报销等级不存在");
            }
            
            // 验证等级编码唯一性
            if (reimbursementLevelMapper.checkLevelCodeUnique(level.getLevelCode(), id) > 0) {
                throw new RuntimeException("等级编码已存在");
            }
            
            // 设置更新信息
            level.setId(id);
            level.setUpdateTime(LocalDateTime.now());
            
            reimbursementLevelMapper.updateReimbursementLevel(level);
            log.info("更新报销等级成功，ID：{}", id);
            
        } catch (Exception e) {
            log.error("更新报销等级失败，ID：{}", id, e);
            throw new RuntimeException("更新报销等级失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteReimbursementLevel(Long id) {
        try {
            log.info("删除报销等级，ID：{}", id);
            
            // 检查是否存在
            ReimbursementLevel level = reimbursementLevelMapper.selectReimbursementLevelById(id);
            if (level == null) {
                throw new RuntimeException("报销等级不存在");
            }
            
            reimbursementLevelMapper.deleteReimbursementLevel(id);
            log.info("删除报销等级成功，ID：{}", id);
            
        } catch (Exception e) {
            log.error("删除报销等级失败，ID：{}", id, e);
            throw new RuntimeException("删除报销等级失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteReimbursementLevel(List<Long> ids) {
        try {
            log.info("批量删除报销等级，IDs：{}", ids);
            
            if (ids == null || ids.isEmpty()) {
                throw new RuntimeException("删除ID列表不能为空");
            }
            
            reimbursementLevelMapper.batchDeleteReimbursementLevel(ids);
            log.info("批量删除报销等级成功，删除数量：{}", ids.size());
            
        } catch (Exception e) {
            log.error("批量删除报销等级失败，IDs：{}", ids, e);
            throw new RuntimeException("批量删除报销等级失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        try {
            log.info("更新报销等级状态，ID：{}，状态：{}", id, status);
            
            // 检查是否存在
            ReimbursementLevel level = reimbursementLevelMapper.selectReimbursementLevelById(id);
            if (level == null) {
                throw new RuntimeException("报销等级不存在");
            }
            
            reimbursementLevelMapper.updateStatus(id, status);
            log.info("更新报销等级状态成功，ID：{}，状态：{}", id, status);
            
        } catch (Exception e) {
            log.error("更新报销等级状态失败，ID：{}，状态：{}", id, status, e);
            throw new RuntimeException("更新报销等级状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<ReimbursementLevel> getEffectiveReimbursementLevels() {
        try {
            log.info("获取有效的报销等级列表");
            List<ReimbursementLevel> levels = reimbursementLevelMapper.selectEffectiveReimbursementLevels();
            log.info("获取有效的报销等级列表完成，数量：{}", levels.size());
            return levels;
        } catch (Exception e) {
            log.error("获取有效的报销等级列表失败", e);
            throw new RuntimeException("获取有效的报销等级列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<ReimbursementLevel> getMatchingReimbursementLevels(String insuranceType, String hospitalLevel) {
        try {
            log.info("根据条件匹配报销等级，医保类型：{}，医院等级：{}", insuranceType, hospitalLevel);
            List<ReimbursementLevel> levels = reimbursementLevelMapper.selectMatchingReimbursementLevels(
                    insuranceType, hospitalLevel);
            log.info("匹配报销等级完成，数量：{}", levels.size());
            return levels;
        } catch (Exception e) {
            log.error("匹配报销等级失败，医保类型：{}，医院等级：{}", insuranceType, hospitalLevel, e);
            throw new RuntimeException("匹配报销等级失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void copyReimbursementLevel(Long id) {
        try {
            log.info("复制报销等级配置，ID：{}", id);
            
            // 获取原配置
            ReimbursementLevel original = reimbursementLevelMapper.selectReimbursementLevelById(id);
            if (original == null) {
                throw new RuntimeException("报销等级不存在");
            }
            
            // 创建新配置
            ReimbursementLevel newLevel = new ReimbursementLevel();
            newLevel.setLevelCode(original.getLevelCode() + "_COPY");
            newLevel.setLevelName(original.getLevelName() + "_副本");
            newLevel.setInsuranceType(original.getInsuranceType());
            newLevel.setHospitalLevel(original.getHospitalLevel());
            newLevel.setMinAmount(original.getMinAmount());
            newLevel.setMaxAmount(original.getMaxAmount());
            newLevel.setDeductible(original.getDeductible());
            newLevel.setMaxReimbursement(original.getMaxReimbursement());
            newLevel.setCategoryARate(original.getCategoryARate());
            newLevel.setCategoryBRate(original.getCategoryBRate());
            newLevel.setCategoryCRate(original.getCategoryCRate());
            newLevel.setTreatmentRate(original.getTreatmentRate());
            newLevel.setServiceRate(original.getServiceRate());
            newLevel.setStatus(0); // 默认禁用状态
            newLevel.setEffectiveTime(original.getEffectiveTime());
            newLevel.setExpireTime(original.getExpireTime());
            newLevel.setCreateTime(LocalDateTime.now());
            newLevel.setUpdateTime(LocalDateTime.now());
            newLevel.setCreateBy(original.getCreateBy());
            newLevel.setRemark("复制自：" + original.getLevelName());
            
            reimbursementLevelMapper.insertReimbursementLevel(newLevel);
            log.info("复制报销等级配置成功，新ID：{}", newLevel.getId());
            
        } catch (Exception e) {
            log.error("复制报销等级配置失败，ID：{}", id, e);
            throw new RuntimeException("复制报销等级配置失败: " + e.getMessage());
        }
    }

    @Override
    public boolean validateReimbursementLevel(ReimbursementLevel level) {
        try {
            log.info("验证报销等级配置");
            
            // 基本字段验证
            if (level.getLevelCode() == null || level.getLevelCode().trim().isEmpty()) {
                log.warn("等级编码不能为空");
                return false;
            }
            
            if (level.getLevelName() == null || level.getLevelName().trim().isEmpty()) {
                log.warn("等级名称不能为空");
                return false;
            }
            
            if (level.getInsuranceType() == null || level.getInsuranceType().trim().isEmpty()) {
                log.warn("医保类型不能为空");
                return false;
            }
            
            if (level.getHospitalLevel() == null || level.getHospitalLevel().trim().isEmpty()) {
                log.warn("医院等级不能为空");
                return false;
            }
            
            // 比例验证
            if (level.getCategoryARate() != null && 
                (level.getCategoryARate().compareTo(java.math.BigDecimal.ZERO) < 0 || 
                 level.getCategoryARate().compareTo(new java.math.BigDecimal("100")) > 0)) {
                log.warn("甲类药品报销比例必须在0-100之间");
                return false;
            }
            
            if (level.getCategoryBRate() != null && 
                (level.getCategoryBRate().compareTo(java.math.BigDecimal.ZERO) < 0 || 
                 level.getCategoryBRate().compareTo(new java.math.BigDecimal("100")) > 0)) {
                log.warn("乙类药品报销比例必须在0-100之间");
                return false;
            }
            
            if (level.getCategoryCRate() != null && 
                (level.getCategoryCRate().compareTo(java.math.BigDecimal.ZERO) < 0 || 
                 level.getCategoryCRate().compareTo(new java.math.BigDecimal("100")) > 0)) {
                log.warn("丙类药品报销比例必须在0-100之间");
                return false;
            }
            
            if (level.getTreatmentRate() != null && 
                (level.getTreatmentRate().compareTo(java.math.BigDecimal.ZERO) < 0 || 
                 level.getTreatmentRate().compareTo(new java.math.BigDecimal("100")) > 0)) {
                log.warn("诊疗费用报销比例必须在0-100之间");
                return false;
            }
            
            if (level.getServiceRate() != null && 
                (level.getServiceRate().compareTo(java.math.BigDecimal.ZERO) < 0 || 
                 level.getServiceRate().compareTo(new java.math.BigDecimal("100")) > 0)) {
                log.warn("服务设施费用报销比例必须在0-100之间");
                return false;
            }
            
            // 金额验证
            if (level.getMinAmount() != null && level.getMaxAmount() != null && 
                level.getMinAmount().compareTo(level.getMaxAmount()) > 0) {
                log.warn("最低费用不能大于最高费用");
                return false;
            }
            
            if (level.getDeductible() != null && level.getDeductible().compareTo(java.math.BigDecimal.ZERO) < 0) {
                log.warn("起付线不能为负数");
                return false;
            }
            
            if (level.getMaxReimbursement() != null && level.getMaxReimbursement().compareTo(java.math.BigDecimal.ZERO) < 0) {
                log.warn("最高报销额度不能为负数");
                return false;
            }
            
            // 时间验证
            if (level.getEffectiveTime() != null && level.getExpireTime() != null && 
                level.getEffectiveTime().isAfter(level.getExpireTime())) {
                log.warn("生效时间不能晚于失效时间");
                return false;
            }
            
            log.info("报销等级配置验证通过");
            return true;
            
        } catch (Exception e) {
            log.error("验证报销等级配置失败", e);
            return false;
        }
    }
} 