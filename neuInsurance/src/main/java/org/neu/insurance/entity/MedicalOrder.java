package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 医疗订单实体类（整合结算功能）
 */
@Data
public class MedicalOrder {
    private Long id;
    private String orderNo;            // 订单编号
    private Long patientId;            // 患者ID
    private String patientName;        // 患者姓名
    private String patientIdCard;      // 患者身份证
    private Long hospitalId;           // 医院ID
    private String hospitalName;       // 医院名称
    private String hospitalLevel;      // 医院等级
    private Long doctorId;             // 医生ID
    private String doctorName;         // 医生姓名
    private String departmentName;     // 科室名称
    private String diagnosis;          // 诊断信息
    private String orderType;          // 订单类型：门诊/住院/急诊
    private LocalDateTime visitTime;   // 就诊时间
    private LocalDateTime dischargeTime; // 出院时间（住院）
    private Integer stayDays;          // 住院天数
    
    // 费用信息
    private BigDecimal totalAmount;    // 总费用
    private BigDecimal drugAmount;     // 药品费用
    private BigDecimal treatmentAmount; // 诊疗费用
    private BigDecimal serviceAmount;  // 服务设施费用
    private BigDecimal otherAmount;    // 其他费用
    
    // 结算信息（整合自Settlement）
    private BigDecimal categoryAAmount; // 甲类药品费用 (对应数据库字段 category_a_amount)
    private BigDecimal categoryBAmount; // 乙类药品费用 (对应数据库字段 category_b_amount)
    private BigDecimal categoryCAmount; // 丙类药品费用 (对应数据库字段 category_c_amount)
    private BigDecimal deductible;      // 起付线
    private BigDecimal reimbursementRatio; // 报销比例 (对应数据库字段 reimbursement_ratio)
    private BigDecimal reimbursableAmount; // 可报销金额 (对应数据库字段 reimbursable_amount)
    private BigDecimal actualReimbursement; // 实际报销金额 (对应数据库字段 actual_reimbursement)
    private BigDecimal selfPayAmount;   // 自付金额 (对应数据库字段 self_pay_amount)
    private String settlementNo;        // 结算单号 (对应数据库字段 settlement_no)
    private LocalDateTime settlementTime; // 结算时间 (对应数据库字段 settlement_time)
    private String approvalResult;      // 审批结果 (对应数据库字段 approval_result)
    private String approvalRemark;      // 审批备注 (对应数据库字段 approval_remark)
    private String rejectReason;        // 拒绝原因 (对应数据库字段 reject_reason)
    
    // 状态管理
    private Integer status;            // 状态：1-待结算，2-已结算，3-已支付，0-已取消，4-待审批，5-已拒绝
    private LocalDateTime createTime;  // 创建时间 (对应数据库字段 create_time)
    private LocalDateTime updateTime;  // 更新时间 (对应数据库字段 update_time)
    private String createBy;           // 创建人 (对应数据库字段 create_by)
    private String updateBy;           // 更新人 (对应数据库字段 update_by)
    private String remark;             // 备注
    
    // 关联信息（数据库映射时使用）
    private Patient patient;           // 患者信息
    private Hospital hospital;         // 医院信息
    private Admin doctor;              // 医生信息
    private List<MedicalOrderItem> items; // 订单明细
    
    // 状态常量
    public static final int STATUS_PENDING = 1;      // 待结算
    public static final int STATUS_SETTLED = 2;      // 已结算
    public static final int STATUS_PAID = 3;         // 已支付
    public static final int STATUS_CANCELLED = 0;    // 已取消
    public static final int STATUS_PENDING_APPROVAL = 4; // 待审批
    public static final int STATUS_REJECTED = 5;     // 已拒绝
    
    // 订单类型常量
    public static final String TYPE_OUTPATIENT = "门诊";
    public static final String TYPE_INPATIENT = "住院";
    public static final String TYPE_EMERGENCY = "急诊";
    
    /**
     * 获取状态描述
     */
    public String getStatusDescription() {
        switch (status) {
            case STATUS_PENDING: return "待结算";
            case STATUS_SETTLED: return "已结算";
            case STATUS_PAID: return "已支付";
            case STATUS_CANCELLED: return "已取消";
            case STATUS_PENDING_APPROVAL: return "待审批";
            case STATUS_REJECTED: return "已拒绝";
            default: return "未知状态";
        }
    }
    
    /**
     * 检查是否可以结算
     */
    public boolean canSettle() {
        return status == STATUS_PENDING || status == STATUS_PENDING_APPROVAL;
    }
    
    /**
     * 检查是否可以审批
     */
    public boolean canApprove() {
        return status == STATUS_PENDING_APPROVAL;
    }
    
    /**
     * 检查是否可以支付
     */
    public boolean canPay() {
        return status == STATUS_SETTLED;
    }
    
    /**
     * 检查是否可以取消
     */
    public boolean canCancel() {
        return status == STATUS_PENDING || status == STATUS_PENDING_APPROVAL;
    }
    
    /**
     * 计算总报销金额
     */
    public BigDecimal calculateTotalReimbursement() {
        if (reimbursableAmount != null && actualReimbursement != null) {
            return actualReimbursement;
        }
        return reimbursableAmount != null ? reimbursableAmount : BigDecimal.ZERO;
    }
    
    /**
     * 计算总自付金额
     */
    public BigDecimal calculateTotalSelfPay() {
        if (selfPayAmount != null) {
            return selfPayAmount;
        }
        if (totalAmount != null && reimbursableAmount != null) {
            return totalAmount.subtract(reimbursableAmount);
        }
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }
} 