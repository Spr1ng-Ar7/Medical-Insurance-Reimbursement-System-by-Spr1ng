package org.neu.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销等级DTO
 */
@Data
@Schema(description = "报销等级数据传输对象")
public class ReimbursementLevelDTO {
    
    @Schema(description = "报销等级ID")
    private Long id;
    
    @Schema(description = "等级编码", example = "LEVEL_001")
    private String levelCode;
    
    @Schema(description = "等级名称", example = "城镇职工医保一级医院")
    private String levelName;
    
    @Schema(description = "适用医保类型", example = "城镇职工", allowableValues = {"城镇职工", "城乡居民", "新农合"})
    private String insuranceType;
    
    @Schema(description = "适用医院等级", example = "一级", allowableValues = {"一级", "二级", "三级"})
    private String hospitalLevel;
    
    @Schema(description = "最低费用", example = "0.00")
    private BigDecimal minAmount;
    
    @Schema(description = "最高费用", example = "100000.00")
    private BigDecimal maxAmount;
    
    @Schema(description = "起付线", example = "200.00")
    private BigDecimal deductible;
    
    @Schema(description = "最高报销额度", example = "300000.00")
    private BigDecimal maxReimbursement;
    
    @Schema(description = "甲类药品报销比例", example = "0.90")
    private BigDecimal categoryARate;
    
    @Schema(description = "乙类药品报销比例", example = "0.80")
    private BigDecimal categoryBRate;
    
    @Schema(description = "丙类药品报销比例", example = "0.00")
    private BigDecimal categoryCRate;
    
    @Schema(description = "诊疗费用报销比例", example = "0.85")
    private BigDecimal treatmentRate;
    
    @Schema(description = "服务设施费用报销比例", example = "0.75")
    private BigDecimal serviceRate;
    
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;
    
    @Schema(description = "生效时间")
    private LocalDateTime effectiveTime;
    
    @Schema(description = "失效时间")
    private LocalDateTime expireTime;
    
    @Schema(description = "备注")
    private String remark;
} 