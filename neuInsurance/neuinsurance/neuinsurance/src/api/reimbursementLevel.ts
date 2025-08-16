import { http } from "@/utils/http";

// 辅助函数：安全获取数字值
const safeNumber = (value: any): number => {
  if (value === null || value === undefined || value === '') return 0;
  const num = Number(value);
  return isNaN(num) ? 0 : num;
};

// 辅助函数：安全获取字符串值
const safeString = (value: any): string => {
  if (value === null || value === undefined) return '';
  return String(value);
};

// 下划线转驼峰
const toCamel = (obj: any): any => {
  if (Array.isArray(obj)) {
    return obj.map(toCamel);
  }
  if (obj !== null && typeof obj === 'object') {
    return Object.keys(obj).reduce((acc, key) => {
      const camelKey = key.replace(/_([a-z])/g, (_, c) => c.toUpperCase());
      acc[camelKey] = toCamel(obj[key]);
      return acc;
    }, {} as any);
  }
  return obj;
};

// 数据处理函数：处理报销等级数据
const processLevelData = (data: any): any => {
  if (!data) return data;
  
  // 转换下划线字段为驼峰
  const camelData = toCamel(data);
  
  // 详细的字段映射和处理
  const processed = {
    id: camelData.id || data.id,
    levelCode: safeString(camelData.levelCode || camelData.level_code || data.levelCode || data.level_code),
    levelName: safeString(camelData.levelName || camelData.level_name || data.levelName || data.level_name),
    insuranceType: safeString(camelData.insuranceType || camelData.insurance_type || data.insuranceType || data.insurance_type),
    hospitalLevel: safeString(camelData.hospitalLevel || camelData.hospital_level || data.hospitalLevel || data.hospital_level),
    minAmount: safeNumber(camelData.minAmount || camelData.min_amount || data.minAmount || data.min_amount),
    maxAmount: safeNumber(camelData.maxAmount || camelData.max_amount || data.maxAmount || data.max_amount),
    deductible: safeNumber(camelData.deductible || data.deductible),
    maxReimbursement: safeNumber(camelData.maxReimbursement || camelData.max_reimbursement || data.maxReimbursement || data.max_reimbursement),
    categoryARate: safeNumber(camelData.categoryARate || camelData.category_a_rate || data.categoryARate || data.category_a_rate),
    categoryBRate: safeNumber(camelData.categoryBRate || camelData.category_b_rate || data.categoryBRate || data.category_b_rate),
    categoryCRate: safeNumber(camelData.categoryCRate || camelData.category_c_rate || data.categoryCRate || data.category_c_rate),
    treatmentRate: safeNumber(camelData.treatmentRate || camelData.treatment_rate || data.treatmentRate || data.treatment_rate),
    serviceRate: safeNumber(camelData.serviceRate || camelData.service_rate || data.serviceRate || data.service_rate),
    status: camelData.status !== undefined ? camelData.status : (data.status !== undefined ? data.status : 1),
    effectiveTime: safeString(camelData.effectiveTime || camelData.effective_time || data.effectiveTime || data.effective_time),
    expireTime: safeString(camelData.expireTime || camelData.expire_time || data.expireTime || data.expire_time),
    createTime: safeString(camelData.createTime || camelData.create_time || data.createTime || data.create_time),
    updateTime: safeString(camelData.updateTime || camelData.update_time || data.updateTime || data.update_time),
    createBy: safeString(camelData.createBy || camelData.create_by || data.createBy || data.create_by),
    updateBy: safeString(camelData.updateBy || camelData.update_by || data.updateBy || data.update_by),
    remark: safeString(camelData.remark || data.remark)
  };

  console.log('报销等级原始数据:', data);
  console.log('报销等级处理后数据:', processed);
  console.log('关键字段检查:', {
    levelName: processed.levelName,
    insuranceType: processed.insuranceType,
    hospitalLevel: processed.hospitalLevel,
    deductible: processed.deductible,
    maxReimbursement: processed.maxReimbursement
  });

  return processed;
};

export interface ReimbursementLevel {
  id?: number;
  levelCode?: string; // 等级编码
  levelName: string; // 等级名称
  insuranceType: string; // 医保类型
  hospitalLevel: string; // 医院等级：一级/二级/三级
  minAmount?: number; // 最低费用
  maxAmount?: number; // 最高费用
  deductible?: number; // 起付线
  maxReimbursement?: number; // 最高报销额度
  categoryARate?: number; // 甲类药品报销比例
  categoryBRate?: number; // 乙类药品报销比例
  categoryCRate?: number; // 丙类药品报销比例
  treatmentRate?: number; // 诊疗费用报销比例
  serviceRate?: number; // 服务设施费用报销比例
  status?: number; // 1=启用 0=停用
  effectiveTime?: string; // 生效时间
  expireTime?: string; // 失效时间
  createTime?: string;
  updateTime?: string;
  createBy?: string;
  updateBy?: string;
  remark?: string;
}

export interface LevelQueryParams {
  pageNum: number;
  pageSize: number;
  levelName?: string;
  insuranceType?: string;
  hospitalLevel?: string;
  status?: number;
}

export interface LevelPageResult {
  records: ReimbursementLevel[];
  total: number;
  size: number;
  current: number;
}

/** 分页查询报销等级 */
export const getLevelList = async (params: LevelQueryParams) => {
  try {
    const response = await http.request<any>("post", "/api/reimbursement-level/list", {
      data: {
        pageNum: params.pageNum,
        pageSize: params.pageSize
      },
      params: {
        levelName: params.levelName,
        insuranceType: params.insuranceType,
        hospitalLevel: params.hospitalLevel,
        status: params.status
      }
    });

    console.log('报销等级列表API响应:', response);
    
    // 处理响应数据
    const data = response?.data || response;
    const records = data?.records || data?.list || [];
    
    // 处理每个报销等级数据
    const processedRecords = records.map((level: any) => processLevelData(level));
    
    return {
      records: processedRecords,
      total: data?.total || data?.count || processedRecords.length,
      size: data?.size || data?.pageSize || params.pageSize,
      current: data?.current || data?.pageNum || params.pageNum
    };
  } catch (error) {
    console.error('获取报销等级列表失败:', error);
    throw error;
  }
};

/** 详情 */
export const getLevelDetail = async (id: number) => {
  try {
    const response = await http.request<any>("get", `/api/reimbursement-level/${id}`);
    console.log('报销等级详情API响应:', response);
    
    const data = response?.data || response;
    return processLevelData(data);
  } catch (error) {
    console.error('获取报销等级详情失败:', error);
    throw error;
  }
};

/** 新增 */
export const addLevel = async (data: ReimbursementLevel) => {
  try {
    // 确保字段名称正确
    const submitData = {
      levelCode: data.levelCode,
      levelName: data.levelName,
      insuranceType: data.insuranceType,
      hospitalLevel: data.hospitalLevel,
      minAmount: data.minAmount || 0,
      maxAmount: data.maxAmount || 0,
      deductible: data.deductible || 0,
      maxReimbursement: data.maxReimbursement || 0,
      categoryARate: data.categoryARate || 0,
      categoryBRate: data.categoryBRate || 0,
      categoryCRate: data.categoryCRate || 0,
      treatmentRate: data.treatmentRate || 0,
      serviceRate: data.serviceRate || 0,
      status: data.status || 1,
      effectiveTime: data.effectiveTime,
      expireTime: data.expireTime,
      remark: data.remark || ''
    };

    console.log('新增报销等级提交数据:', submitData);

    const response = await http.request("post", "/api/reimbursement-level", { data: submitData });
    return response;
  } catch (error) {
    console.error('新增报销等级失败:', error);
    throw error;
  }
};

/** 更新 */
export const updateLevel = (id: number, data: ReimbursementLevel) => {
  return http.request("put", `/api/reimbursement-level/${id}`, { data });
};

/** 删除 */
export const deleteLevel = (id: number) => {
  return http.request("delete", `/api/reimbursement-level/${id}`);
};

/** 批量删除 */
export const batchDeleteLevel = (ids: number[]) => {
  return http.request("delete", `/api/reimbursement-level/batch`, { data: ids });
};

/** 更改状态 */
export const updateLevelStatus = (id: number, status: number) => {
  return http.request("put", `/api/reimbursement-level/${id}/status`, { data: { status } });
};

/** 有效列表（下拉用） */
export const getEffectiveLevels = async () => {
  try {
    const response = await http.request<any>("get", "/api/reimbursement-level/effective");
    const data = response?.data || response;
    const records = Array.isArray(data) ? data : (data?.records || []);
    return records.map((level: any) => processLevelData(level));
  } catch (error) {
    console.error('获取有效报销等级失败:', error);
    throw error;
  }
};

/** 根据医保类型和医院等级获取匹配的报销等级 */
export const getMatchingLevels = async (insuranceType: string, hospitalLevel: string) => {
  try {
    const response = await http.request<any>("get", "/api/reimbursement-level/match", {
      params: { insuranceType, hospitalLevel }
    });
    const data = response?.data || response;
    const records = Array.isArray(data) ? data : (data?.records || []);
    return records.map((level: any) => processLevelData(level));
  } catch (error) {
    console.error('获取匹配报销等级失败:', error);
    throw error;
  }
};

/** 复制报销等级配置 */
export const copyLevel = (id: number) => {
  return http.request("post", `/api/reimbursement-level/${id}/copy`);
};

/** 验证报销等级配置 */
export const validateLevel = (data: ReimbursementLevel) => {
  return http.request("post", "/api/reimbursement-level/validate", { data });
};
