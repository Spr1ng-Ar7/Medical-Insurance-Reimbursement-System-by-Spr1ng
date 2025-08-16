package org.neu.insurance.util;

import org.neu.insurance.dto.PatientCreateDTO;
import org.neu.insurance.dto.DrugCreateDTO;
import org.neu.insurance.dto.ReimbursementLevelDTO;
import org.neu.insurance.entity.Patient;
import org.neu.insurance.entity.Drug;
import org.neu.insurance.entity.ReimbursementLevel;
import java.time.LocalDateTime;

/**
 * 实体转换工具类
 */
public class EntityConverter {
    
    /**
     * 将PatientCreateDTO转换为Patient实体
     */
    public static Patient convertToPatient(PatientCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Patient patient = new Patient();
        patient.setPatientId(dto.getPatientId());
        patient.setName(dto.getName());
        patient.setIdCard(dto.getIdCard());
        patient.setGender(dto.getGender());
        patient.setBirthDate(dto.getBirthDate());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setInsuranceType(dto.getInsuranceType());
        patient.setInsuranceNumber(dto.getInsuranceNumber());
        patient.setStatus(dto.getStatus());
        patient.setRemark(dto.getRemark());
        
        // 设置时间字段
        LocalDateTime now = LocalDateTime.now();
        patient.setCreateTime(now);
        patient.setUpdateTime(now);
        
        return patient;
    }
    
    /**
     * 将DrugCreateDTO转换为Drug实体
     */
    public static Drug convertToDrug(DrugCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Drug drug = new Drug();
        drug.setDrugCode(dto.getDrugCode());
        drug.setDrugName(dto.getDrugName());
        drug.setTradeName(dto.getTradeName());
        drug.setDrugType(dto.getDrugType());
        drug.setSelfPayRatio(dto.getSelfPayRatio());
        drug.setSpecification(dto.getSpecification());
        drug.setUnit(dto.getUnit());
        drug.setPrice(dto.getPrice());
        drug.setManufacturer(dto.getManufacturer());
        drug.setStatus(dto.getStatus());
        drug.setCreateBy(dto.getCreateBy());
        drug.setRemark(dto.getRemark());
        
        // 设置时间字段
        LocalDateTime now = LocalDateTime.now();
        drug.setCreateTime(now);
        drug.setUpdateTime(now);
        
        return drug;
    }
    
    /**
     * 将ReimbursementLevelDTO转换为ReimbursementLevel实体
     */
    public static ReimbursementLevel convertToReimbursementLevel(ReimbursementLevelDTO dto) {
        if (dto == null) {
            return null;
        }
        
        ReimbursementLevel level = new ReimbursementLevel();
        level.setId(dto.getId());
        level.setLevelCode(dto.getLevelCode());
        level.setLevelName(dto.getLevelName());
        level.setInsuranceType(dto.getInsuranceType());
        level.setHospitalLevel(dto.getHospitalLevel());
        level.setMinAmount(dto.getMinAmount());
        level.setMaxAmount(dto.getMaxAmount());
        level.setDeductible(dto.getDeductible());
        level.setMaxReimbursement(dto.getMaxReimbursement());
        level.setCategoryARate(dto.getCategoryARate());
        level.setCategoryBRate(dto.getCategoryBRate());
        level.setCategoryCRate(dto.getCategoryCRate());
        level.setTreatmentRate(dto.getTreatmentRate());
        level.setServiceRate(dto.getServiceRate());
        level.setStatus(dto.getStatus());
        level.setEffectiveTime(dto.getEffectiveTime());
        level.setExpireTime(dto.getExpireTime());
        level.setRemark(dto.getRemark());
        
        // 设置时间字段
        LocalDateTime now = LocalDateTime.now();
        if (dto.getId() == null) {
            level.setCreateTime(now);
        }
        level.setUpdateTime(now);
        
        return level;
    }
    
    /**
     * 将ReimbursementLevel实体转换为ReimbursementLevelDTO
     */
    public static ReimbursementLevelDTO convertToReimbursementLevelDTO(ReimbursementLevel level) {
        if (level == null) {
            return null;
        }
        
        ReimbursementLevelDTO dto = new ReimbursementLevelDTO();
        dto.setId(level.getId());
        dto.setLevelCode(level.getLevelCode());
        dto.setLevelName(level.getLevelName());
        dto.setInsuranceType(level.getInsuranceType());
        dto.setHospitalLevel(level.getHospitalLevel());
        dto.setMinAmount(level.getMinAmount());
        dto.setMaxAmount(level.getMaxAmount());
        dto.setDeductible(level.getDeductible());
        dto.setMaxReimbursement(level.getMaxReimbursement());
        dto.setCategoryARate(level.getCategoryARate());
        dto.setCategoryBRate(level.getCategoryBRate());
        dto.setCategoryCRate(level.getCategoryCRate());
        dto.setTreatmentRate(level.getTreatmentRate());
        dto.setServiceRate(level.getServiceRate());
        dto.setStatus(level.getStatus());
        dto.setEffectiveTime(level.getEffectiveTime());
        dto.setExpireTime(level.getExpireTime());
        dto.setRemark(level.getRemark());
        
        return dto;
    }
} 