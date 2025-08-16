package org.neu.insurance.mapper;

import org.neu.insurance.entity.Disease;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 疾病管理数据访问层接口
 */
@Mapper
public interface DiseaseMapper {
    
    /**
     * 根据ID查询疾病
     */
    Disease selectById(@Param("id") Long id);
    
    /**
     * 根据疾病编码查询疾病
     */
    Disease selectByDiseaseCode(@Param("diseaseCode") String diseaseCode);
    
    /**
     * 根据ICD编码查询疾病
     */
    Disease selectByIcdCode(@Param("icdCode") String icdCode);
    
    /**
     * 分页查询疾病列表
     */
    List<Disease> selectPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 查询疾病总数
     */
    long count();
    
    /**
     * 根据疾病分类查询疾病列表
     */
    List<Disease> selectByCategory(@Param("category") String category);
    
    /**
     * 插入疾病
     */
    void insert(Disease disease);
    
    /**
     * 更新疾病信息
     */
    void update(Disease disease);
    
    /**
     * 根据ID删除疾病
     */
    void deleteById(@Param("id") Long id);
    
    /**
     * 批量删除疾病
     */
    void batchDelete(@Param("ids") List<Long> ids);
    
    /**
     * 更新疾病状态
     */
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 检查疾病编码是否存在
     */
    boolean existsByDiseaseCode(@Param("diseaseCode") String diseaseCode);
    
    /**
     * 检查ICD编码是否存在
     */
    boolean existsByIcdCode(@Param("icdCode") String icdCode);
    
    /**
     * 模糊搜索疾病名称
     */
    List<Disease> searchByName(@Param("keyword") String keyword);
} 