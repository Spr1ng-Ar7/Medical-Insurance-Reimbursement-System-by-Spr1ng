package org.neu.insurance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.neu.insurance.entity.ReimbursementLevel;
import org.neu.insurance.service.ReimbursementLevelService;
import org.neu.insurance.util.ReimbursementLevelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销等级功能测试类
 */
@SpringBootTest
@ActiveProfiles("test")
public class ReimbursementLevelTest {
    
    @Autowired
    private ReimbursementLevelService reimbursementLevelService;
    
    @Test
    public void testCreateReimbursementLevel() {
        // 创建测试数据
        ReimbursementLevel level = new ReimbursementLevel();
        level.setLevelCode("UE_L3_001");
        level.setLevelName("城镇职工三级医院标准");
        level.setInsuranceType(ReimbursementLevelConstants.InsuranceType.URBAN_EMPLOYEE);
        level.setHospitalLevel(ReimbursementLevelConstants.HospitalLevel.LEVEL_THREE);
        level.setMinAmount(new BigDecimal("0"));
        level.setMaxAmount(new BigDecimal("100000"));
        level.setDeductible(new BigDecimal("1000"));
        level.setMaxReimbursement(new BigDecimal("50000"));
        level.setCategoryARate(new BigDecimal("100"));
        level.setCategoryBRate(new BigDecimal("80"));
        level.setCategoryCRate(new BigDecimal("0"));
        level.setTreatmentRate(new BigDecimal("90"));
        level.setServiceRate(new BigDecimal("85"));
        level.setStatus(ReimbursementLevelConstants.Status.ENABLED);
        level.setEffectiveTime(LocalDateTime.now());
        level.setExpireTime(LocalDateTime.now().plusYears(1));
        level.setRemark("城镇职工在三级医院的标准报销配置");
        
        // 验证配置
        boolean isValid = reimbursementLevelService.validateReimbursementLevel(level);
        System.out.println("报销等级配置验证结果: " + isValid);
        
        // 如果验证通过，则创建
        if (isValid) {
            try {
                reimbursementLevelService.addReimbursementLevel(level);
                System.out.println("报销等级创建成功，ID: " + level.getId());
            } catch (Exception e) {
                System.err.println("报销等级创建失败: " + e.getMessage());
            }
        }
    }
    
    @Test
    public void testQueryReimbursementLevels() {
        try {
            // 查询有效的报销等级列表
            java.util.List<org.neu.insurance.entity.ReimbursementLevel> effectiveLevels = reimbursementLevelService.getEffectiveReimbursementLevels();
            System.out.println("有效报销等级数量: " + effectiveLevels.size());
            
            // 查询匹配的报销等级
            java.util.List<org.neu.insurance.entity.ReimbursementLevel> matchingLevels = reimbursementLevelService.getMatchingReimbursementLevels(
                    ReimbursementLevelConstants.InsuranceType.URBAN_EMPLOYEE,
                    ReimbursementLevelConstants.HospitalLevel.LEVEL_THREE);
            System.out.println("匹配的报销等级数量: " + matchingLevels.size());
            
        } catch (Exception e) {
            System.err.println("查询报销等级失败: " + e.getMessage());
        }
    }
    
    @Test
    public void testValidateReimbursementLevel() {
        // 测试有效配置
        ReimbursementLevel validLevel = new ReimbursementLevel();
        validLevel.setLevelCode("TEST_001");
        validLevel.setLevelName("测试配置");
        validLevel.setInsuranceType(ReimbursementLevelConstants.InsuranceType.URBAN_RESIDENT);
        validLevel.setHospitalLevel(ReimbursementLevelConstants.HospitalLevel.LEVEL_TWO);
        validLevel.setCategoryARate(new BigDecimal("100"));
        validLevel.setCategoryBRate(new BigDecimal("80"));
        validLevel.setTreatmentRate(new BigDecimal("90"));
        validLevel.setServiceRate(new BigDecimal("85"));
        
        boolean isValid = reimbursementLevelService.validateReimbursementLevel(validLevel);
        System.out.println("有效配置验证结果: " + isValid);
        
        // 测试无效配置
        ReimbursementLevel invalidLevel = new ReimbursementLevel();
        invalidLevel.setLevelCode(""); // 空编码
        invalidLevel.setCategoryARate(new BigDecimal("150")); // 超过100%
        
        boolean isInvalid = reimbursementLevelService.validateReimbursementLevel(invalidLevel);
        System.out.println("无效配置验证结果: " + isInvalid);
    }
} 