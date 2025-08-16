package org.neu.insurance.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 医院实体类
 */
@Data
public class Hospital {
    private Long id;
    private String hospitalCode;       // 医院编码
    private String hospitalName;       // 医院名称
    private String hospitalLevel;      // 医院等级：一级/二级/三级
    private String hospitalType;       // 医院类型：公立/私立
    private String address;            // 地址
    private String province;           // 省份
    private String city;               // 城市
    private String district;           // 区县
    private String phone;              // 联系电话
    private String contactPerson;      // 联系人
    private String email;              // 邮箱
    private String website;            // 网站
    private String description;        // 描述
    private Integer status;            // 状态：1-正常，0-停用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;           // 创建人
    private String updateBy;           // 更新人
    private String remark;             // 备注
} 