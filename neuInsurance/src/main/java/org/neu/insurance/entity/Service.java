package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 服务设施实体类
 */
@Data
public class Service {
    private Long id;
    private String serviceCode;        // 服务设施编码
    private String serviceName;        // 服务设施名称
    private String serviceType;        // 服务类型：床位费/护理费等
    private BigDecimal price;          // 价格
    private String unit;              // 单位
    private Integer reimbursable;      // 是否可报销：1-可报销，0-不可报销
    private BigDecimal reimbursementRatio; // 报销比例
    private Integer status;            // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;             // 备注
} 