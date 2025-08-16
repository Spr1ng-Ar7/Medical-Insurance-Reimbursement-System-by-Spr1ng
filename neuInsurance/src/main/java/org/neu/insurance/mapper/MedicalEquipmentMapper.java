package org.neu.insurance.mapper;

import org.neu.insurance.entity.MedicalEquipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 医疗器械数据访问层接口
 */
@Mapper
public interface MedicalEquipmentMapper {
    
    /**
     * 根据ID查询医疗器械
     */
    MedicalEquipment selectById(@Param("id") Long id);
    
    /**
     * 根据器械编码查询医疗器械
     */
    MedicalEquipment selectByEquipmentCode(@Param("equipmentCode") String equipmentCode);
    
    /**
     * 分页查询医疗器械列表
     */
    List<MedicalEquipment> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询医疗器械总数
     */
    long count();
    
    /**
     * 根据器械类型查询医疗器械列表
     */
    List<MedicalEquipment> selectByEquipmentType(@Param("equipmentType") String equipmentType);
    
    /**
     * 插入医疗器械
     */
    void insert(MedicalEquipment medicalEquipment);
    
    /**
     * 更新医疗器械信息
     */
    void update(MedicalEquipment medicalEquipment);
    
    /**
     * 根据ID删除医疗器械
     */
    void deleteById(@Param("id") Long id);
    
    /**
     * 批量删除医疗器械
     */
    void batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 更新医疗器械状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 检查器械编码是否存在
     */
    boolean existsByEquipmentCode(@Param("equipmentCode") String equipmentCode);
    
    /**
     * 模糊搜索医疗器械名称
     */
    List<MedicalEquipment> searchByName(@Param("keyword") String keyword);
} 