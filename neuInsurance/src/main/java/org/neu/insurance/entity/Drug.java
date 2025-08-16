package org.neu.insurance.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 药品目录实体类
 */
@Data
@ToString
public class Drug {
    private Long id;
    
    @JsonProperty("drugCode")
    private String drugCode;           // 药品编码
    
    @JsonProperty("drugName")
    private String drugName;           // 医保目录名称
    
    @JsonProperty("tradeName")
    private String tradeName;          // 商品名
    
    @JsonProperty("drugType")
    private String drugType;           // 药品类型：甲类/乙类/丙类
    
    @JsonProperty("selfPayRatio")
    private BigDecimal selfPayRatio;   // 自付比例
    
    @JsonProperty("specification")
    private String specification;      // 规格
    
    @JsonProperty("unit")
    private String unit;              // 单位
    
    @JsonProperty("price")
    private BigDecimal price;         // 支付标准（价格）
    
    @JsonProperty("manufacturer")
    private String manufacturer;       // 生产企业
    
    @JsonProperty("status")
    private Integer status;           // 状态：1-正常，0-停用
    
    @JsonProperty("createTime")
    private LocalDateTime createTime;
    
    @JsonProperty("updateTime")
    private LocalDateTime updateTime;
    
    @JsonProperty("createBy")
    private String createBy;           // 创建人
    
    @JsonProperty("updateBy")
    private String updateBy;           // 更新人
    
    @JsonProperty("remark")
    private String remark;            // 备注
} 