package org.neu.insurance.mapper;

import org.neu.insurance.entity.ReimbursementLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 报销等级Mapper接口
 */
@Mapper
public interface ReimbursementLevelMapper {
    
    /**
     * 分页查询报销等级列表
     */
    List<ReimbursementLevel> selectReimbursementLevelList(@Param("levelName") String levelName,
                                                          @Param("insuranceType") String insuranceType,
                                                          @Param("hospitalLevel") String hospitalLevel,
                                                          @Param("status") Integer status);
    
    /**
     * 根据ID查询报销等级
     */
    ReimbursementLevel selectReimbursementLevelById(@Param("id") Long id);
    
    /**
     * 获取有效的报销等级列表
     */
    List<ReimbursementLevel> selectEffectiveReimbursementLevels();
    
    /**
     * 根据条件匹配报销等级
     */
    List<ReimbursementLevel> selectMatchingReimbursementLevels(@Param("insuranceType") String insuranceType,
                                                               @Param("hospitalLevel") String hospitalLevel);
    
    /**
     * 新增报销等级
     */
    int insertReimbursementLevel(ReimbursementLevel level);
    
    /**
     * 更新报销等级
     */
    int updateReimbursementLevel(ReimbursementLevel level);
    
    /**
     * 删除报销等级
     */
    int deleteReimbursementLevel(@Param("id") Long id);
    
    /**
     * 批量删除报销等级
     */
    int batchDeleteReimbursementLevel(@Param("ids") List<Long> ids);
    
    /**
     * 检查等级编码是否唯一
     */
    int checkLevelCodeUnique(@Param("levelCode") String levelCode, @Param("excludeId") Long excludeId);
    
    /**
     * 根据等级编码查询报销等级
     */
    ReimbursementLevel selectByLevelCode(@Param("levelCode") String levelCode);
    
    /**
     * 更新状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    /**
     * 获取所有报销等级（不分页）
     */
    List<ReimbursementLevel> selectAllReimbursementLevels();
} 