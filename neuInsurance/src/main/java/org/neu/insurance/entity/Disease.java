package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 疾病编码实体类
 */
@Data
public class Disease {
    private Long id;
    private String icdCode;            // 国际ICD编码
    private String diseaseCode;        // 疾病编码
    private String diseaseName;        // 疾病名称
    private String diseaseCategory;    // 疾病分类
    private String description;        // 疾病描述
    private Integer status;            // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
} 