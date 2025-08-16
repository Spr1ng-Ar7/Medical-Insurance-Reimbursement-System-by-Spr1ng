package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医疗订单明细实体类
 */
@Data
public class MedicalOrderItem {
    private Long id;
    private Long orderId;              // 订单ID
    private String itemType;           // 项目类型：drug-药品/service-服务/equipment-设备
    private Long itemId;               // 关联项目ID
    private String itemCode;           // 项目编码
    private String itemName;           // 项目名称
    private String specification;      // 规格
    private String unit;               // 单位
    private BigDecimal quantity;       // 数量
    private BigDecimal unitPrice;      // 单价
    private BigDecimal totalAmount;    // 总金额
    private BigDecimal selfPayRatio;   // 自付比例
    private BigDecimal selfPayAmount;  // 自付金额
    private BigDecimal reimbursableAmount; // 可报销金额
    private String category;           // 类别：甲类/乙类/丙类
    private String serviceType;        // 服务类型（当itemType为service时）
    private String department;         // 科室
    private String doctor;             // 医生
    private LocalDateTime serviceTime; // 服务时间
    private Integer status;            // 状态：1-正常，0-取消
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;             // 备注
    
    // 关联信息
    private MedicalService medicalService; // 医疗服务信息
    private Drug drug;                 // 药品信息
    private MedicalEquipment equipment; // 设备信息
} 