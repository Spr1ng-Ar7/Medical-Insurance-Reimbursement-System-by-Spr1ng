package org.neu.insurance.service.impl;

import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.neu.insurance.entity.MedicalService;
import org.neu.insurance.mapper.MedicalServiceMapper;
import org.neu.insurance.service.MedicalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 统一医疗服务管理服务实现类
 */
@Service
public class MedicalServiceServiceImpl implements MedicalServiceService {

    @Autowired
    private MedicalServiceMapper medicalServiceMapper;

    @Override
    public PageResponse<MedicalService> getMedicalServiceList(PageRequest pageRequest, String serviceType, String category, String keyword) {
        int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
        List<MedicalService> services = medicalServiceMapper.selectByCondition(serviceType, category, keyword, offset, pageRequest.getPageSize());
        int total = medicalServiceMapper.countByCondition(serviceType, category, keyword);
        
        return new PageResponse<>(services, (long) total, pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    @Override
    public MedicalService getMedicalServiceById(Long id) {
        return medicalServiceMapper.selectById(id);
    }

    @Override
    public MedicalService getMedicalServiceByCode(String serviceCode) {
        return medicalServiceMapper.selectByServiceCode(serviceCode);
    }

    @Override
    public boolean addMedicalService(MedicalService medicalService) {
        // 检查服务编码是否存在
        if (isServiceCodeExists(medicalService.getServiceCode())) {
            return false;
        }
        
        // 设置默认值
        medicalService.setStatus(1);
        medicalService.setCreateTime(LocalDateTime.now());
        medicalService.setUpdateTime(LocalDateTime.now());
        
        return medicalServiceMapper.insert(medicalService) > 0;
    }

    @Override
    public boolean updateMedicalService(MedicalService medicalService) {
        medicalService.setUpdateTime(LocalDateTime.now());
        return medicalServiceMapper.update(medicalService) > 0;
    }

    @Override
    public boolean deleteMedicalService(Long id) {
        return medicalServiceMapper.deleteById(id) > 0;
    }

    @Override
    public boolean batchDeleteMedicalService(List<Long> ids) {
        return medicalServiceMapper.batchDelete(ids) > 0;
    }

    @Override
    public List<MedicalService> getMedicalServicesByType(String serviceType) {
        return medicalServiceMapper.selectByServiceType(serviceType);
    }

    @Override
    public List<MedicalService> getMedicalServicesByCategory(String category) {
        return medicalServiceMapper.selectByCategory(category);
    }

    @Override
    public List<MedicalService> getMedicalServicesByDepartment(String department) {
        return medicalServiceMapper.selectByDepartment(department);
    }

    @Override
    public List<MedicalService> getMedicalServicesByInsuranceCategory(String insuranceCategory) {
        return medicalServiceMapper.selectByInsuranceCategory(insuranceCategory);
    }

    @Override
    public List<MedicalService> getReimbursableServices() {
        return medicalServiceMapper.selectByReimbursable(1);
    }

    @Override
    public List<MedicalService> getNonReimbursableServices() {
        return medicalServiceMapper.selectByReimbursable(0);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        MedicalService service = new MedicalService();
        service.setId(id);
        service.setStatus(status);
        service.setUpdateTime(LocalDateTime.now());
        return medicalServiceMapper.update(service) > 0;
    }

    @Override
    public List<String> getAllServiceTypes() {
        return medicalServiceMapper.selectAllServiceTypes();
    }

    @Override
    public List<String> getAllCategories() {
        return medicalServiceMapper.selectAllCategories();
    }

    @Override
    public List<String> getAllDepartments() {
        return medicalServiceMapper.selectAllDepartments();
    }

    @Override
    public List<String> getAllInsuranceCategories() {
        return medicalServiceMapper.selectAllInsuranceCategories();
    }

    @Override
    public boolean isServiceCodeExists(String serviceCode) {
        return medicalServiceMapper.countByServiceCode(serviceCode) > 0;
    }

    @Override
    public boolean isNationalCodeExists(String nationalCode) {
        return medicalServiceMapper.countByNationalCode(nationalCode) > 0;
    }

    @Override
    public List<MedicalService> getMedicalServicesByPriceRange(Double minPrice, Double maxPrice) {
        return medicalServiceMapper.selectByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<MedicalService> getMedicalServicesByReimbursementRatio(Double minRatio, Double maxRatio) {
        return medicalServiceMapper.selectByReimbursementRatio(minRatio, maxRatio);
    }
} 