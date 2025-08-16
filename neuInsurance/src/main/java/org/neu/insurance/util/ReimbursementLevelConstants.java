package org.neu.insurance.util;

/**
 * 报销等级常量类
 */
public class ReimbursementLevelConstants {
    
    /**
     * 医保类型常量
     */
    public static class InsuranceType {
        public static final String URBAN_EMPLOYEE = "城镇职工";      // 城镇职工医保
        public static final String URBAN_RESIDENT = "城乡居民";      // 城乡居民医保
        public static final String NEW_RURAL = "新农合";            // 新型农村合作医疗
        public static final String MEDICAL_AID = "医疗救助";        // 医疗救助
        public static final String COMMERCIAL = "商业保险";         // 商业保险
    }
    
    /**
     * 医院等级常量
     */
    public static class HospitalLevel {
        public static final String LEVEL_ONE = "一级";              // 一级医院
        public static final String LEVEL_TWO = "二级";              // 二级医院
        public static final String LEVEL_THREE = "三级";            // 三级医院
        public static final String SPECIALIZED = "专科";            // 专科医院
        public static final String COMMUNITY = "社区卫生服务中心";   // 社区卫生服务中心
        public static final String CLINIC = "诊所";                // 诊所
    }
    
    /**
     * 状态常量
     */
    public static class Status {
        public static final int DISABLED = 0;                       // 禁用
        public static final int ENABLED = 1;                        // 启用
    }
    
    /**
     * 默认报销比例
     */
    public static class DefaultRates {
        public static final String CATEGORY_A_RATE = "100";         // 甲类药品默认报销比例100%
        public static final String CATEGORY_B_RATE = "80";          // 乙类药品默认报销比例80%
        public static final String CATEGORY_C_RATE = "0";           // 丙类药品默认报销比例0%
        public static final String TREATMENT_RATE = "90";           // 诊疗费用默认报销比例90%
        public static final String SERVICE_RATE = "85";             // 服务设施费用默认报销比例85%
    }
    
    /**
     * 默认金额
     */
    public static class DefaultAmounts {
        public static final String DEDUCTIBLE = "1000";             // 默认起付线1000元
        public static final String MAX_REIMBURSEMENT = "50000";     // 默认最高报销额度50000元
    }
    
    /**
     * 等级编码前缀
     */
    public static class LevelCodePrefix {
        public static final String URBAN_EMPLOYEE = "UE";           // 城镇职工
        public static final String URBAN_RESIDENT = "UR";           // 城乡居民
        public static final String NEW_RURAL = "NR";                // 新农合
        public static final String MEDICAL_AID = "MA";              // 医疗救助
        public static final String COMMERCIAL = "CM";               // 商业保险
    }
    
    /**
     * 医院等级编码
     */
    public static class HospitalLevelCode {
        public static final String LEVEL_ONE = "L1";                // 一级医院
        public static final String LEVEL_TWO = "L2";                // 二级医院
        public static final String LEVEL_THREE = "L3";              // 三级医院
        public static final String SPECIALIZED = "SP";              // 专科医院
        public static final String COMMUNITY = "CM";                // 社区卫生服务中心
        public static final String CLINIC = "CL";                   // 诊所
    }
} 