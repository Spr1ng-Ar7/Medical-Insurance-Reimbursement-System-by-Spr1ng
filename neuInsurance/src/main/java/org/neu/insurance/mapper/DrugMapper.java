package org.neu.insurance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.neu.insurance.entity.Drug;

import java.util.List;

/**
 * 药品数据访问接口
 */
@Mapper
public interface DrugMapper {
    
    /**
     * 统计药品总数
     */
    int countDrugs(@Param("drugCode") String drugCode, 
                   @Param("drugName") String drugName, 
                   @Param("drugType") String drugType, 
                   @Param("manufacturer") String manufacturer, 
                   @Param("keyword") String keyword);
    
    /**
     * 分页查询药品列表
     */
    List<Drug> getDrugList(@Param("offset") int offset, 
                           @Param("pageSize") int pageSize, 
                           @Param("drugCode") String drugCode, 
                           @Param("drugName") String drugName, 
                           @Param("drugType") String drugType, 
                           @Param("manufacturer") String manufacturer, 
                           @Param("keyword") String keyword);
    
    /**
     * 根据ID查询药品
     */
    Drug getDrugById(@Param("id") Long id);
    
    /**
     * 根据药品编码查询药品
     */
    Drug getDrugByCode(@Param("drugCode") String drugCode);
    
    /**
     * 根据药品名称查询药品列表
     */
    List<Drug> getDrugsByName(@Param("drugName") String drugName);
    
    /**
     * 根据药品类型查询药品列表
     */
    List<Drug> getDrugsByType(@Param("drugType") String drugType);
    
    /**
     * 根据生产厂家查询药品列表
     */
    List<Drug> getDrugsByManufacturer(@Param("manufacturer") String manufacturer);
    
    /**
     * 新增药品
     */
    int addDrug(Drug drug);
    
    /**
     * 更新药品信息
     */
    int updateDrug(Drug drug);
    
    /**
     * 删除药品
     */
    int deleteDrug(@Param("id") Long id);
    
    /**
     * 批量删除药品
     */
    int batchDeleteDrugs(@Param("ids") List<Long> ids);
    
    /**
     * 根据自付比例范围查询药品
     */
    List<Drug> getDrugsBySelfPayRatio(@Param("minRatio") Double minRatio, @Param("maxRatio") Double maxRatio);
    
    /**
     * 根据价格范围查询药品
     */
    List<Drug> getDrugsByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    /**
     * 查询可报销的药品
     */
    List<Drug> getReimbursableDrugs();
    
    /**
     * 查询不可报销的药品
     */
    List<Drug> getNonReimbursableDrugs();
    
    /**
     * 更新药品状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 获取所有药品类型
     */
    List<String> getAllDrugTypes();
    
    /**
     * 获取所有生产厂家
     */
    List<String> getAllManufacturers();
    
    /**
     * 检查药品编码是否存在
     */
    boolean isDrugCodeExists(@Param("drugCode") String drugCode);
    
    /**
     * 检查药品编码是否存在（排除指定ID）
     */
    boolean isDrugCodeExistsExcludeId(@Param("drugCode") String drugCode, @Param("excludeId") Long excludeId);
    
    /**
     * 根据关键词搜索药品
     */
    List<Drug> searchDrugs(@Param("keyword") String keyword);
    
    /**
     * 获取药品统计信息
     */
    Object getDrugStatistics();
    
    /**
     * 根据规格查询药品列表
     */
    List<Drug> getDrugsBySpecification(@Param("specification") String specification);
    
    /**
     * 根据单位查询药品列表
     */
    List<Drug> getDrugsByUnit(@Param("unit") String unit);
    
    /**
     * 获取所有规格
     */
    List<String> getAllSpecifications();
    
    /**
     * 获取所有单位
     */
    List<String> getAllUnits();
    
    /**
     * 根据商品名查询药品列表
     */
    List<Drug> getDrugsByTradeName(@Param("tradeName") String tradeName);
    
    /**
     * 检查商品名是否存在
     */
    boolean isTradeNameExists(@Param("tradeName") String tradeName);
    
    /**
     * 根据医保分类查询药品列表
     */
    List<Drug> getDrugsByInsuranceCategory(@Param("category") String category);
    
    /**
     * 获取所有医保分类
     */
    List<String> getAllInsuranceCategories();
    
    /**
     * 获取所有药品
     */
    List<Drug> getAllDrugs();
}
