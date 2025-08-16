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

// 状态映射函数
const mapSettlementStatus = (status: any): string => {
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

// 数据处理函数：处理结算数据
const processSettlementData = (data: any): any => {
  if (!data) return data;
  
  // 转换下划线字段为驼峰
  const camelData = toCamel(data);
  
  // 详细的字段映射和处理
  const processed = {
    id: camelData.id || data.id,
    settlementNo: safeString(camelData.settlementNo || camelData.settlement_no || data.settlementNo || data.settlement_no),
    orderNo: safeString(camelData.orderNo || camelData.order_no || data.orderNo || data.order_no),
    orderId: safeNumber(camelData.orderId || camelData.order_id || camelData.id || data.id),
    patientId: safeNumber(camelData.patientId || camelData.patient_id || data.patientId || data.patient_id),
    patientName: safeString(camelData.patientName || camelData.patient_name || data.patientName || data.patient_name),
    patientIdCard: safeString(camelData.patientIdCard || camelData.patient_id_card || data.patientIdCard || data.patient_id_card),
    hospitalName: safeString(camelData.hospitalName || camelData.hospital_name || data.hospitalName || data.hospital_name),
    hospitalLevel: safeString(camelData.hospitalLevel || camelData.hospital_level || data.hospitalLevel || data.hospital_level),
    departmentName: safeString(camelData.departmentName || camelData.department_name || data.departmentName || data.department_name),
    doctorName: safeString(camelData.doctorName || camelData.doctor_name || data.doctorName || data.doctor_name),
    orderType: safeString(camelData.orderType || camelData.order_type || data.orderType || data.order_type),
    diagnosis: safeString(camelData.diagnosis || data.diagnosis),
    visitTime: safeString(camelData.visitTime || camelData.visit_time || data.visitTime || data.visit_time),
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
    settlementTime: safeString(camelData.settlementTime || camelData.settlement_time || data.settlementTime || data.settlement_time),
    approvalResult: safeString(camelData.approvalResult || camelData.approval_result || data.approvalResult || data.approval_result),
    approvalRemark: safeString(camelData.approvalRemark || camelData.approval_remark || data.approvalRemark || data.approval_remark),
    rejectReason: safeString(camelData.rejectReason || camelData.reject_reason || data.rejectReason || data.reject_reason),
    status: camelData.status !== undefined ? camelData.status : (data.status !== undefined ? data.status : 1),
    statusText: mapSettlementStatus(camelData.status !== undefined ? camelData.status : data.status),
    createTime: safeString(camelData.createTime || camelData.create_time || data.createTime || data.create_time),
    updateTime: safeString(camelData.updateTime || camelData.update_time || data.updateTime || data.update_time),
    createBy: safeString(camelData.createBy || camelData.create_by || data.createBy || data.create_by),
    updateBy: safeString(camelData.updateBy || camelData.update_by || data.updateBy || data.update_by),
    remark: safeString(camelData.remark || data.remark)
  };

  console.log('原始结算数据:', data);
  console.log('处理后结算数据:', processed);

  return processed;
};

export interface Settlement {
  id?: number;
  settlementNo?: string;
  orderNo?: string;
  orderId?: number;
  patientId?: number;
  patientName: string;
  patientIdCard?: string;
  hospitalName?: string;
  hospitalLevel?: string;
  departmentName?: string;
  doctorName?: string;
  orderType?: string;
  diagnosis?: string;
  visitTime?: string;
  totalAmount: number;
  drugAmount?: number;
  treatmentAmount?: number;
  serviceAmount?: number;
  otherAmount?: number;
  categoryAAmount?: number;
  categoryBAmount?: number;
  categoryCAmount?: number;
  deductible?: number;
  reimbursementRatio?: number;
  reimbursableAmount: number;
  actualReimbursement: number;
  selfPayAmount: number;
  settlementTime?: string;
  approvalResult?: string;
  approvalRemark?: string;
  rejectReason?: string;
  status: number;
  statusText?: string;
  createTime?: string;
  updateTime?: string;
  createBy?: string;
  updateBy?: string;
  remark?: string;
}

export interface SettlementQueryParams {
  pageNum: number;
  pageSize: number;
  orderNo?: string;
  patientName?: string;
  status?: string;
  keyword?: string;
  startDate?: string;
  endDate?: string;
  hospitalLevel?: string;
  orderType?: string;
}

export interface SettlementListResult {
  records: Settlement[];
  total: number;
  size: number;
  current: number;
}

export interface SettlementStatistics {
  totalCount: number;
  totalAmount: number;
  totalReimbursement: number;
  pendingCount: number;
  pendingAmount: number;
  completedCount: number;
  completedAmount: number;
  rejectedCount: number;
  rejectedAmount: number;
  averageReimbursementRatio: number;
}

export interface PaymentRecord {
  id?: number;
  settlementId: number;
  paymentNo: string;
  paymentType: string;
  paymentAmount: number;
  paymentTime: string;
  paymentStatus: string;
  bankAccount?: string;
  transactionId?: string;
  remarks?: string;
}

/** 获取结算列表 */
export const getSettlementList = async (params: SettlementQueryParams) => {
  try {
    // 映射参数以匹配医疗订单API
    const requestParams = {
      pageNum: params.pageNum,
      pageSize: params.pageSize,
      orderNo: params.orderNo,
      keyword: params.patientName || params.keyword,
      status: params.status,
      startDate: params.startDate,
      endDate: params.endDate
    };

    console.log('结算列表请求参数:', requestParams);

    const response = await http.request<any>("get", "/api/medical-order/list", { params: requestParams });
    console.log('结算列表API响应:', response);
    
    // 处理响应数据
    const data = response?.data || response;
    const records = data?.records || data?.list || [];
    
    // 过滤出需要结算的订单（状态为1, 2, 3的订单）
    const settlementRecords = records
      .filter((order: any) => [1, 2, 3].includes(order.status))
      .map((order: any) => processSettlementData(order));
    
    return {
      records: settlementRecords,
      total: settlementRecords.length,
      size: data?.size || data?.pageSize || params.pageSize,
      current: data?.current || data?.pageNum || params.pageNum
    };
  } catch (error) {
    console.error('获取结算列表失败:', error);
    throw error;
  }
};

/** 获取结算详情 */
export const getSettlementDetail = async (id: number) => {
  try {
    const response = await http.request<any>("get", `/api/medical-order/${id}`);
    console.log('结算详情API响应:', response);
    
    const data = response?.data || response;
    return processSettlementData(data);
  } catch (error) {
    console.error('获取结算详情失败:', error);
    throw error;
  }
};

/** 提交结算申请 */
export const submitSettlement = async (orderId: number) => {
  try {
    const response = await http.request("post", "/api/medical-order/submitSettlement", { 
      params: { orderId } 
    });
    return response;
  } catch (error) {
    console.error('提交结算申请失败:', error);
    throw error;
  }
};

/** 计算结算金额 */
export const calculateSettlement = async (orderId: number) => {
  try {
    const response = await http.request("post", `/api/medical-order/calculateSettlement/${orderId}`);
    return response;
  } catch (error) {
    console.error('计算结算金额失败:', error);
    throw error;
  }
};

/** 审批结算 */
export const approveSettlement = async (orderId: number, approvalResult: string, approvalRemark?: string) => {
  try {
    const response = await http.request("post", "/api/medical-order/approveSettlement", { 
      params: { orderId, approvalResult, approvalRemark } 
    });
    return response;
  } catch (error) {
    console.error('审批结算失败:', error);
    throw error;
  }
};

/** 拒绝结算 */
export const rejectSettlement = async (orderId: number, rejectReason: string) => {
  try {
    const response = await http.request("post", "/api/medical-order/rejectSettlement", { 
      params: { orderId, rejectReason } 
    });
    return response;
  } catch (error) {
    console.error('拒绝结算失败:', error);
    throw error;
  }
};

/** 完成结算 */
export const completeSettlement = async (orderId: number) => {
  try {
    const response = await http.request("post", "/api/medical-order/completeSettlement", { 
      params: { orderId } 
    });
    return response;
  } catch (error) {
    console.error('完成结算失败:', error);
    throw error;
  }
};

/** 批量结算 */
export const batchSettlement = async (orderIds: number[]) => {
  try {
    const response = await http.request("post", "/api/medical-order/batchSubmitSettlement", { 
      data: orderIds 
    });
    return response;
  } catch (error) {
    console.error('批量结算失败:', error);
    throw error;
  }
};

/** 获取结算统计 */
export const getSettlementStatistics = async (startDate?: string, endDate?: string, hospitalId?: number) => {
  try {
    const response = await http.request<any>("get", "/api/medical-order/settlementStatistics", { 
      params: { startDate, endDate, hospitalId } 
    });
    return response?.data || response;
  } catch (error) {
    console.error('获取结算统计失败:', error);
    throw error;
  }
};

/** 导出结算报表 */
export const exportSettlementReport = async (params: SettlementQueryParams) => {
  try {
    const response = await http.request("get", "/api/settlement/export", { 
      params,
      responseType: "blob"
    });
    return response;
  } catch (error) {
    console.error('导出结算报表失败:', error);
    throw error;
  }
};

/** 模拟支付 */
export const simulatePayment = async (settlementId: number, paymentData: {
  paymentType: string;
  bankAccount?: string;
  remarks?: string;
}) => {
  try {
    // 生成模拟支付记录
    const paymentRecord: PaymentRecord = {
      settlementId,
      paymentNo: `PAY${Date.now()}`,
      paymentType: paymentData.paymentType,
      paymentAmount: 0, // 将在后端计算
      paymentTime: new Date().toISOString(),
      paymentStatus: 'SUCCESS',
      bankAccount: paymentData.bankAccount,
      transactionId: `TXN${Date.now()}${Math.floor(Math.random() * 1000)}`,
      remarks: paymentData.remarks
    };

    const response = await http.request("post", "/api/settlement/payment/simulate", { 
      data: paymentRecord 
    });
    return response;
  } catch (error) {
    console.error('模拟支付失败:', error);
    throw error;
  }
};

/** 获取支付记录 */
export const getPaymentRecords = async (settlementId: number) => {
  try {
    const response = await http.request<any>("get", `/api/settlement/${settlementId}/payments`);
    return response?.data || response || [];
  } catch (error) {
    console.error('获取支付记录失败:', error);
    return [];
  }
};

/** 撤销支付 */
export const cancelPayment = async (paymentId: number, reason: string) => {
  try {
    const response = await http.request("post", `/api/settlement/payment/${paymentId}/cancel`, { 
      data: { reason } 
    });
    return response;
  } catch (error) {
    console.error('撤销支付失败:', error);
    throw error;
  }
};

/** 重新计算结算 */
export const recalculateSettlement = async (orderId: number, params: {
  deductible?: number;
  reimbursementRatio?: number;
  categoryAAmount?: number;
  categoryBAmount?: number;
  categoryCAmount?: number;
}) => {
  try {
    const response = await http.request("post", `/api/settlement/${orderId}/recalculate`, { 
      data: params 
    });
    return response;
  } catch (error) {
    console.error('重新计算结算失败:', error);
    throw error;
  }
};

/** 获取结算审计日志 */
export const getSettlementAuditLog = async (settlementId: number) => {
  try {
    const response = await http.request<any>("get", `/api/settlement/${settlementId}/audit-log`);
    return (response as any)?.data || response || [];
  } catch (error) {
    console.error('获取结算审计日志失败:', error);
    return [];
  }
}; 