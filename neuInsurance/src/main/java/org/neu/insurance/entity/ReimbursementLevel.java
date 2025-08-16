package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销等级配置实体类
 */
@Data
public class ReimbursementLevel {
    private Long id;
    private String levelCode;          // 等级编码
    private String levelName;          // 等级名称
    private String insuranceType;      // 适用医保类型：城镇职工/城乡居民等
    private String hospitalLevel;      // 适用医院等级：一级/二级/三级
    private BigDecimal minAmount;      // 最低费用
    private BigDecimal maxAmount;      // 最高费用
    private BigDecimal deductible;     // 起付线
    private BigDecimal maxReimbursement; // 最高报销额度
    private BigDecimal categoryARate;  // 甲类药品报销比例
    private BigDecimal categoryBRate;  // 乙类药品报销比例
    private BigDecimal categoryCRate;  // 丙类药品报销比例（通常为0）
    private BigDecimal treatmentRate;  // 诊疗费用报销比例
    private BigDecimal serviceRate;    // 服务设施费用报销比例
    private Integer status;            // 状态：1-正常，0-禁用
    private LocalDateTime effectiveTime; // 生效时间
    private LocalDateTime expireTime;    // 失效时间
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
} 