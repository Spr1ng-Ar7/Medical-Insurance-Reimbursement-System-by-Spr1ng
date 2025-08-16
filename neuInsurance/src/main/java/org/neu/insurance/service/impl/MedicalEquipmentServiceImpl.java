package org.neu.insurance.service.impl;

import org.neu.insurance.entity.MedicalEquipment;
import org.neu.insurance.service.MedicalEquipmentService;
import org.neu.insurance.mapper.MedicalEquipmentMapper;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 医疗器械管理服务实现类
 */
@Slf4j
@Service
public class MedicalEquipmentServiceImpl implements MedicalEquipmentService {
    
    @Autowired
    private MedicalEquipmentMapper medicalEquipmentMapper;
    
    @Override
    public MedicalEquipment getMedicalEquipmentById(Long id) {
        try {
            log.info("查询医疗器械信息，ID: {}", id);
            return medicalEquipmentMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询医疗器械失败，ID: {}", id, e);
            throw new RuntimeException("查询医疗器械失败: " + e.getMessage());
        }
    }
    
    @Override
    public MedicalEquipment getMedicalEquipmentByCode(String equipmentCode) {
        try {
            log.info("根据器械编码查询医疗器械，code: {}", equipmentCode);
            return medicalEquipmentMapper.selectByEquipmentCode(equipmentCode);
        } catch (Exception e) {
            log.error("根据器械编码查询医疗器械失败，code: {}", equipmentCode, e);
            throw new RuntimeException("查询医疗器械失败: " + e.getMessage());
        }
    }
    
    @Override
    public PageResponse<MedicalEquipment> getMedicalEquipmentList(PageRequest pageRequest) {
        try {
            log.info("分页查询医疗器械列表，页码: {}, 页大小: {}", pageRequest.getPageNum(), pageRequest.getPageSize());
            int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
            List<MedicalEquipment> equipments = medicalEquipmentMapper.selectPage(offset, pageRequest.getPageSize());
            long total = medicalEquipmentMapper.count();
            
            PageResponse<MedicalEquipment> response = new PageResponse<>();
            response.setRecords(equipments);
            response.setTotal(total);
            response.setCurrent(pageRequest.getPageNum());
            response.setSize(pageRequest.getPageSize());
            return response;
        } catch (Exception e) {
            log.error("分页查询医疗器械列表失败", e);
            throw new RuntimeException("查询医疗器械列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MedicalEquipment> getMedicalEquipmentsByType(String equipmentType) {
        try {
            log.info("根据器械类型查询医疗器械列表，type: {}", equipmentType);
            return medicalEquipmentMapper.selectByEquipmentType(equipmentType);
        } catch (Exception e) {
            log.error("根据器械类型查询医疗器械列表失败，type: {}", equipmentType, e);
            throw new RuntimeException("查询医疗器械列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void createMedicalEquipment(MedicalEquipment medicalEquipment) {
        try {
            log.info("创建医疗器械，器械名称: {}", medicalEquipment.getEquipmentName());
            medicalEquipment.setCreateTime(LocalDateTime.now());
            medicalEquipmentMapper.insert(medicalEquipment);
            log.info("医疗器械创建成功，ID: {}", medicalEquipment.getId());
        } catch (Exception e) {
            log.error("创建医疗器械失败，器械名称: {}", medicalEquipment.getEquipmentName(), e);
            throw new RuntimeException("创建医疗器械失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateMedicalEquipment(MedicalEquipment medicalEquipment) {
        try {
            log.info("更新医疗器械信息，ID: {}", medicalEquipment.getId());
            medicalEquipment.setUpdateTime(LocalDateTime.now());
            medicalEquipmentMapper.update(medicalEquipment);
            log.info("医疗器械信息更新成功");
        } catch (Exception e) {
            log.error("更新医疗器械信息失败，ID: {}", medicalEquipment.getId(), e);
            throw new RuntimeException("更新医疗器械失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteMedicalEquipment(Long id) {
        try {
            log.info("删除医疗器械，ID: {}", id);
            medicalEquipmentMapper.deleteById(id);
            log.info("医疗器械删除成功");
        } catch (Exception e) {
            log.error("删除医疗器械失败，ID: {}", id, e);
            throw new RuntimeException("删除医疗器械失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteMedicalEquipments(List<Long> ids) {
        try {
            log.info("批量删除医疗器械，数量: {}", ids.size());
            medicalEquipmentMapper.batchDelete(ids);
            log.info("批量删除医疗器械成功");
        } catch (Exception e) {
            log.error("批量删除医疗器械失败", e);
            throw new RuntimeException("批量删除医疗器械失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void changeMedicalEquipmentStatus(Long id, Integer status) {
        try {
            log.info("更改医疗器械状态，ID: {}, 状态: {}", id, status);
            medicalEquipmentMapper.updateStatus(id, status);
            log.info("医疗器械状态更新成功");
        } catch (Exception e) {
            log.error("更改医疗器械状态失败，ID: {}", id, e);
            throw new RuntimeException("更改医疗器械状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean checkEquipmentCodeExists(String equipmentCode) {
        try {
            log.info("检查器械编码是否存在，equipmentCode: {}", equipmentCode);
            return medicalEquipmentMapper.existsByEquipmentCode(equipmentCode);
        } catch (Exception e) {
            log.error("检查器械编码失败，equipmentCode: {}", equipmentCode, e);
            throw new RuntimeException("检查器械编码失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<MedicalEquipment> searchMedicalEquipmentsByName(String keyword) {
        try {
            log.info("模糊搜索医疗器械名称，keyword: {}", keyword);
            return medicalEquipmentMapper.searchByName(keyword);
        } catch (Exception e) {
            log.error("模糊搜索医疗器械名称失败，keyword: {}", keyword, e);
            throw new RuntimeException("搜索医疗器械失败: " + e.getMessage());
        }
    }
} 