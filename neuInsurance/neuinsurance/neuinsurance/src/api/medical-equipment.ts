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

// 数据处理函数：处理医疗设备数据
const processEquipmentData = (data: any): any => {
  if (!data) return data;
  
  // 转换下划线字段为驼峰
  const camelData = toCamel(data);
  
  // 详细的字段映射和处理
  const processed = {
    id: camelData.id || data.id,
    financeCategory: safeString(camelData.financeCategory || camelData.finance_category || data.financeCategory || data.finance_category),
    equipmentCode: safeString(camelData.equipmentCode || camelData.equipment_code || data.equipmentCode || data.equipment_code),
    nationalCode: safeString(camelData.nationalCode || camelData.national_code || data.nationalCode || data.national_code),
    equipmentName: safeString(camelData.equipmentName || camelData.equipment_name || data.equipmentName || data.equipment_name),
    equipmentContent: safeString(camelData.equipmentContent || camelData.equipment_content || data.equipmentContent || data.equipment_content),
    excludeContent: safeString(camelData.excludeContent || camelData.exclude_content || data.excludeContent || data.exclude_content),
    unitType: safeString(camelData.unitType || camelData.unit_type || data.unitType || data.unit_type),
    price: safeNumber(camelData.price || data.price),
    category: safeString(camelData.category || data.category),
    status: camelData.status !== undefined ? camelData.status : (data.status !== undefined ? data.status : 1),
    createTime: safeString(camelData.createTime || camelData.create_time || data.createTime || data.create_time),
    updateTime: safeString(camelData.updateTime || camelData.update_time || data.updateTime || data.update_time),
    createBy: safeString(camelData.createBy || camelData.create_by || data.createBy || data.create_by),
    updateBy: safeString(camelData.updateBy || camelData.update_by || data.updateBy || data.update_by),
    remark: safeString(camelData.remark || data.remark)
  };

  console.log('医疗设备原始数据:', data);
  console.log('医疗设备处理后数据:', processed);
  console.log('关键字段检查:', {
    equipmentCode: processed.equipmentCode,
    equipmentName: processed.equipmentName,
    category: processed.category,
    price: processed.price,
    status: processed.status
  });

  return processed;
};

export interface MedicalEquipment {
  id?: number;
  financeCategory?: string;    // 财务分类
  equipmentCode: string;       // 项目编码
  nationalCode?: string;       // 国家编码
  equipmentName: string;       // 项目名称
  equipmentContent?: string;   // 项目内容
  excludeContent?: string;     // 除外内容
  unitType?: string;           // 计价单位
  price?: number;              // 价格
  category?: string;           // 设备类别
  status?: number;             // 状态：1-正常，0-停用
  createTime?: string;
  updateTime?: string;
  createBy?: string;           // 创建人
  updateBy?: string;           // 更新人
  remark?: string;             // 说明
}

export interface EquipmentQueryParams {
  pageNum: number;
  pageSize: number;
  equipmentCode?: string;      // 项目编码搜索
  equipmentName?: string;      // 项目名称搜索
  category?: string;           // 设备类别搜索
  status?: number;             // 状态搜索
  keyword?: string;            // 关键词搜索
  sortField?: string;
  sortOrder?: string;
}

export interface PageResponse<T> {
  records: T[];
  total: number;
  pageNum: number;
  pageSize: number;
  totalPages: number;
  size: number;
  current: number;
}

/** 分页获取医疗器械列表 */
export const getMedicalEquipmentList = async (params: EquipmentQueryParams) => {
  try {
    const response = await http.request<any>("post", "/api/medical-equipments/list", { 
      data: {
        pageNum: params.pageNum,
        pageSize: params.pageSize,
        sortField: params.sortField,
        sortOrder: params.sortOrder,
        equipmentCode: params.equipmentCode,
        equipmentName: params.equipmentName,
        category: params.category,
        status: params.status,
        keyword: params.keyword
      }
    });

    console.log('医疗设备列表API响应:', response);
    
    // 处理响应数据
    const data = response?.data || response;
    const records = data?.records || data?.list || [];
    
    // 处理每个设备数据
    const processedRecords = records.map((equipment: any) => processEquipmentData(equipment));
    
    return {
      records: processedRecords,
      total: data?.total || data?.count || processedRecords.length,
      size: data?.size || data?.pageSize || params.pageSize,
      current: data?.current || data?.pageNum || params.pageNum,
      pageNum: data?.pageNum || params.pageNum,
      pageSize: data?.pageSize || params.pageSize,
      totalPages: Math.ceil((data?.total || processedRecords.length) / params.pageSize)
    };
  } catch (error) {
    console.error('获取医疗设备列表失败:', error);
    throw error;
  }
};

/** 根据ID查询医疗器械 */
export const getMedicalEquipmentById = async (id: number) => {
  try {
    const response = await http.request<any>("get", `/api/medical-equipments/${id}`);
    console.log('医疗设备详情API响应:', response);
    
    const data = response?.data || response;
    return processEquipmentData(data);
  } catch (error) {
    console.error('获取医疗设备详情失败:', error);
    throw error;
  }
};

/** 创建医疗器械 */
export const createMedicalEquipment = async (data: MedicalEquipment) => {
  try {
    // 确保字段名称正确
    const submitData = {
      financeCategory: data.financeCategory || '',
      equipmentCode: data.equipmentCode,
      nationalCode: data.nationalCode || '',
      equipmentName: data.equipmentName,
      equipmentContent: data.equipmentContent || '',
      excludeContent: data.excludeContent || '',
      unitType: data.unitType || '',
      price: data.price || 0,
      category: data.category || '',
      status: data.status || 1,
      remark: data.remark || ''
    };

    console.log('新增医疗设备提交数据:', submitData);
    console.log('关键字段验证:', {
      equipmentCode: submitData.equipmentCode,
      equipmentName: submitData.equipmentName,
      category: submitData.category,
      price: submitData.price
    });

    const response = await http.request("post", "/api/medical-equipments", { data: submitData });
    return response;
  } catch (error) {
    console.error('新增医疗设备失败:', error);
    throw error;
  }
};

/** 更新医疗器械 */
export const updateMedicalEquipment = async (id: number, data: MedicalEquipment) => {
  try {
    const submitData = {
      financeCategory: data.financeCategory || '',
      equipmentCode: data.equipmentCode,
      nationalCode: data.nationalCode || '',
      equipmentName: data.equipmentName,
      equipmentContent: data.equipmentContent || '',
      excludeContent: data.excludeContent || '',
      unitType: data.unitType || '',
      price: data.price || 0,
      category: data.category || '',
      status: data.status || 1,
      remark: data.remark || ''
    };

    console.log('更新医疗设备提交数据:', submitData);

    const response = await http.request("put", `/api/medical-equipments/${id}`, { data: submitData });
    return response;
  } catch (error) {
    console.error('更新医疗设备失败:', error);
    throw error;
  }
};

/** 删除医疗器械 */
export const deleteMedicalEquipment = (id: number) => {
  return http.request("delete", `/api/medical-equipments/${id}`);
};

/** 批量删除医疗器械 */
export const batchDeleteMedicalEquipments = (ids: number[]) => {
  return http.request("delete", "/api/medical-equipments/batch", { data: ids });
};

/** 更改状态 */
export const changeMedicalEquipmentStatus = (id: number, status: number) => {
  return http.request("put", `/api/medical-equipments/${id}/status`, { data: { status } });
};

/** 搜索医疗器械 */
export const searchMedicalEquipments = async (keyword: string) => {
  try {
    const response = await http.request<any>("get", "/api/medical-equipments/search", { params: { keyword } });
    const data = response?.data || response;
    const records = Array.isArray(data) ? data : (data?.records || []);
    return records.map((equipment: any) => processEquipmentData(equipment));
  } catch (error) {
    console.error('搜索医疗设备失败:', error);
    throw error;
  }
};

/** 根据类型查询医疗器械 */
export const getMedicalEquipmentsByType = async (equipmentType: string) => {
  try {
    const response = await http.request<any>("get", `/api/medical-equipments/type/${equipmentType}`);
    const data = response?.data || response;
    const records = Array.isArray(data) ? data : (data?.records || []);
    return records.map((equipment: any) => processEquipmentData(equipment));
  } catch (error) {
    console.error('根据类型查询医疗设备失败:', error);
    throw error;
  }
};

/** 根据器械编码查询医疗器械 */
export const getMedicalEquipmentByCode = async (equipmentCode: string) => {
  try {
    const response = await http.request<any>("get", `/api/medical-equipments/code/${equipmentCode}`);
    const data = response?.data || response;
    return processEquipmentData(data);
  } catch (error) {
    console.error('根据编码查询医疗设备失败:', error);
    throw error;
  }
};

/** 检查设备编码是否存在 */
export const checkEquipmentCodeExists = (equipmentCode: string) => {
  return http.request<boolean>("get", "/api/medical-equipments/check-code", { 
    params: { equipmentCode } 
  });
};
