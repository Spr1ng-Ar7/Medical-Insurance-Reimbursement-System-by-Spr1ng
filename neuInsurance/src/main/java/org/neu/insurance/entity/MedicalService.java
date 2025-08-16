package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统一医疗服务实体类
 * 整合：诊疗项目、检查服务、手术服务、服务设施等
 */
@Data
public class MedicalService {
    private Long id;
    private String serviceCode;        // 项目编码
    private String nationalCode;       // 国家编码
    private String serviceName;        // 项目名称
    private String serviceType;        // 服务类型：诊疗/检查/手术/设施/化验/护理
    private String category;           // 服务类别：门诊/住院/急诊/手术室/ICU等
    private String serviceContent;     // 项目内容
    private String excludeContent;     // 除外内容
    private String unitType;           // 计价单位：次/天/小时/项等
    private BigDecimal price;          // 价格
    private BigDecimal reimbursementRatio; // 报销比例
    private String department;         // 科室
    private String doctorLevel;        // 医生级别：主治/副主任/主任
    private Integer reimbursable;      // 是否可报销：1-可报销，0-不可报销
    private String insuranceCategory;  // 医保分类：甲类/乙类/丙类
    private Integer status;            // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 说明
    
    // 扩展字段，用于特殊服务类型
    private String equipmentType;      // 设备类型（用于检查类服务）
    private String surgeryLevel;       // 手术级别（用于手术类服务）
    private String nursingLevel;       // 护理级别（用于护理类服务）
    private Integer duration;          // 服务时长（分钟）
    private String anesthesiaType;     // 麻醉类型（用于手术类服务）
} 