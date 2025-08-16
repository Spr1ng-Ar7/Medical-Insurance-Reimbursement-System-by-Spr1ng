package org.neu.insurance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 结算结果DTO
 */
@Data
public class SettlementResult {
    private Long orderId;                    // 订单ID
    private String orderNo;                  // 订单编号
    private String settlementNo;             // 结算单号
    private Long patientId;                  // 患者ID
    private String patientName;              // 患者姓名
    private Long hospitalId;                 // 医院ID
    private String hospitalName;             // 医院名称
    
    // 费用明细
    private BigDecimal totalAmount;          // 总费用
    private BigDecimal drugAmount;           // 药品费用
    private BigDecimal treatmentAmount;      // 诊疗费用
    private BigDecimal serviceAmount;        // 服务设施费用
    private BigDecimal otherAmount;          // 其他费用
    
    // 分类费用
    private BigDecimal categoryAAmount;      // 甲类药品费用
    private BigDecimal categoryBAmount;      // 乙类药品费用
    private BigDecimal categoryCAmount;      // 丙类药品费用
    
    // 报销计算
    private BigDecimal deductible;           // 起付线
    private BigDecimal reimbursementRatio;   // 报销比例
    private BigDecimal reimbursableAmount;   // 可报销金额
    private BigDecimal actualReimbursement;  // 实际报销金额
    private BigDecimal selfPayAmount;        // 自付金额
    
    // 结算信息
    private Integer status;                  // 结算状态
    private LocalDateTime settlementTime;    // 结算时间
    private String approvalResult;           // 审批结果
    private String approvalRemark;           // 审批备注
    
    // 明细列表
    private List<SettlementItemResult> items; // 结算明细
    
    // 统计信息
    private Integer totalItems;              // 总项目数
    private Integer reimbursableItems;       // 可报销项目数
    private Integer nonReimbursableItems;    // 不可报销项目数
    
    /**
     * 计算报销比例
     */
    public BigDecimal getReimbursementPercentage() {
        if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0 && actualReimbursement != null) {
            return actualReimbursement.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 计算自付比例
     */
    public BigDecimal getSelfPayPercentage() {
        if (totalAmount != null && totalAmount.compareTo(BigDecimal.ZERO) > 0 && selfPayAmount != null) {
            return selfPayAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 检查是否超过起付线
     */
    public boolean isOverDeductible() {
        return totalAmount != null && deductible != null && totalAmount.compareTo(deductible) > 0;
    }
    
    /**
     * 获取起付线内金额
     */
    public BigDecimal getDeductibleAmount() {
        if (totalAmount != null && deductible != null) {
            if (totalAmount.compareTo(deductible) <= 0) {
                return totalAmount;
            } else {
                return deductible;
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 获取起付线外金额
     */
    public BigDecimal getOverDeductibleAmount() {
        if (totalAmount != null && deductible != null) {
            if (totalAmount.compareTo(deductible) > 0) {
                return totalAmount.subtract(deductible);
            }
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * 获取状态描述
     */
    public String getStatusDescription() {
        switch (status) {
            case 1: return "待结算";
            case 2: return "已结算";
            case 3: return "已支付";
            case 4: return "待审批";
            case 5: return "已拒绝";
            case 0: return "已取消";
            default: return "未知状态";
        }
    }
    
    /**
     * 结算明细结果
     */
    @Data
    public static class SettlementItemResult {
        private Long itemId;                 // 明细ID
        private String itemType;             // 项目类型
        private String itemName;             // 项目名称
        private String specification;        // 规格
        private String unit;                 // 单位
        private BigDecimal quantity;         // 数量
        private BigDecimal unitPrice;        // 单价
        private BigDecimal totalAmount;      // 总金额
        private String category;             // 类别
        private BigDecimal selfPayRatio;     // 自付比例
        private BigDecimal selfPayAmount;    // 自付金额
        private BigDecimal reimbursableAmount; // 可报销金额
        private Boolean isReimbursable;      // 是否可报销
        private String remark;               // 备注
    }
} 