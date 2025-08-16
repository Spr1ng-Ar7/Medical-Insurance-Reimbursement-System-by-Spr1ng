package org.neu.insurance.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 医疗器械设备项目实体类
 */
@Data
public class MedicalEquipment {
    private Long id;
    private String financeCategory;    // 财务分类
    private String equipmentCode;      // 项目编码
    private String nationalCode;       // 国家编码
    private String equipmentName;      // 项目名称
    private String equipmentContent;   // 项目内容
    private String excludeContent;     // 除外内容
    private String unitType;           // 计价单位
    private BigDecimal price;          // 价格
    private String category;           // 设备类别
    private Integer status;            // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 说明
} 