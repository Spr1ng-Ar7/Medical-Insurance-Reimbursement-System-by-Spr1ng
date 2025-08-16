package org.neu.insurance.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者创建DTO - 排除ID字段
 */
@Data
public class PatientCreateDTO {
    private String patientId;          // 患者编号
    private String name;               // 姓名
    private String idCard;             // 身份证号
    private String gender;             // 性别
    private LocalDate birthDate;       // 出生日期
    private String phone;              // 联系电话
    private String address;            // 地址
    private String insuranceType;      // 医保类型：城镇职工/城乡居民等
    private String insuranceNumber;    // 医保号
    private Integer status;            // 状态：1-正常，0-注销
    private String remark;             // 备注
} 