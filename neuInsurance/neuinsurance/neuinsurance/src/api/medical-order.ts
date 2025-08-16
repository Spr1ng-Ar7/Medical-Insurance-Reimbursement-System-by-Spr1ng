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

// 辅助函数：状态映射
const mapStatus = (status: any): string => {
  if (status === null || status === undefined) return '未知';
  const statusMap: Record<string, string> = {
    '0': '已取消',
    '1': '待结算', 
    '2': '已结算',
    '3': '已支付',
    '4': '待审核',
    '5': '已拒绝'
  };
  return statusMap[String(status)] || String(status);
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

// 数据处理函数：处理医疗订单数据
const processOrderData = (data: any): any => {
  if (!data) return data;
  
  // 转换下划线字段为驼峰
  const camelData = toCamel(data);
  
  // 详细的字段映射和处理
  const processed = {
    id: camelData.id || data.id,
    orderNo: safeString(camelData.orderNo || camelData.order_no || data.orderNo || data.order_no),
    patientId: safeNumber(camelData.patientId || camelData.patient_id || data.patientId || data.patient_id),
    patientName: safeString(camelData.patientName || camelData.patient_name || data.patientName || data.patient_name),
    patientIdCard: safeString(camelData.patientIdCard || camelData.patient_id_card || data.patientIdCard || data.patient_id_card),
    hospitalId: safeNumber(camelData.hospitalId || camelData.hospital_id || data.hospitalId || data.hospital_id),
    hospitalName: safeString(camelData.hospitalName || camelData.hospital_name || data.hospitalName || data.hospital_name),
    hospitalLevel: safeString(camelData.hospitalLevel || camelData.hospital_level || data.hospitalLevel || data.hospital_level),
    doctorId: safeNumber(camelData.doctorId || camelData.doctor_id || data.doctorId || data.doctor_id),
    doctorName: safeString(camelData.doctorName || camelData.doctor_name || data.doctorName || data.doctor_name),
    departmentName: safeString(camelData.departmentName || camelData.department_name || data.departmentName || data.department_name),
    diagnosis: safeString(camelData.diagnosis || data.diagnosis),
    orderType: safeString(camelData.orderType || camelData.order_type || data.orderType || data.order_type),
    visitTime: safeString(camelData.visitTime || camelData.visit_time || data.visitTime || data.visit_time),
    dischargeTime: safeString(camelData.dischargeTime || camelData.discharge_time || data.dischargeTime || data.discharge_time),
    stayDays: safeNumber(camelData.stayDays || camelData.stay_days || data.stayDays || data.stay_days),
    totalAmount: safeNumber(camelData.totalAmount || camelData.total_amount || data.totalAmount || data.total_amount),
    drugAmount: safeNumber(camelData.drugAmount || camelData.drug_amount || data.drugAmount || data.drug_amount),
    treatmentAmount: safeNumber(camelData.treatmentAmount || camelData.treatment_amount || data.treatmentAmount || data.treatment_amount),
    serviceAmount: safeNumber(camelData.serviceAmount || camelData.service_amount || data.serviceAmount || data.service_amount),
    otherAmount: safeNumber(camelData.otherAmount || camelData.other_amount || data.otherAmount || data.other_amount),
    categoryAAmount: safeNumber(camelData.categoryAAmount || camelData.category_a_amount || data.categoryAAmount || data.category_a_amount),
    categoryBAmount: safeNumber(camelData.categoryBAmount || camelData.category_b_amount || data.categoryBAmount || data.category_b_amount),
    categoryCAmount: safeNumber(camelData.categoryCAmount || camelData.category_c_amount || data.categoryCAmount || data.category_c_amount),
    deductible: safeNumber(camelData.deductible || data.deductible),
    reimbursementRatio: safeNumber(camelData.reimbursementRatio || camelData.reimbursement_ratio || data.reimbursementRatio || data.reimbursement_ratio),
    reimbursableAmount: safeNumber(camelData.reimbursableAmount || camelData.reimbursable_amount || data.reimbursableAmount || data.reimbursable_amount),
    actualReimbursement: safeNumber(camelData.actualReimbursement || camelData.actual_reimbursement || data.actualReimbursement || data.actual_reimbursement),
    selfPayAmount: safeNumber(camelData.selfPayAmount || camelData.self_pay_amount || data.selfPayAmount || data.self_pay_amount),
    settlementNo: safeString(camelData.settlementNo || camelData.settlement_no || data.settlementNo || data.settlement_no),
    settlementTime: safeString(camelData.settlementTime || camelData.settlement_time || data.settlementTime || data.settlement_time),
    approvalResult: safeString(camelData.approvalResult || camelData.approval_result || data.approvalResult || data.approval_result),
    approvalRemark: safeString(camelData.approvalRemark || camelData.approval_remark || data.approvalRemark || data.approval_remark),
    rejectReason: safeString(camelData.rejectReason || camelData.reject_reason || data.rejectReason || data.reject_reason),
    status: camelData.status !== undefined ? camelData.status : (data.status !== undefined ? data.status : 0),
    createTime: safeString(camelData.createTime || camelData.create_time || data.createTime || data.create_time),
    updateTime: safeString(camelData.updateTime || camelData.update_time || data.updateTime || data.update_time),
    createBy: safeString(camelData.createBy || camelData.create_by || data.createBy || data.create_by),
    updateBy: safeString(camelData.updateBy || camelData.update_by || data.updateBy || data.update_by),
    remark: safeString(camelData.remark || data.remark)
  };

  console.log('原始数据:', data);
  console.log('处理后数据:', processed);
  console.log('关键字段检查:', {
    patientName: processed.patientName,
    orderNo: processed.orderNo,
    orderType: processed.orderType,
    doctorName: processed.doctorName,
    departmentName: processed.departmentName
  });

  return processed;
};

export interface MedicalOrder {
  id?: number;
  orderNo?: string;
  patientId?: number;
  patientName: string;
  patientIdCard?: string;
  hospitalId?: number;
  hospitalName?: string;
  hospitalLevel?: string;
  doctorId?: number;
  doctorName: string;
  departmentName: string;
  diagnosis: string;
  orderType: string;
  visitTime?: string;
  dischargeTime?: string;
  stayDays?: number;
  totalAmount?: number;
  drugAmount?: number;
  treatmentAmount?: number;
  serviceAmount?: number;
  otherAmount?: number;
  categoryAAmount?: number;
  categoryBAmount?: number;
  categoryCAmount?: number;
  deductible?: number;
  reimbursementRatio?: number;
  reimbursableAmount?: number;
  actualReimbursement?: number;
  selfPayAmount?: number;
  settlementNo?: string;
  settlementTime?: string;
  approvalResult?: string;
  approvalRemark?: string;
  rejectReason?: string;
  status: number;
  createTime?: string;
  updateTime?: string;
  createBy?: string;
  updateBy?: string;
  remark?: string;
}

export interface MedicalOrderQueryParams {
  pageNum: number;
  pageSize: number;
  orderNo?: string;
  patientId?: number;
  status?: string;
  keyword?: string;
}

export interface MedicalOrderListResult {
  records: MedicalOrder[];
  total: number;
  size: number;
  current: number;
}

export interface SettlementResult {
  orderId: number;
  orderNo: string;
  totalAmount: number;
  reimbursementAmount: number;
  settlementAmount: number;
  reimbursementRate: number;
  settlementStatus: string;
  message: string;
}

/** 获取医疗订单列表 */
export const getMedicalOrderList = async (params: MedicalOrderQueryParams) => {
  try {
    // 映射参数以匹配后端API
    const requestParams = {
      pageNum: params.pageNum,
      pageSize: params.pageSize,
      orderNo: params.orderNo,
      patientId: params.patientId,
      status: params.status,
      keyword: params.keyword
    };

    console.log('医疗订单列表请求参数:', requestParams);

    const response = await http.request<any>("get", "/api/medical-order/list", { params: requestParams });
    console.log('订单列表API响应:', response);
    
    // 处理响应数据
    const data = response?.data || response;
    const records = data?.records || data?.list || [];
    
    // 处理每个订单数据
    const processedRecords = records.map((order: any) => processOrderData(order));
    
    return {
      records: processedRecords,
      total: data?.total || data?.count || processedRecords.length,
      size: data?.size || data?.pageSize || params.pageSize,
      current: data?.current || data?.pageNum || params.pageNum
    };
  } catch (error) {
    console.error('获取订单列表失败:', error);
    throw error;
  }
};

/** 获取医疗订单详情 */
export const getMedicalOrderDetail = async (id: number) => {
  try {
    const response = await http.request<any>("get", `/api/medical-order/${id}`);
    console.log('订单详情API响应:', response);
    
    // 处理响应数据
    const data = response?.data || response;
    return processOrderData(data);
  } catch (error) {
    console.error('获取订单详情失败:', error);
    throw error;
  }
};

/** 根据订单号查询医疗订单 */
export const getMedicalOrderByOrderNo = async (orderNo: string) => {
  try {
    const response = await http.request<any>("get", `/api/medical-order/byOrderNo/${orderNo}`);
    const data = response?.data || response;
    return processOrderData(data);
  } catch (error) {
    console.error('根据订单号查询失败:', error);
    throw error;
  }
};

/** 根据患者ID查询医疗订单列表 */
export const getMedicalOrdersByPatientId = async (patientId: number) => {
  try {
    const response = await http.request<any>("get", `/api/medical-order/byPatient/${patientId}`);
    const data = response?.data || response;
    const records = Array.isArray(data) ? data : (data?.records || []);
    return records.map((order: any) => processOrderData(order));
  } catch (error) {
    console.error('根据患者ID查询失败:', error);
    throw error;
  }
};

/** 新增医疗订单 */
export const addMedicalOrder = async (data: any) => {
  try {
    // 确保字段名称正确
    const submitData = {
      orderNo: data.orderNo,
      patientId: data.patientId || Date.now(), // 如果没有patientId，生成一个
      patientName: data.patientName,
      patientIdCard: data.patientIdCard,
      orderType: data.orderType,
      hospitalName: data.hospitalName,
      hospitalLevel: data.hospitalLevel,
      departmentName: data.departmentName, // 确保使用正确的字段名
      doctorName: data.doctorName,
      diagnosis: data.diagnosis,
      visitTime: data.visitTime,
      status: data.status || 4, // 默认待审核
      totalAmount: data.totalAmount || 0,
      drugAmount: data.drugAmount || 0,
      treatmentAmount: data.treatmentAmount || 0,
      serviceAmount: data.serviceAmount || 0,
      otherAmount: data.otherAmount || 0,
      categoryAAmount: data.categoryAAmount || 0,
      categoryBAmount: data.categoryBAmount || 0,
      categoryCAmount: data.categoryCAmount || 0,
      deductible: data.deductible || 0,
      reimbursementRatio: data.reimbursementRatio || 0,
      reimbursableAmount: data.reimbursableAmount || 0,
      actualReimbursement: data.actualReimbursement || 0,
      selfPayAmount: data.selfPayAmount || 0,
      remark: data.remark || ''
    };

    console.log('新增订单提交数据:', submitData);
    console.log('关键字段验证:', {
      patientName: submitData.patientName,
      orderType: submitData.orderType,
      departmentName: submitData.departmentName,
      doctorName: submitData.doctorName,
      diagnosis: submitData.diagnosis
    });

    const response = await http.request("post", "/api/medical-order/add", { data: submitData });
    return response;
  } catch (error) {
    console.error('新增订单失败:', error);
    throw error;
  }
};

/** 更新医疗订单 */
export const updateMedicalOrder = (data: MedicalOrder) => {
  return http.request("put", "/api/medical-order/update", { data });
};

/** 删除医疗订单 */
export const deleteMedicalOrder = (id: number) => {
  return http.request("delete", `/api/medical-order/${id}`);
};

/** 批量删除医疗订单 */
export const batchDeleteMedicalOrder = (ids: number[]) => {
  return http.request("delete", "/api/medical-order/batch", { data: ids });
};

/** 更新订单状态 */
export const updateOrderStatus = (id: number, status: string) => {
  return http.request("post", "/api/medical-order/updateStatus", { 
    params: { id, status } 
  });
};

/** 取消订单 */
export const cancelOrder = (id: number, reason: string) => {
  return http.request("post", "/api/medical-order/cancel", { 
    params: { id, reason } 
  });
};

/** 完成订单 */
export const completeOrder = (id: number) => {
  return http.request("post", "/api/medical-order/complete", { 
    params: { id } 
  });
};

/** 根据状态查询医疗订单列表 */
export const getMedicalOrdersByStatus = (status: string) => {
  return http.request<MedicalOrder[]>("get", `/api/medical-order/byStatus/${status}`);
};

/** 根据科室查询医疗订单列表 */
export const getMedicalOrdersByDepartment = (department: string) => {
  return http.request<MedicalOrder[]>("get", `/api/medical-order/byDepartment/${department}`);
};

/** 根据医生查询医疗订单列表 */
export const getMedicalOrdersByDoctor = (doctorId: number) => {
  return http.request<MedicalOrder[]>("get", `/api/medical-order/byDoctor/${doctorId}`);
};

/** 根据日期范围查询医疗订单列表 */
export const getMedicalOrdersByDateRange = (startDate: string, endDate: string) => {
  return http.request<MedicalOrder[]>("get", "/api/medical-order/byDateRange", { 
    params: { startDate, endDate } 
  });
};

/** 根据金额范围查询医疗订单列表 */
export const getMedicalOrdersByAmountRange = (minAmount: number, maxAmount: number) => {
  return http.request<MedicalOrder[]>("get", "/api/medical-order/byAmountRange", { 
    params: { minAmount, maxAmount } 
  });
};

/** 获取所有订单状态 */
export const getAllOrderStatuses = () => {
  return http.request<string[]>("get", "/api/medical-order/orderStatuses");
};

/** 获取所有科室 */
export const getAllDepartments = () => {
  return http.request<string[]>("get", "/api/medical-order/departments");
};

/** 检查订单号是否存在 */
export const checkOrderNo = (orderNo: string) => {
  return http.request<boolean>("get", "/api/medical-order/checkOrderNo", { params: { orderNo } });
};

/** 生成订单号 */
export const generateOrderNo = () => {
  return http.request<string>("get", "/api/medical-order/generateOrderNo");
};

/** 计算订单金额 */
export const calculateOrderAmount = (orderId: number) => {
  return http.request<number>("get", `/api/medical-order/calculateAmount/${orderId}`);
};

/** 获取订单统计信息 */
export const getOrderStatistics = () => {
  return http.request("get", "/api/medical-order/statistics");
};

/** 获取患者订单历史 */
export const getPatientOrderHistory = (patientId: number, pageNum: number, pageSize: number) => {
  return http.request<MedicalOrderListResult>("get", `/api/medical-order/patientHistory/${patientId}`, { 
    params: { pageNum, pageSize } 
  });
};

/** 计算报销金额 */
export const calculateSettlement = (orderId: number) => {
  return http.request<SettlementResult>("post", `/api/medical-order/calculateSettlement/${orderId}`);
};

/** 提交结算申请 */
export const submitSettlement = (orderId: number) => {
  return http.request("post", "/api/medical-order/submitSettlement", { 
    params: { orderId } 
  });
};

/** 审批结算 */
export const approveSettlement = (orderId: number, approvalResult: string, approvalRemark?: string) => {
  return http.request("post", "/api/medical-order/approveSettlement", { 
    params: { orderId, approvalResult, approvalRemark } 
  });
};

/** 拒绝结算 */
export const rejectSettlement = (orderId: number, rejectReason: string) => {
  return http.request("post", "/api/medical-order/rejectSettlement", { 
    params: { orderId, rejectReason } 
  });
};

/** 完成结算 */
export const completeSettlement = (orderId: number) => {
  return http.request("post", "/api/medical-order/completeSettlement", { 
    params: { orderId } 
  });
};

/** 生成结算单号 */
export const generateSettlementNo = () => {
  return http.request<string>("get", "/api/medical-order/generateSettlementNo");
};

/** 检查结算单号是否存在 */
export const checkSettlementNo = (settlementNo: string) => {
  return http.request<boolean>("get", "/api/medical-order/checkSettlementNo", { params: { settlementNo } });
};

/** 获取结算统计信息 */
export const getSettlementStatistics = (startDate?: string, endDate?: string, hospitalId?: number) => {
  return http.request("get", "/api/medical-order/settlementStatistics", { 
    params: { startDate, endDate, hospitalId } 
  });
};

/** 根据结算状态查询订单列表 */
export const getOrdersBySettlementStatus = (status: string) => {
  return http.request<MedicalOrder[]>("get", `/api/medical-order/bySettlementStatus/${status}`);
};

/** 根据结算日期范围查询订单列表 */
export const getOrdersBySettlementDateRange = (startDate: string, endDate: string) => {
  return http.request<MedicalOrder[]>("get", "/api/medical-order/bySettlementDateRange", { 
    params: { startDate, endDate } 
  });
};

/** 根据报销金额范围查询订单列表 */
export const getOrdersByReimbursementAmountRange = (minAmount: number, maxAmount: number) => {
  return http.request<MedicalOrder[]>("get", "/api/medical-order/byReimbursementAmountRange", { 
    params: { minAmount, maxAmount } 
  });
};

/** 获取所有结算状态 */
export const getAllSettlementStatuses = () => {
  return http.request<string[]>("get", "/api/medical-order/settlementStatuses");
};

/** 获取患者结算历史 */
export const getPatientSettlementHistory = (patientId: number, pageNum: number, pageSize: number) => {
  return http.request<MedicalOrderListResult>("get", `/api/medical-order/patientSettlementHistory/${patientId}`, { 
    params: { pageNum, pageSize } 
  });
};

/** 批量计算报销金额 */
export const batchCalculateSettlement = (orderIds: number[]) => {
  return http.request<SettlementResult[]>("post", "/api/medical-order/batchCalculateSettlement", { 
    data: orderIds 
  });
};

/** 批量提交结算 */
export const batchSubmitSettlement = (orderIds: number[]) => {
  return http.request("post", "/api/medical-order/batchSubmitSettlement", { 
    data: orderIds 
  });
};

/** 批量审批结算 */
export const batchApproveSettlement = (orderIds: number[], approvalResult: string, approvalRemark?: string) => {
  return http.request("post", "/api/medical-order/batchApproveSettlement", { 
    params: { approvalResult, approvalRemark },
    data: orderIds 
  });
}; 