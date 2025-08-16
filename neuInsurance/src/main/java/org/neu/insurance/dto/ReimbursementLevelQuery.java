package org.neu.insurance.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 报销等级查询DTO
 */
@Data
public class ReimbursementLevelQuery {
    
    private String levelName;        // 等级名称
    private String insuranceType;    // 医保类型
    private String hospitalLevel;    // 医院等级
    private BigDecimal minAmount;    // 最低费用
    private BigDecimal maxAmount;    // 最高费用
    private Integer status;          // 状态
    private String effectiveDate;    // 生效日期
    private String expireDate;       // 失效日期
} 