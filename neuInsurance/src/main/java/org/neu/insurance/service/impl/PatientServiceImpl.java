package org.neu.insurance.service.impl;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.PatientCreateDTO;
import org.neu.insurance.entity.Patient;
import org.neu.insurance.mapper.PatientMapper;
import org.neu.insurance.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 患者管理服务实现类
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public PageResponse<Patient> getPatientList(PageRequest pageRequest, String name, String idCard, String insuranceType, String keyword) {
        // 计算偏移量
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
        
        // 查询总数
        int total = patientMapper.countPatients(name, idCard, insuranceType, keyword);
        
        // 查询数据
        List<Patient> patients = patientMapper.getPatientList(offset, pageRequest.getPageSize(), name, idCard, insuranceType, keyword);
        
        // 构建分页响应
        PageResponse<Patient> response = new PageResponse<>();
        response.setRecords(patients);
        response.setTotal((long) total);
        response.setPageNum(pageRequest.getPageNum());
        response.setPageSize(pageRequest.getPageSize());
        response.setTotalPages((int) Math.ceil((double) total / pageRequest.getPageSize()));
        
        return response;
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientMapper.selectById(id);
    }

    @Override
    public Patient getPatientByIdCard(String idCard) {
        return patientMapper.getPatientByIdCard(idCard);
    }

    @Override
    public Patient getPatientByInsuranceCard(String insuranceCard) {
        return patientMapper.getPatientByInsuranceCard(insuranceCard);
    }

    @Override
    public boolean addPatient(Patient patient) {
        // 检查身份证号是否已存在
        if (patientMapper.getPatientByIdCard(patient.getIdCard()) != null) {
            return false;
        }
        
        // 检查医保卡号是否已存在
        if (patient.getInsuranceNumber() != null && patientMapper.getPatientByInsuranceCard(patient.getInsuranceNumber()) != null) {
            return false;
        }
        
        return patientMapper.addPatient(patient) > 0;
    }

    @Override
    public boolean addPatientByDTO(PatientCreateDTO patientDTO) {
        // 检查身份证号是否已存在
        if (patientMapper.getPatientByIdCard(patientDTO.getIdCard()) != null) {
            return false;
        }
        
        // 检查医保卡号是否已存在
        if (patientDTO.getInsuranceNumber() != null && patientMapper.getPatientByInsuranceCard(patientDTO.getInsuranceNumber()) != null) {
            return false;
        }
        
        // 使用DTO直接插入，避免ID字段问题
        return patientMapper.addPatientByDTO(patientDTO) > 0;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        // 检查身份证号是否被其他患者使用
        Patient existingPatient = patientMapper.getPatientByIdCard(patient.getIdCard());
        if (existingPatient != null && !existingPatient.getId().equals(patient.getId())) {
            return false;
        }
        
        // 检查医保卡号是否被其他患者使用
        if (patient.getInsuranceNumber() != null) {
            existingPatient = patientMapper.getPatientByInsuranceCard(patient.getInsuranceNumber());
            if (existingPatient != null && !existingPatient.getId().equals(patient.getId())) {
                return false;
            }
        }
        
        return patientMapper.updatePatient(patient) > 0;
    }

    @Override
    public boolean deletePatient(Long id) {
        return patientMapper.deletePatient(id) > 0;
    }

    @Override
    public boolean batchDeletePatient(List<Long> ids) {
        return patientMapper.batchDeletePatient(ids) > 0;
    }

    @Override
    public List<Patient> getPatientsByName(String name) {
        return patientMapper.getPatientsByName(name);
    }

    @Override
    public List<Patient> getPatientsByInsuranceType(String insuranceType) {
        return patientMapper.getPatientsByInsuranceType(insuranceType);
    }

    @Override
    public List<Patient> getPatientsByAgeRange(Integer minAge, Integer maxAge) {
        return patientMapper.getPatientsByAgeRange(minAge, maxAge);
    }

    @Override
    public List<Patient> getPatientsByGender(String gender) {
        return patientMapper.getPatientsByGender(gender);
    }

    @Override
    public List<Patient> getPatientsByRegion(String region) {
        return patientMapper.getPatientsByRegion(region);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        return patientMapper.updateStatus(id, status) > 0;
    }

    @Override
    public List<String> getAllInsuranceTypes() {
        return patientMapper.getAllInsuranceTypes();
    }

    @Override
    public List<String> getAllRegions() {
        return patientMapper.getAllRegions();
    }

    @Override
    public boolean isIdCardExists(String idCard) {
        return patientMapper.getPatientByIdCard(idCard) != null;
    }

    @Override
    public boolean isInsuranceCardExists(String insuranceCard) {
        return patientMapper.getPatientByInsuranceCard(insuranceCard) != null;
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return patientMapper.getPatientByPhone(phone) != null;
    }

    @Override
    public List<Patient> searchPatients(String keyword) {
        return patientMapper.searchPatients(keyword);
    }

    @Override
    public Object getPatientStatistics() {
        return patientMapper.getPatientStatistics();
    }
} 