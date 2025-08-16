package org.neu.insurance.service;

import org.neu.insurance.entity.MedicalEquipment;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import java.util.List;

/**
 * 医疗器械管理服务接口
 */
public interface MedicalEquipmentService {
    
    /**
     * 根据ID查询医疗器械
     */
    MedicalEquipment getMedicalEquipmentById(Long id);
    
    /**
     * 根据器械编码查询医疗器械
     */
    MedicalEquipment getMedicalEquipmentByCode(String equipmentCode);
    
    /**
     * 分页查询医疗器械列表
     */
    PageResponse<MedicalEquipment> getMedicalEquipmentList(PageRequest pageRequest);
    
    /**
     * 根据器械类型查询医疗器械列表
     */
    List<MedicalEquipment> getMedicalEquipmentsByType(String equipmentType);
    
    /**
     * 创建医疗器械
     */
    void createMedicalEquipment(MedicalEquipment medicalEquipment);
    
    /**
     * 更新医疗器械信息
     */
    void updateMedicalEquipment(MedicalEquipment medicalEquipment);
    
    /**
     * 删除医疗器械
     */
    void deleteMedicalEquipment(Long id);
    
    /**
     * 批量删除医疗器械
     */
    void batchDeleteMedicalEquipments(List<Long> ids);
    
    /**
     * 启用/禁用医疗器械
     */
    void changeMedicalEquipmentStatus(Long id, Integer status);
    
    /**
     * 检查器械编码是否存在
     */
    boolean checkEquipmentCodeExists(String equipmentCode);
    
    /**
     * 模糊搜索医疗器械名称
     */
    List<MedicalEquipment> searchMedicalEquipmentsByName(String keyword);
} 