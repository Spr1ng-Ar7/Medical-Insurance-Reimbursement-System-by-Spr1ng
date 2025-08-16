package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 患者实体类
 */
@Data
public class Patient {
    private Long id;
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remark;             // 备注
} 