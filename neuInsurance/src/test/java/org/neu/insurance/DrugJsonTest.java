package org.neu.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.neu.insurance.entity.Drug;

public class DrugJsonTest {
    
    @Test
    public void testDrugJsonDeserialization() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            
            String json = "{\n" +
                "  \"drugCode\": \"TEST001\",\n" +
                "  \"drugName\": \"测试药品\",\n" +
                "  \"tradeName\": \"测试商品名\",\n" +
                "  \"drugType\": \"甲类\",\n" +
                "  \"selfPayRatio\": 0.1,\n" +
                "  \"specification\": \"10mg\",\n" +
                "  \"unit\": \"片\",\n" +
                "  \"price\": 25.50,\n" +
                "  \"manufacturer\": \"测试厂家\",\n" +
                "  \"status\": 1,\n" +
                "  \"remark\": \"测试备注\"\n" +
                "}";
            
            Drug drug = objectMapper.readValue(json, Drug.class);
            
            System.out.println("解析后的Drug对象: " + drug);
            System.out.println("药品编码: " + drug.getDrugCode());
            System.out.println("药品名称: " + drug.getDrugName());
            System.out.println("药品类型: " + drug.getDrugType());
            System.out.println("生产厂家: " + drug.getManufacturer());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 