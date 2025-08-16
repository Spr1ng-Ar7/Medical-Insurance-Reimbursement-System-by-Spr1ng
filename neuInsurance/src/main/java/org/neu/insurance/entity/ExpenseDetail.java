package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用明细实体类
 */
@Data
public class ExpenseDetail {
    private Long id;
    private Long patientId;           // 患者ID
    private Long orderId;             // 医疗订单ID
    private String itemType;          // 项目类型：drug-药品/service-服务/equipment-设备
    private Long itemId;              // 项目ID
    private String itemCode;          // 项目编码
    private String itemName;          // 项目名称
    private String specification;     // 规格型号
    private String unit;              // 计量单位
    private Integer quantity;         // 数量
    private BigDecimal unitPrice;     // 单价
    private BigDecimal totalAmount;   // 总金额
    private BigDecimal reimbursementRatio; // 报销比例
    private BigDecimal reimbursementAmount; // 报销金额
    private BigDecimal selfPayAmount; // 自付金额
    private String serviceType;       // 服务类型（当itemType为service时）
    private String department;        // 科室
    private String doctor;            // 医生
    private LocalDateTime expenseDate; // 费用发生日期
    private Integer status;           // 状态：1-正常，2-已报销，0-作废
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;          // 创建人
    private String updateBy;          // 更新人
    private String remark;            // 备注
    
    // 关联信息
    private MedicalService medicalService; // 医疗服务信息
    private Drug drug;                 // 药品信息
    private MedicalEquipment equipment; // 设备信息
} 