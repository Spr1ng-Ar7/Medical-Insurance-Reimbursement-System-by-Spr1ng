package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医保结算实体类
 */
@Data
public class Settlement {
    private Long id;
    private String settlementNo;       // 结算单号
    private Long patientId;            // 患者ID
    private Long hospitalId;           // 医院ID
    private BigDecimal totalAmount;    // 总费用
    private BigDecimal categoryAAmount; // 甲类药品费用
    private BigDecimal categoryBAmount; // 乙类药品费用
    private BigDecimal categoryCAmount; // 丙类药品费用
    private BigDecimal treatmentAmount; // 诊疗费用
    private BigDecimal serviceAmount;   // 服务设施费用
    private BigDecimal deductible;      // 起付线
    private BigDecimal reimbursementRatio; // 报销比例
    private BigDecimal reimbursableAmount; // 可报销金额
    private BigDecimal actualReimbursement; // 实际报销金额
    private BigDecimal selfPayAmount;   // 自付金额
    private Integer status;             // 状态：1-已结算，2-已支付，0-取消
    private LocalDateTime settlementTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;              // 备注
} 