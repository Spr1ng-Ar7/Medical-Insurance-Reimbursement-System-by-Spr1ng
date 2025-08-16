package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.Patient;
import org.neu.insurance.dto.PatientCreateDTO;

import java.util.List;

/**
 * 患者数据访问接口
 */
@Mapper
public interface PatientMapper {
    
    /**
     * 统计患者总数
     */
    int countPatients(@Param("name") String name, 
                     @Param("idCard") String idCard, 
                     @Param("insuranceType") String insuranceType, 
                     @Param("keyword") String keyword);
    
    /**
     * 分页查询患者列表
     */
    List<Patient> getPatientList(@Param("offset") int offset, 
                                @Param("pageSize") int pageSize, 
                                @Param("name") String name, 
                                @Param("idCard") String idCard, 
                                @Param("insuranceType") String insuranceType, 
                                @Param("keyword") String keyword);
    
    /**
     * 根据ID查询患者
     */
    Patient selectById(@Param("id") Long id);
    
    /**
     * 根据身份证号查询患者
     */
    Patient getPatientByIdCard(@Param("idCard") String idCard);
    
    /**
     * 根据医保卡号查询患者
     */
    Patient getPatientByInsuranceCard(@Param("insuranceCard") String insuranceCard);
    
    /**
     * 根据手机号查询患者
     */
    Patient getPatientByPhone(@Param("phone") String phone);
    
    /**
     * 新增患者 - 使用实体类
     */
    int addPatient(Patient patient);
    
    /**
     * 新增患者 - 使用DTO（推荐）
     */
    int addPatientByDTO(PatientCreateDTO patientDTO);
    
    /**
     * 更新患者信息
     */
    int updatePatient(Patient patient);
    
    /**
     * 删除患者
     */
    int deletePatient(@Param("id") Long id);
    
    /**
     * 批量删除患者
     */
    int batchDeletePatient(@Param("ids") List<Long> ids);
    
    /**
     * 根据姓名查询患者列表
     */
    List<Patient> getPatientsByName(@Param("name") String name);
    
    /**
     * 根据医保类型查询患者列表
     */
    List<Patient> getPatientsByInsuranceType(@Param("insuranceType") String insuranceType);
    
    /**
     * 根据年龄段查询患者列表
     */
    List<Patient> getPatientsByAgeRange(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    /**
     * 根据性别查询患者列表
     */
    List<Patient> getPatientsByGender(@Param("gender") String gender);
    
    /**
     * 根据地区查询患者列表
     */
    List<Patient> getPatientsByRegion(@Param("region") String region);
    
    /**
     * 更新患者状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 获取所有医保类型
     */
    List<String> getAllInsuranceTypes();
    
    /**
     * 获取所有地区
     */
    List<String> getAllRegions();
    
    /**
     * 根据关键词搜索患者
     */
    List<Patient> searchPatients(@Param("keyword") String keyword);
    
    /**
     * 获取患者统计信息
     */
    Object getPatientStatistics();
} 