package org.neu.insurance.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Excel药品数据实体类 - 用于EasyExcel导入
 */
@Data
public class ExcelDrugData {
    
    @ExcelProperty(value = "药品编码", index = 0)
    private String drugCode;
    
    @ExcelProperty(value = "药品名称", index = 1)
    private String drugName;
    
    @ExcelProperty(value = "药品类型", index = 2)
    private String drugType;
    
    @ExcelProperty(value = "规格", index = 3)
    private String specification;
    
    @ExcelProperty(value = "单位", index = 4)
    private String unit;
    
    @ExcelProperty(value = "价格", index = 5)
    private BigDecimal price;
    
    @ExcelProperty(value = "自付比例", index = 6)
    private BigDecimal selfPayRatio;
    
    @ExcelProperty(value = "生产厂家", index = 7)
    private String manufacturer;
    
    @ExcelProperty(value = "备注", index = 8)
    private String remark;
    
    /**
     * 转换为Drug实体
     */
    public Drug toDrug() {
        Drug drug = new Drug();
        drug.setDrugCode(this.drugCode);
        drug.setDrugName(this.drugName);
        drug.setDrugType(this.drugType);
        drug.setSpecification(this.specification);
        drug.setUnit(this.unit);
        drug.setPrice(this.price);
        drug.setSelfPayRatio(this.selfPayRatio);
        drug.setManufacturer(this.manufacturer);
        drug.setRemark(this.remark);
        drug.setStatus(1); // 默认状态为正常
        return drug;
    }
} 