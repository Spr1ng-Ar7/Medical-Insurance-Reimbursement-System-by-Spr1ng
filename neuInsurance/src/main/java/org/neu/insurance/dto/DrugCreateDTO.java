package org.neu.insurance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 药品创建DTO - 排除ID字段
 */
@Data
public class DrugCreateDTO {
    private String drugCode;           // 药品编码
    private String drugName;           // 医保目录名称
    private String tradeName;          // 商品名
    private String drugType;           // 药品类型：甲类/乙类/丙类
    private BigDecimal selfPayRatio;   // 自付比例
    private String specification;      // 规格
    private String unit;              // 单位
    private BigDecimal price;         // 支付标准（价格）
    private String manufacturer;       // 生产企业
    private Integer status;           // 状态：1-正常，0-停用
    private String createBy;           // 创建人
    private String remark;            // 备注
} 