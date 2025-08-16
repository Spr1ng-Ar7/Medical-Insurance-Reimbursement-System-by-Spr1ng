import { http } from "@/utils/http";

export interface DrugResponse {
  code: number;
  message: string;
  data: Drug;
}

export interface Drug {
  id: number;
  drugCode: string;           // 药品编码
  drugName: string;           // 医保目录名称 
  tradeName?: string;         // 商品名
  drugType: string;           // 药品类型：甲类/乙类/丙类
  selfPayRatio?: number;      // 自付比例
  specification: string;      // 规格
  unit?: string;             // 单位
  price: number;             // 支付标准（价格）
  manufacturer: string;       // 生产企业
  status: number;            // 状态：1-正常，0-停用
  createTime?: string;
  updateTime?: string;
  createBy?: string;          // 创建人
  updateBy?: string;          // 更新人
  remark?: string;           // 备注
}

export interface DrugQueryParams {
  pageNum: number;
  pageSize: number;
  drugName?: string;    // 后端使用drugName而不是name
  drugType?: string;    // 后端使用drugType而不是category
  status?: number;      // 后端使用数字状态
  manufacturer?: string;
  keyword?: string;
}

export interface DrugListResult {
  records: Drug[];
  total: number;
  size: number;
  current: number;
}

// 数据转换辅助函数
const safeNumber = (value: any, defaultValue: number = 0): number => {
  if (value === null || value === undefined || value === '') {
    return defaultValue;
  }
  const num = Number(value);
  return isNaN(num) ? defaultValue : num;
};

const safeString = (value: any, defaultValue: string = ''): string => {
  if (value === null || value === undefined) {
    return defaultValue;
  }
  return String(value);
};

/** 获取药品列表 */
export const getDrugList = (params: DrugQueryParams) => {
  console.log('发送药品列表请求，参数:', params);
  return http.request<any>("get", "/api/drug/list", { params }).then((response) => {
    console.log('药品列表原始响应:', response);
    
    // 检查响应格式
    if (!response) {
      throw new Error('响应为空');
    }
    
    let data;
    if (response.data) {
      // 标准格式: { code, message, data }
      data = response.data;
      console.log('使用标准格式响应，data:', data);
    } else {
      // 直接返回数据
      data = response;
      console.log('使用直接格式响应，data:', data);
    }
    
    // 确保data.records存在且为数组
    if (!data.records || !Array.isArray(data.records)) {
      console.error('无效的records数据:', data);
      return {
        records: [],
        total: 0,
        size: params.pageSize,
        current: params.pageNum
      } as DrugListResult;
    }
    
    console.log('原始records:', data.records);
    
    // 处理每个记录，添加详细日志
    const processedRecords = data.records.map((item: any, index: number) => {
      console.log(`处理第${index + 1}个药品原始数据:`, JSON.stringify(item, null, 2));
      
      // 检查所有可能的字段名
      const drugNameCandidates = [item.drugName, item.drug_name, item.name, item.drugname];
      const drugTypeCandidates = [item.drugType, item.drug_type, item.type, item.category, item.drugtype];
      
      const finalDrugName = drugNameCandidates.find(val => val !== null && val !== undefined && val !== '') || '未知药品';
      const finalDrugType = drugTypeCandidates.find(val => val !== null && val !== undefined && val !== '') || '未分类';
      
      console.log(`字段提取结果 - 药品名称候选: [${drugNameCandidates.join(', ')}] -> 最终: ${finalDrugName}`);
      console.log(`字段提取结果 - 药品类型候选: [${drugTypeCandidates.join(', ')}] -> 最终: ${finalDrugType}`);
      
      const processed = {
        id: safeNumber(item.id),
        drugCode: safeString(item.drugCode || item.drug_code),
        drugName: finalDrugName,
        tradeName: safeString(item.tradeName || item.trade_name),
        drugType: finalDrugType,
        selfPayRatio: safeNumber(item.selfPayRatio || item.self_pay_ratio),
        specification: safeString(item.specification),
        unit: safeString(item.unit),
        price: safeNumber(item.price),
        manufacturer: safeString(item.manufacturer),
        status: safeNumber(item.status, 1),
        createTime: safeString(item.createTime || item.create_time),
        updateTime: safeString(item.updateTime || item.update_time),
        createBy: safeString(item.createBy || item.create_by),
        updateBy: safeString(item.updateBy || item.update_by),
        remark: safeString(item.remark)
      };
      
      console.log(`第${index + 1}个药品处理结果:`, processed);
      return processed;
    });
    
    const result = {
      records: processedRecords,
      total: safeNumber(data.total),
      size: safeNumber(data.size || data.pageSize, params.pageSize),
      current: safeNumber(data.current || data.pageNum, params.pageNum)
    } as DrugListResult;
    
    console.log('最终返回结果:', result);
    return result;
  }).catch((error) => {
    console.error('获取药品列表失败:', error);
    throw error;
  });
};

/** 获取药品详情 */
export const getDrugDetail = (id: number) => {
  return http.request<Drug>("get", `/api/drug/${id}`);
};

/** 新增药品 */
export const addDrug = (data: Drug) => {
  console.log('新增药品请求数据:', data);
  
  // 数据验证
  if (!data.drugCode || !data.drugName || !data.drugType || !data.manufacturer) {
    throw new Error('请填写必填字段：药品编码、药品名称、药品类型、生产厂家');
  }
  
  // 确保数据格式正确，处理字段映射
  const requestData = {
    drugCode: data.drugCode?.trim() || '',
    drugName: data.drugName?.trim() || '',
    tradeName: data.tradeName?.trim() || null,
    drugType: data.drugType?.trim() || '',
    selfPayRatio: data.selfPayRatio || 0,
    specification: data.specification?.trim() || null,
    unit: data.unit?.trim() || null,
    price: data.price || 0,
    manufacturer: data.manufacturer?.trim() || '',
    status: data.status || 1,
    remark: data.remark?.trim() || null
  };
  
  console.log('处理后的请求数据:', requestData);
  
  return http.request("post", "/api/drug", { data: requestData }).then((response) => {
    console.log('新增药品API响应:', response);
    return response;
  }).catch((error) => {
    console.error('新增药品API错误:', error);
    throw error;
  });
};

/** 更新药品信息 */
export const updateDrug = (data: Drug) => {
  return http.request("put", `/api/drug/${data.id}`, { data });
};

/** 删除药品 */
export const deleteDrug = (id: number) => {
  return http.request("delete", `/api/drug/${id}`);
};

/** 批量新增药品 */
export const batchAddDrugs = (data: Drug[]) => {
  return http.request("post", "/api/drug/batch", { data });
};

/** 根据药品编码查询药品 */
export const getDrugByCode = (drugCode: string) => {
  return http.request<Drug>("get", `/api/drug/code/${drugCode}`);
};

/** 获取药品类型统计 */
export const getDrugTypeStatistics = () => {
  return http.request("get", "/api/drug/statistics/type");
};

/** 获取药品状态统计 */
export const getDrugStatusStatistics = () => {
  return http.request("get", "/api/drug/statistics/status");
};

/** 获取生产厂家统计 */
export const getManufacturerStatistics = () => {
  return http.request("get", "/api/drug/statistics/manufacturer");
};

/** 获取药品价格统计 */
export const getDrugPriceStatistics = () => {
  return http.request("get", "/api/drug/statistics/price");
};

/** 获取综合统计信息 */
export const getDrugComprehensiveStatistics = () => {
  return http.request("get", "/api/drug/statistics/comprehensive");
};