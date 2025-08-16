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

// 计算年龄
const calculateAge = (birthDate: string | Date | null): number => {
  if (!birthDate) return 0;
  const birth = new Date(birthDate);
  if (isNaN(birth.getTime())) return 0;
  
  const today = new Date();
  let age = today.getFullYear() - birth.getFullYear();
  const monthDiff = today.getMonth() - birth.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
    age--;
  }
  return Math.max(0, age);
};

// 根据身份证号计算年龄
const calculateAgeFromIdCard = (idCard: string): number => {
  if (!idCard || idCard.length !== 18) return 0;
  
  const year = parseInt(idCard.substring(6, 10));
  const month = parseInt(idCard.substring(10, 12));
  const day = parseInt(idCard.substring(12, 14));
  
  if (year < 1900 || year > new Date().getFullYear()) return 0;
  if (month < 1 || month > 12) return 0;
  if (day < 1 || day > 31) return 0;
  
  const birthDate = new Date(year, month - 1, day);
  return calculateAge(birthDate);
};

// 数据处理函数：处理患者数据
const processPatientData = (data: any): any => {
  if (!data) return data;
  
  // 转换下划线字段为驼峰
  const camelData = toCamel(data);
  
  // 详细的字段映射和处理
  const processed: any = {
    id: camelData.id || data.id,
    patientId: safeString(camelData.patientId || camelData.patient_id || data.patientId || data.patient_id),
    name: safeString(camelData.name || data.name),
    idCard: safeString(camelData.idCard || camelData.id_card || data.idCard || data.id_card),
    phone: safeString(camelData.phone || data.phone),
    gender: safeString(camelData.gender || data.gender),
    birthDate: safeString(camelData.birthDate || camelData.birth_date || data.birthDate || data.birth_date),
    address: safeString(camelData.address || data.address),
    insuranceType: safeString(camelData.insuranceType || camelData.insurance_type || data.insuranceType || data.insurance_type),
    insuranceNumber: safeString(camelData.insuranceNumber || camelData.insurance_number || data.insuranceNumber || data.insurance_number),
    status: camelData.status !== undefined ? camelData.status : (data.status !== undefined ? data.status : 1),
    createTime: safeString(camelData.createTime || camelData.create_time || data.createTime || data.create_time),
    updateTime: safeString(camelData.updateTime || camelData.update_time || data.updateTime || data.update_time),
    remark: safeString(camelData.remark || data.remark)
  };

  // 计算年龄
  processed.age = processed.birthDate 
    ? calculateAge(processed.birthDate) 
    : calculateAgeFromIdCard(processed.idCard);

  console.log('患者原始数据:', data);
  console.log('患者处理后数据:', processed);
  console.log('关键字段检查:', {
    name: processed.name,
    idCard: processed.idCard,
    phone: processed.phone,
    insuranceType: processed.insuranceType,
    age: processed.age
  });

  return processed;
};

export interface Patient {
  id?: number;
  patientId?: string;         // 患者编号
  name: string;               // 姓名
  idCard: string;             // 身份证号
  phone: string;              // 联系电话
  gender: string;             // 性别
  birthDate?: string;         // 出生日期
  age?: number;               // 年龄（计算得出）
  address: string;            // 地址
  insuranceType: string;      // 医保类型
  insuranceNumber: string;    // 医保号
  status?: number;            // 状态：1-正常，0-注销
  createTime?: string;        // 创建时间
  updateTime?: string;        // 更新时间
  remark?: string;            // 备注
}

export interface PatientQueryParams {
  page: number;
  size: number;
  name?: string;
  idCard?: string;
  phone?: string;
  insuranceType?: string;
  status?: number;
  keyword?: string;
}

export interface PatientListResult {
  records: Patient[];
  total: number;
  size: number;
  current: number;
}

/** 获取患者列表 */
export const getPatientList = async (params: PatientQueryParams) => {
  try {
    // 映射参数以匹配后端API
    const requestParams = {
      pageNum: params.page,
      pageSize: params.size,
      name: params.name,
      idCard: params.idCard,
      phone: params.phone,
      insuranceType: params.insuranceType,
      keyword: params.keyword
    };

    console.log('患者列表请求参数:', requestParams);

    const response = await http.request<any>("get", "/api/patient/list", { params: requestParams });
    console.log('患者列表API响应:', response);
    
    // 处理响应数据
    const data = response?.data || response;
    const records = data?.records || data?.list || [];
    
    // 处理每个患者数据
    const processedRecords = records.map((patient: any) => processPatientData(patient));
    
    return {
      records: processedRecords,
      total: data?.total || data?.count || processedRecords.length,
      size: data?.size || data?.pageSize || params.size,
      current: data?.current || data?.page || params.page
    };
  } catch (error) {
    console.error('获取患者列表失败:', error);
    throw error;
  }
};

/** 获取患者详情 */
export const getPatientDetail = async (id: number) => {
  try {
    const response = await http.request<any>("get", `/api/patient/${id}`);
    console.log('患者详情API响应:', response);
    
    const data = response?.data || response;
    return processPatientData(data);
  } catch (error) {
    console.error('获取患者详情失败:', error);
    throw error;
  }
};

/** 新增患者 */
export const addPatient = async (data: Patient) => {
  try {
    // 确保字段名称正确
    const submitData = {
      patientId: data.patientId || `P${Date.now()}`, // 如果没有提供患者编号，自动生成
      name: data.name,
      idCard: data.idCard,
      phone: data.phone,
      gender: data.gender,
      birthDate: data.birthDate,
      address: data.address,
      insuranceType: data.insuranceType,
      insuranceNumber: data.insuranceNumber,
      status: data.status || 1,
      remark: data.remark || ''
    };

    console.log('新增患者提交数据:', submitData);
    console.log('关键字段验证:', {
      name: submitData.name,
      idCard: submitData.idCard,
      phone: submitData.phone,
      insuranceType: submitData.insuranceType
    });

    const response = await http.request("post", "/api/patient", { data: submitData });
    return response;
  } catch (error) {
    console.error('新增患者失败:', error);
    throw error;
  }
};

/** 更新患者信息 */
export const updatePatient = async (data: Patient) => {
  try {
    const submitData = {
      id: data.id,
      patientId: data.patientId,
      name: data.name,
      idCard: data.idCard,
      phone: data.phone,
      gender: data.gender,
      birthDate: data.birthDate,
      address: data.address,
      insuranceType: data.insuranceType,
      insuranceNumber: data.insuranceNumber,
      status: data.status || 1,
      remark: data.remark || ''
    };

    console.log('更新患者提交数据:', submitData);

    const response = await http.request("put", "/api/patient", { data: submitData });
    return response;
  } catch (error) {
    console.error('更新患者失败:', error);
    throw error;
  }
};

/** 删除患者 */
export const deletePatient = (id: number) => {
  return http.request("delete", `/api/patient/${id}`);
};

/** 批量删除患者 */
export const batchDeletePatient = (ids: number[]) => {
  return http.request("delete", "/api/patient/batch", { data: ids });
};

/** 根据身份证号查询患者 */
export const getPatientByIdCard = async (idCard: string) => {
  try {
    const response = await http.request<any>("get", `/api/patient/byIdCard/${idCard}`);
    const data = response?.data || response;
    return processPatientData(data);
  } catch (error) {
    console.error('根据身份证号查询患者失败:', error);
    throw error;
  }
};

/** 根据医保卡号查询患者 */
export const getPatientByInsuranceCard = async (insuranceCard: string) => {
  try {
    const response = await http.request<any>("get", `/api/patient/byInsuranceCard/${insuranceCard}`);
    const data = response?.data || response;
    return processPatientData(data);
  } catch (error) {
    console.error('根据医保卡号查询患者失败:', error);
    throw error;
  }
};

/** 获取所有医保类型 */
export const getAllInsuranceTypes = () => {
  return http.request<any>("get", "/api/patient/insuranceTypes").then((response) => {
    console.log('保险类型API原始响应:', response);
    
    // 处理不同的响应格式
    let types = [];
    if (response && typeof response === 'object') {
      if (response.data && Array.isArray(response.data)) {
        // 标准格式: { code, message, data: [...] }
        types = response.data;
      } else if (Array.isArray(response)) {
        // 直接返回数组
        types = response;
      } else if (response.data && typeof response.data === 'string') {
        // 如果data是字符串，尝试解析
        try {
          types = JSON.parse(response.data);
        } catch (e) {
          console.error('解析保险类型字符串失败:', e);
          types = [];
        }
      }
    } else if (Array.isArray(response)) {
      types = response;
    } else if (typeof response === 'string') {
      // 如果结果是字符串，尝试解析
      try {
        types = JSON.parse(response);
      } catch (e) {
        console.error('解析保险类型字符串失败:', e);
        types = [];
      }
    }
    
    // 确保是字符串数组，过滤无效值
    const result = Array.isArray(types) 
      ? types.filter(type => type && typeof type === 'string' && type.trim() !== '')
      : ['城镇职工', '城乡居民', '新农合', '医疗救助', '商业保险']; // 默认值
      
    console.log('保险类型处理结果:', result);
    return result;
  }).catch((error) => {
    console.error('获取保险类型失败:', error);
    // 返回默认保险类型
    return ['城镇职工', '城乡居民', '新农合', '医疗救助', '商业保险'];
  });
};

/** 获取所有地区 */
export const getAllRegions = () => {
  return http.request<string[]>("get", "/api/patient/regions");
};

/** 检查身份证号是否存在 */
export const checkIdCard = (idCard: string) => {
  return http.request<boolean>("get", "/api/patient/checkIdCard", { params: { idCard } });
};

/** 检查医保卡号是否存在 */
export const checkInsuranceCard = (insuranceCard: string) => {
  return http.request<boolean>("get", "/api/patient/checkInsuranceCard", { params: { insuranceCard } });
};

/** 检查手机号是否存在 */
export const checkPhone = (phone: string) => {
  return http.request<boolean>("get", "/api/patient/checkPhone", { params: { phone } });
};

/** 搜索患者 */
export const searchPatients = async (keyword: string) => {
  try {
    const response = await http.request<any>("get", "/api/patient/search", { params: { keyword } });
    const data = response?.data || response;
    const records = Array.isArray(data) ? data : (data?.records || []);
    return records.map((patient: any) => processPatientData(patient));
  } catch (error) {
    console.error('搜索患者失败:', error);
    throw error;
  }
};

/** 获取患者统计信息 */
export const getPatientStatistics = () => {
  return http.request("get", "/api/patient/statistics");
};

/** 更新患者状态 */
export const updateStatus = (id: number, status: number) => {
  return http.request("post", "/api/patient/updateStatus", { params: { id, status } });
}; 