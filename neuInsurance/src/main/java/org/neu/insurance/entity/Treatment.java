package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 诊疗项目实体类
 */
@Data
public class Treatment {
    private Long id;
    private String treatmentCode;      // 诊疗项目编码
    private String treatmentName;      // 诊疗项目名称
    private String treatmentType;      // 诊疗类型：检查/手术/化验等
    private BigDecimal price;          // 价格
    private String department;         // 科室
    private Integer reimbursable;      // 是否可报销：1-可报销，0-不可报销
    private BigDecimal reimbursementRatio; // 报销比例
    private Integer status;            // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;             // 备注
} 