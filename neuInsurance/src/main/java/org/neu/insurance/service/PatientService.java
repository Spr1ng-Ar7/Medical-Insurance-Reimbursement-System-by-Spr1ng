package org.neu.insurance.service;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.dto.PatientCreateDTO;
import org.neu.insurance.entity.Patient;

import java.util.List;

/**
 * 患者管理服务接口
 */
public interface PatientService {
    
    /**
     * 分页查询患者列表
     */
    PageResponse<Patient> getPatientList(PageRequest pageRequest, String name, String idCard, String insuranceType, String keyword);
    
    /**
     * 根据ID查询患者
     */
    Patient getPatientById(Long id);
    
    /**
     * 根据身份证号查询患者
     */
    Patient getPatientByIdCard(String idCard);
    
    /**
     * 根据医保卡号查询患者
     */
    Patient getPatientByInsuranceCard(String insuranceCard);
    
    /**
     * 新增患者 - 使用实体类
     */
    boolean addPatient(Patient patient);
    
    /**
     * 新增患者 - 使用DTO（推荐）
     */
    boolean addPatientByDTO(PatientCreateDTO patientDTO);
    
    /**
     * 更新患者信息
     */
    boolean updatePatient(Patient patient);
    
    /**
     * 删除患者
     */
    boolean deletePatient(Long id);
    
    /**
     * 批量删除患者
     */
    boolean batchDeletePatient(List<Long> ids);
    
    /**
     * 根据姓名查询患者列表
     */
    List<Patient> getPatientsByName(String name);
    
    /**
     * 根据医保类型查询患者列表
     */
    List<Patient> getPatientsByInsuranceType(String insuranceType);
    
    /**
     * 根据年龄段查询患者列表
     */
    List<Patient> getPatientsByAgeRange(Integer minAge, Integer maxAge);
    
    /**
     * 根据性别查询患者列表
     */
    List<Patient> getPatientsByGender(String gender);
    
    /**
     * 根据地区查询患者列表
     */
    List<Patient> getPatientsByRegion(String region);
    
    /**
     * 启用/禁用患者
     */
    boolean updateStatus(Long id, Integer status);
    
    /**
     * 获取所有医保类型
     */
    List<String> getAllInsuranceTypes();
    
    /**
     * 获取所有地区
     */
    List<String> getAllRegions();
    
    /**
     * 检查身份证号是否存在
     */
    boolean isIdCardExists(String idCard);
    
    /**
     * 检查医保卡号是否存在
     */
    boolean isInsuranceCardExists(String insuranceCard);
    
    /**
     * 检查手机号是否存在
     */
    boolean isPhoneExists(String phone);
    
    /**
     * 根据关键词搜索患者
     */
    List<Patient> searchPatients(String keyword);
    
    /**
     * 获取患者统计信息
     */
    Object getPatientStatistics();
} 